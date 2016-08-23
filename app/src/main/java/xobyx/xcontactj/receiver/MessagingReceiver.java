package xobyx.xcontactj.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

import org.mistergroup.muzutozvednout.ShouldIAnswerBinder;

import xobyx.xcontactj.common.BlockedConversationHelper;
import xobyx.xcontactj.common.ConversationPrefsHelper;
import xobyx.xcontactj.common.utils.PackageUtils;
import xobyx.xcontactj.data.Message;
import xobyx.xcontactj.service.NotificationService;
import xobyx.xcontactj.ui.settings.SettingsFragment;
import xobyx.xcontactj.until.NotificationManager;
import xobyx.xcontactj.until.SmsHelper;

public class MessagingReceiver extends BroadcastReceiver {
    private final String TAG = "MessagingReceiver";

    private Context mContext;
    private SharedPreferences mPrefs;

    private String mAddress;
    private String mBody;
    private long mDate;

    private Uri mUri;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "onReceive");
        abortBroadcast();

        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (intent.getExtras() != null) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            SmsMessage sms = messages[0];
            if (messages.length == 1 || sms.isReplace()) {
                mBody = sms.getDisplayMessageBody();
            } else {
                StringBuilder bodyText = new StringBuilder();
                for (SmsMessage message : messages) {
                    bodyText.append(message.getMessageBody());
                }
                mBody = bodyText.toString();
            }

            mAddress = sms.getDisplayOriginatingAddress();
            mDate = sms.getTimestampMillis();

            if (mPrefs.getBoolean(SettingsFragment.SHOULD_I_ANSWER, false) &&
                    PackageUtils.isAppInstalled(mContext, "org.mistergroup.muzutozvednout")) {

                final ShouldIAnswerBinder shouldIAnswerBinder = new ShouldIAnswerBinder();
                shouldIAnswerBinder.setCallback(new ShouldIAnswerBinder.Callback() {
                    @Override
                    public void onNumberRating(String number, int rating) {
                        Log.i(TAG, "onNumberRating " + number + ": " + String.valueOf(rating));
                        shouldIAnswerBinder.unbind(context.getApplicationContext());
                        if (rating != ShouldIAnswerBinder.RATING_NEGATIVE) {
                            insertMessageAndNotify();
                        }
                    }

                    @Override
                    public void onServiceConnected() {
                        try {
                            shouldIAnswerBinder.getNumberRating(mAddress);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected() {
                    }
                });

                shouldIAnswerBinder.bind(context.getApplicationContext());
            } else {
                insertMessageAndNotify();
            }
        }
    }

    private void insertMessageAndNotify() {
        mUri = SmsHelper.addMessageToInbox(mContext, mAddress, mBody, mDate);

        Message message = new Message(mContext, mUri);
        ConversationPrefsHelper conversationPrefs = new ConversationPrefsHelper(mContext, message.getThreadId());

        // The user has set messages from this address to be blocked, but we at the time there weren't any
        // messages from them already in the database, so we couldn't block any thread URI. Now that we have one,
        // we can block it, so that the conversation list adapter knows to ignore this thread in the main list
        if (BlockedConversationHelper.isFutureBlocked(mPrefs, mAddress)) {
            BlockedConversationHelper.unblockFutureConversation(mPrefs, mAddress);
            BlockedConversationHelper.blockConversation(mPrefs, message.getThreadId());
            message.markSeen();
            BlockedConversationHelper.FutureBlockedConversationObservable.getInstance().futureBlockedConversationReceived();

            // If we have notifications enabled and this conversation isn't blocked
        } else if (conversationPrefs.getNotificationsEnabled() && !BlockedConversationHelper.getBlockedConversationIds(
                PreferenceManager.getDefaultSharedPreferences(mContext)).contains(message.getThreadId())) {
            Intent messageHandlerIntent = new Intent(mContext, NotificationService.class);
            messageHandlerIntent.putExtra(NotificationService.EXTRA_POPUP, true);
            messageHandlerIntent.putExtra(NotificationService.EXTRA_URI, mUri.toString());
            mContext.startService(messageHandlerIntent);

            UnreadBadgeService.update(mContext);
            NotificationManager.create(mContext);

        } else { // We shouldn't show a notification for this message
            message.markSeen();
        }

        if (conversationPrefs.getWakePhoneEnabled()) {
            PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "MessagingReceiver");
            wakeLock.acquire();
            wakeLock.release();
        }
    }
}

package xobyx.xcontactj.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;

import com.google.android.mms.pdu_alt.PduHeaders;

import java.util.ArrayList;
import java.util.Collection;

import xobyx.xcontactj.common.google.DraftCache;
import xobyx.xcontactj.common.utils.MessageUtils;
import xobyx.xcontactj.data.Conversation;
import xobyx.xcontactj.ui.ContentFragment;
import xobyx.xcontactj.ui.base.QKActivity;
import xobyx.xcontactj.ui.view.slidingmenu.SlidingMenu;
import xobyx.xcontactj.until.SmsHelper;

/**
 * Created by xobyx on 3/22/2016.
 * For xobyx.xcontactj.activities/XContactj2
 */
public abstract class XStartActivity  extends QKActivity implements SlidingMenu.SlidingMenuListener  {


    private final String TAG = "MainActivity";

    public final static String EXTRA_THREAD_ID = "thread_id";

    public static long sThreadShowing;

    private static final int THREAD_LIST_QUERY_TOKEN = 1701;
    private static final int UNREAD_THREADS_QUERY_TOKEN = 1702;
    public static final int DELETE_CONVERSATION_TOKEN = 1801;
    public static final int HAVE_LOCKED_MESSAGES_TOKEN = 1802;
    private static final int DELETE_OBSOLETE_THREADS_TOKEN = 1803;

    public static final String MMS_SETUP_DONT_ASK_AGAIN = "mmsSetupDontAskAgain";
    private SlidingMenu mSlidingMenu;

    public static Intent createAddContactIntent(String address) {
        // address must be a single recipient
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        if (SmsHelper.isEmailAddress(address)) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, address);
        } else {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, address);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        return intent;
    }

    public void showMenu() {
        mSlidingMenu.showMenu();
    }

    public SlidingMenu getSlidingMenu() {
        return mSlidingMenu;
    }

    public abstract void setConversation(long threadId);

    public abstract void switchContent(ContentFragment fragment, boolean b);

    public abstract void setConversation(long threadId, long rowId, String pattern, boolean animate);

    public static void confirmDeleteThreadDialog(DeleteThreadListener deleteThreadListener, ArrayList<Long> threadIds, boolean b, QKActivity mContext) {

    }

    public static class DeleteThreadListener implements DialogInterface.OnClickListener {
        private final Collection<Long> mThreadIds;
        private final Conversation.ConversationQueryHandler mHandler;
        private final Context mContext;
        private boolean mDeleteLockedMessages;

        public DeleteThreadListener(Collection<Long> threadIds, Conversation.ConversationQueryHandler handler, Context context) {
            mThreadIds = threadIds;
            mHandler = handler;
            mContext = context;
        }

        public void setDeleteLockedMessage(boolean deleteLockedMessages) {
            mDeleteLockedMessages = deleteLockedMessages;
        }

        @Override
        public void onClick(DialogInterface dialog, final int whichButton) {
            MessageUtils.handleReadReport(mContext, mThreadIds,
                    PduHeaders.READ_STATUS__DELETED_WITHOUT_BEING_READ, new Runnable() {
                        @Override
                        public void run() {
                            int token = DELETE_CONVERSATION_TOKEN;
                            if (mThreadIds == null) {
                                Conversation.startDeleteAll(mHandler, token, mDeleteLockedMessages);
                                DraftCache.getInstance().refresh();
                            } else {
                                Conversation.startDelete(mHandler, token, mDeleteLockedMessages, mThreadIds);
                            }
                        }
                    }
            );
            dialog.dismiss();
        }
    }
}

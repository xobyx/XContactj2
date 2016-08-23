package xobyx.xcontactj.common;

import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.XStartActivity;
import xobyx.xcontactj.data.Conversation;
import xobyx.xcontactj.ui.base.QKActivity;
import xobyx.xcontactj.ui.dialog.DefaultSmsHelper;
import xobyx.xcontactj.ui.dialog.QKDialog;
import xobyx.xcontactj.until.SmsHelper;

public class DialogHelper {
    private static final String TAG = "DialogHelper";

    public static void showDeleteConversationDialog(XStartActivity context, long threadId) {
        Set<Long> threadIds = new HashSet<>();
        threadIds.add(threadId);
        showDeleteConversationsDialog(context, threadIds);
    }

    public static void showDeleteConversationsDialog(final XStartActivity context, final Set<Long> threadIds) {
        new DefaultSmsHelper(context, R.string.not_default_delete).showIfNotDefault(null);

        final Set<Long> threads = new HashSet<>(threadIds); // Make a copy so the list isn't reset when multi-select is disabled
        new QKDialog()
                .setContext(context)
                .setTitle(R.string.delete_conversation)
                .setMessage(context.getString(R.string.delete_confirmation, threads.size()))
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Log.d(TAG, "Deleting threads: " + Arrays.toString(threads.toArray()));
                        Conversation.ConversationQueryHandler handler = new Conversation.ConversationQueryHandler(context.getContentResolver());
                        Conversation.startDelete(handler, 0, false, threads);
                        Conversation.asyncDeleteObsoleteThreads(handler, 0);
                        context.showMenu();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();

    }

    public static void showDeleteFailedMessagesDialog(final XStartActivity context, final Set<Long> threadIds) {
        new DefaultSmsHelper(context, R.string.not_default_delete).showIfNotDefault(null);

        final Set<Long> threads = new HashSet<>(threadIds); // Make a copy so the list isn't reset when multi-select is disabled
        new QKDialog()
                .setContext(context)
                .setTitle(R.string.delete_all_failed)
                .setMessage(context.getString(R.string.delete_all_failed_confirmation, threads.size()))
                .setPositiveButton(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                for (long threadId : threads) {
                                    SmsHelper.deleteFailedMessages(context, threadId);
                                }
                            }
                        }).start();
                    }
                })

                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    public static void showChangelog(QKActivity context) {



    }

}

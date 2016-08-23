package xobyx.xcontactj.ui.dialog.conversationdetails;


import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import xobyx.xcontactj.R;
import xobyx.xcontactj.common.utils.DateFormatter;
import xobyx.xcontactj.data.Conversation;
import xobyx.xcontactj.interfaces.ConversationDetails;
import xobyx.xcontactj.ui.base.QKActivity;
import xobyx.xcontactj.ui.dialog.QKDialog;
import xobyx.xcontactj.ui.view.QKTextView;


public class ConversationDetailsDialog implements ConversationDetails {

    private QKActivity mContext;
    private FragmentManager mFragmentManager;

    public ConversationDetailsDialog(QKActivity context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void showDetails(Conversation conversation) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        AbsListView.LayoutParams listParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = View.inflate(mContext, R.layout.dialog_conversation_details, null);
        view.setLayoutParams(listParams);
        ((QKTextView) view.findViewById(R.id.date)).setText(DateFormatter.getDate(mContext, conversation.getDate()));
        ((QKTextView) view.findViewById(R.id.message_count)).setText(Integer.toString(conversation.getMessageCount()));
        ((QKTextView) view.findViewById(R.id.recipients)).setText(mContext.getString(
                R.string.dialog_conversation_details_recipients, Integer.toString(conversation.getRecipients().size())));

        ListView listView = new ListView(mContext);
        listView.setLayoutParams(params);
        listView.addHeaderView(view);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setAdapter(new ConversationDetailsContactListAdapter(mContext, conversation.getRecipients()));

        new QKDialog()
                .setContext(mContext)
                .setTitle(R.string.dialog_conversation_details_title)
                .setCustomView(listView)
                .setPositiveButton(R.string.okay, null)
                .show();
    }

}

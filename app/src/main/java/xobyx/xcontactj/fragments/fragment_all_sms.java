package xobyx.xcontactj.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import xobyx.xcontactj.R;
import xobyx.xcontactj.views.StateView;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_all_sms extends Fragment
//implements LoaderManager.LoaderCallbacks<Cursor>,
     //   RecyclerCursorAdapter.ItemClickListener<Conversation>, RecyclerCursorAdapter.MultiSelectListener {
{

    private ListView conv_list;
    private ListView thread_list;
    private StateView inject;

    public fragment_all_sms() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_fragment_all_sms, container, false);

        thread_list = (ListView) inflate.findViewById(R.id.sms_all_thread);
        conv_list = (ListView) inflate.findViewById(R.id.sms_all_conv);
        return inflate;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor query = getActivity().getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI, null, null, null, Telephony.Sms.Conversations.DEFAULT_SORT_ORDER);
        //int a=query.getColumnIndex(Telephony.Sms.Conversations.)
        if(query!=null)
        {
            if(query.moveToFirst())
            {




            }

        }


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inject = StateView.inject(view);
        inject.showLoading();
    }

    public static Fragment newInstance() {
        return new fragment_all_sms();
    }

  /*  private ConversationListAdapter mAdapter;
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), SmsHelper.CONVERSATIONS_CONTENT_PROVIDER, Conversation.ALL_THREADS_PROJECTION,
                BlockedConversationHelper.getCursorSelection(mPrefs, mShowBlocked),
                BlockedConversationHelper.getBlockedConversationArray(mPrefs), "date DESC");
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            // Swap the new cursor in.  (The framework will take care of closing the, old cursor once we return.)
            mAdapter.changeCursor(data);
            if (mPosition != 0) {
                mRecyclerView.scrollToPosition(Math.min(mPosition, data.getCount() - 1));
                mPosition = 0;
            }
        }


        if(data != null && data.getCount() > 0)inject.showContent(); else inject.showEmpty();
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null) {
            mAdapter.changeCursor(null);
        }
    }

    @Override
    public void onItemClick(Conversation object, View view) {

    }

    @Override
    public void onItemLongClick(Conversation object, View view) {

    }

    @Override
    public void onMultiSelectStateChanged(boolean enabled) {

    }

    @Override
    public void onItemAdded(long id) {

    }

    @Override
    public void onItemRemoved(long id) {

    }*/
}

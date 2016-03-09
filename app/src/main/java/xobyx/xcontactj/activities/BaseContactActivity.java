package xobyx.xcontactj.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.ContactBaseAdapter;
import xobyx.xcontactj.until.Contact;

/**
 * Created by xobyx on 12/14/2014.
 * c# to java
 */
public abstract class BaseContactActivity extends AppCompatActivity {

    LoaderManager.LoaderCallbacks<List<Contact>> CallBack = new LoaderManager.LoaderCallbacks<List<Contact>>() {

        @Override
        public Loader<List<Contact>> onCreateLoader(int var1, Bundle var2) {
            return OnCreateContactLoader(var1, var2);
        }

        @Override
        public void onLoadFinished(Loader<List<Contact>> var1, List<Contact> var2) {

            onContactLoaderFinish(var1, var2);
            setListShown(true, false);
        }

        @Override
        public void onLoaderReset(Loader<List<Contact>> var1) {
        }
    };
    private ListView mList;
    private Runnable mRequestFocus = new Runnable() {
        public void run() {
            mList.focusableViewAvailable(mList);
        }
    };
    private FrameLayout mListContainer;
    private LinearLayout mProgressContainer;
    private ContactBaseAdapter mAdapter;
    private AdapterView.OnItemClickListener mOnClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            onItemClicked(mAdapter, v, position, id);
        }
    };
    private boolean mListShown = true;
    private Handler mHandler = new Handler();
    private boolean mFinishedStart = false;

    @Override
    public void onContentChanged() {
        super.onContentChanged();

      //  mList = (ListView) findViewById(R.id.margelist);
       // mListContainer = (FrameLayout) findViewById(R.id.listContainer);
        mProgressContainer = (LinearLayout) findViewById(R.id.progressContainer);


        if (mList == null) {
            throw new RuntimeException(
                    "Your content must have TouchedItem ListView whose id attribute is " +
                            "'android.R.id.list'"
            );
        }
        setListShown(false, true);

        mList.setOnItemClickListener(mOnClickListener);
        if (mFinishedStart) {
            setListAdapter(mAdapter);
        }
        mHandler.post(mRequestFocus);

        mFinishedStart = true;
    }

    @Override
    protected void onCreate(Bundle var1) {
        super.onCreate(var1);
        CheckList();
    }

    void Filter(String qur) {
        mAdapter.getFilter().filter(qur);

    }

    void StartGetContacts() {
        if (CallBack != null)
            getSupportLoaderManager().initLoader(0, null, CallBack);
        else {
            throw new RuntimeException("You Implement Loader CallBack first");
        }
    }

    private void CheckList() {
        if (mList != null) {
            return;
        }

        setContentView(R.layout.activity_marge_contacts);


    }

    private void setListShown(boolean shown, boolean animate) {

        if (mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with TouchedItem custom content view");
        }
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        this, android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        this, android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        this, android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        this, android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        }
    }

    public void setSelection(int position) {
        mList.setSelection(position);
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {
        return mList.getSelectedItemPosition();
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {
        return mList.getSelectedItemId();
    }

    /**
     * Get the activity's list view widget.
     */
    public ListView getListView() {
        CheckList();
        return mList;
    }

    protected abstract void onContactLoaderFinish(Loader<List<Contact>> var1, List<Contact> var2);

    protected abstract Loader<List<Contact>> OnCreateContactLoader(int var1, Bundle var2);

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param contactBaseAdapter The AdapterView where the click happened.
     * @param view               The view within the AdapterView that was clicked (this
     *                           will be TouchedItem view provided by the adapter)
     * @param position           The position of the view in the adapter.
     * @param id                 The row id of the item that was clicked.
     */

    protected void onItemClicked(ContactBaseAdapter contactBaseAdapter, View view, int position, long id) {

    }

    public void setListAdapter(ContactBaseAdapter listAdapter) {
        synchronized (this) {
            CheckList();
            mAdapter = listAdapter;
            mList.setAdapter(mAdapter);
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        CheckList();
        super.onRestoreInstanceState(state);
    }

    /**
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRequestFocus);
        super.onDestroy();
    }
}

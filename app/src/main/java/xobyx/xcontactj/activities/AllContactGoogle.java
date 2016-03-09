package xobyx.xcontactj.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import xobyx.xcontactj.R;
import xobyx.xcontactj.me.ContactListFilter;
import xobyx.xcontactj.me.DefaultContactBrowseListFragment;
import xobyx.xcontactj.me.OnContactBrowserActionListener;

public class AllContactGoogle extends Activity implements SearchView.OnQueryTextListener {

    private DefaultContactBrowseListFragment mFragment;



    private OnContactBrowserActionListener fg = new OnContactBrowserActionListener() {
        @Override
        public void onViewContactAction(Uri contactUri) {
            Intent j = new Intent(Intent.ACTION_VIEW);
            j.setData(contactUri);
            mFragment.getAdapter();
            startActivity(j);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        mFragment = (DefaultContactBrowseListFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mFragment.setOnContactListActionListener(fg);
        ContactListFilter g=ContactListFilter.createFilterWithType(ContactListFilter.FILTER_TYPE_WITH_PHONE_NUMBERS_ONLY);
        mFragment.setFilter(g);
        //mFragment.setQueryString("?",false);
        // mFragment.setSectionHeaderDisplayEnabled(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search2).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mFragment.setQueryString(newText, true);
        return true;
    }
}

package xobyx.xcontactj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.List;

import xobyx.xcontactj.until.Contact;

/**
 * Created by xobyx on 3/12/2016.
 * For xobyx.xcontactj.fragments/XContactj
 */
abstract class BaseFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Contact>>
        , AbsListView.OnItemClickListener {

    NetFragment.ContactsAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null)
        {

        }
    }

    @Override
    public abstract Loader<List<Contact>> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {

    }

    @Override
    abstract public void onItemClick(AdapterView<?> parent, View view, int position, long id);
}

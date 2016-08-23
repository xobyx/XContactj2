package xobyx.xcontactj.fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.ME;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostFragment extends Fragment implements   LoaderManager.LoaderCallbacks<Cursor> {


    public MostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_all_sms, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       getActivity().getSupportLoaderManager().initLoader(0,null,this);
    }

    public static Fragment newInstance() {
        return new fragment_all_sms();
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ME._uri,null,null,null, ContactsContract.CommonDataKinds.Callable.TIMES_CONTACTED);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

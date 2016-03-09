package xobyx.xcontactj.fragments;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Fragment;
import android.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import xobyx.xcontactj.R;
import xobyx.xcontactj.me.*;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.FontTextView;
import xobyx.xcontactj.views.LetterImageView;

/**
 * Not use...
 * Created by xobyx on 6/30/2015.
 */
public class Net2  extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private int net=0;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0,null,this);
    }

    public static Net2 newInstance(int i) {
        Net2 sd=new Net2();
        sd.net=i;
        return sd;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       return new CursorLoader(getActivity().getBaseContext(),ME._uri,null,ME.DatabaseSelector[0],ME.getDatabaseArg[0],null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        CursorAdapter y=new CursorAdapter(getActivity(),data) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return View.inflate(context, R.layout.child_main,null);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
                String m = cursor.getString(nameColumnIndex);
                LetterImageView mk = (LetterImageView) view.findViewById(R.id.imagec);
                ((FontTextView) view.findViewById(R.id.ptextname)).setText(m);
                int Photothumb = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_THUMBNAIL_URI);
                if (!cursor.isNull(Photothumb))
                    mk.setImageURI(Uri.parse(cursor.getString(Photothumb)));
                else
                    mk.setLetter(m.charAt(0));


            }
        };
        setListShown(true);
        setListAdapter(y);
     //   getListView().setAdapter(y);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}

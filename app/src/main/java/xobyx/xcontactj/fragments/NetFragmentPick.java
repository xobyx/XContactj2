package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

import java.util.List;

import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.XPickDialog;

/**
 * Created by xobyx on 12/10/2014.
 * c# to java
 */
public class NetFragmentPick extends NetFragment {


    public static NetFragmentPick newInstance(int net) {

        NetFragmentPick p = new NetFragmentPick();
        Bundle i = new Bundle();
        i.putInt(ARG_SECTION_NUMBER, net);

        p.setArguments(i);
        return p;
    }

    @Override
    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {
        return super.onCreateView(var1, var2, var3);
    }

    @Override
    public void onViewCreated(View var1, @Nullable Bundle var2) {
        super.onViewCreated(var1, var2);
    }

    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    protected int getListModeForFragment() {
        //return super.getListModeForFragment();
    return 0;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Adapter ad = parent.getAdapter();

        final Contact d = ((Contact) ad.getItem(position));
        final Intent f = new Intent();
        final MainActivity activity = (MainActivity) getActivity();

        if (d.Phone.size() > 1) {


            final XPickDialog pickDialog = XPickDialog
                    .Instance(activity)
                    .setListPhoneList(d.Phone)
                    .SetOnFinish(new XPickDialog.OnFinish() {
                        @Override
                        public void DialogFinish(int id, boolean isDefault) {
                            f.setData(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, d.Phone.get(id).ID));
                            if (isDefault)
                                ME.setSuperPrimary(activity, Long.parseLong(d.Phone.get(id).ID));
                            activity.setResult(Activity.RESULT_OK, f);
                            activity.finish();
                        }
                    })
                    .SetOnCancel(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {

                        }
                    })
                    .build();
            pickDialog.show();


        } else {
            f.setData(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, d.Phone.get(0).ID));
            activity.setResult(Activity.RESULT_OK, f);
            activity.finish();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
        return super.onCreateLoader(id, args);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void SearchFor(String Qu) {
        super.SearchFor(Qu);
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        super.onLoadFinished(loader, data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setupPinndHeader(boolean all) {
        super.setupPinndHeader(all);

    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        super.onLoaderReset(loader);

    }


}

package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import java.util.Collections;
import java.util.List;

import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.until.XPickDialog;

/**
 * Created by xobyx on 12/10/2014.
 * c# to java
 */
public class NetFragmentPick extends NetFragment {

    private MainActivity mActivity;
    public static NetFragmentPick newInstance(int net) {

        NetFragmentPick p = new NetFragmentPick();
        Bundle i = new Bundle();
        i.putInt(ARG_SECTION_NUMBER, net);

        p.setArguments(i);
        return p;
    }
    @Override
    public void onCreate(Bundle var1) {



        super.onCreate(var1);

       // list_mode = 0;

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
        final MainActivity activity = (xobyx.xcontactj.activities.MainActivity) getActivity();

        if (d.Phone.size() > 1) {


            final XPickDialog build = XPickDialog
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
            build.show();


        } else {
            f.setData(Uri.withAppendedPath(ContactsContract.Data.CONTENT_URI, d.Phone.get(0).ID));
            activity.setResult(Activity.RESULT_OK, f);
            activity.finish();
        }
    }
    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {

        Collections.sort(data);
        ME.$[net].clear();
        ME.$[net].addAll(data);
        mShowNumber = SettingHelp.getShowNumb(getActivity().getBaseContext());
        if (mShowNumber && list_mode == 0) {
            //mAdapter = new ContactNumberAdapter(getActivity().getBaseContext(), ME.$[net]);
            mAdapter=new ContactsAdapter(getActivity(),ME.$[net],SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);

        }
        else {
          //  mAdapter = new ContactBaseAdapter(getActivity().getBaseContext(), ME.$[net], list_mode);
            mAdapter=new ContactsAdapter(getActivity(),ME.$[net],SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);


        }


       // mAdapter.mClip = SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0;


        if(Me!=null)
        {
            Me.setAdapter(mAdapter);
        }



        //mAdapter.notifyDataSetChanged();


    }


}

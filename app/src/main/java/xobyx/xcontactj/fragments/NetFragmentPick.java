package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

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



}

package xobyx.xcontactj.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.SendBalanceActivity;
import xobyx.xcontactj.adapters.PhoneAdapter;
import xobyx.xcontactj.until.Contact;

/**
 * Created by xobyx on 1/22/2016.
 * For xobyx.xcontactj.fragments/XContactj
 */
public class widget_manger implements PhoneAdapter.NumberOnClickListener {

    private final Context mcontext;
    private Contact contact;

    public widget_manger(Context x,Contact m)
    {

        mcontext = x;
        contact=m;
    }
        @Override
        public void onClick(int op, Contact.PhoneClass num) {
            Uri uri = contact.LookupUri;
            Intent i = new Intent();
            switch (op) {
                case R.id.d_call:
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + num.Fnumber));
                    break;

                case R.id.d_sms:
                    i.setAction(Intent.ACTION_SENDTO);
                    i.setData(Uri.parse("smsto:" + num.Fnumber));
                    break;

                case R.id.d_share:
                    i.setAction(Intent.ACTION_SEND);
                    i.setType("num/plain");
                    i.putExtra(Intent.EXTRA_TEXT, num.User + " :\n " + num.Fnumber + "\nShared using XOBYX contacts");
                    break;

                case R.id.d_edit:
                    i.setAction(Intent.ACTION_EDIT);
                    i.setData(uri);
                    break;

                case R.id.d_delete:
                    Toast.makeText(mcontext, "Delete :" + num.User + " " + num.Fnumber, Toast.LENGTH_SHORT).show();
                    break;

                case R.id.d_minfo:
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(uri);
                    break;

                case R.id.d_sendbla:
                    i.setClass(mcontext, SendBalanceActivity.class);
                    i.putExtra("NAME", num.User);
                    i.putExtra("NUMBER", num.Fnumber);
                    break;

            }
            mcontext.startActivity(i);
        }

}

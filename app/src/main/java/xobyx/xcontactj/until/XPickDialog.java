package xobyx.xcontactj.until;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.Contact.Phones;

/**
 * Created by xobyx on 12/6/2014.
 * c# to java
 */
public class XPickDialog {

    public static final int PICK_MULTI = 1;
    public static final int PICK_SINGLE = 0;
    private static int mPick = PICK_SINGLE;
    LIST_TYPE mType;
    private List<String> list_s;
    private Context mContext;
    private List<Phones> list;
    private OnFinish finish;
    private DialogInterface.OnCancelListener onCancel;
    private AlertDialog mDialog;
    private OnMultiFinish onMultiFinish;

    public static XPickDialog Instance(Context s) {

        XPickDialog j = new XPickDialog();
        j.mContext = s;
        return j;
    }

    public XPickDialog setListPhoneList(List<Phones> j) {

        this.list = j;
        this.mType = LIST_TYPE.PHONE_CLASS;
        return this;
    }

    public XPickDialog setListString(List<String> j) {

        this.list_s = j;
        this.mType = LIST_TYPE.PHONE_NUMBER;
        return this;
    }

    public XPickDialog SetOnMultiFinish(OnMultiFinish i) {
        this.onMultiFinish = i;
        return this;
    }

    public XPickDialog SetOnFinish(OnFinish i) {
        this.finish = i;
        return this;
    }

    public XPickDialog SetOnCancel(DialogInterface.OnCancelListener f) {
        this.onCancel = f;
        return this;
    }

    public XPickDialog GetDialog(List<Phones> listx, final OnMultiFinish finish, DialogInterface.OnCancelListener f) {

        list = listx;
        onMultiFinish = finish;
        mType=LIST_TYPE.PHONE_CLASS;
        onCancel = f;


        return this;
    }

    public XPickDialog GetDialog(List<Phones> mlist, final OnFinish finishs, DialogInterface.OnCancelListener f) {
        mType=LIST_TYPE.PHONE_CLASS;


        list = mlist;
        finish = finishs;
        mPick = PICK_SINGLE;
        onCancel = f;


        return this;
    }

    public XPickDialog build() {

        AlertDialog.Builder m=new AlertDialog.Builder(mContext);
        mDialog = m.create();
        mDialog.setContentView(R.layout.dialog_pick_contact);
        final Button cancel = (Button) mDialog.findViewById(R.id.cd_cancel);
        final AppCompatCheckedTextView defu = (AppCompatCheckedTextView) mDialog.findViewById(R.id.cd_check_default);
        final ListView mList = (ListView) mDialog.findViewById(R.id.cd_list);
        final Button ok = (Button) mDialog.findViewById(R.id.cd_ok);
        mDialog.setOnCancelListener(onCancel);
        mDialog.setCanceledOnTouchOutside(false);
        if (mPick == PICK_SINGLE) {
            mList.setAdapter(mType == LIST_TYPE.PHONE_CLASS
                    ? new adp(mContext, LIST_TYPE.PHONE_CLASS, list)
                    : new adp(mContext, LIST_TYPE.PHONE_NUMBER, list_s));
            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int position = mList.getCheckedItemPosition();
                    if (position > -1) {
                        finish.DialogFinish(position, defu.isChecked());
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(mContext, "Select Phone First..", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        if (mPick == PICK_MULTI) {
            defu.setVisibility(View.GONE);
            mList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mList.setAdapter(mType == LIST_TYPE.PHONE_CLASS
                    ? new adp(mContext, LIST_TYPE.PHONE_CLASS, list, true)
                    : new adp(mContext, LIST_TYPE.PHONE_NUMBER, list_s, true));
            ok.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final SparseBooleanArray position = mList.getCheckedItemPositions();
                    if (position.size() > 0) {

                        String[] f = new String[position.size()];
                        for (int i = 0; i < position.size(); i++) {
                            f[i] = (String) mList.getItemAtPosition(position.keyAt(i));

                        }
                        onMultiFinish.DialogFinish(f);
                        mDialog.dismiss();
                    } else {
                        Toast.makeText(mContext, "Select Phone First..", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        return this;
    }

    public void show() {
        mDialog.show();
    }

    enum LIST_TYPE {
        PHONE_CLASS(0), PHONE_NUMBER(1);

        private int value;

        LIST_TYPE(int i) {

            value = i;
        }

        public int getValue() {
            return value;
        }


    }

    public interface OnFinish {
        void DialogFinish(int id, boolean isDefault);


    }

    public interface OnMultiFinish {
        void DialogFinish(String[] position);
    }

    private class adp extends ArrayAdapter<String> {
        private final List<?> lis;
        private LIST_TYPE mType;


        adp(Context s, LIST_TYPE g, List<?> t) {
            super(s, android.R.layout.select_dialog_singlechoice, android.R.id.text1);
            mType = g;
            lis = t;

        }

        adp(Context s, LIST_TYPE ft, List<?> t, boolean mult) {
            super(s, android.R.layout.select_dialog_multichoice, android.R.id.text1);
            lis = t;
            mType = ft;


        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = super.getView(position, convertView, parent);
            if (mType == LIST_TYPE.PHONE_NUMBER)
                ((Checkable) view.findViewById(android.R.id.text1)).setChecked(((Phones) lis.get(position)).IsPrimyer);

            return view;
        }

        @Override
        public long getItemId(int position) {
            return 0L;
        }

        @Override
        public String getItem(int position) {
            if (mType == LIST_TYPE.PHONE_CLASS) return ((Phones) lis.get(position)).Fnumber;
            return (String) lis.get(position);
        }

        @Override
        public int getCount() {
            return lis.size();
        }


    }
}

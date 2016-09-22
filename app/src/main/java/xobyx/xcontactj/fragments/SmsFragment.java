package xobyx.xcontactj.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.ContactSpecificsActivity;
import xobyx.xcontactj.adapters.BaseRecycleAdapter;
import xobyx.xcontactj.base.massage;
import xobyx.xcontactj.views.FontTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SmsFragment extends AsyncLoadFragment<massage> implements View.OnTouchListener {

    private static final String ARG_NUMBERS_LIST = "number_list";
    private ArrayList<String> Numbers;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view == recyclerView) {
            ((ContactSpecificsActivity) getActivity()).hideSoftKeyboard(view);
        }
        return false;
    }

    private static final String ARG_POS = "pos";
    private static final String ARG_NET = "net";
    private static final String ARG_ALL = "all";


    private ArrayList<massage> items_b = new ArrayList<>();
    private ArrayList<String> mSendtoNumber = new ArrayList<>();
    private AdapterView.OnItemLongClickListener ItemLongClick = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final BaseAdapter ad = (BaseAdapter) parent.getAdapter();
            final massage item = (massage) ad.getItem(position);
            switch (item.state) {
                case Telephony.Sms.STATUS_COMPLETE: {
                    //TODO:add dialog to del
                }
                case Telephony.TextBasedSmsColumns.STATUS_PENDING:
                    //TODO:add dialog to ..
                case Telephony.TextBasedSmsColumns.STATUS_FAILED:
                    //TODO:add dialog to resend
            }
            return true;
        }
    };
    private TextView text;
    private massage mLastFMessage;
    private RecyclerView recyclerView;



    public SmsFragment() {
        // Required empty public constructor
    }


    public static SmsFragment newInstance(ArrayList<String> numbersList, Parcelable l) {
        SmsFragment b = new SmsFragment();
        Bundle s = new Bundle();
        s.putParcelable("message", l);
        s.putStringArrayList(ARG_NUMBERS_LIST, numbersList);
        b.setArguments(s);
        return b;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.sms_listview);
        recyclerView.setOnTouchListener(this);
        install(recyclerView);
        return view;

    }

    private void install(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new SmsAdapter(getActivity(), items_b, R.layout.smshead);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle var2) {
        super.onViewCreated(v, var2);
        AppCompatSpinner a = (AppCompatSpinner) v.findViewById(R.id.spinner);

        if (Numbers.size() == 0) return;
        List<String> mk = new ArrayList<>();
        mk.add("All");
        mk.addAll(Numbers);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, mk);
        a.setAdapter(arrayAdapter);
        a.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setMessageType(((String) parent.getAdapter().getItem(position)));
                String x = (String) parent.getAdapter().getItem(position);
                mSendtoNumber.clear();
                if (x.equals("All")) {

                    mSendtoNumber.addAll(Numbers);

                }
                else {

                    mSendtoNumber.add(x);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // recyclerView.setOnItemLongClickListener(ItemLongClick);

        ImageView send = (ImageView) v.findViewById(R.id.sms_send);
        text = (TextView) v.findViewById(R.id.sms_messege_text);

        final ArrayList<String> pend = getPendingSms();
        //TODO: SMS
        //if (!pend.equals(""))
        //    text.setText(pend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                massage t = new massage();
                t.body = text.getText().toString();
                t.type = Telephony.Sms.MESSAGE_TYPE_SENT;
                t.state = Telephony.Sms.STATUS_PENDING;
                t.date = new Date();
                items_b.add(t);
                mAdapter.notifyDataSetChanged();

                //recyclerView.smoothScrollToPosition(items.size());
                text.setText("");
                newMessage(t);
            }
        });

    }

    @NonNull
    protected ArrayList<String> getPendingSms() {
        ArrayList<String> temp = new ArrayList<>();
        SharedPreferences preferences = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        for (String number : Numbers) {

            String sms = preferences.getString(number, "");
            if (!sms.isEmpty())
                temp.add(sms);
        }
        return temp;
    }

    private void newMessage(final massage i) {
        // SmsManager.getDefault().sendTextMessage(m);
        BroadcastReceiver o = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int in = getResultCode();
                if (in == Activity.RESULT_OK) {
                    i.state = Telephony.Sms.STATUS_COMPLETE;

                }
                else {

                  /*  i.state = Telephony.Sms.STATUS_FAILED;
                    Toast.makeText(context, "Can't Send the Message!", Toast.LENGTH_SHORT).show();
                    final Contact b = contact;
                    Intent u = new Intent(getActivity().getBaseContext().getApplicationContext()
                            , ContactSpecificsActivity.class);
                    //u.putExtra("pos", mPos);
                    //u.putExtra("net", mNet);
                    u.putExtra("sec", 2);

                    u.putExtra("message", i);
                    final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, u, PendingIntent.FLAG_UPDATE_CURRENT);
                    xobyx.xcontactj.until.xNotification.notify(context, null, 0, "Sending Failed", "Message to " + b.Name + "is not send ,click to resend", pendingIntent);
*/

                }
                context.unregisterReceiver(this);
                //  items.set(id, i);
                mAdapter.notifyDataSetChanged();
            }

        };

        final PendingIntent update = PendingIntent.getBroadcast(getActivity(), 0, new Intent("update"), 0);
        getActivity().registerReceiver(o, new IntentFilter("update"));

        for (String s : mSendtoNumber) {
            SmsManager.getDefault().sendTextMessage(s, null, i.body, update, update);
        }


    }

    SmsAdapter mAdapter;




    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle var1) {
        super.onActivityCreated(var1);


    }

    private static final Uri _uri = Uri.parse("content://sms/");

    public ArrayList<massage> Start() {

        ArrayList<massage> items =new ArrayList<>();
        if(Numbers.size()==0) return null;
        // Uri m= Telephony.Threads;
        String m = "";
        //new String[]{"%" + contact.Phone.get(0).Fnumber.substring(4)}
        String[] h = new String[Numbers.size()];

        for (int i = 0; i < Numbers.size(); i++) {
            String phone = Numbers.get(i);

            m += " address LIKE ? ";
            if (i != Numbers.size() - 1)
                m += "OR";
            h[i] = "%" + phone;
        }
        Cursor d = getActivity().getContentResolver().query(_uri, new String[]{Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE, Telephony.Sms.TYPE, Telephony.Sms.DATE, Telephony.Sms.STATUS}, m, h, "date ASC");
        if (d != null) {
            final int adder = d.getColumnIndex(Telephony.Sms.ADDRESS);
            final int body = d.getColumnIndex(Telephony.Sms.BODY);
            final int data = d.getColumnIndex(Telephony.Sms.DATE);
            final int type = d.getColumnIndex(Telephony.Sms.TYPE);
            final int state = d.getColumnIndex(Telephony.Sms.STATUS);

            while (d.moveToNext()) {
                massage a = new massage();
                a.body = d.getString(body);
                a.addres = d.getString(adder);
                a.date = new Date(d.getLong(data));
                a.type = d.getInt(type);
                a.state = d.getInt(state);


                items.add(a);
            }
            d.close();
        }
        return items;
    }

    @Override
    public void doAfterFinish(ArrayList<massage> result) {
        items_b.clear(); items_b.addAll(result);
        mAdapter.notifyDataSetChanged();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onPause() {
        super.onPause();
        ///FIXME:always crash
        if (Numbers.size() == 0 || text == null) return;
        if ( text.getText().length() != 0) {
            for (String s : mSendtoNumber) {
                getActivity().getSharedPreferences("sms", Context.MODE_APPEND).edit().putString(s, text.getText().toString()).commit();

            }

        }
        else {
            for (String s : mSendtoNumber) {
                getActivity().getSharedPreferences("sms", Context.MODE_APPEND).edit().remove(s).commit();

            }

        }



    }

    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        final Bundle arg = getArguments();
        if (arg != null) {

            if (arg.containsKey("message")) {
                mLastFMessage = arg.getParcelable("message");
            }
            Numbers = arg.getStringArrayList(ARG_NUMBERS_LIST);


        }

    }


    class SmsAdapter extends BaseRecycleAdapter<SmsHolder, massage> {


        private String mType = "All";

        public SmsAdapter(Context context, List<massage> s, int layout) {
            super(context, s, layout);
            //FIXME: Crash
            this.Filter = new FilterBuilder<massage>() {
                @Override
                public boolean IsMatch(massage b) {
                    return b.addres != null && b.addres.equals(mType);
                }
            };

        }

        @Override
        public void onBindViewHolder(SmsHolder holder, massage massage) {


            // holder.smsBase.setBackgroundResource(R.drawable.com_android_mms_hairline_right);

            holder.smsBase.setBackgroundResource(R.drawable.com_android_mms_hairline_right);

            switch (massage.state) {
                case Telephony.Sms.STATUS_FAILED://listener
                    holder.smsStute.setText("Failed");
                    holder.smsStute.setTextColor(Color.RED);
                    break;
                case Telephony.Sms.STATUS_COMPLETE://C
                    holder.smsStute.setText("Send");
                    holder.smsStute.setTextColor(Color.DKGRAY);
                    break;
                case Telephony.Sms.STATUS_PENDING://P
                    holder.smsStute.setText("Pending");
                    holder.smsStute.setTextColor(Color.DKGRAY);
                    break;
                case Telephony.Sms.STATUS_NONE:
                    holder.smsStute.setText("");
                    holder.smsStute.setTextColor(Color.DKGRAY);

            }

            if (Telephony.Sms.MESSAGE_TYPE_INBOX == massage.type) {

                holder.smsBase.setBackgroundResource(R.drawable.com_android_mms_hairline_left);
                holder.smsBase.setLayoutParams(x);

            }
            DateFormat.getTimeInstance(DateFormat.SHORT);
            holder.smsTime.setText(DateFormat.getTimeInstance().format(massage.date));

            holder.smsHeadBody.setText(massage.body);

            holder.smsHeadDate.setText(android.text.format.DateFormat.getMediumDateFormat(getActivity()).format(massage.date));
        }

        LinearLayout.LayoutParams x = new LinearLayout.LayoutParams(-2, -1, Gravity.START);

        @Override
        public SmsHolder onCreateViewHolder(View view, int position) {
            return new SmsHolder(view);
        }

        public void setMessageType(String numb) {


            if (!mType.equals(numb)) {

                mType = numb;

                if (mType.equals("All")) {
                    RestartFilter();
                }
                else {
                    StartFilter();
                }
            }
        }
    }

    class SmsHolder extends RecyclerView.ViewHolder {
        public TextView smsHeadDate;
        public TextView smsStute;
        public TextView smsTime;
        public RelativeLayout smsBase;
        public FontTextView smsHeadBody;

        public SmsHolder(View itemView) {
            super(itemView);
            smsBase = (RelativeLayout) itemView.findViewById(R.id.sms_base);
            smsHeadBody = (FontTextView) itemView.findViewById(R.id.sms_head_body);
            smsTime = (TextView) itemView.findViewById(R.id.sms_time);
            smsStute = (TextView) itemView.findViewById(R.id.sms_stute);
            smsHeadDate = (TextView) itemView.findViewById(R.id.sms_head_date);
        }
    }
}



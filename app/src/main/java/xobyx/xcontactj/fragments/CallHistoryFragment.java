package xobyx.xcontactj.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.BaseRecycleAdapter;
import xobyx.xcontactj.base.Base_CallLog;
import xobyx.xcontactj.until.AsyncLoad;
import xobyx.xcontactj.until.Contact;

import static xobyx.xcontactj.until.ME.$;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallHistoryFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, AsyncLoad.IRun {


    private static final String ARG_NET = "net";
    private static final String ARG_POS = "pos";
    private static final String ARG_ALL = "all";
    private int callType;
    private CallHistoryAdapter mAdapter;
    private ArrayList<Base_CallLog> vcall = new ArrayList<>();
    private int mPos;
    private int mNet;
    private LinearLayout hide;
    private boolean mAll;


    public CallHistoryFragment() {
        // Required empty public constructor
    }

    static public CallHistoryFragment newInstance(int pos, int net, boolean all) {
        CallHistoryFragment p = new CallHistoryFragment();
        Bundle s = new Bundle();
        s.putInt(ARG_NET, net);
        s.putInt(ARG_POS, pos);
        s.putBoolean(ARG_ALL, all);
        p.setArguments(s);
        return p;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View b = inflater.inflate(R.layout.fragment_call_history, null, false);
        hide = (LinearLayout) b.findViewById(R.id.call_log_empty_layout);
        RecyclerView rv = (RecyclerView) b.findViewById(R.id.calls_listView);
        setupRecyclerView(rv);
        return b;


    }

    private void setupRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        mAdapter = new CallHistoryAdapter(getActivity(), vcall, R.layout.child_calls);
        rv.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle var2) {
        super.onViewCreated(v, var2);

        AppCompatSpinner i = (AppCompatSpinner) v.findViewById(R.id.calls_call_type_selector);
        i.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.call_history)));

        i.setOnItemSelectedListener(this);

    }

    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        Bundle s = getArguments();
        if (s != null) {
            mNet = s.getInt(ARG_NET);
            mPos = s.getInt(ARG_POS);
            mAll=s.getBoolean(ARG_ALL);



        }

    }

    AsyncLoad m;

    @Override
    public void onResume() {
        super.onResume();

        m=new AsyncLoad(this);

        if (m.getStatus() != AsyncTask.Status.RUNNING)
            m.execute();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.m != null && this.m.getStatus() == AsyncTask.Status.RUNNING) {
            this.m.cancel(true);
        }

    }

    public void Start() {
        final Contact con =!mAll? (Contact) $[mNet].get(mPos):fragment_all_phones.mList.get(mPos);

        String number;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            number = android.provider.CallLog.Calls.CACHED_NORMALIZED_NUMBER;
        } else {
            number = android.provider.CallLog.Calls.NUMBER;
        }
        String[] h=new String[con.Phone.size()];
        String m="";
        ArrayList<Contact.PhoneClass> phone = con.Phone;
        for (int i = 0; i < phone.size(); i++) {
            Contact.PhoneClass phoneClass = phone.get(i);

            m += " "+number+" LIKE ? ";
            if (i!=phone.size()-1)
                m += "OR";
            h[i]="%" + phoneClass.Fnumber.substring(4);
        }

        //String ass[] = new String[ME.getDatabaseArg[mNet].length + 1];
        //ass[0] = con.Name;

        //String sel = ME.DatabaseSelector[mNet].replace("data4", number);
        //System.arraycopy(ME.getDatabaseArg[mNet], 0, ass, 1, ass.length - 1);


        final Cursor cur = getActivity().getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI
                , null, m, h, android.provider.CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cur != null) {
            final int iNumber = cur.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
            final int iDuration = cur.getColumnIndex(android.provider.CallLog.Calls.DURATION);
            final int iDate = cur.getColumnIndex(android.provider.CallLog.Calls.DATE);
            final int iType = cur.getColumnIndex(android.provider.CallLog.Calls.TYPE);
            vcall.clear();
            while (cur.moveToNext()) {
                try {
                    Base_CallLog i = new Base_CallLog();
                    i.setDuration_int(cur.getInt(iDuration));
                    i.setType(cur.getInt(iType));
                    i.setNumber(cur.getString(iNumber));
                    i.setDateLong(cur.getLong(iDate));
                    i.setDateStr(cur.getLong(iDate));
                    vcall.add(i);
                } catch (Exception ignored) {


                }
            }


            cur.close();


        }
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
        mAdapter.setType(callType);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.setType(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (getCallType() != 0) setCallType(0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // mAdapter.objects.get(position).
    }


    @Override
    public void doAfterFinish() {

        mAdapter.notifyDataSetChanged();
        hide.setVisibility(vcall.size() == 0 ? View.VISIBLE : View.GONE);
    }


    class CallHistoryAdapter extends BaseRecycleAdapter<ViewHolder, Base_CallLog> {

        public CallHistoryAdapter(Context context, List<Base_CallLog> s, int layout) {
            super(context, s, layout);
            this.Filter = new FilterBuilder<Base_CallLog>() {
                @Override
                public boolean IsMatch(Base_CallLog b) {
                    return b.getType() == type;
                }
            };

        }

        int type = 0;

        public void setType(int i) {
            if (type == i) return;
            type = i;
            if (type == 0)
                RestartFilter();
            else
            this.StartFilter();

        }


        @Override
        public void onBindViewHolder(ViewHolder numHolder, Base_CallLog item) {

            numHolder.callsNumberText.setText(item.getNumber());
            if (DateUtils.isToday(item.getDate_str().getTime()))
                numHolder.callsDateText.setText("Today" + DateFormat.getInstance().format(item.getDate_str()));
            else
                numHolder.callsDateText.setText(DateFormat.getInstance().format(item.getDate_str()));

            numHolder.callsDurTxt.setText(DateUtils.formatElapsedTime(item.getDuration_int()));
            switch (item.getType()) {
                case 3:
                    numHolder.callsTypeImg.setImageResource(R.drawable.call_missed);
                    break;
                case 2:
                    numHolder.callsTypeImg.setImageResource(R.drawable.call_outgoing);
                    break;
                case 1:
                    numHolder.callsTypeImg.setImageResource(R.drawable.call_incoming);
                    break;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(View view, int position) {
            return new ViewHolder(view);
        }
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private TextView callsNumberText;
        private TextView callsDateText;
        private TextView callsDurTxt;
        private ImageView callsTypeImg;

        public ViewHolder(View convertView) {
            super(convertView);
            this.mView = convertView;
            callsNumberText = (TextView) convertView.findViewById(R.id.calls_number_text);
            callsDateText = (TextView) convertView.findViewById(R.id.calls_date_text);
            callsDurTxt = (TextView) convertView.findViewById(R.id.calls_dur_txt);
            callsTypeImg = (ImageView) convertView.findViewById(R.id.calls_type_img);


        }
    }
}

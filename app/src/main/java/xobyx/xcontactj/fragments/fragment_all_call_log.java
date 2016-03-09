package xobyx.xcontactj.fragments;


import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.all_call_log_adapter;
import xobyx.xcontactj.base.Base_CallLog;
import xobyx.xcontactj.until.ME;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_all_call_log#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_all_call_log extends Fragment {
    private ArrayList<Base_CallLog> objList = new ArrayList<>();
    private int total_in;
    private int total_in_dur;
    private int total_out;
    private int total_out_dur;
    private int total_miss;
    private int total_miss_dur;
    private String sec_end;
    private String min;
    private String min_end;
    private AsyncTask q;
    Comp mComp = new Comp();
    private ListView mList;
    private all_call_log_adapter adapter;
    private View hide;

    /**
     * Use this factory method to create mNumber new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment fragment_all_call_log.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_all_call_log newInstance() {
        fragment_all_call_log fragment = new fragment_all_call_log();

        return fragment;
    }

    public fragment_all_call_log() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        this.sec_end = "sec.";
        this.min = "min";
        this.min_end = "min.";


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.all_call_log_fragment, menu);
        ArrayList<String> m=new ArrayList<>();
        m.add("All");m.add("Incoming");m.add("Outgoing");m.add("Missed");
        menu.findItem(R.id.action_call_log_static).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder m=new AlertDialog.Builder(getActivity(),R.style.Base_Theme_AppCompat_Light_Dialog);
                m.setTitle("Call Log Static");
                //m.setMessage(getStatic());
                final AlertDialog alertDialog = m.create();
                final WindowManager.LayoutParams decorView = alertDialog.getWindow().getAttributes();
                decorView.width=400;
                //layoutParams.width=400;
                alertDialog.getWindow().setAttributes(decorView);


               // alertDialog.getDelegate().onCreate(null);
                alertDialog.setMessage(getStatic());
               // ((TextView) alertDialog.findViewById(android.R.id.message)).setText(getStatic(), TextView.BufferType.SPANNABLE);
                alertDialog.show();
                return true;
            }
        });


        AppCompatSpinner x=(AppCompatSpinner) menu.findItem(R.id.action_change_call_log_type).getActionView();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, android.R.id.text1, m);
        x.setAdapter(arrayAdapter);
        x.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setLogType(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((SearchView) menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter(newText);
                try {
                    adapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }


    DialogFragment v=new DialogFragment();
    public void StartListLog() {
        objList.clear();



        Cursor var1 = getActivity().getContentResolver().query(Uri.parse("content://call_log/calls"), (String[]) null, (String) null, (String[]) null, "date DESC");
        final int date = var1.getColumnIndex("date");
        final int type = var1.getColumnIndex("type");
        final int id = var1.getColumnIndex("_id");
        final int duration = var1.getColumnIndex("duration");
        final int name = var1.getColumnIndex("name");
        final int number = var1.getColumnIndex("number");
        final int lookup_uri = var1.getColumnIndex("lookup_uri");

        if (var1 != null) {


            while (var1.moveToNext()) {
                Base_CallLog logObj = new Base_CallLog();
                String var6 = "";


                logObj.setDateStr( var1.getLong(date));
                logObj.setDateLong(var1.getLong(date));

                int var11 = var1.getInt(type);

                switch (var11) {
                    case CallLog.Calls.INCOMING_TYPE:
                        logObj.setDrawableRes(R.drawable.call_incoming);
                        logObj.setType(1);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        logObj.setDrawableRes(R.drawable.call_outgoing);
                        logObj.setType(2);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        logObj.setDrawableRes(R.drawable.call_missed);
                        logObj.setType(3);
                        break;
                    default:
                        logObj.setDrawableRes(R.drawable.ic_action_unknown);
                        break;
                }

                logObj.setID(var1.getInt(id));

                int var10 = var1.getInt(duration);
                logObj.setDuration_int(var10);
                logObj.setDurationStr(FormatTime(var10));

                if (var1.getString(number) == null) {
                    logObj.setNumber("No Number");
                } else {
                    logObj.setNumber(var1.getString(number));
                    final int net = ME.getNet(getActivity(), logObj.getNumber());
                    if(net!=3) {
                        logObj.setNetworkColor(ME.nColors[net]);
                        logObj.setNetDrawable(ME.NetDrawables[net][0]);

                    }
                    else
                    {
                        logObj.setNetDrawable(R.drawable.ic_action_unknown);
                        logObj.setNetworkColor(R.color.accent);
                    }
                }

                logObj.setName(var1.getString(name));
                logObj.setLookup_uri(var1.getString(lookup_uri));


                // logObj.setAll(var6);
                objList.add(logObj);

            }

            var1.close();
        }

        total_in = 0;
        total_in_dur = 0;
        total_out = 0;
        total_out_dur = 0;
        total_miss = 0;
        total_miss_dur = 0;
        Iterator var2 = objList.iterator();

        while (var2.hasNext()) {
            Base_CallLog var3 = (Base_CallLog) var2.next();
            if (var3.getType() == 1) {
                ++total_in;
                total_in_dur += var3.getDuration_int();
            } else if (var3.getType() == 2) {
                ++total_out;
                total_out_dur += var3.getDuration_int();
            } else if (var3.getType() == 3) {
                ++total_miss;
                total_miss_dur += var3.getDuration_int();
            }
        }

        Collections.sort(objList, mComp);

    }

    public void onDestroy() {
        super.onDestroy();
        if (this.q != null && this.q.getStatus() == AsyncTask.Status.RUNNING) {
            this.q.cancel(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_call_log_static) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = ((ListView) view.findViewById(R.id.all_call_log_list));
        hide = (LinearLayout) view.findViewById(R.id.call_log_empty_layout);
        adapter = new all_call_log_adapter(this.getActivity(), objList);
        mList.setAdapter(adapter);

    }

    public void onResume() {
        super.onResume();
        this.q = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                StartListLog();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                adapter.notifyDataSetChanged();
                hide.setVisibility(objList.size() == 0 ? View.VISIBLE : View.GONE);
            }
        };
        this.q.execute(new String[]{""});
    }


    class Comp implements Comparator {


        public int a(Base_CallLog var1, Base_CallLog var2) {
            return var2.getDateLong() > var1.getDateLong() ? 1 : (var2.getDateLong() == var1.getDateLong() ? 0 : -1);
        }


        public int compare(Object var1, Object var2) {
            return this.a((Base_CallLog) var1, (Base_CallLog) var2);
        }
    }

    private String FormatTime(int var1) {
        if (var1 < 60) return var1 + " " + this.sec_end;
        else if (var1 == 60) return "1 " + this.min;
        else if (var1 % 60 == 0) return var1 / 60 + " " + this.min_end;
        else return var1 / 60 + " " + this.min + ", " + var1 % 60 + " " + this.sec_end;
    }


    CharacterStyle r = new CharacterStyle() {
        @Override
        public void updateDrawState(TextPaint tp) {
            tp.setColor(getResources().getColor(R.color.accent));
        }
    };
    SpannableString getStatic()
    {


        String[] x={ "Total Outgoing Call : "+ total_out+ " \n",
                "Total Outgoing Calls Duration :"+ FormatTime(total_out_dur)+"\n"+
                "Total Incoming Calls : "+total_in+" \n",
                "Total Incoming Calls Duration : "+FormatTime(total_out_dur)+"\n",
                "Total Missed Calls : "+total_miss};
        int m=0;
        String k="";
        for (String s : x) {
            k+=s;
        }
       // SpannableStringBuilder sm=new SpannableStringBuilder(k);

        SpannableString j=new SpannableString(k);
        for (String s : x) {

            j.setSpan(r,m+s.indexOf(':')+2,s.length()+m,0);
            m+=s.length();
        }
        //


       // _tLabelName.setText(j, TextView.BufferType.SPANNABLE);
        return j;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_all_call_log, container, false);
    }


}

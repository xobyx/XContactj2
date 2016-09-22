package xobyx.xcontactj.fragments;


import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.all_call_log_adapter;
import xobyx.xcontactj.base.LogItem;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.StateView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_all_call_log#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_all_call_log extends AsyncLoadFragment<LogItem> {
    Comp mComp = new Comp();

    CharacterStyle getCharStyle(int color) {


        return new ForegroundColorSpan(color);
    }

    private ArrayList<LogItem> objList = new ArrayList<>();
    private int total_in;
    private int total_in_dur;
    private int total_out;
    private int total_out_dur;
    private int total_miss;
    private String sec_end;
    private String min;
    private String min_end;
    private all_call_log_adapter adapter;
    private StateView mStateView;


    public fragment_all_call_log() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create mNumber new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment fragment_all_call_log.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_all_call_log newInstance() {

        return new fragment_all_call_log();
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


        menu.findItem(R.id.action_call_log_static).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder m = new AlertDialog.Builder(getActivity(), R.style.Base_Theme_AppCompat_Light_Dialog);
                SpannableString mt=new SpannableString("your call log static");
                mt.setSpan(getCharStyle(0xFF4081),0,"your call log static".length(),0);
                m.setTitle(mt);

                //m.setMessage(getStatic());
                final AlertDialog alertDialog = m.create();
                final WindowManager.LayoutParams decorView = alertDialog.getWindow().getAttributes();
                Point mg = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(mg);
                decorView.width = mg.x - 40;
                //layoutParams.width=400;
                alertDialog.getWindow().setAttributes(decorView);


                // alertDialog.getDelegate().onCreate(null);
                alertDialog.setMessage(getCallLogStatic());
                // ((TextView) alertDialog.findViewById(android.R.id.message)).setText(getStatic(), TextView.BufferType.SPANNABLE);
                alertDialog.show();
                return true;
            }
        });


        AppCompatSpinner x = (AppCompatSpinner) menu.findItem(R.id.action_change_call_log_type).getActionView();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.base_item,
                android.R.id.text1, getResources().getStringArray(R.array.call_types));
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
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    public ArrayList<LogItem> getPhonelogs() {
        ArrayList<LogItem> temp = new ArrayList<>();


        Cursor var1 = getActivity().getContentResolver().query(Uri.parse("content://call_log/calls"), null, null, null, "date DESC");
        if (var1 != null) {
            final int date = var1.getColumnIndex("date");
            final int type = var1.getColumnIndex("type");
            final int id = var1.getColumnIndex("_id");
            final int duration = var1.getColumnIndex("duration");
            final int name = var1.getColumnIndex("name");
            final int number = var1.getColumnIndex("number");
            final int lookup_uri = var1.getColumnIndex("lookup_uri");


            while (var1.moveToNext()) {
                LogItem log = new LogItem();


                log.setDateStr(var1.getLong(date));
                log.setDateLong(var1.getLong(date));

                int var11 = var1.getInt(type);

                switch (var11) {
                    case CallLog.Calls.INCOMING_TYPE:
                        log.setDrawableRes(R.drawable.call_incoming);
                        log.setType(1);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        log.setDrawableRes(R.drawable.call_outgoing);
                        log.setType(2);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        log.setDrawableRes(R.drawable.call_missed);
                        log.setType(3);
                        break;
                    default:
                        log.setDrawableRes(R.drawable.ic_action_unknown);
                        break;
                }

                log.setID(var1.getInt(id));


                int var10 = var1.getInt(duration);
                log.setDuration_int(var10);
                log.setDurationStr(FormatTime(var10));

                if (var1.getString(number) == null) {
                    log.setNumber("No Number");
                }
                else {
                    log.setNumber(var1.getString(number));
                    final int net = ME.getNetForNumber(log.getNumber());
                    if (net != 3) {
                        log.setNetworkColor(ME.nColors[net]);
                        log.setNetDrawable(ME.NetDrawables[net][0]);

                    }
                    else {
                        log.setNetDrawable(R.drawable.ic_action_unknown);
                        log.setNetworkColor(R.color.accent);
                    }
                }

                log.setName(var1.getString(name));
                log.setLookup_uri(var1.getString(lookup_uri));


                // log.setAll(var6);
                temp.add(log);

            }

            var1.close();
        }

        total_in = 0;
        total_in_dur = 0;
        total_out = 0;
        total_out_dur = 0;
        total_miss = 0;

        for (LogItem var3 : temp) {
            if (var3.getType() == 1) {
                ++total_in;
                total_in_dur += var3.getDuration_int();
            }
            else if (var3.getType() == 2) {
                ++total_out;
                total_out_dur += var3.getDuration_int();
            }
            else if (var3.getType() == 3) {
                ++total_miss;
            }
        }

        Collections.sort(temp, mComp);
        return temp;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_call_log_static || super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mStateView = StateView.inject(view);
        mStateView.setEmptyResource(R.layout.emty_layout);
        mStateView.showLoading();
        ListView mList = ((ListView) view.findViewById(R.id.all_call_log_list));

        adapter = new all_call_log_adapter(this.getActivity(), objList);
        mList.setAdapter(adapter);

    }


    private String FormatTime(int var1) {
        if (var1 < 60) return var1 + " " + this.sec_end;
        else if (var1 == 60) return "1 " + this.min;
        else if (var1 % 60 == 0) return var1 / 60 + " " + this.min_end;
        else return var1 / 60 + " " + this.min + ", " + var1 % 60 + " " + this.sec_end;
    }

    SpannableStringBuilder getCallLogStatic() {


        String[] x = {"Total Outgoing Call : " + total_out + " calls\n",
                "Total Outgoing Calls Duration : " + FormatTime(total_out_dur) + "\n" ,
                "Total Incoming Calls : " + total_in + " calls\n",
                "Total Incoming Calls Duration : " + FormatTime(total_in_dur) + "\n",
                "Total Missed Calls : " + total_miss + " calls"};

        int k = 0;

        SpannableStringBuilder sp = new SpannableStringBuilder();

        for (String s : x) {
            sp.append(s);
            sp.setSpan(getCharStyle(0xFF4081), k + s.indexOf(':') + 2, k + s.length(), 0);
            k += s.length();
        }
        // SpannableStringBuilder sm=new SpannableStringBuilder(k);


        // _tLabelName.setText(j, TextView.BufferType.SPANNABLE);
        return sp;

        // _tLabelName.setText(j, TextView.BufferType.SPANNABLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_fragment_all_call_log, container, false);
    }

    @Override
    public ArrayList<LogItem> Start() {
        return getPhonelogs();
    }

    @Override
    public void doAfterFinish(ArrayList<LogItem> result) {

        objList.clear();
        objList.addAll(result);
        adapter.notifyDataSetInvalidated();

        if (objList != null && objList.size() != 0)
            mStateView.showContent();
        else
            mStateView.showEmpty();

    }

    class Comp implements Comparator<LogItem> {



        @Override
        public int compare(LogItem var1, LogItem var2) {
            return var2.getDateLong() > var1.getDateLong() ? 1 : (var2.getDateLong() == var1.getDateLong() ? 0 : -1);
        }
    }


}

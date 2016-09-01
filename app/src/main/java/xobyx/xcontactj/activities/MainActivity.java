package xobyx.xcontactj.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.SectionsPagerAdapter;
import xobyx.xcontactj.fragments.DialerFragment;
import xobyx.xcontactj.fragments.NetFragment;
import xobyx.xcontactj.fragments.NetFragmentPick;
import xobyx.xcontactj.gcm.RegistrationIntentService;
import xobyx.xcontactj.until.DialerActionModeHelper;
import xobyx.xcontactj.until.DialerActionModeHelper.NumberChangeListener;
import xobyx.xcontactj.until.MDatabase;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.views.HeaderTabs;
import xobyx.xcontactj.views.xViewPager;

import static xobyx.xcontactj.until.ME.NET_N;
///TODO: @{@link Se}

public class MainActivity extends AppCompatActivity implements DialerFragment.DialerHandler, android.support.v7.widget.SearchView.OnQueryTextListener {


    public static String WN_NAME = null;
    public static MDatabase DB;
    /**
     * pick mode
     */
    public static boolean pick_mode;
    /**
     * worked net
     */
    public static int WN_ID;
    /**
     * pick mode local
     */
    public boolean pick_mode_local;
    public FloatingActionButton mCall;
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    HeaderTabs mheader;

    /* Dialer Fragment handler */
    xViewPager mViewPager;
    public DialerActionModeHelper.NumberChangeListener NumberChangeListener = new NumberChangeListener() {
        @Override
        public void onNumberChange(String v) {

            mViewPager.setCurrentItem(ME.getNetForNumber(v));
            onQueryTextChange(v);
        }
    };
    private DialerActionModeHelper DialerHelper;
    private boolean mdialer_stat;
    private PhoneStateListener lis = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_IDLE) return;

            int net = ME.getNetForNumber(incomingNumber);
            String s = NET_N[net];
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            int i = serviceState.getState();
            if (i == ServiceState.STATE_EMERGENCY_ONLY || i == ServiceState.STATE_OUT_OF_SERVICE) {
                WN_ID = 3;
                WN_NAME = "out of service";

                Toast.makeText(MainActivity.this, "No Network,out of service " + NET_N[WN_ID], Toast.LENGTH_SHORT).show();
            }
            else {
                WN_ID = ME.getCurrentNetwork(MainActivity.this);
                mViewPager.setCurrentItem(WN_ID, false);

                Toast.makeText(MainActivity.this, "Found Network" + NET_N[WN_ID], Toast.LENGTH_SHORT).show();

            }
        }
    };
    private NetFragmentPick netFragmentPick;
    private int default_network;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startService(new Intent(this, RegistrationIntentService.class));


        Intent mInt = getIntent();

        // ME.SetInternetSettingFor(0,this);

        setContentView(R.layout.activity_main_1);

        final Toolbar viewById = (Toolbar) findViewById(R.id.toolbar);
        viewById.inflateMenu(R.menu.main_activity);
        setSupportActionBar(viewById);

        WN_ID = ME.getCurrentNetwork(this);

        mCall = (FloatingActionButton) findViewById(R.id.main_call);
        if (WN_ID != 3) {
            WN_NAME = NET_N[WN_ID];
            mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StartDialer("");
                }
            });
        }

        if (mInt.getAction().equals(Intent.ACTION_DIAL)) {
            StartDialer(mInt.getDataString());

        }
        else if (mInt.getAction().equals(Intent.ACTION_PICK)) {
            pick_mode = true;
        }
        if (mInt.hasExtra("local")) {
            pick_mode_local = true;
        }


        // PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(QKPreference.DELIVERY_VIBRATE.getKey(),true).commit();

        // Set up the action bar.

        View vb = findViewById(R.id.rep_me);

        vb.setBackgroundColor(SettingHelp.getBackground(getBaseContext()));
        mheader = (HeaderTabs) findViewById(R.id.mhrader);


        mViewPager = (xViewPager) findViewById(R.id.pager);


        mViewPager.setMoveEnabled(!pick_mode_local);
        if (pick_mode_local) {

            ///FIXME: //(for send_balance Activity we will not reach this code if they are no network )
            getSupportActionBar().setTitle("Pick Contact:");
            mheader.setVisibility(View.GONE);
            mCall.setVisibility(View.GONE);
            netFragmentPick = NetFragmentPick.newInstance(WN_ID);
            getSupportFragmentManager().beginTransaction().replace(R.id.rep_me, netFragmentPick).commit();
            //No need for Network Header in Pick a Contact mode..


        }
        else {
            //getActionBar().setTitle("Pick Contact:");
            //final ITelephony telephonyService = ME.getTelephonyService(this);

            ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).listen(lis, PhoneStateListener.LISTEN_CALL_STATE | PhoneStateListener.LISTEN_CELL_INFO);
            DB = new MDatabase(getBaseContext());
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());      //


            mViewPager.setAdapter(mSectionsPagerAdapter);
            mheader.setViewPager(mViewPager);
            DialerHelper = new DialerActionModeHelper(this);

            if (WN_ID == 3)
                if (PreferenceManager.getDefaultSharedPreferences(this).contains("default_Network")) {
                    default_network = PreferenceManager.getDefaultSharedPreferences(this).getInt("default_Network", -1);
                }
                else {
                    setDefaultNetwork();
                }

            mViewPager.setCurrentItem(WN_ID != 3 ? WN_ID : default_network);


        }
    }

    private void setDefaultNetwork() {
        final int[] n = new int[1];
        AlertDialog.Builder o = new AlertDialog.Builder(this);

        o.setCancelable(false).setMessage("No Network Founded plz Select your default Network..").setTitle("No Network Founded")
                .setSingleChoiceItems(R.array.net_names, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        n[0] = which;

                    }
                }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (n[0] != -1) {
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("default_Network", which).commit();

                    dialog.dismiss();
                }
            }
        }).show();
    }

    private void StartDialer(String dataString) {
        if (WN_ID != 3) {
            if (!mdialer_stat) {


                DialerHelper.StartDialerActionMode(dataString);

            }
        }
        else
            Toast.makeText(MainActivity.this, "No Network..", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // NetFragment item = null;
        /* FIXME:search don't works when change show number... */
        NetFragment item = null;
        if (!pick_mode_local) {
            mViewPager.setMoveEnabled(newText.isEmpty());
            item = mSectionsPagerAdapter.Fragments[mViewPager.getCurrentItem()];
        }
        else {
            item = netFragmentPick;
        }
        try {
            if (item != null) {

                item.SearchFor(newText);
            }

        }
        catch (Exception a) {
            Toast.makeText(this, a.getMessage(), Toast.LENGTH_LONG).show();
        }


        return true;
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (DB != null)
            DB.Close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);

        final SearchView item = (SearchView) menu.findItem(R.id.action_search).getActionView();
        item.setOnQueryTextListener(this);

        item.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mViewPager.setMoveEnabled(true);
                //clear Query first..
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify TouchedItem parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                AboutActivity.ShowAboutActivity(this);
                break;
            case R.id.action_search:
                break;
            case R.id.action_settings:
                Intent i = new Intent(this.getBaseContext(), SettingsActivity.class);
                startActivityForResult(i, 0);
                break;
            case R.id.action_all_contact:
                Intent d = new Intent(this, AllMainActivity.class);
                startActivity(d);
                break;

            default:
                break;

        }
        return true;
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns TouchedItem fragment corresponding to
     * one of the sections/tabs/pages.
     */

    @Override
    protected void onActivityResult(int var1, int var2, Intent var3) {
        super.onActivityResult(var1, var2, var3);
        if (var2 == RESULT_OK) {
            //    finish();
            //   startActivity(getIntent());
            Bundle t = new Bundle();
            t.putInt("ListMode", 0);

            onSaveInstanceState(t);
            recreate();


        }

    }

    @Override
    public void onBackPressed() {
        if (mdialer_stat) {
            DialerHelper.finish();


        }
        else
            super.onBackPressed();

    }


    @Override
    public void onVisibilityChange(boolean IsOpen) {
        mdialer_stat = IsOpen;
        if (!IsOpen) mViewPager.setMoveEnabled(true);
        findViewById(R.id.main_call).setVisibility(IsOpen ? View.GONE : View.VISIBLE);
    }

    @Override
    public DialerActionModeHelper getDialerAction() {
        return DialerHelper;
    }

    @Override
    public void onCall(CharSequence number) {
        Intent y = new Intent(Intent.ACTION_CALL);
        y.setData(Uri.fromParts("tel", String.valueOf(number), null));
        startActivity(y);

    }

    @Override
    public boolean getDialerState() {
        return mdialer_stat;
    }

    /**
     * A placeholder fragment containing TouchedItem simple view.
     */


}



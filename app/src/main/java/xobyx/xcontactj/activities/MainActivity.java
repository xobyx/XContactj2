package xobyx.xcontactj.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.acra.ACRA;

import io.fabric.sdk.android.Fabric;
import xobyx.xcontactj.MyApp;
import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.SectionsPagerAdapter;
import xobyx.xcontactj.adapters.SmAdapter;
import xobyx.xcontactj.base.IDialerHandler;
import xobyx.xcontactj.fragments.NetFragment;
import xobyx.xcontactj.fragments.NetFragmentPick;
import xobyx.xcontactj.gcm.RegistrationIntentService;
import xobyx.xcontactj.until.DialerActionModeHelper;
import xobyx.xcontactj.until.MDatabase;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.views.HeaderTabs;
import xobyx.xcontactj.views.xViewPager;

import static xobyx.xcontactj.until.ME.NET_N;
///TODO: @{@link Se}

public class MainActivity extends AppCompatActivity implements IDialerHandler, android.support.v7.widget.SearchView.OnQueryTextListener {


    public static String wn_name = null;
    public static MDatabase DB;
    /**
     * pick mode
     */
    public static boolean pick_mode;
    /**
     * worked net
     */
    public static int wn_id;
    /**
     * pick mode local
     */
    public boolean pick_mode_local;
    public FloatingActionButton call_button;
    SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    HeaderTabs tabs;
    /* Dialer Fragment handler */
    xViewPager mViewPager;
    private int default_network;
    private DialerActionModeHelper DialerHelper;
    private boolean is_dialer_open=false;
    private final View.OnClickListener call_handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            throw new RuntimeException("This is a crash");
           /* Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.jump_);

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d(MainActivity.class.getSimpleName(),"dial anmi end");
                    StartDialer("");
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            v.startAnimation(animation);*/
        }
    };
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {



        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == TelephonyManager.CALL_STATE_IDLE) return;

            String c="";
            try {
               c = ME.getTelephonyService(MainActivity.this).getCallerName();
            }
            catch (RemoteException e) {
                e.printStackTrace();
            }
            int net = ME.getNetForNumber(incomingNumber);
            String s = getNetworkName(net);
            Toast.makeText(MainActivity.this, s+" "+c, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            int i = serviceState.getState();
            if (i == ServiceState.STATE_EMERGENCY_ONLY || i == ServiceState.STATE_OUT_OF_SERVICE) {
                wn_id = 3;
                wn_name = "out of service";

                Toast.makeText(MainActivity.this, "No Network,out of service " + getNetworkName(), Toast.LENGTH_SHORT).show();
            }
            else {
                wn_id = ME.getCurrentNetwork(MainActivity.this);
                if (wn_id != 3) {
                    mViewPager.setCurrentItem(wn_id, false);

                    Toast.makeText(MainActivity.this, "Found Network" + getNetworkName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private NetFragmentPick netFragmentPick;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());

        Fabric fabric = new Fabric.Builder(this).kits(new Crashlytics()).debuggable(true).build();
        Fabric.with(fabric);
        Intent mInt = getIntent();


        // ME.SetInternetSettingFor(0,this);

        setContentView(R.layout.activity_main_1);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.main_activity);
        setSupportActionBar(mToolbar);

        wn_id = ME.getCurrentNetwork(this);

        call_button = (FloatingActionButton) findViewById(R.id.main_call);
        if (wn_id != 3) {
            wn_name = getNetworkName();
            call_button.setOnClickListener(call_handler);
        }
        call_button.setOnClickListener(call_handler);
        if (mInt.getAction() != null) {
            if (mInt.getAction().equals(Intent.ACTION_DIAL)) {
                StartDialer(mInt.getDataString());

            }
            else if (mInt.getAction().equals(Intent.ACTION_PICK)) {
                pick_mode = true;
                if (mInt.hasExtra("local")) {
                    pick_mode_local = true;
                }
            }

        }

        // PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(QKPreference.DELIVERY_VIBRATE.getKey(),true).commit();

        // Set up the action bar.

        View vb = findViewById(R.id.rep_me);

        vb.setBackgroundColor(SettingHelp.getBackground(getBaseContext()));
        tabs = (HeaderTabs) findViewById(R.id.mhrader);
        if (pick_mode_local) {

            ///FIXME: //(for send_balance Activity we will not reach this code if they are no network )
            getSupportActionBar().setTitle("Pick Contact:");
            //changed://tabs.setVisibility(View.GONE);
            call_button.setVisibility(View.GONE);
            tabs.setVisibility(View.GONE);
            netFragmentPick = NetFragmentPick.newInstance(wn_id);
            getSupportFragmentManager().beginTransaction().replace(R.id.repl, netFragmentPick).commit();
            //No need for Network Header in Pick a Contact mode..


        }
        else {
            //getActionBar().setTitle("Pick Contact:");
            //final ITelephony telephonyService = ME.getTelephonyService(this);
            //changed:  moved from top;
            tabs = (HeaderTabs) findViewById(R.id.mhrader);
            mViewPager = (xViewPager) findViewById(R.id.pager);
            mViewPager.setMoveEnabled(!pick_mode_local);
            startService(new Intent(this, RegistrationIntentService.class));
            ((MyApp) getApplicationContext()).getTelephonyManager().listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE | PhoneStateListener.LISTEN_CELL_INFO);
            DB = new MDatabase(getBaseContext());
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());      //


            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabs.setViewPager(mViewPager);
            DialerHelper = new DialerActionModeHelper(this);

            if (wn_id == 3)
                if (PreferenceManager.getDefaultSharedPreferences(this).contains("default_Network")) {
                    default_network = PreferenceManager.getDefaultSharedPreferences(this).getInt("default_Network", -1);
                }
                else {
                    setDefaultNetwork();
                }

            mViewPager.setCurrentItem(wn_id != 3 ? wn_id : default_network);


        }
    }

    private String getNetworkName() {
        return NET_N[wn_id];
    }

    private String getNetworkName(int id) {
        return NET_N[id];
    }

    private void setDefaultNetwork() {
        final int[] n = new int[1];
        AlertDialog.Builder o = new AlertDialog.Builder(this,R.style.Base_Theme_AppCompat_Light_Dialog);

        SmAdapter m = new SmAdapter();
        m.newInstance(MainActivity.this).SetupItems(R.array.net_names).SetupLayout(R.layout.r_header).setInflater(new SmAdapter.inflater() {
            @Override
            public void inflate(View n, int pos, Object item) {
                if (n.getId() == android.R.id.text1) {
                    ((TextView) n).setText((String) item);

                }
                else {
                    ((ImageView) n).setImageResource(ME.NetDrawables[pos][0]);
                }
            }
        });
        o.setCancelable(false).setMessage("No Network Founded plz Select your default Network..").setTitle("No Network Founded")
                .setSingleChoiceItems(m.BuildAdapter(), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        n[0] = which;
                    }
                }).setPositiveButton("Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (n[0] != -1) {
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putInt("default_Network", which).apply();

                    dialog.dismiss();
                }
            }
        }).show();
    }

    private void StartDialer(String dataString) {
        if (wn_id != 3) {
            if (!is_dialer_open) {


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
           // ACRA.getErrorReporter().handleException(a);
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

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mViewPager.setMoveEnabled(true);

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
        if (is_dialer_open) {
            DialerHelper.finish();


        }
        else
            super.onBackPressed();

    }


    @Override
    public void onVisibilityChange(boolean isopen) {
        is_dialer_open=isopen;
        if (isopen) {
            mViewPager.setMoveEnabled(true);
            findViewById(R.id.main_call).setVisibility(View.GONE);
        }
        else
            findViewById(R.id.main_call).setVisibility(View.VISIBLE);

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
    public Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    public boolean getDialerState() {
        return is_dialer_open;
    }

    @Override
    public void onNumberChange(String v, int n) {

        if (n < 3) {
            mViewPager.setCurrentItem(n);
        }
        onQueryTextChange(v);
    }

    /**
     * A placeholder fragment containing TouchedItem simple view.
     */


}



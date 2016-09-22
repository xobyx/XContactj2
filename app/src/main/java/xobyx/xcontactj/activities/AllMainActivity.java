package xobyx.xcontactj.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.AllFragmentAdapter;
import xobyx.xcontactj.base.IDialerHandler;
import xobyx.xcontactj.fragments.NetFragmentAll;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.DialerActionModeHelper;
import xobyx.xcontactj.until.ME;

public class AllMainActivity extends AppCompatActivity implements IDialerHandler {


    private TabLayout.OnTabSelectedListener Tab_Listener=new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    public ArrayList<Contact> mNumberList;
    private ViewPager mev;
    private boolean dialer_state;
    private DialerActionModeHelper DialerHelper;
    private FloatingActionButton fab;
    private AllFragmentAdapter fragment_adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  index = (LinearLayout) findViewById(R.id.index);

        setContentView(R.layout.activity_marge_contacts);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.all_main_activity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mev=(ViewPager)findViewById(R.id.all_view_pagger);
        DialerHelper=new DialerActionModeHelper(this);
        fab = ((FloatingActionButton) findViewById(R.id.main_call));

        fab.setOnClickListener(call_handler);

        fragment_adapter = new AllFragmentAdapter(getSupportFragmentManager());
        mev.setAdapter(fragment_adapter);


        tabLayout.setupWithViewPager(mev);
        //tabLayout.addTab(phone_tab);
        //tabLayout.addTab(messages_tab);
        //tabLayout.addTab(call_log_tab);
        //tabLayout.setTabsFromPagerAdapter(new asd(getSupportFragmentManager()));
        //getSupportFragmentManager().




    }

    private final View.OnClickListener call_handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StartDialer("");
        }
    };

    private void StartDialer(String dataString) {
        if (ME.getCurrentNetwork(this) != 3) {
            if (!dialer_state) {


                DialerHelper.StartDialerActionMode(dataString);

            }
        }
        else
            Toast.makeText(this, "No Network..", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.all_main_activity, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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



        }
        return true;
    }





    @Override
    public boolean getDialerState() {
        return dialer_state;
    }

    @Override
    public void onNumberChange(String v,int n) {

        NetFragmentAll numberFragment = fragment_adapter.getNumberFragment();
        if(numberFragment!=null)
        {
            numberFragment.SearchFor(v);
        }
    }

    @Override
    public void onBackPressed() {
        if (dialer_state) {
            DialerHelper.finish();


        }
        else
            super.onBackPressed();

    }


    @Override
    public void onVisibilityChange(boolean IsOpen) {
        dialer_state = IsOpen;


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
    public Toolbar getToolBar() {
        return toolbar;
    }
}

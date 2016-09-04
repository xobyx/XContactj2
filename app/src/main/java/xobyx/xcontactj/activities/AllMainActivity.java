package xobyx.xcontactj.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import xobyx.xcontactj.R;
import xobyx.xcontactj.fragments.DialerFragment;
import xobyx.xcontactj.fragments.fragment_all_call_log;
import xobyx.xcontactj.fragments.NetFragmentAll;
import xobyx.xcontactj.fragments.fragment_all_sms;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.DialerActionModeHelper;

public class AllMainActivity extends AppCompatActivity implements DialerFragment.DialerHandler {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  index = (LinearLayout) findViewById(R.id.index);

        setContentView(R.layout.activity_marge_contacts);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.all_main_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        final ViewPager mev=(ViewPager)findViewById(R.id.all_view_pagger);

        mev.setAdapter(new asd(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(mev);
        //tabLayout.addTab(phone_tab);
        //tabLayout.addTab(messages_tab);
        //tabLayout.addTab(call_log_tab);
        //tabLayout.setTabsFromPagerAdapter(new asd(getSupportFragmentManager()));
        //getSupportFragmentManager().




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
    public void onBackPressed() {
    //    if (mShow) {
      //      final Fragment fragment = getSupportFragmentManager().findFragmentByTag("s");
        //    if (fragment != null) {
          ////      getSupportFragmentManager().beginTransaction().remove(fragment).commit();
           ///     mShow = false;
             //   return;
          //  }
       // }
        super.onBackPressed();
    }


    @Override
    public void onVisibilityChange(boolean IsOpen) {

    }

    @Override
    public DialerActionModeHelper getDialerAction() {
        return null;
    }

    @Override
    public void onCall(CharSequence number) {

    }

    @Override
    public boolean getDialerState() {
        return false;
    }
    class asd extends FragmentPagerAdapter

    {
        final Fragment[] list={NetFragmentAll.cv(),fragment_all_sms.newInstance(),fragment_all_call_log.newInstance()};
        final private String[] b={"Phone","Messages","Phone Logs"};
        @Override
        public CharSequence getPageTitle(int position) {
            return  b[position];
        }

        public asd(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return list[0];
                case 1:
                    return list[1];
                case 2:
                    return list[2];

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

package xobyx.xcontactj.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import xobyx.xcontactj.R;
import xobyx.xcontactj.fragments.DialerFragment;
import xobyx.xcontactj.fragments.MostFragment;
import xobyx.xcontactj.fragments.fragment_all_call_log;
import xobyx.xcontactj.fragments.fragment_all_phones;
import xobyx.xcontactj.ui.ContentFragment;
import xobyx.xcontactj.ui.base.QKActivity;
import xobyx.xcontactj.ui.conversationlist.ConversationListFragment;
import xobyx.xcontactj.ui.view.slidingmenu.SlidingMenu;
import xobyx.xcontactj.until.DialerActionModeHelper;
import xobyx.xcontactj.until.SmsHelper;

public class AllMainActivity extends QKActivity implements DialerFragment.DialerHandler,SlidingMenu.SlidingMenuListener {


    public final static String EXTRA_THREAD_ID = "thread_id";

    public static long sThreadShowing;

    private static final int THREAD_LIST_QUERY_TOKEN = 1701;
    private static final int UNREAD_THREADS_QUERY_TOKEN = 1702;
    public static final int DELETE_CONVERSATION_TOKEN = 1801;
    public static final int HAVE_LOCKED_MESSAGES_TOKEN = 1802;
    private static final int DELETE_OBSOLETE_THREADS_TOKEN = 1803;

    public static final String MMS_SETUP_DONT_ASK_AGAIN = "mmsSetupDontAskAgain";


    View mRoot;
     SlidingMenu mSlidingMenu;

    private ConversationListFragment mConversationList;
    private ContentFragment mContent;
    private long mWaitingForThreadId = -1;

    private boolean mIsDestroyed = false;

    public interface FabHandler
    {
        Drawable getFragmentDrawable();
        View.OnClickListener onFabClicked();

    }
    private TabLayout.OnTabSelectedListener Tab_Listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            mev.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
    private ViewPager mev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  index = (LinearLayout) findViewById(R.id.index);

        setContentView(R.layout.activity_marge_contacts);


        mRoot=findViewById(R.id.root);
        mSlidingMenu= (SlidingMenu) findViewById(R.id.sliding_menu);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.all_main_activity);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mev = (ViewPager) findViewById(R.id.all_view_pagger);

        mev.setAdapter(new asd(getSupportFragmentManager()));

        //tabLayout.setupWithViewPager(mev);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_star_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_chat_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_access_time_white_24dp));
        tabLayout.setOnTabSelectedListener(Tab_Listener);

        mev.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_access_time_white_24dp));
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


    public void showMenu() {
        mSlidingMenu.showMenu();
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

    @Override
    public void onOpen() {

    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onClosed() {

    }

    @Override
    public void onChanging(float percentOpen) {

    }

    public SlidingMenu getSlidingMenu() {
        return mSlidingMenu;
    }

    public static Intent createAddContactIntent(String address) {
        // address must be a single recipient
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        if (SmsHelper.isEmailAddress(address)) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, address);
        } else {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, address);
            intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        return intent;
    }

    class asd extends FragmentPagerAdapter

    {
        final Fragment[] list = {MostFragment.newInstance(), fragment_all_phones.cv(), new ConversationListFragment(), fragment_all_call_log.newInstance()};
        final private String[] b = {"most", "Phone", "Messages", "Phone Logs"};

        @Override
        public CharSequence getPageTitle(int position) {
            return b[position];
        }

        public asd(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < list.length)
                return list[position];
            return null;

        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}

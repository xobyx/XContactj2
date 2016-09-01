package xobyx.xcontactj.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.ContactSpecFragmentAdapter;
import xobyx.xcontactj.fragments.fragment_all_phones;
import xobyx.xcontactj.until.AppAnimations;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.LetterImageView;

import static xobyx.xcontactj.until.ME.$;


public class ContactSpecificsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String NET = "net";
    private static final String POS = "pos";
    private static final String SEC = "sec";

    Animator mt1;
    Animator mt2;
    AppAnimations.Rotate3dAnimation df;

    private int mPos;
    private int mNet;


    private TextView name;


    //private TabLayout tabHost;
    private ViewPager pager;

    private Parcelable lmessage;
    private int mSection;
    private CollapsingToolbarLayout collapsingToolbar;
    private Toolbar toolbar;
    private TabLayout tabHost;
    private boolean all;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        final Intent in = getIntent();
        SetPageParameters(in);
        if(!all)
        ME.setTheme(this, mNet);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detiles);



        ContactSpecFragmentAdapter fragAd = new ContactSpecFragmentAdapter(getSupportFragmentManager(), mPos, mNet, lmessage,all);
//error when none netWork
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager

        pager.setAdapter(fragAd);


        tabHost = (TabLayout) this.findViewById(R.id.tabHost);

        tabHost.setupWithViewPager(pager);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Contact a =!all? (Contact) $[mNet].get(mPos) : fragment_all_phones.mList.get(mPos);
        //tabHost.setBackgroundColor(getResources().getColor(ME.nColors[a.Net]));


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if(null!=collapsingToolbar)
        collapsingToolbar.setTitle(a.Name);

        ((TextView) findViewById(R.id.toolbar_title)).setText(a.Name);
        //collapsingToolbar.setContentScrimColor(ME.nColors[mNet]);

        final LetterImageView imageView = (LetterImageView) findViewById(R.id.d_image);
        if (a.PhotoUri != null) {
            imageView.setImageURI(a.PhotoUri);

            // ((CircleImageView) findViewById(R.id.profile_image)).setImageURI(a.PhotoThumbUri);

        } else {
            imageView.setLetter(a.Name.charAt(0));
            if(!all) {
                imageView.setCustomColor(a.Net);
            }
            else
            {
                imageView.setContact(a);
            }



        }
        //set startup page//
        pager.addOnPageChangeListener(this);

        //Startup Animator..
       // mt1 = AnimatorInflater.loadAnimator(getBaseContext(), R.animator.df);
        //mt2 = AnimatorInflater.loadAnimator(getBaseContext(), R.animator.i_df);
        //df = new AppAnimations.Rotate3dAnimation(180, 0, 0, 0, 20, false);
        //df.setDuration(800);


        for (int i = 0; i < tabHost.getTabCount(); i++) {

            //AttributeSet m;m.getAttributeValue(R.attr.actionBarDivider)
            tabHost.getTabAt(i)
                    .setIcon(getResources().getDrawable(icons[i]));
            ;//.setText(text[i]);


        }

    }


    final String[] text = {"Numbers", "Call Logs", "Messages"};

    private void SetPageParameters(Intent in) {
        if(in.hasExtra("all"))
            all=true;
        mPos = in.getIntExtra(POS, 0);
        mNet = all ? ME.getCurrentNetwork(this) : in.getIntExtra(NET, 0);
        mSection = in.getIntExtra(SEC, 0);
        lmessage = in.hasExtra("message") ? in.getParcelableExtra("message") : null;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        name.startAnimation(df);

    }


    final int[] icons =

            {R.drawable.icalls, R.drawable.icall_log, R.drawable.imessages
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 1:
                //    name2.setVisibility(View.VISIBLE);
                //  cPreview.setVisibility(View.GONE);
                break;
            case 0:
                //name2.setVisibility(View.GONE);
                //cPreview.setVisibility(View.VISIBLE);
                break;
            case 3:
                break;
            case 2:
                //name2.setVisibility(View.VISIBLE);
                //cPreview.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}


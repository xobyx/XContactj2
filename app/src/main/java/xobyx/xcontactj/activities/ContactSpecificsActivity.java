package xobyx.xcontactj.activities;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.onCreateFragmentAdapter;
import xobyx.xcontactj.until.AppAnimations;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.LetterImageView;

import static xobyx.xcontactj.until.ME.$;


public class ContactSpecificsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final String NET = "net";
    protected static final String POS = "pos";
    private static final String SEC = "sec";

    Animator mt1;
    Animator mt2;
    AppAnimations.Rotate3dAnimation df;

    protected int mPos;
    private int mNet;


    private TextView name;


    //private TabLayout tabHost;
    protected ViewPager pager;

    protected Parcelable lmessage;
    private int mSection;
    private CollapsingToolbarLayout collapsingToolbar;
    protected Toolbar toolbar;
    protected TabLayout tabHost;
    private boolean all;
    protected Contact mContact;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        SetPageParameters(getIntent());


        onApplyCustomTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detiles);

        mContact = getContact();
        //tabHost.setBackgroundColor(getResources().getColor(ME.nColors[mContact.Net]));
        if (mContact == null) {
            Toast.makeText(this, "Contact not found..", Toast.LENGTH_SHORT).show();
            finish();
        }
        onCreateFragmentAdapter fragAd = getFragmentAdapter();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pager = (ViewPager) this.findViewById(R.id.pager);

        // init view pager

        pager.setAdapter(fragAd);


        tabHost = (TabLayout) this.findViewById(R.id.tabHost);

        tabHost.setupWithViewPager(pager);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (null != collapsingToolbar)
            collapsingToolbar.setTitle(mContact.Name);

        ((TextView) findViewById(R.id.toolbar_title)).setText(mContact.Name);
        //collapsingToolbar.setContentScrimColor(ME.nColors[mNet]);

        final LetterImageView imageView = (LetterImageView) findViewById(R.id.d_image);
        SetupContactImage(imageView);
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






    @NonNull
    protected onCreateFragmentAdapter getFragmentAdapter() {
        return new onCreateFragmentAdapter(getSupportFragmentManager(),mContact, mPos, mNet, lmessage, false);
    }

    protected void SetupContactImage(LetterImageView imageView) {
        if (mContact.PhotoUri != null) {
            imageView.setImageURI(mContact.PhotoUri);

            // ((CircleImageView) findViewById(R.id.profile_image)).setImageURI(mContact.PhotoThumbUri);

        }
        else {
            imageView.setLetter(mContact.Name.charAt(0));

            imageView.setCustomColor(mContact.Net);


        }
    }

    protected void onApplyCustomTheme() {
        ME.setTheme(this, mNet);
    }

    protected Contact getContact() {
        return (Contact) $[mNet].get(mPos);
    }


    final String[] text = {"Numbers", "Call Logs", "Messages"};

    protected void SetPageParameters(Intent in) {

        mPos = in.getIntExtra(POS, 0);
        mNet = in.getIntExtra(NET, 0);
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

    public void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


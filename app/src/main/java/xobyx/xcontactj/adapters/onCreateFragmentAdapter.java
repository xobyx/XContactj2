package xobyx.xcontactj.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import xobyx.xcontactj.fragments.CallHistoryFragment;
import xobyx.xcontactj.fragments.NumSpecFragment;
import xobyx.xcontactj.fragments.SmsFragment;
import xobyx.xcontactj.until.Contact;

/**
 * Created by xobyx on 8/5/2015.
 * For xobyx.xcontactj.adapters/XContactj
 */
public class onCreateFragmentAdapter extends FragmentStatePagerAdapter {


    private final boolean all;
    private final FragmentManager fm;
    private final Contact m;
    private final int mPos;
    private final int mNet;
    private final Parcelable mlast;

    public onCreateFragmentAdapter(FragmentManager fm, Contact m, int pos, int net, Parcelable lmessage, boolean all) {
        super(fm);
        this.fm = fm;
        this.m = m;
        this.mPos = pos;
        this.mNet = net;

        mlast=lmessage;

        this.all = all;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment b = null;
        switch (i) {
            case 1:


                b = CallHistoryFragment.newInstance(m.getNumbersList());


                break;


            case 0:
                b = NumSpecFragment.newInstance(mPos, mNet,all);

                break;

            case 3:
                break;
            case 2:
                b = SmsFragment.newInstance(m.getNumbersList(),mlast);


                break;

        }
        return b;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

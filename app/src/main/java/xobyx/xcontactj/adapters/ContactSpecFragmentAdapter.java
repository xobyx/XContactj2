package xobyx.xcontactj.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import xobyx.xcontactj.fragments.CallHistoryFragment;
import xobyx.xcontactj.fragments.NumSpecFragment;
import xobyx.xcontactj.fragments.SmsFragment;

/**
 * Created by xobyx on 8/5/2015.
 * For xobyx.xcontactj.adapters/XContactj
 */
public class ContactSpecFragmentAdapter extends FragmentStatePagerAdapter {

    private final int mPos;
    private final int mNet;
    private final boolean all;
    private final Parcelable mlast;

    public ContactSpecFragmentAdapter(FragmentManager fm, int Pos, int Net, Parcelable lmessage, boolean all) {
        super(fm);
        mPos = Pos;
        mlast=lmessage;
        mNet = Net;
        this.all = all;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment b = null;
        switch (i) {
            case 1:
                b = CallHistoryFragment.newInstance(mPos, mNet,all);

                break;


            case 0:
                b = NumSpecFragment.newInstance(mPos, mNet,all);

                break;

            case 3:
                break;
            case 2:
                b = SmsFragment.newInstance(mPos, mNet, mlast,all);


                break;

        }
        return b;
    }

    @Override
    public int getCount() {
        return 3;
    }

}

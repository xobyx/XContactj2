package xobyx.xcontactj.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import xobyx.xcontactj.R;
import xobyx.xcontactj.fragments.NetFragment;
import xobyx.xcontactj.fragments.NetFragmentPick;
import xobyx.xcontactj.views.HeaderTabs;

public class SectionsPagerAdapter extends FragmentPagerAdapter implements HeaderTabs.HeaderTabsAdapter {


    final NetFragment z = NetFragment.newInstance(0);
    final NetFragment s = NetFragment.newInstance(1);
    final NetFragment m = NetFragment.newInstance(2);
    public final NetFragment Fragments[] = {z, s, m};
    private final boolean mPick;


    public SectionsPagerAdapter(FragmentManager fm, int net, boolean pickMode) {
        super(fm);

        mPick = pickMode;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return TouchedItem PlaceholderFragment (defined as TouchedItem static inner class below).
        if (!mPick)
            return Fragments[position];

        return NetFragmentPick.newInstance(position);
    }


    @Override
    public int getCount() {
        // Show 3 total pages.

        return 3;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Zain";
            case 1:
                return "Sudani";
            case 2:
                return "Mtn";
        }
        return null;
    }


    @Override
    public int getDrawbleResourse(int index) {
        switch (index) {
            case 0:
                return R.drawable.i_zain;
            case 1:
                return R.drawable.i_sudani;
            case 2:
                return R.drawable.i_mtn;
        }
        return 0;
    }
}

package xobyx.xcontactj.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import xobyx.xcontactj.fragments.NetFragmentAll;
import xobyx.xcontactj.fragments.fragment_all_call_log;
import xobyx.xcontactj.fragments.fragment_all_sms;

/**
 * Created by xobyx on 9/6/2016.
 * For xobyx.xcontactj.adapters/XContactj2
 */
public class AllFragmentAdapter extends FragmentPagerAdapter

{
    public NetFragmentAll getNumberFragment() {
        return ((NetFragmentAll) list[0]);
    }

    final Fragment[] list = {NetFragmentAll.cv(), fragment_all_sms.newInstance(), fragment_all_call_log.newInstance()};
    final private String[] b = {"Phone", "Messages", "Phone Logs"};

    @Override
    public CharSequence getPageTitle(int position) {
        return b[position];
    }

    public AllFragmentAdapter(FragmentManager fm) {
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

package layout.ui.welcome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.moez.QKSMS.ui.base.QKFragment;

import xobyx.xcontactj.ui.welcome.*;
import xobyx.xcontactj.ui.welcome.WelcomeActivity;

public class BaseWelcomeFragment extends QKFragment {

    public interface WelcomeScrollListener {
        void onScrollOffsetChanged(xobyx.xcontactj.ui.welcome.WelcomeActivity activity, float offset);
    }

    protected static ViewPager mPager;
    protected static xobyx.xcontactj.ui.welcome.WelcomeActivity mContext;

    public static void setPager(ViewPager pager) {
        mPager = pager;
    }

    public static void setContext(WelcomeActivity context) {
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Do nothing
    }
}

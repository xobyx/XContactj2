package xobyx.xcontactj.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import xobyx.xcontactj.common.LiveViewManager;
import xobyx.xcontactj.enums.QKPreference;
import xobyx.xcontactj.interfaces.LiveView;
import xobyx.xcontactj.ui.ThemeManager;

public class QKFragment extends Fragment {

    protected QKActivity mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (QKActivity) activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key) {
                if (getView() != null) {
                    getView().setBackgroundColor(ThemeManager.getBackgroundColor());
                }
            }


        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    //    Icepick.saveInstanceState(this, outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // RefWatcher refWatcher = MyApp.getRefWatcher(getActivity());
       // refWatcher.watch(this);
    }
}

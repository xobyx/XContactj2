package xobyx.xcontactj.base;

import android.support.v7.widget.Toolbar;

import xobyx.xcontactj.until.DialerActionModeHelper;

/**
 * Created by xobyx on 9/4/2016.
 * For xobyx.xcontactj.base/XContactj2
 */
public interface IDialerHandler {
    void onVisibilityChange(boolean isopen);

    DialerActionModeHelper getDialerAction();

    void onCall(CharSequence number);

    Toolbar getToolBar();

    boolean getDialerState();

    void onNumberChange(String v);
}

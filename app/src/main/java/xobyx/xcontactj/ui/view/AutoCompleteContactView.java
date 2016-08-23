package xobyx.xcontactj.ui.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.MultiAutoCompleteTextView;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;

import xobyx.xcontactj.enums.QKPreference;

import xobyx.xcontactj.common.FontManager;
import xobyx.xcontactj.common.LiveViewManager;
import xobyx.xcontactj.common.TypefaceManager;
import xobyx.xcontactj.interfaces.LiveView;
import xobyx.xcontactj.ui.ThemeManager;
import xobyx.xcontactj.ui.base.QKActivity;
import xobyx.xcontactj.ui.settings.SettingsFragment;


public class AutoCompleteContactView extends RecipientEditTextView {
    public static final String TAG = "AutoCompleteContactView";

    private QKActivity mContext;
    private BaseRecipientAdapter mAdapter;

    public AutoCompleteContactView(Context context) {
        this(context, null);
        init(context);
    }

    public AutoCompleteContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = (QKActivity) context;

        mAdapter = new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, getContext());

        setThreshold(1);
        setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        setAdapter(mAdapter);
        setOnItemClickListener(this);

        LiveViewManager.registerView(new LiveView() {
            @Override
            public void refresh(String key) {
                int fontFamily = FontManager.getFontFamily(mContext);
                int fontWeight = FontManager.getFontWeight(mContext, false);
                setTypeface(TypefaceManager.obtainTypeface(mContext, fontFamily, fontWeight));
            }


        }, QKPreference.FONT_FAMILY, QKPreference.FONT_WEIGHT);

        LiveViewManager.registerView(QKPreference.FONT_SIZE, this, new LiveView() {
            @Override
            public void refresh(String key) {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, FontManager.getTextSize(mContext, FontManager.TEXT_TYPE_PRIMARY));
            }
        });

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key)  {
            setTextColor(ThemeManager.getTextOnBackgroundPrimary());
            setHintTextColor(ThemeManager.getTextOnBackgroundSecondary());
        }
        });

        LiveViewManager.registerView(QKPreference.MOBILE_ONLY, this,  new LiveView() {
            @Override
            public void refresh(String key)  {
            if (mAdapter != null) {
                SharedPreferences prefs1 = mContext.getPrefs();
                mAdapter.setShowMobileOnly(prefs1.getBoolean(SettingsFragment.MOBILE_ONLY, false));
            }
        }});
    }
}

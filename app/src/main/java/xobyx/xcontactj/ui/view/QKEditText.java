package xobyx.xcontactj.ui.view;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

import xobyx.xcontactj.common.FontManager;
import xobyx.xcontactj.common.LiveViewManager;
import xobyx.xcontactj.common.TypefaceManager;
import xobyx.xcontactj.interfaces.LiveView;
import xobyx.xcontactj.ui.ThemeManager;
import xobyx.xcontactj.enums.QKPreference;
public class QKEditText extends android.widget.EditText {
    public static final String TAG = "QKEditText";

    public interface TextChangedListener {
        void onTextChanged(CharSequence s);
    }

    private Context mContext;

    public QKEditText(Context context) {
        super(context);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public QKEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            init(context);
        }
    }

    public QKEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            init(context);
        }
    }

    private void init(Context context) {
        mContext = context;

        LiveViewManager.registerView(new LiveView() {
            @Override
            public void refresh(String key) {
                int fontFamily = FontManager.getFontFamily(mContext);
                int fontWeight = FontManager.getFontWeight(mContext, false);
                QKEditText.this.setTypeface(TypefaceManager.obtainTypeface(mContext, fontFamily, fontWeight));
            }
        }, QKPreference.FONT_FAMILY, QKPreference.FONT_WEIGHT);

        LiveViewManager.registerView(QKPreference.FONT_SIZE, this, new LiveView() {
            @Override
            public void refresh(String key) {
                QKEditText.this.setTextSize(TypedValue.COMPLEX_UNIT_SP, FontManager.getTextSize(mContext, FontManager.TEXT_TYPE_PRIMARY));
            }
        });

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, new LiveView() {
            @Override
            public void refresh(String key) {
                QKEditText.this.setTextColor(ThemeManager.getTextOnBackgroundPrimary());
                QKEditText.this.setHintTextColor(ThemeManager.getTextOnBackgroundSecondary());
            }
        });

        setText(getText());
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text) || Build.VERSION.SDK_INT < 19) {
            text = new SpannableStringBuilder(text);
        }
        super.setText(text, type);
    }

    public void setTextChangedListener(final TextChangedListener listener) {
        if (listener != null) {
            addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    listener.onTextChanged(s);
                }
            });
        }
    }
}

package xobyx.xcontactj.views;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.HashMap;

import xobyx.xcontactj.R;

/**
 * Created by xobyx on 12/14/2014.
 * c# to java
 */
@CoordinatorLayout.DefaultBehavior(FontTextView.Behavior.class)
public class FontTextView extends android.support.v7.widget.AppCompatTextView {

    public static class Behavior extends CoordinatorLayout.Behavior<FontTextView> {
        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, FontTextView child, int layoutDirection) {
            //return
            ViewCompat.setLayoutDirection(child, ViewCompat.LAYOUT_DIRECTION_RTL);
            return super.onLayoutChild(parent, child, ViewCompat.LAYOUT_DIRECTION_RTL);

        }
    }

    public static HashMap<String, Typeface> mTypefaces;


    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!this.isInEditMode()) {
            if (mTypefaces == null) {
                mTypefaces = new HashMap<>();
            }

            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
            if (array != null) {

                final String typefaceAssetPath = array.getString(
                        R.styleable.FontTextView_font);

                Typeface font = getFont(context, typefaceAssetPath);
                if (font != null)
                    setTypeface(font);
                array.recycle();
            }
        }
    }

    public static Typeface getFont(Context context, String typefaceAssetPath) {
        if (typefaceAssetPath != null) {
            Typeface font;

            if (mTypefaces.containsKey(typefaceAssetPath)) {
                font = mTypefaces.get(typefaceAssetPath);
            } else {
                AssetManager assets = context.getAssets();
                font = Typeface.createFromAsset(assets, typefaceAssetPath);
                mTypefaces.put(typefaceAssetPath, font);
            }

            return font;
        }
        return null;
    }


}

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
public class FontTextView extends TextView {

    public static class Behavior extends CoordinatorLayout.Behavior<FontTextView> {
        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, FontTextView child, int layoutDirection) {
            //return
            ViewCompat.setLayoutDirection(child,ViewCompat.LAYOUT_DIRECTION_RTL);
           return super.onLayoutChild(parent, child, ViewCompat.LAYOUT_DIRECTION_RTL);

        }
    }

    private static HashMap<String, Typeface> mTypefaces;


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
                mTypefaces = new HashMap<String, Typeface>();
            }

            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
            if (array != null) {

                final String typefaceAssetPath = array.getString(
                        R.styleable.FontTextView_font);

                if (typefaceAssetPath != null) {
                    Typeface font;

                    if (mTypefaces.containsKey(typefaceAssetPath)) {
                        font = mTypefaces.get(typefaceAssetPath);
                    } else {
                        AssetManager assets = context.getAssets();
                        font = Typeface.createFromAsset(assets, typefaceAssetPath);
                        mTypefaces.put(typefaceAssetPath, font);
                    }

                    setTypeface(font);
                }
                array.recycle();
            }
        }
    }


}

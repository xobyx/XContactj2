package xobyx.xcontactj.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import xobyx.xcontactj.R;

/**
 * Created by xobyx on 11/28/2014.
 * c# to java
 */
public class xViewPager extends ViewPager {
    private final PageTransformer vb =new PageTransformer() {
        private final float MinScale = 0.85f;
        private final float MinAlpha = 0.5f;
        @Override
        public void transformPage(View page, float position) {
            if(mPageTransformer !=null)
                mPageTransformer.transformPage(page,position);
            if(transformStateListener !=null)
                transformStateListener.ViewPagerTransform(indexOfChild(page),position);


        }
    };

    PageTransformer mPageTransformer;
    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        mPageTransformer =transformer;

    }

    private boolean MoveEnabled = true;
    private TransformStateListener transformStateListener;

    public xViewPager(Context context) {
        super(context);
    }

    interface TransformStateListener
    {
        /**
         * Apply a property transformation to the given page.
         *
         *
         * @param position Position of page relative to the current front-and-center
         *                 position of the pager. 0 is front and center. 1 is one full
         *                 page position to the right, and -1 is one page position to the left.
         */
        void ViewPagerTransform(int index,float position);
    }

    public void setViewTransStatListener(TransformStateListener c)
    {
        this.transformStateListener =c;
    }
    public xViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.xViewPager, 0, 0);
        final boolean hasValue = styledAttributes.hasValue(R.styleable.xViewPager_lock_move);
        setMoveEnabled(styledAttributes.getBoolean(
                R.styleable.xViewPager_lock_move, false));
        styledAttributes.recycle();
        //super.setPageTransformer(false,vb);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return isMoveEnabled() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent var1) {
        /*if (!isMoveEnabled()) {
            int var2 = 255 & var1.getAction();
            float var3 = var1.getX();
            if (var2 == 0)
                xold = var3;
            if (var2 == 2)
                if (Math.abs(var3 - xold) > 250) {
                    Toast.makeText(getContext(), "You cant swap when Searching or Picking ..", Toast.LENGTH_SHORT).show();
                    xold = 400;
                }


        }
*/

        return isMoveEnabled() && super.onInterceptTouchEvent(var1);
    }

    public boolean isMoveEnabled() {
        return MoveEnabled;
    }

    public void setMoveEnabled(boolean moveEnabled) {
        MoveEnabled = moveEnabled;
    }
}

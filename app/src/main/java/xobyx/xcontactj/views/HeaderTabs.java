package xobyx.xcontactj.views;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import xobyx.xcontactj.R;

/**
 * Created by xobyx on 12/4/2014.
 * c# to java
 */
public class HeaderTabs extends ViewGroup implements ViewPager.OnPageChangeListener, View.OnClickListener {

    View old;
    Runnable itemSelector;
    private xViewPager mViewPager;
    private int selected;
    private Animation mItemAnmiton;
    private AnimationSet n;


    public HeaderTabs(Context context, AttributeSet attrs) {

        super(context, attrs);
        for (int i = 0; i < 3; i++) {
            ImageView view = new ImageView(context);



            view.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setOnClickListener(this);


            view.setClickable(true);

          //  addView(view, i, new LayoutParams(0, 50-1, 1));
        //    addView(view, i, new LayoutParams(0, LayoutParams.MATCH_PARENT));
            addViewInLayout(view, -1, new LayoutParams(0, LayoutParams.MATCH_PARENT), false);
        }


       //this.setWeightSum(3);


    }
    Rect a = new Rect();
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent Motion) {

        return super.dispatchTouchEvent(Motion);
    }

    public HeaderTabs(Context context) {
        this(context, null);
    }



    private void Draw() {
        if (mViewPager.getAdapter() != null) {
            final HeaderTabsAdapter adapter = (HeaderTabsAdapter) mViewPager.getAdapter();
            for (int i = 0; i < 3; i++) {

                ((ImageView) this.getChildAt(i)).setImageResource(adapter.getDrawableRecourse(i));
                //((ImageView) this.getChildAt(i)).setScaleType(ImageView.ScaleType.FIT_XY);



            }
        }
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     *                 Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    float pakl;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        int t= 3;




       /* if (checkDirection) {
            if (thresholdOffset > positionOffset && positionOffsetPixels > thresholdOffsetPixels) {
                toPos=selected+1>2?2:selected+1;
                Toast.makeText(getContext(),"to : "+toPos,Toast.LENGTH_SHORT).show();
                mmm = (0.4f * ((float) positionOffsetPixels / (float) getWidth()));
            } else {
                toPos=selected-1<0?0:selected-1;
                // toPos = lastPOs - 1;
                Toast.makeText(getContext(),"to : "+toPos,Toast.LENGTH_SHORT).show();

                mmm = (0.4f * (((float)getWidth()-(float) positionOffsetPixels) / (float) getWidth()));
            }
            if(toPos!=selected) {
                g[toPos] += mmm;  ////0.2+mmm  ///0.1,0.2,0.3,0.4
                g[selected] -=mmm;

            }
            checkDirection = false;
        }

        requestLayout();*/














    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPageSelected(int i) {


        selected=i;

   //     ((ImageView) this.getChildAt(i)).setImageAlpha(255);

        final View at = getChildAt(i);
        Runnable n = new Runnable() {
            @Override
            public void run() {
                anim(at);
            }
        };
        post(n);





    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }




    private void anim(View at) {
        if (!at.equals(old)) {
            at.setSelected(true);



            //at.startAnimation(new ScaleAnimation(1,2,1,2,at.getPivotX(),at.getPivotY()));

            if (old != null) {

                old.setSelected(false);



            }
            old = at;
        }

    }



    Animation itemAnim() {
        if (mItemAnmiton == null)
            mItemAnmiton = AnimationUtils.loadAnimation(getContext(), R.anim.abc_popup_enter);
        return mItemAnmiton;

    }

    @Override
    public void onClick(final View v) {
        if (itemSelector != null) {
            removeCallbacks(itemSelector);
        }

        itemSelector = new Runnable() {
            @Override
            public void run() {
                if (!mViewPager.isMoveEnabled() || v == null) return;
                int i = indexOfChild(v);
                if (i > -1) {
                    mViewPager.setCurrentItem(i);

                    v.startAnimation(itemAnim());
                }
            }
        };
        postDelayed(itemSelector, 20);


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (itemSelector != null) {
            // Re-post the selector we saved
            post(itemSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (itemSelector != null) {
            removeCallbacks(itemSelector);
        }


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int total_width = getWidth();
        int width= (int) (total_width /3f);

/// |---|---|----|----|----|----|
//  0f      1f         2f       3f
//    0+width/2    width+2f/2    2F+3f/2
//       (i*width)+((i+1)*width




        int from_left=0;

        for(int i=0;i<getChildCount();i++)
        {

            View view = getChildAt(i);
            view.layout(from_left,0, from_left+width,getHeight());
            from_left+=width;


        }







    }

    public void setViewPager(xViewPager viewPager) {
        this.mViewPager = viewPager;
        Draw();
        mViewPager.addOnPageChangeListener(this);
    }











    public interface HeaderTabsAdapter {
        int getDrawableRecourse(int index);
    }
}

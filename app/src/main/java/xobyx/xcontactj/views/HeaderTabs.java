package xobyx.xcontactj.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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

    private Animation mItemAnmiton;
    private boolean mIsDown;
    private int yLast;
    private ActionBar actionBar;
    private float mx;
    private float my;
    private int w;
    private android.graphics.Path m=new Path();
    private static final Paint mj;

    static {
        mj=new Paint(Paint.ANTI_ALIAS_FLAG);
        mj.setColor(Color.GREEN);
        mj.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    private Rect tmpRect=new Rect();
    private int selected;
    private int lastPOs;
    private int toPos;
    private float mmm;
    private boolean scrollStarted;
    private boolean checkDirection;
    private static final float thresholdOffset = 0.5f;
    private static final int thresholdOffsetPixels = 1;


    @Override

    protected void dispatchDraw(Canvas canvas) {



       // int save = canvas.save();

       // Rect newRect = canvas.getClipBounds();
      //  super.dispatchDraw(canvas);
       // drawChild()
       //
      // canvas.save();
       // SizeAnimation.setFillType(Path.FillType.INVERSE_WINDING);
      //  canvas.save();

        //canvas.drawPath(SizeAnimation, HeaderTabs.mj);

     //   getDrawingRect(tmpRect);

     //   SizeAnimation.reset();
        //newRect.inset(0, -getHeight() * 5);  //make the rect larger

        //canvas.clipRect(newRect, Region.Op.REPLACE);
//canvas.clipRect(0, 0, getWidth(), getHeight());
      //  SizeAnimation.reset();
      // SizeAnimation.addCircle(getWidth() / 2f, getHeight() * 4f, getHeight() * 4.2f, Path.Direction.CW);
        //    canvas.clipPath(SizeAnimation);

          //  canvas.drawPath(SizeAnimation, mj);

           // canvas.drawColor(Color.GREEN);
        ;


       // canvas.drawPath(SizeAnimation,mj);
       // canvas.clipPath(SizeAnimation);
        //canvas.clipRect(5,10,getWidth()-5,getHeight()-10);


        //canvas.drawPath(SizeAnimation,mj);
      //  canvas.clipPath(SizeAnimation);



        //canvas.drawPath(SizeAnimation,mj);



       super.dispatchDraw(canvas);

        //canvas.restoreToCount(save);
        //canvas.restore();
       // canvas.restoreToCount(save);
    }


    public HeaderTabs(Context context, AttributeSet attrs) {

        super(context, attrs);
        for (int i = 0; i < 3; i++) {
            ImageView view = new ImageView(context);

            view.setOnClickListener(this);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            view.setClickable(true);

          //  addView(view, i, new LayoutParams(0, 50-1, 1));
        //    addView(view, i, new LayoutParams(0, LayoutParams.MATCH_PARENT));
            addViewInLayout(view, -1, new LayoutParams(0, LayoutParams.MATCH_PARENT), false);
        }


       // this.setWeightSum(3);


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

                ((ImageView) this.getChildAt(i)).setImageResource(adapter.getDrawbleResourse(i));



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
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        Log.d("ViewPager","--------------------");
        Log.d("ViewPager","position : "+position);
        Log.d("ViewPager","positionOffset : "+position);
        Log.d("ViewPager","positionOffsetPixels : "+positionOffsetPixels);
        Log.d("ViewPager","---------------------");


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





        Log.d("ViewPager","mmm : "+mmm);








    }

    @Override
    public void onPageSelected(int i) {
       /* switch (i) {
     a       case 0:
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_ba_za));
                break;
            case 1:
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_ba_sd));
                break;
            case 2:
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_ba_mtn));
                break;

        }*/

        selected=i;

        setGrav(selected);

//        requestLayout();
        final View at = getChildAt(i);
        Runnable n = new Runnable() {
            @Override
            public void run() {
                anim(at);
            }
        };
        post(n);


        ///base Listener


    }

    private void setGrav(int selected) {
        for(int i=0;i<getChildCount();i++)
            g[i]=i==selected?0.7f:0.15f;
    }


    private void anim(View at) {
        if (!at.equals(old)) {
            at.setSelected(true);


            // setBackgroundResource(nColors[i]);

            if (old != null) {

                old.setSelected(false);


            }
            old = at;
        }
        requestLayout();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

        if (!scrollStarted && i == ViewPager.SCROLL_STATE_DRAGGING) {
            scrollStarted = true;
            checkDirection = true;
        } else {
            scrollStarted = false;
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
        post(itemSelector);


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
        w=getWidth();
        int f= (int) (w/3f);

/// |---|---|----|----|----|----|
//  0f      1f         2f       3f
//    0+f/2    f+2f/2    2F+3f/2
//       (i*f)+((i+1)*f



        float gtotal=1f;
        float max= 0.6f;
        float min= 0.2f;
        int fromleft=0;

        for(int i=0;i<getChildCount();i++)
        {

            View view = getChildAt(i);
            view.layout(fromleft,0,(int)(fromleft+getWidth()*g[i]),getHeight());
            fromleft+=getWidth()*g[i];


        }







    }

    public void setViewPager(xViewPager viewPager) {
        this.mViewPager = viewPager;
        Draw();
        mViewPager.addOnPageChangeListener(this);
    }

    float[] g={0,0,0};
void m()
{

    float gtotal=1f;
    float max= 0.6f;
    float min= 0.2f;
    int fromleft=0;

    for(int i=0;i<getChildCount();i++)
    {

        View view = getChildAt(i);
        view.layout(fromleft,0,(int)(fromleft+getWidth()*g[i]),getHeight());
        fromleft+=getWidth()*g[i];


    }







}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       // canvas.drawColor(Color.BLACK);


    }

    final Paint paint = new Paint();

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;

    }



    public interface HeaderTabsAdapter {
        int getDrawbleResourse(int index);
    }
}

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import xobyx.xcontactj.R;

/**
 * Created by xobyx on 12/4/2014.
 * c# to java
 */
public class HeaderTabs extends ViewGroup implements ViewPager.OnPageChangeListener, View.OnClickListener {

    View old;
    Runnable itemSelector;
    private xViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;
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

    @Override

    protected void dispatchDraw(Canvas canvas) {

        int save = canvas.save();

        Rect newRect = canvas.getClipBounds();
      //  super.dispatchDraw(canvas);
       // drawChild()
       //
      // canvas.save();
       // SizeAnimation.setFillType(Path.FillType.INVERSE_WINDING);
      //  canvas.save();

        //canvas.drawPath(SizeAnimation, HeaderTabs.mj);

        getDrawingRect(tmpRect);

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
        canvas.restoreToCount(save);
    }


    public HeaderTabs(Context context, AttributeSet attrs) {

        super(context, attrs);
        for (int i = 0; i < 3; i++) {
            ImageView view = new ImageView(context);

            view.setOnClickListener(this);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            view.setClickable(true);
          //  addView(view, i, new LayoutParams(0, 50-1, 1));
            addView(view, i, new LayoutParams(0, LayoutParams.MATCH_PARENT));
        }

        setClipChildren(false);

       // this.setWeightSum(3);


    }
    Rect a = new Rect();
    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent Motion) {
        int action = Motion.getAction();
        float MotionX = Motion.getX();
//(int) ((width)-(Motion.getX()))/18

        int width = this.getWidth() - (this.getPaddingLeft() + this.getPaddingRight());
        int height = this.getHeight();
        //loc size
        int i1 = (int) (width - (Motion.getX()) - 1);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.mIsDown = true;

                getChildAt(0).getDrawingRect(a);

                if (a.contains((int) MotionX, (int) Motion.getY())) {
                    Toast.makeText(getContext(), "Zain", Toast.LENGTH_SHORT).show();
                }
                getChildAt(1).getDrawingRect(a);
                if (a.contains((int) MotionX, (int) Motion.getY())) {
                    Toast.makeText(getContext(), "Sudani", Toast.LENGTH_SHORT).show();
                }
                getChildAt(2).getDrawingRect(a);
                if (a.contains((int) MotionX, (int) Motion.getY())) {
                    Toast.makeText(getContext(), "Mtn", Toast.LENGTH_SHORT).show();
                }
                mIsDown = true;
                mx = MotionX;
                my=Motion.getY();


                break;
            case MotionEvent.ACTION_UP:
                mIsDown = false;
                break;

            default:
               // return false;
                break;
        }
        invalidate();
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


    @Override
    public void onPageScrolled(int i, float v, int i2) {
        if (mListener != null)
            mListener.onPageScrolled(i, v, i2);
    }

    @Override
    public void onPageSelected(int i) {
       /* switch (i) {
            case 0:
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_ba_za));
                break;
            case 1:
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_ba_sd));
                break;
            case 2:
                actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_ba_mtn));
                break;

        }*/

        final View at = getChildAt(i);
        Runnable n = new Runnable() {
            @Override
            public void run() {
                anim(at);
            }
        };
        post(n);


        ///base Listener
        if (mListener != null)
            mListener.onPageSelected(i);

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
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        if (mListener != null)
            mListener.onPageScrollStateChanged(i);
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
        m.reset();
/// |-------|---------|---------|
//  0       f         2f       3f
//    0+f/2    f+2f/2    2f+3f/2
//       (i*f)+((i+1)*f
        int f= (int) (w/3f);
        for (int i=0;i<getChildCount();i++) {
            getChildAt(i).layout(i * f, 0, ((i + 1) * f), getHeight());
            float mk=((i*f)+((i+1)*f))/2f;
            m.addCircle(mk, getHeight() / 2f, getHeight()/2.1f, Path.Direction.CW);
            m.close();

        }
    }

    public void setViewPager(xViewPager viewPager) {
        this.mViewPager = viewPager;
        Draw();
        mViewPager.addOnPageChangeListener(this);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mListener = onPageChangeListener;
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

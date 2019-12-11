package xobyx.xcontactj.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by xobyx on 9/28/2016.
 for ${PACKAGE_NAME}
 M:09
 */

public class m extends View {
    private Paint mm;
    private int nrm=0xFF4081;
    private float sm;
    private float circle_red=0f;
    private Paint tt;

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
           animator.start();
        }
    };

    public m(Context context) {
        super(context);
        intd();
    }

    public m(Context context, AttributeSet attrs) {
        super(context, attrs);
        intd();
    }

    public m(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intd();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public m(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        intd();
    }

    public void setColor(int h)
    {
       // if(animator.isRunning())animator.cancel();
        nrm=h;
        mm.setColor(h);
        if(!animator.isStarted())getHandler().post(runnable);

        //x.post(runnable);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        animator.setFloatValues(0, right/2,0);
    }

    ValueAnimator animator = new ValueAnimator();
    void intd()
    {
        mm=new Paint(Paint.ANTI_ALIAS_FLAG);
        mm.setColor(nrm);
        tt=new Paint(mm);
        tt.setColor(Color.WHITE);

        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setFloatValues(0, getWidth(),0);
        //animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                circle_red =  (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

          //      x.postDelayed(runnable,500);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            //    x.removeCallbacks(runnable);


            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }


    Handler x=new Handler();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),mm);

        canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,circle_red,tt);


    }
}

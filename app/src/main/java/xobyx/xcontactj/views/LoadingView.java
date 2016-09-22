package xobyx.xcontactj.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class LoadingView extends View {


    //静止状态
    private final int STATUS_STILL = 0;
    //加载状态
    private final int STATUS_LOADING = 1;
    //线条最大长度
    private final int MAX_LINE_LENGTH = dp2px(getContext(), 120);
    //线条最短长度
    private final int MIN_LINE_LENGTH = dp2px(getContext(), 40);
    //最大间隔时长
    private final int MAX_DURATION = 3000;
    //最小间隔时长
    private final int MIN_DURATION = 500;
    //Canvas起始旋转角度
    private final int CANVAS_ROTATE_ANGLE = 60;
    private Paint mPaint;
    private int[] mColors = new int[]{0xb004a7dd, 0xb0f8c810, 0xb0c10278, 0xb05aba94};
    private int mWidth, mHeight;
    //动画间隔时长
    private int mDuration = MIN_DURATION;
    //线条总长度
    private int mEntireLineLength = MIN_LINE_LENGTH;
    //圆半径
    private int mCircleRadius;
    //所有动画
    private List<Animator> mAnimList = new ArrayList<>();
    //动画当前状态
    private int mStatus = STATUS_STILL;
    //Canvas旋转角度
    private int mCanvasAngle;
    //线条长度
    private float mLineLength;
    //半圆Y轴位置
    private float mCircleY;
    //第几部动画
    private int mStep;
    private Paint mTextPaint;
    private Rect mCharBound = new Rect();
    private Paint mPaint2;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(dp2px(this.getContext(),15));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mPaint2=new Paint(mTextPaint);

        mPaint.setAntiAlias(true);
        mPaint.setColor(mColors[0]);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initData() {
        mCanvasAngle = CANVAS_ROTATE_ANGLE;
        mLineLength = mEntireLineLength;
        mCircleRadius = mEntireLineLength / 5;
        mPaint.setStrokeWidth(mCircleRadius * 2);
        mStep = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStep % 4) {
            case 0:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    drawCRLC(canvas, Net[i], mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 - mLineLength, mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mEntireLineLength, mPaint, mCanvasAngle + i * 90);
                }
                break;
            case 1:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    mPaint2.setColor(mColors[i+2<=3?i+2:Math.abs(i+2-4)]);
                    drawCR(canvas, Net[i], mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mEntireLineLength, mPaint,mPaint2, mCanvasAngle + i * 90);
                }
                break;
            case 2:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    drawCRCC(canvas, Net[i], mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mCircleY, mPaint, mCanvasAngle + i * 90);
                }
                break;
            case 3:
                for (int i = 0; i < mColors.length; i++) {
                    mPaint.setColor(mColors[i]);
                    drawLC(canvas, mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mEntireLineLength, mWidth / 2 - mEntireLineLength / 2.2f, mHeight / 2 + mLineLength, mPaint, mCanvasAngle + i * 90);
                }
                break;
        }

    }

    Path m = new Path();
    String Net[] = {"Zain", "All", "Sudani", "Mtn"};//s+2

    private void drawCRLC(Canvas canvas, String s, float startX, float startY, float stopX, float stopY, @NonNull Paint paint, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);

       // m.moveTo(startX, startY);

        //m.lineTo(stopX, stopY);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        //float mTextWidth = mTextPaint.measureText(s);
        //float textHeight = mCharBound.height();

        //mTextPaint.getTextBounds(s, 0, 1, mCharBound);
        //canvas.drawTextOnPath(s, m, 0, 0, mTextPaint);
        //canvas.drawText(s,((startX+stopX)/2f)- mTextWidth / 2f,((startY+stopY)/2f)+ textHeight / 2f,mTextPaint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);
        m.reset();
    }

    RectF ms = new RectF();

    private void drawCR(Canvas canvas, String s, float x, float y, @NonNull Paint paint, Paint paint1, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);


        canvas.drawCircle(x, y, mCircleRadius, paint);


        ms.set((mWidth / 2f) - mEntireLineLength * 1.2f, (mHeight / 2f) - mEntireLineLength * 1.2f, (mWidth / 2f) + mEntireLineLength * 1.2f, (mHeight / 2f) + mEntireLineLength * 1.2f);


        m.addArc(ms, (float) Math.toDegrees(Math.atan(((mHeight / 2f) - y) / ((mWidth / 2f) - x))), 90);
        canvas.drawTextOnPath(s, m, 0, 0, paint1);


        m.reset();
        //canvas.drawText("Sudan PhoneBook", x, y, mTextPaint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);
    }

    private void drawCRCC(Canvas canvas, String net, float x, float y, @NonNull Paint paint, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);
        canvas.drawCircle(x, y, mCircleRadius, paint);
       // float mTextWidth = mTextPaint.measureText(String.valueOf(net.charAt(0)));
        //float textHeight = mCharBound.height();

       // mTextPaint.getTextBounds(String.valueOf(net.charAt(0)), 0, 1, mCharBound);
       // canvas.drawText(String.valueOf(net.charAt(0)), x - mTextWidth / 2f, y + textHeight / 2f, mTextPaint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);
    }

    private void drawLC(Canvas canvas, float startX, float startY, float stopX, float stopY, @NonNull Paint paint, int rotate) {
        canvas.rotate(rotate, mWidth / 2, mHeight / 2);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
        //canvas.drawText("Sudan PhoneBook",(startX+stopX)/2f,(startY+stopY)/2f,mTextPaint);
        canvas.rotate(-rotate, mWidth / 2, mHeight / 2);
    }

    /**
     * Animation1
     * 动画1
     * Canvas Rotate Line Change
     * 画布旋转及线条变化动画
     */
    private void startCRLCAnim() {

        Collection<Animator> animList = new ArrayList<>();

        ValueAnimator canvasRotateAnim = ValueAnimator.ofInt(CANVAS_ROTATE_ANGLE + 0, CANVAS_ROTATE_ANGLE + 360);
        canvasRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasAngle = (int) animation.getAnimatedValue();
            }
        });

        animList.add(canvasRotateAnim);

        ValueAnimator lineWidthAnim = ValueAnimator.ofFloat(mEntireLineLength, -mEntireLineLength);
        lineWidthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animList.add(lineWidthAnim);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(mDuration);
        animationSet.playTogether(animList);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.d("@=>", "动画1结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startCRAnim();
                }
            }
        });
        animationSet.start();

        mAnimList.add(animationSet);
    }

    /**
     * Animation2
     * 动画2
     * Canvas Rotate
     * 画布旋转动画
     */
    private void startCRAnim() {
        ValueAnimator canvasRotateAnim = ValueAnimator.ofInt(mCanvasAngle, mCanvasAngle + 180);
        canvasRotateAnim.setDuration(mDuration / 2);
        canvasRotateAnim.setInterpolator(new LinearInterpolator());
        canvasRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasAngle = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        canvasRotateAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.d("@=>", "动画2结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startCRCCAnim();
                }
            }
        });
        canvasRotateAnim.start();

        mAnimList.add(canvasRotateAnim);
    }

    /**
     * Animation3
     * 动画3
     * Canvas Rotate Circle Change
     * 画布旋转圆圈变化动画
     */
    private void startCRCCAnim() {
        Collection<Animator> animList = new ArrayList<>();

        ValueAnimator canvasRotateAnim = ValueAnimator.ofInt(mCanvasAngle, mCanvasAngle + 90, mCanvasAngle + 180);
        canvasRotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCanvasAngle = (int) animation.getAnimatedValue();
            }
        });

        animList.add(canvasRotateAnim);

        ValueAnimator circleYAnim = ValueAnimator.ofFloat(mEntireLineLength, mEntireLineLength / 4, mEntireLineLength);
        circleYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCircleY = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        animList.add(circleYAnim);

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(mDuration);
        animationSet.playTogether(animList);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.d("@=>", "动画3结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startLCAnim();
                }
            }
        });
        animationSet.start();

        mAnimList.add(animationSet);
    }

    /**
     * Animation4
     * 动画4
     * Line Change
     * 线条变化动画
     */
    private void startLCAnim() {
        ValueAnimator lineWidthAnim = ValueAnimator.ofFloat(mEntireLineLength - dp2px(getContext(), 1), -mEntireLineLength);
        lineWidthAnim.setDuration(mDuration);
        lineWidthAnim.setInterpolator(new LinearInterpolator());
        lineWidthAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineLength = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        lineWidthAnim.addListener(new AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //Log.d("@=>", "动画4结束");
                if (mStatus == STATUS_LOADING) {
                    mStep++;
                    startCRLCAnim();
                }
            }
        });
        lineWidthAnim.start();

        mAnimList.add(lineWidthAnim);
    }

    public void setLineLength(float scale) {
        mEntireLineLength = (int) (scale * (MAX_LINE_LENGTH - MIN_LINE_LENGTH)) + MIN_LINE_LENGTH;
        reset();
    }

    public void setDuration(float scale) {
        mDuration = (int) (scale * (MAX_DURATION - MIN_DURATION)) + MIN_DURATION;
        reset();
    }

    public void start() {
        if (mStatus == STATUS_STILL) {
            mAnimList.clear();
            mStatus = STATUS_LOADING;
            startCRLCAnim();
        }
    }

    public void reset() {
        if (mStatus == STATUS_LOADING) {
            mStatus = STATUS_STILL;
            for (Animator anim : mAnimList) {
                anim.cancel();
            }
        }
        initData();
        invalidate();
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public abstract class AnimatorListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
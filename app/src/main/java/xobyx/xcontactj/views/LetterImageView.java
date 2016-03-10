package xobyx.xcontactj.views;

/**
 * Created by xobyx on 6/17/2015.
 * c# to java
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.Random;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.ME;

public class LetterImageView extends ImageView {


    private char mLetter;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private int mTextColor = Color.WHITE;
    private boolean isOval;
    private int mNet;
    private Rect textBounds=new Rect();
    private float textSize=0;
    private boolean isLetter;
    private float m=1;

    public LetterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterImageView);
        final boolean hasValue = array.hasValue(R.styleable.LetterImageView_randomColor);
        final boolean m = array.hasValue(R.styleable.LetterImageView_fchar);
        if(m){
            final String string = array.getString(R.styleable.LetterImageView_fchar);
            mLetter=string.charAt(0);
        }
        init(array.getBoolean(
                R.styleable.LetterImageView_randomColor, false));
        array.recycle();
    }

    public void setCustomColor(int u) {
        mNet = u;


        mBackgroundPaint.setColor(ME.nColors[u]);
        invalidate();

    }

    public void setFont(Typeface t) {
        mTextPaint.setTypeface(t);
        invalidate();

    }

    private void init(boolean a) {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        if (a)
            mBackgroundPaint.setColor(randomColor());
        else {
            mBackgroundPaint.setColor(getResources().getColor(R.color.transparent));
        }

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                m = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    public char getLetter() {
        return mLetter;
    }

    public void setLetter(char letter) {
        isLetter=true;
        mTextPaint.measureText(String.valueOf(letter));
        mLetter = letter;
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    ValueAnimator animator=new ValueAnimator();
    public boolean isOval() {
        return isOval;
    }

    public void setOval(boolean oval) {
        isOval = oval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //long v= 0x1c2a344b6bL;
        canvas.scale(m,m,canvas.getWidth()/2f,canvas.getHeight()/2f);

        if (getDrawable() == null) {

            // Set a text font size based on the height of the view
            if(textSize==0) textSize= canvas.getHeight() - getTextPadding() * 2;
            mTextPaint.setTextSize(textSize);
            if (isOval()) {
                canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth(), canvas.getHeight()) / 2f,
                        mBackgroundPaint);

            } else {
                canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint);
            }
            ME.DrawNetworkLogo(getContext(), canvas, mNet);

            // Measure a text

            mTextPaint.getTextBounds(String.valueOf(mLetter), 0, 1, textBounds);
            float textWidth = mTextPaint.measureText(String.valueOf(mLetter));
            float textHeight = textBounds.height();
            // Draw the text
            canvas.drawText(String.valueOf(mLetter), canvas.getWidth() / 2f - textWidth / 2f,
                    canvas.getHeight() / 2f + textHeight / 2f, mTextPaint);

        } else

            ME.DrawNetworkLogo(getContext(), canvas, mNet);
    }


    private float getTextPadding() {
        // Set a default padding to 8dp
        return 8 * getResources().getDisplayMetrics().density;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==event.ACTION_DOWN) {
           /* if(isLetter) {*/
                animator.setFloatValues(1f, 1.5f,1f);


                animator.start();
           /* }
            else {
                setPivotX(getWidth() / 2f);
                setPivotY(getHeight() / 2f);

                ViewCompat.animate(this).scaleX(1.5f).scaleY(1.5f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        LetterImageView.this.animate().scaleX(1.0f).scaleY(1.0f).start();
                    }
                });
                ViewCompat.setElevation(this, 20);
            }*/
        }


        return super.onTouchEvent(event);

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        isLetter=false;
    }

    private int randomColor() {
        if (isInEditMode()) {
            return 0x0099cc;
        }
        Random random = new Random();
        String[] colorsArr = getResources().getStringArray(R.array.colors);
        return Color.parseColor(colorsArr[random.nextInt(colorsArr.length)]);
    }
}

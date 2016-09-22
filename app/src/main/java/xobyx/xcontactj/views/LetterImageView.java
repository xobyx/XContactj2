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
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;

public class LetterImageView extends ImageView {


    private char mLetter;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;
    private int mTextColor = Color.WHITE;
    private boolean isOval;
    private int mNet;
    private Rect textBounds = new Rect();
    private float textSize = 0;
    private boolean isLetter;
    private float m = 1;
    private boolean anm;
    private float circle_red = 0;
    private Paint sx;
    private float Xc;
    private float Xy;
    private boolean contact_is_set;
    private int total_numbers;
    private ArrayList<Contact.Phones> phonelist;
    private boolean mDrawNetLogo =true;


    public LetterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LetterImageView);
        final boolean hasValue = array.hasValue(R.styleable.LetterImageView_randomColor);
        final boolean m = array.hasValue(R.styleable.LetterImageView_fchar);
        mDrawNetLogo = array.getBoolean(R.styleable.LetterImageView_draw_net_logo,true);
        if (m) {
            final String string = array.getString(R.styleable.LetterImageView_fchar);
            mLetter = string != null ? string.charAt(0) : ' ';
        }
        init(array.getBoolean(
                R.styleable.LetterImageView_randomColor, false));
        array.recycle();
    }

    public LetterImageView(Context context) {
        super(context);
        init(true);

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
        b = new Handler();
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        if (a)
            mBackgroundPaint.setColor(randomColor());
        else {
            mBackgroundPaint.setColor(getResources().getColor(R.color.transparent));
        }

        sx = new Paint(Paint.ANTI_ALIAS_FLAG);
        sx.setStyle(Paint.Style.FILL);
        sx.setColor(0x94FFFFFF);

        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator(1.2f));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                m = (float) animation.getAnimatedValue();
                circle_red = (float) ((m - 1f) / 0.5f) * (getHeight() / 2);
                invalidate();
            }
        });

    }

    public char getLetter() {
        return mLetter;
    }

    Rect mCharBound = new Rect();

    public void setLetter(char letter) {
        isLetter = true;

        if (letter == '\u0647') {
            letter = '\uFEEB';
        }

        // mTextPaint.setElegantTextHeight(true);
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

    //////// z,s,m,u


    public void setContact(Contact ds) {
        // WeakReference<Contact> a=new WeakReference<Contact>(ds);
        phonelist=new ArrayList<Contact.Phones>(ds.Phone);
        //total_numbers = ds.Phone.size();
        contact_is_set = true;


        invalidate();
    }

    RectF ovl = new RectF();

    void draw_background(Canvas n) {
        int i[] = {0, 0, 0, 0};
        for (Contact.Phones pn : phonelist) {
            i[pn.nNet.getValue()]++;
        }
        float angle;
        float start_angle = 0;
        ovl.set(0, 0, n.getWidth(), n.getHeight());
        for (int i1 = 0; i1 < i.length; i1++) {
            if (i[i1] > 0) {
                angle = ((float) i[i1] / (float) phonelist.size()) * 360f;
                mBackgroundPaint.setColor(ME.nColors[i1]);

                n.drawArc(ovl, start_angle, angle, isOval, mBackgroundPaint);
                start_angle += angle;
            }
        }
    }

    ValueAnimator animator = new ValueAnimator();

    public boolean isOval() {
        return isOval;
    }

    public void setOval(boolean oval) {
        isOval = oval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(m, m, canvas.getWidth() / 2f, canvas.getHeight() / 2f);
        super.onDraw(canvas);


        //long v= 0x1c2a344b6bL;


        if (getDrawable() == null) {

            // Set a text font size based on the height of the view
            if (textSize == 0) textSize = canvas.getHeight() - getTextPadding() * 2;
            mTextPaint.setTextSize(textSize);
            if (!contact_is_set) {
                if (isOval()) {
                    canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth(), canvas.getHeight()) / 2f,
                            mBackgroundPaint);

                } else {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mBackgroundPaint);
                }
            } else {
                int save = canvas.save();

                canvas.rotate(45,canvas.getWidth() / 2f, canvas.getHeight() / 2f);
                draw_background(canvas);

                canvas.restoreToCount(save);
            }

            // Measure a text

            float mTextWidth = mTextPaint.measureText(String.valueOf(mLetter));

            mTextPaint.getTextBounds(String.valueOf(mLetter), 0, 1, mCharBound);

            float textHeight = mCharBound.height();

            if (mLetter >= '\u0623' && mLetter <= '\u064a') {
                textHeight = mCharBound.height() / 2f;
            }
            // Draw the text
            canvas.drawText(String.valueOf(mLetter), canvas.getWidth() / 2f - mTextWidth / 2f,
                    canvas.getHeight() / 2f + textHeight / 2f, mTextPaint);


        }

        canvas.restore();
        if (!contact_is_set&& mDrawNetLogo)
            ME.DrawNetworkLogo(getContext(), canvas, mNet);


        canvas.drawCircle(Xc, Xy, circle_red, sx);


    }


    private float getTextPadding() {
        // Set a default padding to 8dp
        return 8 * getResources().getDisplayMetrics().density;
    }

    Handler b;
    private final Runnable runnable = new Runnable() {
        public void run() {
            LetterImageView.this.invalidate();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
           /* if(isLetter) {*/
            if(animator.isRunning())animator.cancel();
            animator.setFloatValues(1f, 2f, 1f);
            Xc = event.getX();
            Xy = event.getY();


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
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (animator.isRunning()) {
                animator.cancel();
                animator.setFloatValues(1f, 1.5f, 1f);
                Xc = event.getX();
                Xy = event.getY();


                animator.start();
            } else if(event.getAction() == MotionEvent.ACTION_UP||event.getAction() == MotionEvent.ACTION_CANCEL) {
                animator.cancel();


            }
               //{} animator.start();
        }

        return true;

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        isLetter = false;
    }

    private int randomColor() {
        if (isInEditMode()) {
            return 0x0099cc;
        }
        Random random = new Random();
        String[] colorsArr = getResources().getStringArray(R.array.colors);
        return Color.parseColor(colorsArr[random.nextInt(colorsArr.length)]);
    }

    public void setmDrawNetLogo(boolean mDrawNetLogo) {
        this.mDrawNetLogo = mDrawNetLogo;
    }
}

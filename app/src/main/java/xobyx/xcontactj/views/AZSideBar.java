package xobyx.xcontactj.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.Contact;

/**
 * TODO: document your custom view class.
 */

public class AZSideBar extends View implements AbsListView.OnScrollListener {
    public static String[] TestCharIndex = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public List<String> CharsIndex = new ArrayList<>();
    private IOnItemTouch onItemTouch;
    private int yLast = -1;
    private Paint TextPaint = new Paint();
    private boolean mIsDown = false;
    private int backgroundColor;

    private int selectorColor;
    private int textColor;
    private int selectTextColor;
    private Paint mPainter;
    private float mTextSize;
    private Paint evel;
    private float mani = 25f;
    private char mLastChar = '#';
    public boolean mfinishAdapter;
    private boolean change;
    private boolean DoSalid;
    private boolean hied;
    private boolean show;
    private Drawable mdraw;
    private int m = 0;
    private int[] PHOTO_TEXT_BACKGROUND_COLORS;


    public AZSideBar(Context var1) {
        super(var1);
        this.SetupTextDraw(var1);
    }

    public AZSideBar(Context var1, AttributeSet var2) {
        super(var1, var2);
        this.SetupTextDraw(var1);
        this.init(var1, var2, 0);
    }

    public AZSideBar(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        if (isInEditMode())//just for test
        {
            Collections.addAll(CharsIndex, TestCharIndex);
        }
        this.SetupTextDraw(var1);
        this.init(var1, var2, var3);
    }


    private void SetupTextDraw(Context var1) {
        mTextSize = TypedValue.applyDimension(2, 12.0F, var1.getResources().getDisplayMetrics());
        TextPaint.setTextSize(mTextSize);
        TextPaint.setTextAlign(Align.CENTER);
        TextPaint.setAntiAlias(true);
        if (!isInEditMode())
            TextPaint.setTypeface(Typeface.createFromAsset(var1.getAssets(), "bein.ttf"));

    }

    private void init(Context var1, AttributeSet var2, int var3) {
        TypedArray var4 = var1.obtainStyledAttributes(var2, R.styleable.AZSideBar, var3, 0);
        if (var4 != null) {
            this.backgroundColor = var4.getColor(R.styleable.AZSideBar_shape_background, this.backgroundColor);
            this.selectorColor = var4.getColor(R.styleable.AZSideBar_selector_color, this.selectorColor);
            this.selectTextColor = var4.getColor(R.styleable.AZSideBar_text_selected_color, this.selectTextColor);
            this.textColor = var4.getColor(R.styleable.AZSideBar_text_color, this.textColor);
            var4.recycle();

            mdraw = getResources().getDrawable(R.drawable.head);
            mdraw.setCallback(this);
            evel = new Paint(Paint.ANTI_ALIAS_FLAG);
            evel.setStyle(Style.STROKE);
            evel.setColor(0x83080808);
            evel.setStrokeWidth(3);
            PHOTO_TEXT_BACKGROUND_COLORS=getResources().getIntArray(R.array.letter_tile_colors);

        }
        SizeAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mani = (float) animation.getAnimatedValue();
                invalidate();

            }
        });


    }

    public final void setOnItemTouchListener(IOnItemTouch var1) {
        this.onItemTouch = var1;
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent Motion) {
        int action = Motion.getAction();
        float MotionY = Motion.getY();

        int width = this.getWidth() - (this.getPaddingLeft() + this.getPaddingRight());
        int height = this.getHeight();
        //loc size
        int i1 = (int) ((MotionY - (float) (width / 2))
                / (float) (height - width) * (float) CharsIndex.size());
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                this.mIsDown = true;
                if (yLast != i1 && onItemTouch != null && i1 >= 0 && i1 < CharsIndex.size()) {
                    onItemTouch.onTouchedItem(CharsIndex.get(i1));
                    this.yLast = i1;
                }

                SizeAnimation.setFloatValues(25, 40, 25);
                SizeAnimation.setDuration(500);
                SizeAnimation.start();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.mIsDown = false;
                this.yLast = -1;
                SizeAnimation.setFloatValues(40, 25);
                SizeAnimation.setDuration(500);


                onItemTouch.onTouchOut();
                //SizeAnimation.start();


                return true;
            case MotionEvent.ACTION_MOVE:
                if (yLast != i1 && onItemTouch != null && i1 >= 0 && i1 < CharsIndex.size()) {
                    onItemTouch.onTouchedItem(CharsIndex.get(i1));
                    this.yLast = i1;
                    this.invalidate();
                    return true;
                }
            default:
                return true;
        }
    }

    ValueAnimator SizeAnimation = new ValueAnimator();

    Paint getShapPaint() {
        if (mPainter == null)
            mPainter = new Paint();

        return mPainter;
    }

    RectF rect = new RectF();

    @test(value = true, me = "fdf")
    protected void onDraw(Canvas var1) {

        boolean var21;
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();
        int mpWidth = this.getWidth() - (paddingRight + paddingLeft);
        int mHeight = this.getHeight();
        int mWidth = mpWidth / 2;

        int var8 = this.backgroundColor;
        ;
        if (this.mIsDown) {
            // var8 = this.selectorColor;

        } else {

            var8 = this.backgroundColor;
        }
        Paint mPaint = getShapPaint();
        mPaint.setColor(var8);
        mPaint.setStyle(Style.FILL);//dd
        mPaint.setAntiAlias(true);

        //rect.set((float) paddingLeft, (float) mWidth, (float) (paddingLeft + mpWidth), (float) (mHeight - mWidth));
        rect.set((float) paddingLeft, 0.0F, (float) (paddingLeft + mpWidth), mHeight);
        var1.drawRect(rect, mPaint);
        //rect.set((float) paddingLeft, 0.0F, (float) (paddingLeft + mpWidth), (float) mpWidth);
        // var1.drawArc(rect, 0.0F, -180.0F, true, mPaint);
        //rect.set((float) paddingLeft, (float) (mHeight - mpWidth), (float) (paddingLeft + mpWidth), (float) mHeight);
        //var1.drawArc(rect, 0.0F, 180.0F, true, mPaint);
        int paddingLeft1 = this.getPaddingLeft();
        int paddingRight1 = this.getPaddingRight();
        int height = this.getHeight();
        int realWidth = this.getWidth() - (paddingRight1 + paddingLeft1);
        float yBlock = (float) (height - realWidth) / (float) CharsIndex.size();
        int error = realWidth >> 1;
        int xPos = error + paddingLeft1;
        float size;

        for (int i = 0; i < CharsIndex.size(); ++i) {
            float yPos = yBlock + yBlock * (float) i + (float) error;
            int var19;
            if ((i == this.yLast && mIsDown == false) /*||i==CharsIndex.indexOf(mLastChar)*/ || CharsIndex.get(i).charAt(0) == mLastChar) {
                var19 = this.selectTextColor;
                size = 35;
                TextPaint.setColor(selectorColor);
                var1.save();
                Rect newRect = var1.getClipBounds();

                newRect.inset(-60, -0);  //make the rect larger

                var1.clipRect(newRect, Region.Op.REPLACE);
                // var1.drawCircle((float) xPos - nb, yPos, mani, TextPaint);
                // var1.drawCircle((float) xPos - nb, yPos, mani, evel);

                mdraw.setBounds((int) (xPos - m), (int) yPos - m, (int) (xPos), (int) yPos);


                // if(show) {
                mdraw.draw(var1);
                TextPaint.measureText(CharsIndex.get(i));

                var1.drawText(CharsIndex.get(i), xPos - m / 2, yPos - m / 2, TextPaint);
                //}
                //  var1.restore();
                var21 = true;

            } else {
                var19 = this.textColor;
                size = mTextSize;
                var21 = false;
                TextPaint.setColor(PHOTO_TEXT_BACKGROUND_COLORS[(int)((yPos/height)*13)]);
                TextPaint.measureText(CharsIndex.get(i));
                TextPaint.setFakeBoldText(var21);
                var1.drawText(CharsIndex.get(i), xPos, yPos, TextPaint);
            }


            //TextPaint.setTextSize(size);


          //  TextPaint.setFakeBoldText(var21);


            TextPaint.setColor(var19);
            TextPaint.measureText(CharsIndex.get(i));
            float x = var21 ? (float) xPos - nb : (float) xPos;
            float y = var21 ? yPos + 8 : yPos;
          //  if (var21)
            //    var1.drawText(CharsIndex.get(i), x, y, TextPaint);


        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    ValueAnimator ms = new ValueAnimator();

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE ) {
            show=false;

            if (nb == 50) {
                H_Ainmation.setFloatValues(50, 0);

                H_Ainmation.setDuration(200);

                //postOnAnimationDelayed();
                H_Ainmation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        nb = ((float) animation.getAnimatedValue());
                        m = (int) ((nb / 50f) * 60f);
                        invalidate();
                    }
                });
                H_Ainmation.start();
            }
        } else if (!show) {


            H_Ainmation.setInterpolator(v);
            if (H_Ainmation.isStarted()) H_Ainmation.end();
            H_Ainmation.setFloatValues(0, 50);
            H_Ainmation.setDuration(200);

            //postOnAnimationDelayed();
            H_Ainmation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    nb = ((float) animation.getAnimatedValue());
                    m = (int) ((nb / 50f) * 60f);
                    invalidate();
                }
            });
            H_Ainmation.start();

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (!mfinishAdapter) return;
        show=true;

        if (view.getAdapter() != null) {
            final Object item = view
                    .getAdapter()
                    .getItem(firstVisibleItem);
            Contact f = item instanceof Contact ? ((Contact) item) : null;
            if (f != null) {
                //char v= f.Name.charAt(0);

                setLastChar(f.Name.charAt(0));


            }
            invalidate();
        }
    }

    @interface test {

        boolean value();

        String me();


    }

    public interface IOnItemTouch {

        void onTouchOut();


        void onTouchedItem(String var1);


    }

    ValueAnimator H_Ainmation = new ValueAnimator();
    final OvershootInterpolator v = new OvershootInterpolator() ;
    public float nb = 0;

    Handler mkk = new Handler();

    public void setLastChar(char m) {
        change = mLastChar != m;

        if (mLastChar != m)

        {
            mLastChar = m;
            DoSalid = false;
        }


    }


}


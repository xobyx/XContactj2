package xobyx.xcontactj.until;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.InputStream;

import xobyx.xcontactj.R;
import xobyx.xcontactj.views.LetterImageView;

import static android.provider.ContactsContract.Contacts;
import static android.provider.ContactsContract.Contacts.lookupContact;

/**
 * Created by xobyx on 12/2/2014.
 * c# to java
 */
public class AsyncDrawer {
    public static final String N_PATH = "%s/Files/_%s_.obf";
    public static final int DRAWSTRING = 1;
    public static final int DRAWSTRING_CLIP = 2;
    public static final int NORMAL_CLIP = 3;
    public static final int NORMAL = 4;
    static final Paint as = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    static {
        as.setTextSize(160);
        as.setColor(Color.WHITE);
        as.setTextAlign(Paint.Align.CENTER);

    }

    static {
        sPaint.setAlpha(23);
        sPaint.setColor(0xc7000b8b);


    }

    final String mCache;
    private final ContentResolver mContentResolver;
    private final Resources mRes;
    private TYPE mMode;
    private BitmapShader mBitmapShader;

    @SuppressWarnings("ConstantConditions")
    public AsyncDrawer(@NonNull Context u) {

        as.setTypeface(Typeface.createFromAsset(u.getAssets(), "bein.ttf"));
        mCache = u.getExternalCacheDir().getPath();
        mContentResolver = u.getContentResolver();
        mRes = u.getResources();


    }

    @SuppressWarnings("ConstantConditions")
    public void DrawImageString(Contact t, LetterImageView d, boolean Oval, int net) {
        if (d.getDrawable() == null) {
            mMode = Oval ? TYPE.DRAWSTRING_OVAL : TYPE.DRAWSTRING;
            if (net != -1) {
                d.setCustomColor(net);
            } else d.setContact(t);
            d.setFont(Typeface.createFromAsset(d.getContext().getAssets(), d.getResources().getString(R.string.letter_image_font)));
            d.setLetter(t.Name.charAt(0));
            d.setOval(mMode == TYPE.DRAWSTRING_OVAL);
        }
    }

    public void GetPhoto(Contact s, LetterImageView d, boolean clip, int net) {
        d.setImageBitmap(null);


        mMode = clip ? TYPE.NORMAL_OVAL : TYPE.NORMAL;

        if (mMode == TYPE.NORMAL) {//m.exists()) {
            d.setImageURI(s.PhotoThumbUri);

        } else {


            if (null != ImageCache.INSTANCE.getBitmapFromMemCache(String.valueOf(net) + ME.getMD5Hex(s.Lookup))) {
                d.setImageBitmap(ImageCache.INSTANCE.getBitmapFromMemCache(net + ME.getMD5Hex(s.Lookup)));//.setImageDrawable(Drawable.createFromPath(m.getPath()));
            } else {
                DrawImageString(s, d, clip, net);

                AsyncDrawTask i = new AsyncDrawTask(d, mMode, net);

                //    d.setImageURI(s.PhotoThumbUri);
                //  i.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, s.Lookup);
                i.execute(s.Lookup);
            }
        }
    }

    private void setImageDrawable(Bitmap drawable, ImageView imageView) {
        imageView.setImageBitmap(drawable);


    }

    enum TYPE {
        DRAWSTRING(1) {

        }, DRAWSTRING_OVAL(2), NORMAL_OVAL(3), NORMAL(4);

        private final int mValue;

        TYPE(int i) {
            mValue = i;
        }

        public int getValue() {
            return mValue;
        }
    }


    private class AsyncDrawTask extends AsyncTask<String, Integer, Bitmap> {

        final Object mLock = new Object();
        private final ImageView mImageView;
        private final TYPE mMode;
        private int net;
        private String look;

        public AsyncDrawTask(ImageView d, TYPE mode, int net) {

            mImageView = d;
            mMode = mode;

            this.net = net;
        }

        @Override
        protected void onPostExecute(Bitmap bitmapDrawable) {
            setImageDrawable(bitmapDrawable, mImageView);
        }

        @Override
        protected Bitmap doInBackground(String[] params) {
            look = params[0];

            Bitmap temp = null;
            if (mMode == TYPE.NORMAL_OVAL) {
///FIXME: java.lang.NullPointerException
                final Uri uri = lookupContact(mContentResolver, Uri.withAppendedPath(Contacts.CONTENT_LOOKUP_URI, this.look));
                final InputStream stream = Contacts.openContactPhotoInputStream(mContentResolver, uri);
                if (stream != null) {
                    final Bitmap a = BitmapFactory.decodeStream(stream).copy(Bitmap.Config.ARGB_8888, true);
                    temp = GetBitmapClippedCircle(a, 3);

                } else {
                    temp = BitmapFactory.decodeResource(mRes, R.drawable.ic_action_unknown);
                }
            }


            SaveImage(ME.getMD5Hex(look), temp);

            return temp;

        }

        public Bitmap GetBitmapClippedCircle(Bitmap inbit, int mMode) {

            final int width = inbit.getWidth();
            final int height = inbit.getHeight();
            mBitmapShader = new BitmapShader(inbit, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(outputBitmap);
            Paint d = new Paint(Paint.ANTI_ALIAS_FLAG);
            Paint d2 = new Paint(Paint.ANTI_ALIAS_FLAG);
            d2.setStyle(Paint.Style.STROKE);
            d2.setStrokeWidth(1.3f);
            d2.setColor(net >= 0 ? ME.nColors[net] : 3);

            d.setShader(mBitmapShader);

            if (mMode != 1) {
                canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth(), canvas.getHeight()) / 2f, d);
               /* final Path path = new Path();
                path.addCircle(
                        canvas.getWidth() / 2f, canvas.getHeight() / 2f, Math.min(canvas.getWidth(), canvas.getHeight()) / 2f
                        , Path.Direction.CCW);

                canvas.save();
                canvas.clipPath(path);
                Paint d = new Paint(Paint.ANTI_ALIAS_FLAG);
                Paint d2 = new Paint(Paint.ANTI_ALIAS_FLAG);
                d2.setStyle(Paint.Style.STROKE);
                d2.setStrokeWidth(3);
                d2.setColor(ME.nColors[net]);



                canvas.drawBitmap(inbit, 0, 0, d);
                canvas.restore();

                canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, (Math.min(canvas.getWidth(), canvas.getHeight()) / 2f)-2f,d2);
*/
                canvas.drawCircle(canvas.getWidth() / 2f, canvas.getHeight() / 2f, (Math.min(canvas.getWidth(), canvas.getHeight()) / 2f) - 1f, d2);
            }
            if (mMode != 3)
                canvas.drawRect(new Rect(inbit.getWidth(), inbit.getHeight(), 0, 0), sPaint);
            return outputBitmap;
        }

        String temp = null;

        @SuppressWarnings("ResultOfMethodCallIgnored")
        private void SaveImage(String c, Bitmap s) {
            if (!c.equals(temp))
                synchronized (mLock) {

                    temp = c;
                    ImageCache.INSTANCE.addBitmapToCache(String.valueOf(net) + c, s);

                }
            else {
                ImageCache.INSTANCE.addBitmapToCache(String.valueOf(net) + c, s);
            }


        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


    }


}
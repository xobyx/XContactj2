/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xobyx.xcontactj.me;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.Directory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import junit.framework.Assert;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import xobyx.xcontactj.R;
import static android.os.Build.VERSION.SDK_INT;

/**
 * Asynchronously loads contact photos and maintains a cache of photos.
 */
public abstract class ContactPhotoManager implements ComponentCallbacks2 {
    /**
     * Contact type constants used for default letter images
     */
    public static final int TYPE_PERSON = LetterTileDrawable.TYPE_PERSON;
    public static final int TYPE_BUSINESS = LetterTileDrawable.TYPE_BUSINESS;
    public static final int TYPE_VOICEMAIL = LetterTileDrawable.TYPE_VOICEMAIL;
    public static final int TYPE_DEFAULT = LetterTileDrawable.TYPE_DEFAULT;
    /**
     * Scale and offset default constants used for default letter images
     */
    public static final float SCALE_DEFAULT = 1.0f;
    public static final float OFFSET_DEFAULT = 0.0f;
    public static final boolean IS_CIRCULAR_DEFAULT = false;
    public static final String CONTACT_PHOTO_SERVICE = "contactPhotos";
    public static final DefaultImageProvider DEFAULT_BLANK = new BlankDefaultImageProvider();
    static final String TAG = "ContactPhotoManager";
    static final boolean DEBUG = false; // Don't submit with true
    static final boolean DEBUG_SIZES = false; // Don't submit with true
    /**
     * Uri-related constants used for default letter images
     */
    private static final String DISPLAY_NAME_PARAM_KEY = "display_name";
    private static final String IDENTIFIER_PARAM_KEY = "identifier";
    private static final String CONTACT_TYPE_PARAM_KEY = "contact_type";
    private static final String SCALE_PARAM_KEY = "scale";
    private static final String OFFSET_PARAM_KEY = "offset";
    private static final String IS_CIRCULAR_PARAM_KEY = "is_circular";
    private static final String DEFAULT_IMAGE_URI_SCHEME = "defaultimage";
    private static final Uri DEFAULT_IMAGE_URI = Uri.parse(DEFAULT_IMAGE_URI_SCHEME + "://");
    public static DefaultImageProvider DEFAULT_AVATAR = new LetterTileDefaultImageProvider();
    // Static field used to cache the default letter avatar drawable that is created
    // using a null {@link DefaultImageRequest}
    private static Drawable sDefaultLetterAvatar = null;

    /**
     * Given a {@link DefaultImageRequest}, returns a {@link Drawable}, that when drawn, will
     * draw a letter tile avatar based on the request parameters defined in the
     * {@link DefaultImageRequest}.
     */
    public static Drawable getDefaultAvatarDrawableForContact(Resources resources, boolean hires,
                                                              DefaultImageRequest defaultImageRequest) {
        if (defaultImageRequest == null) {
            if (sDefaultLetterAvatar == null) {
                // Cache and return the letter tile drawable that is created by a null request,
                // so that it doesn't have to be recreated every time it is requested again.
                sDefaultLetterAvatar = LetterTileDefaultImageProvider.getDefaultImageForContact(
                        resources, null);
            }
            return sDefaultLetterAvatar;
        }
        return LetterTileDefaultImageProvider.getDefaultImageForContact(resources,
                defaultImageRequest);
    }

    /**
     * Given a {@link DefaultImageRequest}, returns an Uri that can be used to request a
     * letter tile avatar when passed to the {@link ContactPhotoManager}. The internal
     * implementation of this uri is not guaranteed to remain the same across application
     * versions, so the actual uri should never be persisted in long-term storage and reused.
     *
     * @param request A {@link DefaultImageRequest} object with the fields configured
     *                to return a
     * @return A Uri that when later passed to the {@link ContactPhotoManager} via
     * , can be
     * used to request a default contact image, drawn as a letter tile using the
     * parameters as configured in the provided {@link DefaultImageRequest}
     */
    public static Uri getDefaultAvatarUriForContact(DefaultImageRequest request) {
        final Builder builder = DEFAULT_IMAGE_URI.buildUpon();
        if (request != null) {
            if (!TextUtils.isEmpty(request.displayName)) {
                builder.appendQueryParameter(DISPLAY_NAME_PARAM_KEY, request.displayName);
            }
            if (!TextUtils.isEmpty(request.identifier)) {
                builder.appendQueryParameter(IDENTIFIER_PARAM_KEY, request.identifier);
            }
            if (request.contactType != TYPE_DEFAULT) {
                builder.appendQueryParameter(CONTACT_TYPE_PARAM_KEY,
                        String.valueOf(request.contactType));
            }
            if (request.scale != SCALE_DEFAULT) {
                builder.appendQueryParameter(SCALE_PARAM_KEY, String.valueOf(request.scale));
            }
            if (request.offset != OFFSET_DEFAULT) {
                builder.appendQueryParameter(OFFSET_PARAM_KEY, String.valueOf(request.offset));
            }
            if (request.isCircular != IS_CIRCULAR_DEFAULT) {
                builder.appendQueryParameter(IS_CIRCULAR_PARAM_KEY,
                        String.valueOf(request.isCircular));
            }

        }
        return builder.build();
    }

    /**
     * Adds a business contact type encoded fragment to the URL.  Used to ensure photo URLS
     * from Nearby Places can be identified as business photo URLs rather than URLs for personal
     * contact photos.
     *
     * @param photoUrl The photo URL to modify.
     * @return URL with the contact type parameter added and set to TYPE_BUSINESS.
     */
    public static String appendBusinessContactType(String photoUrl) {
        Uri uri = Uri.parse(photoUrl);
        Builder builder = uri.buildUpon();
        builder.encodedFragment(String.valueOf(TYPE_BUSINESS));
        return builder.build().toString();
    }

    /**
     * Removes the contact type information stored in the photo URI encoded fragment.
     *
     * @param photoUri The photo URI to remove the contact type from.
     * @return The photo URI with contact type removed.
     */
    public static Uri removeContactType(Uri photoUri) {
        String encodedFragment = photoUri.getEncodedFragment();
        if (!TextUtils.isEmpty(encodedFragment)) {
            Builder builder = photoUri.buildUpon();
            builder.encodedFragment(null);
            return builder.build();
        }
        return photoUri;
    }

    /**
     * Inspects a photo URI to determine if the photo URI represents a business.
     *
     * @param photoUri The URI to inspect.
     * @return Whether the URI represents a business photo or not.
     */
    public static boolean isBusinessContactUri(Uri photoUri) {
        if (photoUri == null) {
            return false;
        }

        String encodedFragment = photoUri.getEncodedFragment();
        return !TextUtils.isEmpty(encodedFragment)
                && encodedFragment.equals(String.valueOf(TYPE_BUSINESS));
    }

    protected static DefaultImageRequest getDefaultImageRequestFromUri(Uri uri) {
        final DefaultImageRequest request = new DefaultImageRequest(
                uri.getQueryParameter(DISPLAY_NAME_PARAM_KEY),
                uri.getQueryParameter(IDENTIFIER_PARAM_KEY), false);
        try {
            String contactType = uri.getQueryParameter(CONTACT_TYPE_PARAM_KEY);
            if (!TextUtils.isEmpty(contactType)) {
                request.contactType = Integer.valueOf(contactType);
            }

            String scale = uri.getQueryParameter(SCALE_PARAM_KEY);
            if (!TextUtils.isEmpty(scale)) {
                request.scale = Float.valueOf(scale);
            }

            String offset = uri.getQueryParameter(OFFSET_PARAM_KEY);
            if (!TextUtils.isEmpty(offset)) {
                request.offset = Float.valueOf(offset);
            }

            String isCircular = uri.getQueryParameter(IS_CIRCULAR_PARAM_KEY);
            if (!TextUtils.isEmpty(isCircular)) {
                request.isCircular = Boolean.valueOf(isCircular);
            }
        } catch (NumberFormatException e) {
            Log.w(TAG, "Invalid DefaultImageRequest image parameters provided, ignoring and using "
                    + "defaults.");
        }

        return request;
    }

    public static ContactPhotoManager getInstance(Context context) {
        Context applicationContext = context.getApplicationContext();
        /*ContactPhotoManager service =
                (ContactPhotoManager) applicationContext.getSystemService(CONTACT_PHOTO_SERVICE);*/

        // if (service == null) {
        ContactPhotoManager service = createContactPhotoManager(applicationContext);
        Log.e(TAG, "No contact photo service in context: " + applicationContext);
        //}
        return service;
    }

    public static synchronized ContactPhotoManager createContactPhotoManager(Context context) {
        return new ContactPhotoManagerImpl(context);
    }

    protected boolean isDefaultImageUri(Uri uri) {
        return DEFAULT_IMAGE_URI_SCHEME.equals(uri.getScheme());
    }

    /**
     * Load thumbnail image into the supplied image view. If the photo is already cached,
     * it is displayed immediately.  Otherwise a request is sent to load the photo
     * from the database.
     */
    public abstract void loadThumbnail(ImageView view, long photoId, boolean darkTheme,
                                       boolean isCircular, DefaultImageRequest defaultImageRequest,
                                       DefaultImageProvider defaultProvider);

    /**
     * Calls  using the {@link DefaultImageProvider} {@link #DEFAULT_AVATAR}.
     */
    public final void loadThumbnail(ImageView view, long photoId, boolean darkTheme,
                                    boolean isCircular, DefaultImageRequest defaultImageRequest) {
        loadThumbnail(view, photoId, darkTheme, isCircular, defaultImageRequest, DEFAULT_AVATAR);
    }

    /**
     * Load photo into the supplied image view. If the photo is already cached,
     * it is displayed immediately. Otherwise a request is sent to load the photo
     * from the location specified by the URI.
     *
     * @param view                The target view
     * @param photoUri            The uri of the photo to load
     * @param requestedExtent     Specifies an approximate Max(width, height) of the targetView.
     *                            This is useful if the source image can be a lot bigger that the target, so that the decoding
     *                            is done using efficient sampling. If requestedExtent is specified, no sampling of the image
     *                            is performed
     * @param darkTheme           Whether the background is dark. This is used for default avatars
     * @param defaultImageRequest {@link DefaultImageRequest} object that specifies how a default
     *                            letter tile avatar should be drawn.
     * @param defaultProvider     The provider of default avatars (this is used if photoUri doesn't
     *                            refer to an existing image)
     */
    public abstract void loadPhoto(ImageView view, Uri photoUri, int requestedExtent,
                                   boolean darkTheme, boolean isCircular, DefaultImageRequest defaultImageRequest,
                                   DefaultImageProvider defaultProvider);

    public final void loadPhoto(ImageView view, Uri photoUri, int requestedExtent,
                                boolean darkTheme, boolean isCircular, DefaultImageRequest defaultImageRequest) {
        loadPhoto(view, photoUri, requestedExtent, darkTheme, isCircular,
                defaultImageRequest, DEFAULT_AVATAR);
    }

    /**
     * Calls  with {@link #DEFAULT_AVATAR} and with the assumption, that
     * the image is a thumbnail.
     *
     * @param defaultImageRequest {@link DefaultImageRequest} object that specifies how a default
     *                            letter tile avatar should be drawn.
     */
    public final void loadDirectoryPhoto(ImageView view, Uri photoUri, boolean darkTheme,
                                         boolean isCircular, DefaultImageRequest defaultImageRequest) {
        loadPhoto(view, photoUri, -1, darkTheme, isCircular, defaultImageRequest, DEFAULT_AVATAR);
    }

    /**
     * Remove photo from the supplied image view. This also cancels current pending load request
     * inside this photo manager.
     */
    public abstract void removePhoto(ImageView view);

    /**
     * Cancels all pending requests to load photos asynchronously.
     */
    public abstract void cancelPendingRequests(View fragmentRootView);

    /**
     * Temporarily stops loading photos from the database.
     */
    public abstract void pause();

    /**
     * Resumes loading photos from the database.
     */
    public abstract void resume();

    /**
     * Marks all cached photos for reloading.  We can continue using cache but should
     * also make sure the photos haven't changed in the background and notify the views
     * if so.
     */
    public abstract void refreshCache();

    /**
     * Stores the given bitmap directly in the LRU bitmap cache.
     *
     * @param photoUri   The URI of the photo (for future requests).
     * @param bitmap     The bitmap.
     * @param photoBytes The bytes that were parsed to create the bitmap.
     */
    public abstract void cacheBitmap(Uri photoUri, Bitmap bitmap, byte[] photoBytes);

    /**
     * Initiates a background process that over time will fill up cache with
     * preload photos.
     */
    public abstract void preloadPhotosInBackground();

    // ComponentCallbacks2
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    }

    // ComponentCallbacks2
    @Override
    public void onLowMemory() {
    }

    // ComponentCallbacks2
    @Override
    public void onTrimMemory(int level) {
    }

    /**
     * Contains fields used to contain contact details and other user-defined settings that might
     * be used by the ContactPhotoManager to generate a default contact image. This contact image
     * takes the form of a letter or bitmap drawn on top of a colored tile.
     */
    public static class DefaultImageRequest {
        /**
         * Used to indicate that a drawable that represents a contact without any contact details
         * should be returned.
         */
        public static DefaultImageRequest EMPTY_DEFAULT_IMAGE_REQUEST = new DefaultImageRequest();
        /**
         * Used to indicate that a drawable that represents a business without a business photo
         * should be returned.
         */
        public static DefaultImageRequest EMPTY_DEFAULT_BUSINESS_IMAGE_REQUEST =
                new DefaultImageRequest(null, null, TYPE_BUSINESS, false);
        /**
         * Used to indicate that a circular drawable that represents a contact without any contact
         * details should be returned.
         */
        public static DefaultImageRequest EMPTY_CIRCULAR_DEFAULT_IMAGE_REQUEST =
                new DefaultImageRequest(null, null, true);
        /**
         * Used to indicate that a circular drawable that represents a business without a business
         * photo should be returned.
         */
        public static DefaultImageRequest EMPTY_CIRCULAR_BUSINESS_IMAGE_REQUEST =
                new DefaultImageRequest(null, null, TYPE_BUSINESS, true);
        /**
         * The contact's display name. The display name is used to
         */
        public String displayName;
        /**
         * A unique and deterministic string that can be used to identify this contact. This is
         * usually the contact's lookup key, but other contact details can be used as well,
         * especially for non-local or temporary contacts that might not have a lookup key. This
         * is used to determine the color of the tile.
         */
        public String identifier;
        /**
         * The type of this contact. This contact type may be used to decide the kind of
         * image to use in the case where a unique letter cannot be generated from the contact's
         * display name and identifier. See:
         * {@link #TYPE_PERSON}
         * {@link #TYPE_BUSINESS}
         * {@link #TYPE_PERSON}
         * {@link #TYPE_DEFAULT}
         */
        public int contactType = TYPE_DEFAULT;
        /**
         * The amount to scale the letter or bitmap to, as a ratio of its default size (from a
         * range of 0.0f to 2.0f). The default value is 1.0f.
         */
        public float scale = SCALE_DEFAULT;
        /**
         * The amount to vertically offset the letter or image to within the tile.
         * The provided offset must be within the range of -0.5f to 0.5f.
         * If set to -0.5f, the letter will be shifted upwards by 0.5 times the height of the canvas
         * it is being drawn on, which means it will be drawn with the center of the letter starting
         * at the top edge of the canvas.
         * If set to 0.5f, the letter will be shifted downwards by 0.5 times the height of the
         * canvas it is being drawn on, which means it will be drawn with the center of the letter
         * starting at the bottom edge of the canvas.
         * The default is 0.0f, which means the letter is drawn in the exact vertical center of
         * the tile.
         */
        public float offset = OFFSET_DEFAULT;
        /**
         * Whether or not to draw the default image as a circle, instead of as a square/rectangle.
         */
        public boolean isCircular = false;

        public DefaultImageRequest() {
        }

        public DefaultImageRequest(String displayName, String identifier, boolean isCircular) {
            this(displayName, identifier, TYPE_DEFAULT, SCALE_DEFAULT, OFFSET_DEFAULT, isCircular);
        }

        public DefaultImageRequest(String displayName, String identifier, int contactType,
                                   boolean isCircular) {
            this(displayName, identifier, contactType, SCALE_DEFAULT, OFFSET_DEFAULT, isCircular);
        }

        public DefaultImageRequest(String displayName, String identifier, int contactType,
                                   float scale, float offset, boolean isCircular) {
            this.displayName = displayName;
            this.identifier = identifier;
            this.contactType = contactType;
            this.scale = scale;
            this.offset = offset;
            this.isCircular = isCircular;
        }
    }

    public static abstract class DefaultImageProvider {
        /**
         * Applies the default avatar to the ImageView. Extent is an indicator for the size (width
         * or height). If darkTheme is set, the avatar is one that looks better on dark background
         *
         * @param defaultImageRequest {@link DefaultImageRequest} object that specifies how a
         *                            default letter tile avatar should be drawn.
         */
        public abstract void applyDefaultImage(ImageView view, int extent, boolean darkTheme,
                                               DefaultImageRequest defaultImageRequest);
    }

    /**
     * A default image provider that applies a letter tile consisting of a colored background
     * and a letter in the foreground as the default image for a contact. The color of the
     * background and the type of letter is decided based on the contact's details.
     */
    private static class LetterTileDefaultImageProvider extends DefaultImageProvider {
        public static Drawable getDefaultImageForContact(Resources resources,
                                                         DefaultImageRequest defaultImageRequest) {
            final LetterTileDrawable drawable = new LetterTileDrawable(resources);
            if (defaultImageRequest != null) {
                // If the contact identifier is null or empty, fallback to the
                // displayName. In that case, use {@code null} for the contact's
                // display name so that a default bitmap will be used instead of a
                // letter
                if (TextUtils.isEmpty(defaultImageRequest.identifier)) {
                    drawable.setContactDetails(null, defaultImageRequest.displayName);
                } else {
                    drawable.setContactDetails(defaultImageRequest.identifier,
                            defaultImageRequest.identifier);
                }
                drawable.setContactType(defaultImageRequest.contactType);
                drawable.setScale(defaultImageRequest.scale);
                drawable.setOffset(defaultImageRequest.offset);
                drawable.setIsCircular(defaultImageRequest.isCircular);
            }
            return drawable;
        }

        @Override
        public void applyDefaultImage(ImageView view, int extent, boolean darkTheme,
                                      DefaultImageRequest defaultImageRequest) {
            final Drawable drawable = getDefaultImageForContact(view.getResources(),
                    defaultImageRequest);
            view.setImageDrawable(drawable);
        }
    }

    private static class BlankDefaultImageProvider extends DefaultImageProvider {
        private static Drawable sDrawable;

        @Override
        public void applyDefaultImage(ImageView view, int extent, boolean darkTheme,
                                      DefaultImageRequest defaultImageRequest) {
            if (sDrawable == null) {
                Context context = view.getContext();
                sDrawable = new ColorDrawable(context.getResources().getColor(
                        R.color.a1));
            }
            view.setImageDrawable(sDrawable);

        }
    }

    public static class LetterTileDrawable extends Drawable {

        /**
         * Contact type constants
         */
        public static final int TYPE_PERSON = 1;
        public static final int TYPE_BUSINESS = 2;
        public static final int TYPE_VOICEMAIL = 3;
        public static final int TYPE_DEFAULT = TYPE_PERSON;
        /**
         * Reusable components to avoid new allocations
         */
        private static final Paint sPaint = new Paint();
        private static final Rect sRect = new Rect();
        private static final char[] sFirstChar = new char[1];
        /**
         * Letter tile
         */
        private static TypedArray sColors;
        private static int sDefaultColor;
        private static int sTileFontColor;
        private static float sLetterToTileRatio;
        private static Bitmap DEFAULT_PERSON_AVATAR;
        private static Bitmap DEFAULT_BUSINESS_AVATAR;
        private static Bitmap DEFAULT_VOICEMAIL_AVATAR;
        private final String TAG = LetterTileDrawable.class.getSimpleName();
        private final Paint mPaint;
        private String mDisplayName;
        private String mIdentifier;
        private int mContactType = TYPE_DEFAULT;
        private float mScale = 1.0f;
        private float mOffset = 0.0f;
        private boolean mIsCircle = false;

        public LetterTileDrawable(final Resources res) {
            mPaint = new Paint();
            mPaint.setFilterBitmap(true);
            mPaint.setDither(true);

            if (sColors == null) {
                sColors = res.obtainTypedArray(R.array.letter_tile_colors);

                sDefaultColor = Color.BLUE;
                sTileFontColor = Color.WHITE;
                sLetterToTileRatio = 1; //res.getFraction(R.dimen.letter_to_tile_ratio, 1, 1);
                DEFAULT_PERSON_AVATAR = DEFAULT_BUSINESS_AVATAR =

                        DEFAULT_VOICEMAIL_AVATAR = BitmapFactory.decodeResource(res,
                                R.drawable.i_mtn);
                sPaint.setTypeface(Typeface.create(
                        res.getString(R.string.letter_tile_letter_font_family), Typeface.NORMAL));
                sPaint.setTextAlign(Paint.Align.CENTER);
                sPaint.setAntiAlias(true);
            }
        }

        private static Bitmap getBitmapForContactType(int contactType) {
            switch (contactType) {
                case TYPE_PERSON:
                    return DEFAULT_PERSON_AVATAR;
                case TYPE_BUSINESS:
                    return DEFAULT_BUSINESS_AVATAR;
                case TYPE_VOICEMAIL:
                    return DEFAULT_VOICEMAIL_AVATAR;
                default:
                    return DEFAULT_PERSON_AVATAR;
            }
        }

        private static boolean isEnglishLetter(final char c) {
           return true;// return ('A' <= c && c <= 'Z') || ('a' <= c && c <= 'z');
        }

        @Override
        public void draw(final Canvas canvas) {
            final Rect bounds = getBounds();
            if (!isVisible() || bounds.isEmpty()) {
                return;
            }
            // Draw letter tile.
            drawLetterTile(canvas);
        }

        /**
         * Draw the bitmap onto the canvas at the current bounds taking into account the current scale.
         */
        private void drawBitmap(final Bitmap bitmap, final int width, final int height,
                                final Canvas canvas) {
            // The bitmap should be drawn in the middle of the canvas without changing its width to
            // height ratio.
            final Rect destRect = copyBounds();

            // Crop the destination bounds into a square, scaled and offset as appropriate
            final int halfLength = (int) (mScale * Math.min(destRect.width(), destRect.height()) / 2);

            destRect.set(destRect.centerX() - halfLength,
                    (int) (destRect.centerY() - halfLength + mOffset * destRect.height()),
                    destRect.centerX() + halfLength,
                    (int) (destRect.centerY() + halfLength + mOffset * destRect.height()));

            // Source rectangle remains the entire bounds of the source bitmap.
            sRect.set(0, 0, width, height);

            canvas.drawBitmap(bitmap, sRect, destRect, mPaint);
        }

        private void drawLetterTile(final Canvas canvas) {
            // Draw background color.
            sPaint.setColor(pickColor(mIdentifier));

            sPaint.setAlpha(mPaint.getAlpha());
            final Rect bounds = getBounds();
            final int minDimension = Math.min(bounds.width(), bounds.height());

            if (mIsCircle) {
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), minDimension / 2, sPaint);
            } else {
                canvas.drawRect(bounds, sPaint);
            }

            // Draw letter/digit only if the first character is an english letter
            if (mDisplayName != null && isEnglishLetter(mDisplayName.charAt(0))) {
                // Draw letter or digit.
                sFirstChar[0] = Character.toUpperCase(mDisplayName.charAt(0));

                // Scale text by canvas bounds and user selected scaling factor
                sPaint.setTextSize(mScale * sLetterToTileRatio * minDimension);
                //sPaint.setTextSize(sTileLetterFontSize);
                sPaint.getTextBounds(sFirstChar, 0, 1, sRect);
                sPaint.setColor(sTileFontColor);

                // Draw the letter in the canvas, vertically shifted up or down by the user-defined
                // offset
                canvas.drawText(sFirstChar, 0, 1, bounds.centerX(),
                        bounds.centerY() + mOffset * bounds.height() + sRect.height() / 2,
                        sPaint);
            } else {
                // Draw the default image if there is no letter/digit to be drawn
                final Bitmap bitmap = getBitmapForContactType(mContactType);
                drawBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                        canvas);
            }
        }

        public int getColor() {
            return pickColor(mIdentifier);
        }

        /**
         * Returns a deterministic color based on the provided contact identifier string.
         */
        private int pickColor(final String identifier) {
            if (TextUtils.isEmpty(identifier) || mContactType == TYPE_VOICEMAIL) {
                return sDefaultColor;
            }
            // String.hashCode() implementation is not supposed to change across java versions, so
            // this should guarantee the same email address always maps to the same color.
            // The email should already have been normalized by the ContactRequest.
            final int color = Math.abs(identifier.hashCode()) % sColors.length();
            return sColors.getColor(color, sDefaultColor);
        }

        @Override
        public void setAlpha(final int alpha) {
            mPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(final ColorFilter cf) {
            mPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return android.graphics.PixelFormat.OPAQUE;
        }

        /**
         * Scale the drawn letter tile to a ratio of its default size
         *
         * @param scale The ratio the letter tile should be scaled to as a percentage of its default
         *              size, from a scale of 0 to 2.0f. The default is 1.0f.
         */
        public void setScale(float scale) {
            mScale = scale;
        }

        /**
         * Assigns the vertical offset of the position of the letter tile to the ContactDrawable
         *
         * @param offset The provided offset must be within the range of -0.5f to 0.5f.
         *               If set to -0.5f, the letter will be shifted upwards by 0.5 times the height of the canvas
         *               it is being drawn on, which means it will be drawn with the center of the letter starting
         *               at the top edge of the canvas.
         *               If set to 0.5f, the letter will be shifted downwards by 0.5 times the height of the canvas
         *               it is being drawn on, which means it will be drawn with the center of the letter starting
         *               at the bottom edge of the canvas.
         *               The default is 0.0f.
         */
        public void setOffset(float offset) {
            Assert.assertTrue(offset >= -0.5f && offset <= 0.5f);
            mOffset = offset;
        }

        public void setContactDetails(final String displayName, final String identifier) {
            mDisplayName = displayName;
            mIdentifier = identifier;
        }

        public void setContactType(int contactType) {
            mContactType = contactType;
        }

        public void setIsCircular(boolean isCircle) {
            mIsCircle = isCircle;
        }
    }

    static class ContactPhotoManagerImpl extends ContactPhotoManager implements Callback {
        private static final String LOADER_THREAD_NAME = "ContactPhotoLoader";

        private static final int FADE_TRANSITION_DURATION = 200;

        /**
         * Type of message sent by the UI thread to itself to indicate that some photos
         * need to be loaded.
         */
        private static final int MESSAGE_REQUEST_LOADING = 1;

        /**
         * Type of message sent by the loader thread to indicate that some photos have
         * been loaded.
         */
        private static final int MESSAGE_PHOTOS_LOADED = 2;

        private static final String[] EMPTY_STRING_ARRAY = new String[0];

        private static final String[] COLUMNS = new String[]{Photo._ID, Photo.PHOTO};
        /**
         * Cache size for {@link #mBitmapHolderCache} for devices with "large" RAM.
         */
        private static final int HOLDER_CACHE_SIZE = 2000000;
        /**
         * Cache size for {@link #mBitmapCache} for devices with "large" RAM.
         */
        private static final int BITMAP_CACHE_SIZE = 36864 * 48; // 1728K
        /**
         * Height/width of a thumbnail image
         */
        private static int mThumbnailSize;
        private final Context mContext;
        /**
         * An LRU cache for bitmap holders. The cache contains bytes for photos just
         * as they come from the database. Each holder has a soft reference to the
         * actual bitmap.
         */
        private final LruCache<Object, BitmapHolder> mBitmapHolderCache;
        /**
         * Cache size threshold at which bitmaps will not be preloaded.
         */
        private final int mBitmapHolderCacheRedZoneBytes;
        /**
         * Level 2 LRU cache for bitmaps. This is a smaller cache that holds
         * the most recently used bitmaps to save time on decoding
         * them from bytes (the bytes are stored in {@link #mBitmapHolderCache}.
         */
        private final LruCache<Object, Bitmap> mBitmapCache;
        /**
         * A map from ImageView to the corresponding photo ID or uri, encapsulated in a request.
         * The request may swapped out before the photo loading request is started.
         */
        private final ConcurrentHashMap<ImageView, Request> mPendingRequests =
                new ConcurrentHashMap<ImageView, Request>();
        /**
         * Handler for messages sent to the UI thread.
         */
        private final Handler mMainThreadHandler = new Handler(this);
        /**
         * For debug: How many times we had to reload cached photo for a stale entry
         */
        private final AtomicInteger mStaleCacheOverwrite = new AtomicInteger();
        /**
         * For debug: How many times we had to reload cached photo for a fresh entry.  Should be 0.
         */
        private final AtomicInteger mFreshCacheOverwrite = new AtomicInteger();
        /**
         * {@code true} if ALL entries in {@link #mBitmapHolderCache} are NOT fresh.
         */
        private volatile boolean mBitmapHolderCacheAllUnfresh = true;
        /**
         * Thread responsible for loading photos from the database. Created upon
         * the first request.
         */
        private LoaderThread mLoaderThread;
        /**
         * A gate to make sure we only send one instance of MESSAGE_PHOTOS_NEEDED at a time.
         */
        private boolean mLoadingRequested;
        /**
         * Flag indicating if the image loading is paused.
         */
        private boolean mPaused;

        public ContactPhotoManagerImpl(Context context) {
            mContext = context;

            final ActivityManager am = ((ActivityManager) context.getSystemService(
                    Context.ACTIVITY_SERVICE));
            float cacheSizeAdjustment =1;
            if(SDK_INT>=19) {
                 cacheSizeAdjustment = (am.isLowRamDevice()) ? 0.5f : 1.0f;
            }
            else
            {
                 cacheSizeAdjustment = 1.0f;
            }
            final int bitmapCacheSize = (int) (cacheSizeAdjustment * BITMAP_CACHE_SIZE);
            mBitmapCache = new LruCache<Object, Bitmap>(bitmapCacheSize) {
                @Override
                protected int sizeOf(Object key, Bitmap value) {
                    return value.getByteCount();
                }

                @Override
                protected void entryRemoved(
                        boolean evicted, Object key, Bitmap oldValue, Bitmap newValue) {
                    if (DEBUG) dumpStats();
                }
            };
            final int holderCacheSize = (int) (cacheSizeAdjustment * HOLDER_CACHE_SIZE);
            mBitmapHolderCache = new LruCache<Object, BitmapHolder>(holderCacheSize) {
                @Override
                protected int sizeOf(Object key, BitmapHolder value) {
                    return value.bytes != null ? value.bytes.length : 0;
                }

                @Override
                protected void entryRemoved(
                        boolean evicted, Object key, BitmapHolder oldValue, BitmapHolder newValue) {
                    if (DEBUG) dumpStats();
                }
            };
            mBitmapHolderCacheRedZoneBytes = (int) (holderCacheSize * 0.75);
            Log.i(TAG, "Cache adj: " + cacheSizeAdjustment);
            if (DEBUG) {
                Log.d(TAG, "Cache size: " + btk(mBitmapHolderCache.maxSize())
                        + " + " + btk(mBitmapCache.maxSize()));
            }

            mThumbnailSize = context.getResources().getDimensionPixelSize(
                    R.dimen.contact_browser_list_item_photo_size);
        }

        /**
         * Converts bytes to K bytes, rounding up.  Used only for debug log.
         */
        private static String btk(int bytes) {
            return ((bytes + 1023) / 1024) + "K";
        }

        private static final int safeDiv(int dividend, int divisor) {
            return (divisor == 0) ? 0 : (dividend / divisor);
        }

        private static boolean isChildView(View parent, View potentialChild) {
            return potentialChild.getParent() != null && (potentialChild.getParent() == parent || (
                    potentialChild.getParent() instanceof ViewGroup && isChildView(parent,
                            (ViewGroup) potentialChild.getParent())));
        }

        /**
         * If necessary, decodes bytes stored in the holder to Bitmap.  As long as the
         * bitmap is held either by {@link #mBitmapCache} or by a soft reference in
         * the holder, it will not be necessary to decode the bitmap.
         */
        private static void inflateBitmap(BitmapHolder holder, int requestedExtent) {
            final int sampleSize =
                    BitmapUtil.findOptimalSampleSize(holder.originalSmallerExtent, requestedExtent);
            byte[] bytes = holder.bytes;
            if (bytes == null || bytes.length == 0) {
                return;
            }

            if (sampleSize == holder.decodedSampleSize) {
                // Check the soft reference.  If will be retained if the bitmap is also
                // in the LRU cache, so we don't need to check the LRU cache explicitly.
                if (holder.bitmapRef != null) {
                    holder.bitmap = holder.bitmapRef.get();
                    if (holder.bitmap != null) {
                        return;
                    }
                }
            }

            try {
                Bitmap bitmap = BitmapUtil.decodeBitmapFromBytes(bytes, sampleSize);

                // TODO: As a temporary workaround while framework support is being added to
                // clip non-square bitmaps into a perfect circle, manually crop the bitmap into
                // into a square if it will be displayed as a thumbnail so that it can be cropped
                // into a circle.
                final int height = bitmap.getHeight();
                final int width = bitmap.getWidth();

                // The smaller dimension of a scaled bitmap can range from anywhere from 0 to just
                // below twice the length of a thumbnail image due to the way we calculate the optimal
                // sample size.
                if (height != width && Math.min(height, width) <= mThumbnailSize * 2) {
                    final int dimension = Math.min(height, width);
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                }
                // make bitmap mutable and draw size onto it
                if (DEBUG_SIZES) {
                    Bitmap original = bitmap;
                    bitmap = bitmap.copy(bitmap.getConfig(), true);
                    original.recycle();
                    Canvas canvas = new Canvas(bitmap);
                    Paint paint = new Paint();
                    paint.setTextSize(16);
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Style.FILL);
                    canvas.drawRect(0.0f, 0.0f, 50.0f, 20.0f, paint);
                    paint.setColor(Color.WHITE);
                    paint.setAntiAlias(true);
                    canvas.drawText(bitmap.getWidth() + "/" + sampleSize, 0, 15, paint);
                }

                holder.decodedSampleSize = sampleSize;
                holder.bitmap = bitmap;
                holder.bitmapRef = new SoftReference<Bitmap>(bitmap);
                if (DEBUG) {
                    Log.d(TAG, "inflateBitmap " + btk(bytes.length) + " -> "
                            + bitmap.getWidth() + "x" + bitmap.getHeight()
                            + ", " + btk(bitmap.getByteCount()));
                }
            } catch (OutOfMemoryError e) {
                // Do nothing - the photo will appear to be missing
            }
        }

        /**
         * Dump cache stats on logcat.
         */
        private void dumpStats() {
            if (!DEBUG) return;
            {
                int numHolders = 0;
                int rawBytes = 0;
                int bitmapBytes = 0;
                int numBitmaps = 0;
                for (BitmapHolder h : mBitmapHolderCache.snapshot().values()) {
                    numHolders++;
                    if (h.bytes != null) {
                        rawBytes += h.bytes.length;
                    }
                    Bitmap b = h.bitmapRef != null ? h.bitmapRef.get() : null;
                    if (b != null) {
                        numBitmaps++;
                        bitmapBytes += b.getByteCount();
                    }
                }
                Log.d(TAG, "L1: " + btk(rawBytes) + " + " + btk(bitmapBytes) + " = "
                        + btk(rawBytes + bitmapBytes) + ", " + numHolders + " holders, "
                        + numBitmaps + " bitmaps, avg: "
                        + btk(safeDiv(rawBytes, numHolders))
                        + "," + btk(safeDiv(bitmapBytes, numBitmaps)));
                Log.d(TAG, "L1 Stats: " + mBitmapHolderCache.toString()
                        + ", overwrite: fresh=" + mFreshCacheOverwrite.get()
                        + " stale=" + mStaleCacheOverwrite.get());
            }

            {
                int numBitmaps = 0;
                int bitmapBytes = 0;
                for (Bitmap b : mBitmapCache.snapshot().values()) {
                    numBitmaps++;
                    bitmapBytes += b.getByteCount();
                }
                Log.d(TAG, "L2: " + btk(bitmapBytes) + ", " + numBitmaps + " bitmaps"
                        + ", avg: " + btk(safeDiv(bitmapBytes, numBitmaps)));
                // We don't get from L2 cache, so L2 stats is meaningless.
            }
        }

        @Override
        public void onTrimMemory(int level) {
            if (DEBUG) Log.d(TAG, "onTrimMemory: " + level);
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) {
                // Clear the caches.  Note all pending requests will be removed too.
                clear();
            }
        }

        @Override
        public void preloadPhotosInBackground() {
            ensureLoaderThread();
            mLoaderThread.requestPreloading();
        }

        @Override
        public void loadThumbnail(ImageView view, long photoId, boolean darkTheme, boolean isCircular,
                                  DefaultImageRequest defaultImageRequest, DefaultImageProvider defaultProvider) {
            if (photoId == 0) {
                // No photo is needed
                defaultProvider.applyDefaultImage(view, -1, darkTheme, defaultImageRequest);
                mPendingRequests.remove(view);
            } else {
                if (DEBUG) Log.d(TAG, "loadPhoto request: " + photoId);
                loadPhotoByIdOrUri(view, Request.createFromThumbnailId(photoId, darkTheme, isCircular,
                        defaultProvider));
            }
        }

        @Override
        public void loadPhoto(ImageView view, Uri photoUri, int requestedExtent, boolean darkTheme,
                              boolean isCircular, DefaultImageRequest defaultImageRequest,
                              DefaultImageProvider defaultProvider) {
            if (photoUri == null) {
                // No photo is needed
                defaultProvider.applyDefaultImage(view, requestedExtent, darkTheme,
                        defaultImageRequest);
                mPendingRequests.remove(view);
            } else {
                if (DEBUG) Log.d(TAG, "loadPhoto request: " + photoUri);
                if (isDefaultImageUri(photoUri)) {
                    createAndApplyDefaultImageForUri(view, photoUri, requestedExtent, darkTheme,
                            isCircular, defaultProvider);
                } else {
                    loadPhotoByIdOrUri(view, Request.createFromUri(photoUri, requestedExtent,
                            darkTheme, isCircular, defaultProvider));
                }
            }
        }

        private void createAndApplyDefaultImageForUri(ImageView view, Uri uri, int requestedExtent,
                                                      boolean darkTheme, boolean isCircular, DefaultImageProvider defaultProvider) {
            DefaultImageRequest request = getDefaultImageRequestFromUri(uri);
            request.isCircular = isCircular;
            defaultProvider.applyDefaultImage(view, requestedExtent, darkTheme, request);
        }

        private void loadPhotoByIdOrUri(ImageView view, Request request) {
            boolean loaded = loadCachedPhoto(view, request, false);
            if (loaded) {
                mPendingRequests.remove(view);
            } else {
                mPendingRequests.put(view, request);
                if (!mPaused) {
                    // Send a request to start loading photos
                    requestLoading();
                }
            }
        }

        @Override
        public void removePhoto(ImageView view) {
            view.setImageDrawable(null);
            mPendingRequests.remove(view);
        }

        /**
         * Cancels pending requests to load photos asynchronously for views inside
         * {@param fragmentRootView}. If {@param fragmentRootView} is null, cancels all requests.
         */
        @Override
        public void cancelPendingRequests(View fragmentRootView) {
            if (fragmentRootView == null) {
                mPendingRequests.clear();
                return;
            }
            ImageView[] requestSetCopy = mPendingRequests.keySet().toArray(new ImageView[
                    mPendingRequests.size()]);
            for (ImageView imageView : requestSetCopy) {
                // If an ImageView is orphaned (currently scrap) or a child of fragmentRootView, then
                // we can safely remove its request.
                if (imageView.getParent() == null || isChildView(fragmentRootView, imageView)) {
                    mPendingRequests.remove(imageView);
                }
            }
        }

        @Override
        public void refreshCache() {
            if (mBitmapHolderCacheAllUnfresh) {
                if (DEBUG) Log.d(TAG, "refreshCache -- no fresh entries.");
                return;
            }
            if (DEBUG) Log.d(TAG, "refreshCache");
            mBitmapHolderCacheAllUnfresh = true;
            for (BitmapHolder holder : mBitmapHolderCache.snapshot().values()) {
                holder.fresh = false;
            }
        }

        /**
         * Checks if the photo is present in cache.  If so, sets the photo on the view.
         *
         * @return false if the photo needs to be (re)loaded from the provider.
         */
        private boolean loadCachedPhoto(ImageView view, Request request, boolean fadeIn) {
            BitmapHolder holder = mBitmapHolderCache.get(request.getKey());
            if (holder == null) {
                // The bitmap has not been loaded ==> show default avatar
                request.applyDefaultImage(view, request.mIsCircular);
                return false;
            }

            if (holder.bytes == null) {
                request.applyDefaultImage(view, request.mIsCircular);
                return holder.fresh;
            }

            Bitmap cachedBitmap = holder.bitmapRef == null ? null : holder.bitmapRef.get();
            if (cachedBitmap == null) {
                if (holder.bytes.length < 8 * 1024) {
                    // Small thumbnails are usually quick to inflate. Let's do that on the UI thread
                    inflateBitmap(holder, request.getRequestedExtent());
                    cachedBitmap = holder.bitmap;
                    if (cachedBitmap == null) return false;
                } else {
                    // This is bigger data. Let's send that back to the Loader so that we can
                    // inflate this in the background
                    request.applyDefaultImage(view, request.mIsCircular);
                    return false;
                }
            }

            final Drawable previousDrawable = view.getDrawable();
            if (fadeIn && previousDrawable != null) {
                final Drawable[] layers = new Drawable[2];
                // Prevent cascade of TransitionDrawables.
                if (previousDrawable instanceof TransitionDrawable) {
                    final TransitionDrawable previousTransitionDrawable =
                            (TransitionDrawable) previousDrawable;
                    layers[0] = previousTransitionDrawable.getDrawable(
                            previousTransitionDrawable.getNumberOfLayers() - 1);
                } else {
                    layers[0] = previousDrawable;
                }
                layers[1] = getDrawableForBitmap(mContext.getResources(), cachedBitmap, request);
                TransitionDrawable drawable = new TransitionDrawable(layers);
                view.setImageDrawable(drawable);
                drawable.startTransition(FADE_TRANSITION_DURATION);
            } else {
                view.setImageDrawable(
                        getDrawableForBitmap(mContext.getResources(), cachedBitmap, request));
            }

            // Put the bitmap in the LRU cache. But only do this for images that are small enough
            // (we require that at least six of those can be cached at the same time)
            if (cachedBitmap.getByteCount() < mBitmapCache.maxSize() / 6) {
                mBitmapCache.put(request.getKey(), cachedBitmap);
            }

            // Soften the reference
            holder.bitmap = null;

            return holder.fresh;
        }

        /**
         * Given a bitmap, returns a drawable that is configured to display the bitmap based on the
         * specified request.
         */
        private Drawable getDrawableForBitmap(Resources resources, Bitmap bitmap, Request request) {
            if (request.mIsCircular) {
                final RoundedBitmapDrawable drawable =
                        RoundedBitmapDrawableFactory.create(resources, bitmap);
                drawable.setAntiAlias(true);
                drawable.setCornerRadius(bitmap.getHeight() / 2);
                return drawable;
            } else {
                return new BitmapDrawable(resources, bitmap);
            }
        }

        public void clear() {
            if (DEBUG) Log.d(TAG, "clear");
            mPendingRequests.clear();
            mBitmapHolderCache.evictAll();
            mBitmapCache.evictAll();
        }

        @Override
        public void pause() {
            mPaused = true;
        }

        @Override
        public void resume() {
            mPaused = false;
            if (DEBUG) dumpStats();
            if (!mPendingRequests.isEmpty()) {
                requestLoading();
            }
        }

        /**
         * Sends a message to this thread itself to start loading images.  If the current
         * view contains multiple image views, all of those image views will get a chance
         * to request their respective photos before any of those requests are executed.
         * This allows us to load images in bulk.
         */
        private void requestLoading() {
            if (!mLoadingRequested) {
                mLoadingRequested = true;
                mMainThreadHandler.sendEmptyMessage(MESSAGE_REQUEST_LOADING);
            }
        }

        /**
         * Processes requests on the main thread.
         */
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REQUEST_LOADING: {
                    mLoadingRequested = false;
                    if (!mPaused) {
                        ensureLoaderThread();
                        mLoaderThread.requestLoading();
                    }
                    return true;
                }

                case MESSAGE_PHOTOS_LOADED: {
                    if (!mPaused) {
                        processLoadedImages();
                    }
                    if (DEBUG) dumpStats();
                    return true;
                }
            }
            return false;
        }

        public void ensureLoaderThread() {
            if (mLoaderThread == null) {
                mLoaderThread = new LoaderThread(mContext.getContentResolver());
                mLoaderThread.start();
            }
        }

        /**
         * Goes over pending loading requests and displays loaded photos.  If some of the
         * photos still haven't been loaded, sends another request for image loading.
         */
        private void processLoadedImages() {
            Iterator<ImageView> iterator = mPendingRequests.keySet().iterator();
            while (iterator.hasNext()) {
                ImageView view = iterator.next();
                Request key = mPendingRequests.get(view);
                // TODO: Temporarily disable contact photo fading in, until issues with
                // RoundedBitmapDrawables overlapping the default image drawables are resolved.
                boolean loaded = loadCachedPhoto(view, key, false);
                if (loaded) {
                    iterator.remove();
                }
            }

            softenCache();

            if (!mPendingRequests.isEmpty()) {
                requestLoading();
            }
        }

        /**
         * Removes strong references to loaded bitmaps to allow them to be garbage collected
         * if needed.  Some of the bitmaps will still be retained by {@link #mBitmapCache}.
         */
        private void softenCache() {
            for (BitmapHolder holder : mBitmapHolderCache.snapshot().values()) {
                holder.bitmap = null;
            }
        }

        /**
         * Stores the supplied bitmap in cache.
         */
        private void cacheBitmap(Object key, byte[] bytes, boolean preloading, int requestedExtent) {
            if (DEBUG) {
                BitmapHolder prev = mBitmapHolderCache.get(key);
                if (prev != null && prev.bytes != null) {
                    Log.d(TAG, "Overwriting cache: key=" + key + (prev.fresh ? " FRESH" : " stale"));
                    if (prev.fresh) {
                        mFreshCacheOverwrite.incrementAndGet();
                    } else {
                        mStaleCacheOverwrite.incrementAndGet();
                    }
                }
                Log.d(TAG, "Caching data: key=" + key + ", " +
                        (bytes == null ? "<null>" : btk(bytes.length)));
            }
            BitmapHolder holder = new BitmapHolder(bytes,
                    bytes == null ? -1 : BitmapUtil.getSmallerExtentFromBytes(bytes));

            // Unless this image is being preloaded, decode it right away while
            // we are still on the background thread.
            if (!preloading) {
                inflateBitmap(holder, requestedExtent);
            }

            mBitmapHolderCache.put(key, holder);
            mBitmapHolderCacheAllUnfresh = false;
        }

        @Override
        public void cacheBitmap(Uri photoUri, Bitmap bitmap, byte[] photoBytes) {
            final int smallerExtent = Math.min(bitmap.getWidth(), bitmap.getHeight());
            // We can pretend here that the extent of the photo was the size that we originally
            // requested
            Request request = Request.createFromUri(photoUri, smallerExtent, false /* darkTheme */,
                    false /* isCircular */, DEFAULT_AVATAR);
            BitmapHolder holder = new BitmapHolder(photoBytes, smallerExtent);
            holder.bitmapRef = new SoftReference<Bitmap>(bitmap);
            mBitmapHolderCache.put(request.getKey(), holder);
            mBitmapHolderCacheAllUnfresh = false;
            mBitmapCache.put(request.getKey(), bitmap);
        }

        /**
         * Populates an array of photo IDs that need to be loaded. Also decodes bitmaps that we have
         * already loaded
         */
        private void obtainPhotoIdsAndUrisToLoad(Set<Long> photoIds,
                                                 Set<String> photoIdsAsStrings, Set<Request> uris) {
            photoIds.clear();
            photoIdsAsStrings.clear();
            uris.clear();

            boolean jpegsDecoded = false;

        /*
         * Since the call is made from the loader thread, the map could be
         * changing during the iteration. That's not really a problem:
         * ConcurrentHashMap will allow those changes to happen without throwing
         * exceptions. Since we may miss some requests in the situation of
         * concurrent change, we will need to check the map again once loading
         * is complete.
         */
            Iterator<Request> iterator = mPendingRequests.values().iterator();
            while (iterator.hasNext()) {
                Request request = iterator.next();
                final BitmapHolder holder = mBitmapHolderCache.get(request.getKey());
                if (holder != null && holder.bytes != null && holder.fresh &&
                        (holder.bitmapRef == null || holder.bitmapRef.get() == null)) {
                    // This was previously loaded but we don't currently have the inflated Bitmap
                    inflateBitmap(holder, request.getRequestedExtent());
                    jpegsDecoded = true;
                } else {
                    if (holder == null || !holder.fresh) {
                        if (request.isUriRequest()) {
                            uris.add(request);
                        } else {
                            photoIds.add(request.getId());
                            photoIdsAsStrings.add(String.valueOf(request.mId));
                        }
                    }
                }
            }

            if (jpegsDecoded) mMainThreadHandler.sendEmptyMessage(MESSAGE_PHOTOS_LOADED);
        }

        /**
         * Maintains the state of a particular photo.
         */
        private static class BitmapHolder {
            final byte[] bytes;
            final int originalSmallerExtent;

            volatile boolean fresh;
            Bitmap bitmap;
            Reference<Bitmap> bitmapRef;
            int decodedSampleSize;

            public BitmapHolder(byte[] bytes, int originalSmallerExtent) {
                this.bytes = bytes;
                this.fresh = true;
                this.originalSmallerExtent = originalSmallerExtent;
            }
        }

        public static class UriUtils {
            /**
             * Static helper, not instantiable.
             */
            private UriUtils() {
            }

            /**
             * Checks whether two URI are equal, taking care of the case where either is null.
             */
            public static boolean areEqual(Uri uri1, Uri uri2) {
                if (uri1 == null && uri2 == null) {
                    return true;
                }
                if (uri1 == null || uri2 == null) {
                    return false;
                }
                return uri1.equals(uri2);
            }

            /**
             * Parses a string into a URI and returns null if the given string is null.
             */
            public static Uri parseUriOrNull(String uriString) {
                if (uriString == null) {
                    return null;
                }
                return Uri.parse(uriString);
            }

            /**
             * Converts a URI into a string, returns null if the given URI is null.
             */
            public static String uriToString(Uri uri) {
                return uri == null ? null : uri.toString();
            }

            public static boolean isEncodedContactUri(Uri uri) {
                if (uri == null) {
                    return false;
                }
                final String lastPathSegment = uri.getLastPathSegment();
                if (lastPathSegment == null) {
                    return false;
                }
                return lastPathSegment.equals(Constants.LOOKUP_URI_ENCODED);
            }
        }

        /**
         * A holder for either a Uri or an id and a flag whether this was requested for the dark or
         * light theme
         */
        private static final class Request {
            private final long mId;
            private final Uri mUri;
            private final boolean mDarkTheme;
            private final int mRequestedExtent;
            private final DefaultImageProvider mDefaultProvider;
            /**
             * Whether or not the contact photo is to be displayed as a circle
             */
            private final boolean mIsCircular;

            private Request(long id, Uri uri, int requestedExtent, boolean darkTheme,
                            boolean isCircular, DefaultImageProvider defaultProvider) {
                mId = id;
                mUri = uri;
                mDarkTheme = darkTheme;
                mIsCircular = isCircular;
                mRequestedExtent = requestedExtent;
                mDefaultProvider = defaultProvider;
            }

            public static Request createFromThumbnailId(long id, boolean darkTheme, boolean isCircular,
                                                        DefaultImageProvider defaultProvider) {
                return new Request(id, null /* no URI */, -1, darkTheme, isCircular, defaultProvider);
            }

            public static Request createFromUri(Uri uri, int requestedExtent, boolean darkTheme,
                                                boolean isCircular, DefaultImageProvider defaultProvider) {
                return new Request(0 /* no ID */, uri, requestedExtent, darkTheme, isCircular,
                        defaultProvider);
            }

            public boolean isUriRequest() {
                return mUri != null;
            }

            public Uri getUri() {
                return mUri;
            }

            public long getId() {
                return mId;
            }

            public int getRequestedExtent() {
                return mRequestedExtent;
            }

            @Override
            public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + (int) (mId ^ (mId >>> 32));
                result = prime * result + mRequestedExtent;
                result = prime * result + ((mUri == null) ? 0 : mUri.hashCode());
                return result;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null) return false;
                if (getClass() != obj.getClass()) return false;
                final Request that = (Request) obj;
                if (mId != that.mId) return false;
                if (mRequestedExtent != that.mRequestedExtent) return false;
                if (!UriUtils.areEqual(mUri, that.mUri)) return false;
                // Don't compare equality of mDarkTheme because it is only used in the default contact
                // photo case. When the contact does have a photo, the contact photo is the same
                // regardless of mDarkTheme, so we shouldn't need to put the photo request on the queue
                // twice.
                return true;
            }

            public Object getKey() {
                return mUri == null ? mId : mUri;
            }

            /**
             * Applies the default image to the current view. If the request is URI-based, looks for
             * the contact type encoded fragment to determine if this is a request for a business photo,
             * in which case we will load the default business photo.
             *
             * @param view       The current image view to apply the image to.
             * @param isCircular Whether the image is circular or not.
             */
            public void applyDefaultImage(ImageView view, boolean isCircular) {
                final DefaultImageRequest request;

                if (isCircular) {
                    request = isBusinessContactUri(mUri)
                            ? DefaultImageRequest.EMPTY_CIRCULAR_BUSINESS_IMAGE_REQUEST
                            : DefaultImageRequest.EMPTY_CIRCULAR_DEFAULT_IMAGE_REQUEST;
                } else {
                    request = isBusinessContactUri(mUri)
                            ? DefaultImageRequest.EMPTY_DEFAULT_BUSINESS_IMAGE_REQUEST
                            : DefaultImageRequest.EMPTY_DEFAULT_IMAGE_REQUEST;
                }
                mDefaultProvider.applyDefaultImage(view, mRequestedExtent, mDarkTheme, request);
            }
        }

        public static class BitmapUtil {
            private BitmapUtil() {
            }

            /**
             * Returns Width or Height of the picture, depending on which size is smaller. Doesn't actually
             * decode the picture, so it is pretty efficient to run.
             */
            public static int getSmallerExtentFromBytes(byte[] bytes) {
                final BitmapFactory.Options options = new BitmapFactory.Options();

                // don't actually decode the picture, just return its bounds
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

                // test what the best sample size is
                return Math.min(options.outWidth, options.outHeight);
            }

            /**
             * Finds the optimal sampleSize for loading the picture
             *
             * @param originalSmallerExtent Width or height of the picture, whichever is smaller
             * @param targetExtent          Width or height of the target view, whichever is bigger.
             *                              <p/>
             *                              If either one of the parameters is 0 or smaller, no sampling is applied
             */
            public static int findOptimalSampleSize(int originalSmallerExtent, int targetExtent) {
                // If we don't know sizes, we can't do sampling.
                if (targetExtent < 1) return 1;
                if (originalSmallerExtent < 1) return 1;

                // Test what the best sample size is. To do that, we find the sample size that gives us
                // the best trade-off between resulting image size and memory requirement. We allow
                // the down-sampled image to be 20% smaller than the target size. That way we can get around
                // unfortunate cases where e.g. a 720 picture is requested for 362 and not down-sampled at
                // all. Why 20%? Why not. Prove me wrong.
                int extent = originalSmallerExtent;
                int sampleSize = 1;
                while ((extent >> 1) >= targetExtent * 0.8f) {
                    sampleSize <<= 1;
                    extent >>= 1;
                }

                return sampleSize;
            }

            /**
             * Decodes the bitmap with the given sample size
             */
            public static Bitmap decodeBitmapFromBytes(byte[] bytes, int sampleSize) {
                final BitmapFactory.Options options;
                if (sampleSize <= 1) {
                    options = null;
                } else {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = sampleSize;
                }
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            }

            /**
             * Retrieves a copy of the specified drawable resource, rotated by a specified angle.
             *
             * @param resources  The current resources.
             * @param resourceId The resource ID of the drawable to rotate.
             * @param angle      The angle of rotation.
             * @return Rotated drawable.
             */
            public static Drawable getRotatedDrawable(
                    android.content.res.Resources resources, int resourceId, float angle) {

                // Get the original drawable and make a copy which will be rotated.
                Bitmap original = BitmapFactory.decodeResource(resources, resourceId);
                Bitmap rotated = Bitmap.createBitmap(
                        original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);

                // Perform the rotation.
                Canvas tempCanvas = new Canvas(rotated);
                tempCanvas.rotate(angle, original.getWidth() / 2, original.getHeight() / 2);
                tempCanvas.drawBitmap(original, 0, 0, null);

                return new BitmapDrawable(resources, rotated);
            }

            /**
             * Given an input bitmap, scales it to the given width/height and makes it round.
             *
             * @param input        {@link Bitmap} to scale and crop
             * @param targetWidth  desired output width
             * @param targetHeight desired output height
             * @return output bitmap scaled to the target width/height and cropped to an oval. The
             * cropping algorithm will try to fit as much of the input into the output as possible,
             * while preserving the target width/height ratio.
             */
            public static Bitmap getRoundedBitmap(Bitmap input, int targetWidth, int targetHeight) {
                if (input == null) {
                    return null;
                }
                final Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, input.getConfig());
                final Canvas canvas = new Canvas(result);
                final Paint paint = new Paint();
                canvas.drawARGB(0, 0, 0, 0);
                paint.setAntiAlias(true);
                if(SDK_INT>=21) {
                    canvas.drawOval(0, 0, targetWidth, targetHeight, paint);
                }
                else
                {
                    canvas.drawCircle(0, 0, targetWidth, paint);
                }
                // Specifies that only pixels present in the destination (i.e. the drawn oval) should
                // be overwritten with pixels from the input bitmap.
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                final int inputWidth = input.getWidth();
                final int inputHeight = input.getHeight();

                // Choose the largest scale factor that will fit inside the dimensions of the
                // input bitmap.
                final float scaleBy = Math.min((float) inputWidth / targetWidth,
                        (float) inputHeight / targetHeight);

                final int xCropAmountHalved = (int) (scaleBy * targetWidth / 2);
                final int yCropAmountHalved = (int) (scaleBy * targetHeight / 2);

                final Rect src = new Rect(
                        inputWidth / 2 - xCropAmountHalved,
                        inputHeight / 2 - yCropAmountHalved,
                        inputWidth / 2 + xCropAmountHalved,
                        inputHeight / 2 + yCropAmountHalved);

                final RectF dst = new RectF(0, 0, targetWidth, targetHeight);
                canvas.drawBitmap(input, src, dst, paint);
                return result;
            }
        }

        public class Constants {

            /**
             * Log tag for performance measurement.
             * To enable: adb shell setprop log.tag.ContactsPerf VERBOSE
             */
            public static final String PERFORMANCE_TAG = "ContactsPerf";

            // Used for lookup URI that contains an encoded JSON string.
            public static final String LOOKUP_URI_ENCODED = "encoded";
        }

        /**
         * The thread that performs loading of photos from the database.
         */
        private class LoaderThread extends HandlerThread implements Callback {
            private static final int BUFFER_SIZE = 1024 * 16;
            private static final int MESSAGE_PRELOAD_PHOTOS = 0;
            private static final int MESSAGE_LOAD_PHOTOS = 1;

            /**
             * A pause between preload batches that yields to the UI thread.
             */
            private static final int PHOTO_PRELOAD_DELAY = 1000;

            /**
             * Number of photos to preload per batch.
             */
            private static final int PRELOAD_BATCH = 25;

            /**
             * Maximum number of photos to preload.  If the cache size is 2Mb and
             * the expected average size of a photo is 4kb, then this number should be 2Mb/4kb = 500.
             */
            private static final int MAX_PHOTOS_TO_PRELOAD = 100;
            private static final int PRELOAD_STATUS_NOT_STARTED = 0;
            private static final int PRELOAD_STATUS_IN_PROGRESS = 1;
            private static final int PRELOAD_STATUS_DONE = 2;
            private final ContentResolver mResolver;
            private final StringBuilder mStringBuilder = new StringBuilder();
            private final Set<Long> mPhotoIds = Sets.newHashSet();
            private final Set<String> mPhotoIdsAsStrings = Sets.newHashSet();
            private final Set<Request> mPhotoUris = Sets.newHashSet();
            private final List<Long> mPreloadPhotoIds = Lists.newArrayList();
            private Handler mLoaderThreadHandler;
            private byte mBuffer[];
            private int mPreloadStatus = PRELOAD_STATUS_NOT_STARTED;

            public LoaderThread(ContentResolver resolver) {
                super(LOADER_THREAD_NAME);
                mResolver = resolver;
            }

            public void ensureHandler() {
                if (mLoaderThreadHandler == null) {
                    mLoaderThreadHandler = new Handler(getLooper(), this);
                }
            }

            /**
             * Kicks off preloading of the next batch of photos on the background thread.
             * Preloading will happen after a delay: we want to yield to the UI thread
             * as much as possible.
             * <p/>
             * If preloading is already complete, does nothing.
             */
            public void requestPreloading() {
                if (mPreloadStatus == PRELOAD_STATUS_DONE) {
                    return;
                }

                ensureHandler();
                if (mLoaderThreadHandler.hasMessages(MESSAGE_LOAD_PHOTOS)) {
                    return;
                }

                mLoaderThreadHandler.sendEmptyMessageDelayed(
                        MESSAGE_PRELOAD_PHOTOS, PHOTO_PRELOAD_DELAY);
            }

            /**
             * Sends a message to this thread to load requested photos.  Cancels a preloading
             * request, if any: we don't want preloading to impede loading of the photos
             * we need to display now.
             */
            public void requestLoading() {
                ensureHandler();
                mLoaderThreadHandler.removeMessages(MESSAGE_PRELOAD_PHOTOS);
                mLoaderThreadHandler.sendEmptyMessage(MESSAGE_LOAD_PHOTOS);
            }

            /**
             * Receives the above message, loads photos and then sends a message
             * to the main thread to process them.
             */
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_PRELOAD_PHOTOS:
                        preloadPhotosInBackground();
                        break;
                    case MESSAGE_LOAD_PHOTOS:
                        loadPhotosInBackground();
                        break;
                }
                return true;
            }

            /**
             * The first time it is called, figures out which photos need to be preloaded.
             * Each subsequent call preloads the next batch of photos and requests
             * another cycle of preloading after a delay.  The whole process ends when
             * we either run out of photos to preload or fill up cache.
             */
            private void preloadPhotosInBackground() {
                if (mPreloadStatus == PRELOAD_STATUS_DONE) {
                    return;
                }

                if (mPreloadStatus == PRELOAD_STATUS_NOT_STARTED) {
                    queryPhotosForPreload();
                    if (mPreloadPhotoIds.isEmpty()) {
                        mPreloadStatus = PRELOAD_STATUS_DONE;
                    } else {
                        mPreloadStatus = PRELOAD_STATUS_IN_PROGRESS;
                    }
                    requestPreloading();
                    return;
                }

                if (mBitmapHolderCache.size() > mBitmapHolderCacheRedZoneBytes) {
                    mPreloadStatus = PRELOAD_STATUS_DONE;
                    return;
                }

                mPhotoIds.clear();
                mPhotoIdsAsStrings.clear();

                int count = 0;
                int preloadSize = mPreloadPhotoIds.size();
                while (preloadSize > 0 && mPhotoIds.size() < PRELOAD_BATCH) {
                    preloadSize--;
                    count++;
                    Long photoId = mPreloadPhotoIds.get(preloadSize);
                    mPhotoIds.add(photoId);
                    mPhotoIdsAsStrings.add(photoId.toString());
                    mPreloadPhotoIds.remove(preloadSize);
                }

                loadThumbnails(true);

                if (preloadSize == 0) {
                    mPreloadStatus = PRELOAD_STATUS_DONE;
                }

                Log.v(TAG, "Preloaded " + count + " photos.  Cached bytes: "
                        + mBitmapHolderCache.size());

                requestPreloading();
            }

            private void queryPhotosForPreload() {
                Cursor cursor = null;
                try {
                    Uri uri = Contacts.CONTENT_URI.buildUpon().appendQueryParameter(
                            ContactsContract.DIRECTORY_PARAM_KEY, String.valueOf(Directory.DEFAULT))
                            .appendQueryParameter(ContactsContract.LIMIT_PARAM_KEY,
                                    String.valueOf(MAX_PHOTOS_TO_PRELOAD))
                            .build();
                    cursor = mResolver.query(uri, new String[]{Contacts.PHOTO_ID},
                            Contacts.PHOTO_ID + " NOT NULL AND " + Contacts.PHOTO_ID + "!=0",
                            null,
                            Contacts.STARRED + " DESC, " + Contacts.LAST_TIME_CONTACTED + " DESC");

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            // Insert them in reverse order, because we will be taking
                            // them from the end of the list for loading.
                            mPreloadPhotoIds.add(0, cursor.getLong(0));
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }

            private void loadPhotosInBackground() {
                obtainPhotoIdsAndUrisToLoad(mPhotoIds, mPhotoIdsAsStrings, mPhotoUris);
                loadThumbnails(false);
                loadUriBasedPhotos();
                requestPreloading();
            }

            /**
             * Loads thumbnail photos with ids
             */
            private void loadThumbnails(boolean preloading) {
                if (mPhotoIds.isEmpty()) {
                    return;
                }

                // Remove loaded photos from the preload queue: we don't want
                // the preloading process to load them again.
                if (!preloading && mPreloadStatus == PRELOAD_STATUS_IN_PROGRESS) {
                    for (Long id : mPhotoIds) {
                        mPreloadPhotoIds.remove(id);
                    }
                    if (mPreloadPhotoIds.isEmpty()) {
                        mPreloadStatus = PRELOAD_STATUS_DONE;
                    }
                }

                mStringBuilder.setLength(0);
                mStringBuilder.append(Photo._ID + " IN(");
                for (int i = 0; i < mPhotoIds.size(); i++) {
                    if (i != 0) {
                        mStringBuilder.append(',');
                    }
                    mStringBuilder.append('?');
                }
                mStringBuilder.append(')');

                Cursor cursor = null;
                try {
                    if (DEBUG) Log.d(TAG, "Loading " + TextUtils.join(",", mPhotoIdsAsStrings));
                    cursor = mResolver.query(Data.CONTENT_URI,
                            COLUMNS,
                            mStringBuilder.toString(),
                            mPhotoIdsAsStrings.toArray(EMPTY_STRING_ARRAY),
                            null);

                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            Long id = cursor.getLong(0);
                            byte[] bytes = cursor.getBlob(1);
                            cacheBitmap(id, bytes, preloading, -1);
                            mPhotoIds.remove(id);
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                // Remaining photos were not found in the contacts database (but might be in profile).
                for (Long id : mPhotoIds) {
                    if (ContactsContract.isProfileId(id)) {
                        Cursor profileCursor = null;
                        try {
                            profileCursor = mResolver.query(
                                    ContentUris.withAppendedId(Data.CONTENT_URI, id),
                                    COLUMNS, null, null, null);
                            if (profileCursor != null && profileCursor.moveToFirst()) {
                                cacheBitmap(profileCursor.getLong(0), profileCursor.getBlob(1),
                                        preloading, -1);
                            } else {
                                // Couldn't load a photo this way either.
                                cacheBitmap(id, null, preloading, -1);
                            }
                        } finally {
                            if (profileCursor != null) {
                                profileCursor.close();
                            }
                        }
                    } else {
                        // Not a profile photo and not found - mark the cache accordingly
                        cacheBitmap(id, null, preloading, -1);
                    }
                }

                mMainThreadHandler.sendEmptyMessage(MESSAGE_PHOTOS_LOADED);
            }

            /**
             * Loads photos referenced with Uris. Those can be remote thumbnails
             * (from directory searches), display photos etc
             */
            private void loadUriBasedPhotos() {
                for (Request uriRequest : mPhotoUris) {
                    // Keep the original URI and use this to key into the cache.  Failure to do so will
                    // result in an image being continually reloaded into cache if the original URI
                    // has a contact type encodedFragment (eg nearby places business photo URLs).
                    Uri originalUri = uriRequest.getUri();

                    // Strip off the "contact type" we added to the URI to ensure it was identifiable as
                    // a business photo -- there is no need to pass this on to the server.
                    Uri uri = ContactPhotoManager.removeContactType(originalUri);

                    if (mBuffer == null) {
                        mBuffer = new byte[BUFFER_SIZE];
                    }
                    try {
                        if (DEBUG) Log.d(TAG, "Loading " + uri);
                        final String scheme = uri.getScheme();
                        InputStream is = null;
                        if (scheme.equals("http") || scheme.equals("https")) {
                            is = new URL(uri.toString()).openStream();
                        } else {
                            is = mResolver.openInputStream(uri);
                        }
                        if (is != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            try {
                                int size;
                                while ((size = is.read(mBuffer)) != -1) {
                                    baos.write(mBuffer, 0, size);
                                }
                            } finally {
                                is.close();
                            }
                            cacheBitmap(originalUri, baos.toByteArray(), false,
                                    uriRequest.getRequestedExtent());
                            mMainThreadHandler.sendEmptyMessage(MESSAGE_PHOTOS_LOADED);
                        } else {
                            Log.v(TAG, "Cannot load photo " + uri);
                            cacheBitmap(originalUri, null, false, uriRequest.getRequestedExtent());
                        }
                    } catch (final Exception | OutOfMemoryError ex) {
                        Log.v(TAG, "Cannot load photo " + uri, ex);
                        cacheBitmap(originalUri, null, false, uriRequest.getRequestedExtent());
                    }
                }
            }
        }

    }
}

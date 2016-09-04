package xobyx.xcontactj.until;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by xobyx on 9/2/2016.
 * For xobyx.xcontactj.until/XContactj2
 */
public class ContactCursorLoader extends AsyncTaskLoader<Cursor> {

    public static final int SEARCH_MODE_NONE = 0;
    public static final int SEARCH_MODE_DEFAULT = 1;
    public static final int SEARCH_MODE_CONTACT_SHORTCUT = 2;
    public static final int SEARCH_MODE_DATA_SHORTCUT = 3;
    // This is a virtual column created for a MatrixCursor.
    public static final String DIRECTORY_TYPE = "directoryType";
    private static final String TAG = "ContactEntryListAdapter";

    private final ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            forceLoad();
        }
    };
    private int mDirectorySearchMode;
    private boolean mLocalInvisibleDirectoryEnabled;
    private MatrixCursor mDefaultDirectoryList;

    public ContactCursorLoader(Context context) {
        super(context);
    }

    public void setDirectorySearchMode(int mode) {
        mDirectorySearchMode = mode;
    }

    /**
     * A flag that indicates whether the {@link ContactsContract.Directory#LOCAL_INVISIBLE} directory should
     * be included in the results.
     */
    public void setLocalInvisibleDirectoryEnabled(boolean flag) {
        this.mLocalInvisibleDirectoryEnabled = flag;
    }

    @Override
    protected void onStartLoading() {
        getContext().getContentResolver().
                registerContentObserver(ContactsContract.Data.CONTENT_URI, false, mObserver);
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        getContext().getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    public Cursor loadInBackground() {
        if (mDirectorySearchMode == SEARCH_MODE_NONE) {
            return getDefaultDirectories();
        }

        MatrixCursor result = new MatrixCursor(null);
        Context context = getContext();

        String selection;

                selection = "has_phone_number > 0";

        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                null, selection, null,ContactsContract.Data.LOOKUP_KEY);
        if (cursor == null) {
            return result;
        }
        try {
            while (cursor.moveToNext()) {
                long directoryId = cursor.getLong(DirectoryQuery.ID);
                String directoryType = null;

                String packageName = cursor.getString(DirectoryQuery.PACKAGE_NAME);
                int typeResourceId = cursor.getInt(DirectoryQuery.TYPE_RESOURCE_ID);

                String displayName = cursor.getString(DirectoryQuery.DISPLAY_NAME);
                int photoSupport = cursor.getInt(DirectoryQuery.PHOTO_SUPPORT);
                result.addRow(new Object[]{directoryId, directoryType, displayName, photoSupport});
            }
        } finally {
            cursor.close();
        }

        return result;
    }

    private Cursor getDefaultDirectories() {

        return mDefaultDirectoryList;
    }

    @Override
    protected void onReset() {
        stopLoading();
    }

    private static final class DirectoryQuery {

        public static final int ID = 0;
        public static final int PACKAGE_NAME = 1;
        public static final int TYPE_RESOURCE_ID = 2;
        public static final int DISPLAY_NAME = 3;
        public static final int PHOTO_SUPPORT = 4;
    }
}

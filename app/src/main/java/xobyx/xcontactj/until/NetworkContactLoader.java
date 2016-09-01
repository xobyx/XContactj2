package xobyx.xcontactj.until;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN;

/**
 * Created by xobyx on 12/24/2014.
 * c# to java
 */
public class NetworkContactLoader extends AsyncTaskLoader<List<Contact>> {


    final ContentObserver mContentObserver;
    private final int mNet;
    private final Object i = new Object();
    private Cursor mCursor;

    private CancellationSignal mCancellationSignal;// = new CancellationSignal();
    private List<Contact> mList;
    private boolean mContentChange;

    public NetworkContactLoader(Context context, int net) {
        super(context);

        mNet = net;
        mContentObserver = new ForceLoadContentObserver();

    }

    @Override
    public boolean takeContentChanged() {
        synchronized (lock) {
            boolean var1 = this.mContentChange;
            this.mContentChange = false;
            return var1;
        }


    }

    @Override
    public void onContentChanged() {
        synchronized (lock) {
            this.mContentChange = true;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public List<Contact> loadInBackground() {
        synchronized (lock) {
            if (isAbandoned()) {
                throw new OperationCanceledException();
            }
            // mCancellationSignal = new CancellationSignal();
        }
        try {
            mCursor = getContext().getContentResolver().query(ME._uri, null, ME.DatabaseSelector[mNet], ME.getDatabaseArg[mNet], ME.sortBy);
            if (mCursor != null) {
                try {
                    // Ensure the cursor window is filled.
                    mCursor.getCount();
                    mCursor.registerContentObserver(mContentObserver);
                    // mCursor.setNotificationUri(getContext().getContentResolver(),ME._uri);

                } catch (RuntimeException ex) {
                    mCursor.close();
                    throw ex;
                }



                int phoneColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int time_connected=mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.TIMES_CONTACTED);
                int last_time_connected= mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.LAST_TIME_CONTACTED);
                int pphoneColumnIndex = mCursor.getColumnIndex("data4");
                //ContactsContract.co
                int LableColumnIndex = mCursor.getColumnIndex("phonebook_label");
                // int emailColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Adress.ADDRESS);
                int nameColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
                int idclo = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables._ID);
                int lookupColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY);
                int PhotouriColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_URI);
                int IsPrimaryColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.IS_PRIMARY);
                int Photothumb = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_THUMBNAIL_URI);

                int account = mCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME);
                int Type = mCursor.getColumnIndex("data2");
                int CoustomType = mCursor.getColumnIndex("data3");

                mList = new ArrayList<Contact>();



                Contact b = null;
                String key = "";
                while (mCursor.moveToNext()) {
                    String currentLookupKey = mCursor.getString(lookupColumnIndex);

                    if (!currentLookupKey.equals(key)) {
                        key = currentLookupKey;
                        b = new Contact();
                        b.Net = mNet;
                        b.Lookup = currentLookupKey;
                        b.LookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, b.Lookup);
                        if (!mCursor.isNull(PhotouriColumnIndex))
                            b.PhotoUri = Uri.parse(mCursor.getString(PhotouriColumnIndex));
                        if (!mCursor.isNull(Photothumb))
                            b.PhotoThumbUri = Uri.parse(mCursor.getString(Photothumb));
                        b.Name = mCursor.getString(nameColumnIndex);
                        if (SDK_INT > JELLY_BEAN)
                            b.Lable = mCursor.getString(LableColumnIndex);

                        mList.add(b);

                    }
                    Contact.Phones a = new Contact.Phones();
                    a.Fnumber = mCursor.getString(pphoneColumnIndex);
                    String bh =mCursor.getString(phoneColumnIndex);
                    if(a.Fnumber==null&&bh!=null)
                    {
                        a.Fnumber=bh;
                    }
                    a.IsPrimyer = Boolean.valueOf(mCursor.getString(IsPrimaryColumnIndex));

                    a.setNumber(bh);
                    int m = mCursor.getInt(Type);
                    String bn = mCursor.getString(CoustomType);
                    a.Type = ContactsContract.CommonDataKinds.Phone.getTypeLabel(getContext().getResources(), m, bn).toString();
                    a.ID = mCursor.getString(idclo);

                    a.TimeConnected=mCursor.getInt(time_connected);
                    a.LastTimeConnected=mCursor.getInt(last_time_connected);
                    a.Account = mCursor.getString(account);
                    a.nNet = Network.values()[mNet];
                    if (b != null) {
                        a.User = b.Name;
                        if(!b.Phone.contains(a))
                        b.Phone.add(a);
                    }


                }


                //mCursor.close();
                return mList;
            }

        } finally {
            synchronized (lock) {
                mCancellationSignal = null;
            }

        }

        return null;
    }

    final Object lock=new Object();
    @Override
    public void onCanceled(List<Contact> var1) {
        if (mCursor != null && mCursor.isClosed())
            mCursor.close();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();
        mCursor = null;
        mList = null;
    }


    @Override
    public void deliverResult(List<Contact> var1) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (var1 != null) return;
        }
        if (isStarted()) {
            super.deliverResult(var1);
        }

    }

    @Override
    protected void onStartLoading() {
        if (mList != null) {
            deliverResult(mList);
        }
        if (mList == null || takeContentChanged()) {

            forceLoad();

        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean cancelLoad() {


        synchronized (lock) {
            if (mCancellationSignal != null) {
                mCancellationSignal.cancel();
            }
        }
        return super.cancelLoad();
    }


}

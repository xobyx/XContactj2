package xobyx.xcontactj.until;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Contactables;
import static android.provider.ContactsContract.CommonDataKinds.Email;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static xobyx.xcontactj.until.ME.getCurrentNetwork;

/**
 * Created by xobyx on 12/11/2014.
 * c# to java
 */
public class ContactLoader extends AsyncTaskLoader<List<Contact>> {

    final static String WHATSAPP_ITEM = "vnd.android.cursor.item/vnd.com.whatsapp.profile";
    private final ContentObserver mContentObserver;
    List<Contact> m;
    private Cursor mCursor;
    private boolean mContentChange;
    private int currentNetwork;

    public ContactLoader(Context var1) {
        super(var1);
        mContentObserver = new ForceLoadContentObserver();
    }

    @Override
    public List<Contact> loadInBackground() {
        mCursor = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, "has_phone_number = 1", null, ContactsContract.Data.LOOKUP_KEY);
        if (mCursor != null) {
            mCursor.registerContentObserver(mContentObserver);
            int phoneColumnIndex = mCursor.getColumnIndex(Phone.NUMBER);

            int ItemType = mCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
            int Type = mCursor.getColumnIndex("data2");
            int CoustomType = mCursor.getColumnIndex("data3");
            int pphoneColumnIndex = mCursor.getColumnIndex("data4");

            // int emailColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Adress.ADDRESS);
            int nameColumnIndex = mCursor.getColumnIndex(Contactables.DISPLAY_NAME);
            int idclo = mCursor.getColumnIndex(Contactables._ID);
            int lookupColumnIndex = mCursor.getColumnIndex(Contactables.LOOKUP_KEY);
            int time_connected=mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.TIMES_CONTACTED);
            int last_time_connected= mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.LAST_TIME_CONTACTED);
            int PhotouriColumnIndex = mCursor.getColumnIndex(Contactables.PHOTO_URI);
            int IsPrimaryColumnIndex = mCursor.getColumnIndex(Contactables.IS_PRIMARY);
            int Photothumb = mCursor.getColumnIndex(Contactables.PHOTO_THUMBNAIL_URI);
            currentNetwork = getCurrentNetwork(this.getContext());
            m = new ArrayList<>();
            while (mCursor.moveToNext()) {


                Contact b = null;
                String key = "";
                while (mCursor.moveToNext()) {
                    String currentLookupKey = mCursor.getString(lookupColumnIndex);

                    if (!currentLookupKey.equals(key)) {

                        key = currentLookupKey;
                        b = new Contact();
                        b.Lookup = currentLookupKey;
                        b.LookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, b.Lookup);
                        if (!mCursor.isNull(PhotouriColumnIndex))
                            b.PhotoUri = Uri.parse(mCursor.getString(PhotouriColumnIndex));
                        if (!mCursor.isNull(Photothumb))
                            b.PhotoThumbUri = Uri.parse(mCursor.getString(Photothumb));
                        b.Name = mCursor.getString(nameColumnIndex);
                        b.Lable = String.valueOf(b.Name.charAt(0));

                        m.add(b);


                    }
                    final String mim = mCursor.getString(ItemType);
                    if (mim.equals(Phone.CONTENT_ITEM_TYPE)) {
                        Contact.Phones a = new Contact.Phones();
                        a.Fnumber = mCursor.getString(pphoneColumnIndex);
                        a.IsPrimyer = mCursor.getInt(IsPrimaryColumnIndex) == 1;
                        a.setNumber(mCursor.getString(phoneColumnIndex));
                        a.ID = mCursor.getString(idclo);
                        a.TimeConnected=mCursor.getInt(time_connected);
                        a.LastTimeConnected = mCursor.getInt(last_time_connected);


                        if (a.Fnumber != null && !a.Fnumber.isEmpty()) {


                            a.nNet = Network.values()[ME.getNetForNumber(a.Fnumber)];
                            if (b != null) {
                                if (!b.Phone.contains(a)) {
                                    b.Phone.add(a);


                                }
                            }


                        }

                    } else {


                        if (mim.equals(Email.CONTENT_ITEM_TYPE))
                            if (b != null) {
                                b.otherAccounts.Add(new EmailClass(mCursor.getString(mCursor.getColumnIndex("data1")),
                                        mim, mCursor.getString(CoustomType),
                                        mCursor.getString(Type),
                                        mCursor.getInt(idclo)));
                            } else if (mim.equals(WHATSAPP_ITEM))
                                if (b != null) {
                                    b.otherAccounts.Add(new WhatsappClass(mCursor.getString(mCursor.getColumnIndex("data1")),
                                            mim, mCursor.getString(CoustomType),
                                            mCursor.getString(Type),
                                            mCursor.getInt(idclo)));
                                }

                    }

                }


            }
            return m;
        }
        return null;
    }

    @Override
    public void onCanceled(List<Contact> var1) {
        if (mCursor != null && mCursor.isClosed())
            mCursor.close();
    }

    @Override
    public boolean takeContentChanged() {
        synchronized (this) {
            boolean var1 = this.mContentChange;
            this.mContentChange = false;
            return var1;
        }


    }

    @Override
    public void onContentChanged() {
        synchronized (this) {
            this.mContentChange = true;
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();
        //mCursor = null;
        m = null;
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
        if (m != null) {
            deliverResult(m);
        }
        if (m == null || takeContentChanged()) {

            forceLoad();

        }
    }


    /**
     * Override this method to perform TouchedItem computation on TouchedItem background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */

}


package xobyx.xcontactj.until;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import xobyx.xcontactj.MyApp;
import xobyx.xcontactj.R;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;


public class ME {

    private static ArrayList<Contact> AllList=new ArrayList<>();
    public static ArrayList<Contact> SudaniList=new ArrayList<>() ;
    public static ArrayList<Contact> ZainiList =new ArrayList<>() ;
    public static ArrayList<Contact> MtnList =new ArrayList<>();
    public final static ArrayList[] $ =
            {
                    ZainiList, SudaniList, MtnList


            };


    static public final int nColors[] = {
            0xFFC10278,
            0xff02a7dd,
            0xFFFFCA08,
            0xFF512DA8,
    };
    static public final int nColors2[] = {
            0xFF5FB5BA,
            0xff70b050,
            0xff2878a8,
            0xFFFF4081,
    };
    private static final String SUDANI_MNC = "63407";
    private static final String ZAIN_MNC = "63401";
    private static final String MTN_MNC = "63407";

    static com.google.i18n.phonenumbers.PhoneNumberUtil a = PhoneNumberUtil.getInstance();

    static public int getNetForNumber(String h) {
        String n;

        try {


            n = a.formatOutOfCountryCallingNumber(a.parse(h, "SD"), "SD");

        } catch (NumberParseException e) {
            Log.d(TAG, "getNetForNumber: " + h);

            n = h;
        }
        int net = 3;
        if (n.length() > 2) {

            for (int i = 0; i < Mnet.length; i++) {

                for (String s1 : Mnet[i]) {
                    if (n.startsWith(s1)) {
                        net = i;
                        break;
                    }
                }
            }
        }
        return net;
    }

    static final String[] Sudani_net = {
            "01",

    };
    static final String[] zain_net = {
            "091",

            "090",

            "096",

    };
    static final String[] mtn_net = {
            "092",

            "099",

    };
    public final static String[][] Mnet = {zain_net, Sudani_net, mtn_net};
    /**
     * A map of sample (base) items, by ID.
     */
    public static final int[][] NetDrawables =
            {
                    {
                            R.drawable.izain1,
                            R.drawable.izaind
                    },
                    {
                            R.drawable.isudani1,
                            R.drawable.isudanid
                    },
                    {
                            R.drawable.imtn1,
                            R.drawable.imtnd
                    }
                    , {
                    R.drawable.ic_action_unknown,
                    R.drawable.ic_action_unknown
            }
            };
    public static final Uri _uri;
    final static String[] ZAIN_NUM = {"+24991%", "+24990%", "+24996%"};
    final static String[] SUDANI_NUM = {"+2491%"};
    final static String[] MTN_NUM = {"+24992%", "+24996%", "+24999%"};
    public final static String getDatabaseArg[][] = {

            ZAIN_NUM,

            SUDANI_NUM,

            MTN_NUM,

    };
    final static String[] UAE_NUM = {"+971%"};
    final static String zain_s = "data4 LIKE ? OR data4 LIKE ? OR data4 LIKE ? ";
    final static String sudani_s = "data4 LIKE ? ";
    final static String mtn_s = "data4 LIKE ? OR data4 LIKE ? OR data4 LIKE ? ";
    public final static String DatabaseSelector[] = {

            zain_s,

            sudani_s,

            mtn_s
    };
    public final static String NET_N[] = {"Zain", "Sudani", "MTN", "Unknown"};
    final static String sortBy = ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY;
    private static final String TAG = ME.class.getSimpleName();
    private static final String CLASS_TAG = "ME_Help_class";
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static ITelephony telephonyService = null;

    static {
        if (SDK_INT >= JELLY_BEAN_MR2) {
            _uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;
        } else {
            _uri = ContactsContract.Data.CONTENT_URI;
        }

    }

    /**
     * An array of sample (base) items.
     */
    public static byte[] getMD5(byte[] var0) {
        try {

            MessageDigest var2 = MessageDigest.getInstance("MD5");
            var2.update(var0);
            byte[] var3 = var2.digest();
            return var3;
        } catch (NoSuchAlgorithmException var4) {

            return null;
        }
    }

    public static String getMD5Hex(String var0) {
        final byte[] bytes = var0.getBytes();
        return (new BigInteger(getMD5(bytes))).abs().toString(36);

    }


    public static String[] NetNames = {"Zain", "Sudani", "Mtn", "Unknown"};

    public static void setSuperPrimary(Context var0, long var1) {
        if (var1 == -1L) {
            Log.e(TAG, "Invalid arguments for setSuperPrimary request");
        } else {
            ContentValues var3 = new ContentValues(2);
            var3.put("is_super_primary", 1);
            var3.put("is_primary", 1);
            var0.getContentResolver().update(ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, var1), var3, null, null);
        }
    }


    public static void SetInternetSettingFor(int i, Context mh) {
        Uri m = Uri.parse("content://telephony/carriers");

        //mh.grantUriPermission(mh.getPackageName(),m, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        ContentValues b = new ContentValues();
        b.put(Telephony.Carriers.NAME, "XContactj_" + NET_N[i]);
        b.put(Telephony.Carriers.APN, i == 1 ? "sudaninet" : i == 0 ? "internet" : "internet1");
        b.put(Telephony.Carriers.MCC, "634");
        b.put(Telephony.Carriers.MNC, "07");
        b.put(Telephony.Carriers.NUMERIC, "63407");
        Uri insert = mh.getContentResolver().insert(m, b);


    }

    /**
     * return Current Network {@code ArrayList} for the specified object.
     *
     * @param a Context
     *          the object to search for.
     * @return {@code 0} if {@link Network} is zain
     * {@code 1} if {@code Network} is sudani ,  {@code 2} if {@code Network} is Mtn
     * {@code 3} if Not found..
     */
    public static int getCurrentNetwork(Context a) {


        TelephonyManager d = ((MyApp) a.getApplicationContext()).getTelephonyManager();


        String net = d.getNetworkOperator();


        if (net != null && !net.isEmpty()) {
            if (net.equals(SUDANI_MNC)) {
                return 1;

            } else if (net.equals(ZAIN_MNC))

            {
                return 0;
            } else if (net.equals(MTN_MNC))

            {
                return 2;
            }
        }
        //  Toast.makeText(a, "No Network founded..", Toast.LENGTH_SHORT).show();
        return 3;

    }

    public static void setTheme(Context m, int net) {
        if (net < 3)
            m.setTheme(net_styles[net]);
    }

    static final int[] net_styles = {R.style.zain, R.style.sudani, R.style.mtn};


    public static void DrawNetworkLogo(Context m, Canvas canvas, int net) {
        final Drawable drawable = m.getResources().getDrawable(ME.NetDrawables[net][0]);
        // Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
        // Canvas canvas2 = new Canvas(bitmap);
        drawable.setBounds(canvas.getWidth() - 20, canvas.getHeight() - 20, canvas.getWidth(), canvas.getHeight());


        // drawable.draw(canvas2);

        drawable.draw(canvas);
    }

    private static ITelephony createITelephonyImp(Context con) {
        try {

           /* TelephonyManager telephonyManager = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);

            // Java reflection to gain access to TelephonyManager
            Class c = Class.forName("android.telephony.TelephonyManager");
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(telephonyManager);*/

            //ITelephony phone = ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
            // telephonyService.

            TelephonyManager m = (TelephonyManager) con.getSystemService(con.TELEPHONY_SERVICE);

            Class c = m.getClass();
            //M1:
            //Method getITelephony = c.getDeclaredMethod("getITelephony", m.getClass());

            //M2: {62} index of method in array..
            Method[] declaredMethods = c.getDeclaredMethods();
            Method mk = declaredMethods[62];


            //M3:
            /*for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals( "getITelephony")) {
                    Log.i("F","Find getITelephony Method...");
                }
            }*/


            mk.setAccessible(true);
            try {
                telephonyService = (ITelephony) mk.invoke(m);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        } catch (Exception ex) {
            Log.e(CLASS_TAG, "Problem getting telephony service [" + ex.getMessage() + "]");
            return null;
        }
        return telephonyService;

    }

    public static ITelephony getTelephonyService(Context b) {
        if (telephonyService == null)
            createITelephonyImp(b);

        return telephonyService;
    }

    private static void setTelephonyService(ITelephony telephonyService) {
        ME.telephonyService = telephonyService;
    }

    public static ArrayList<Contact> getAllList() {
        return AllList;
    }

    public static void setAllList(List<Contact> allList) {
        AllList.clear();
        AllList.addAll(allList);
    }

    public static Contact getContactFromUri(Context context, Uri uri) {


        String id="";
        Cursor query = context.getContentResolver().query(uri, new String[]{ContactsContract.Contacts.LOOKUP_KEY}, null, null, "");
        if(query!=null && query.moveToFirst()) {
            id = query.getString(0);
            query.close();
            if(id.isEmpty())return null;
        }
        else
        return null;



        Contact b = null;

        Cursor mCursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, "lookup =?", new String[]{id}, "");
        if (mCursor != null) {

            int phoneColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            int ItemType = mCursor.getColumnIndex(ContactsContract.Data.MIMETYPE);
            int Type = mCursor.getColumnIndex("data2");
            int CoustomType = mCursor.getColumnIndex("data3");
            int pphoneColumnIndex = mCursor.getColumnIndex("data4");
            int account = mCursor.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME);
            // int emailColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Adress.ADDRESS);
            int nameColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.DISPLAY_NAME);
            int idclo = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables._ID);
            int lookupColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.LOOKUP_KEY);
            int time_connected = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.TIMES_CONTACTED);
            int last_time_connected = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Callable.LAST_TIME_CONTACTED);
            int PhotouriColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_URI);
            int IsPrimaryColumnIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.IS_PRIMARY);
            int Photothumb = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Contactables.PHOTO_THUMBNAIL_URI);


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


                }
                final String mim = mCursor.getString(ItemType);
                if (mim.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                    Contact.Phones a = new Contact.Phones();
                    a.Fnumber = mCursor.getString(pphoneColumnIndex);
                    String bh = mCursor.getString(phoneColumnIndex);
                    if (a.Fnumber == null && bh != null) {
                        a.Fnumber = bh;
                    }
                    if (a.Fnumber != null && !a.Fnumber.isEmpty()) {
                        a.setNumber(a.Fnumber);
                        a.IsPrimyer = mCursor.getInt(IsPrimaryColumnIndex) == 1;
                        a.setNumber(bh);
                        a.ID = mCursor.getString(idclo);
                        a.TimeConnected = mCursor.getInt(time_connected);
                        a.LastTimeConnected = mCursor.getInt(last_time_connected);


                        a.nNet = Network.values()[getNetForNumber(a.getNumber())];
                        if (b != null) {
                            if (!(b.Phone.size() > 2 && b.Phone.get(b.Phone.size() - 1).getNumber().equals(a.getNumber()))) {
                                a.User = b.Name;
                                if (!b.Phone.contains(a))
                                    b.Phone.add(a);
                            }

                            //}
                        }


                    }

                }
            }


        }

        mCursor.close();
        return b;
    }

    public interface AfterFinish {
        void finish();

    }

    public static class Ad extends AsyncTask<Integer, Integer, List<Contact.Phones>> {
        public int mNet;
        public int mPos;
        private AfterFinish AfterFinish;

        @Override
        protected List<Contact.Phones> doInBackground(Integer... params) {
            mNet = params[0];
            mPos = params[1];
            final String key = ((Contact) $[mNet].get(mPos)).Lookup;
            final List<Contact.Phones> temp = new ArrayList<Contact.Phones>();
            if (mNet != 1)

                for (Contact var14 : SudaniList)
                    if (var14.Lookup.startsWith(key)) {

                        temp.addAll(var14.Phone);

                    }
            if (mNet != 0)
                for (Contact var11 : ZainiList) {
                    if (var11.Lookup.startsWith(key)) {

                        temp.addAll(var11.Phone);

                    }
                }


            if (mNet != 2)

                for (Contact var8 : MtnList) {
                    if (var8.Lookup.startsWith(key)) {

                        temp.addAll(var8.Phone);

                    }
                }


            if (temp.size() == 0)
                return null;
            else
                return temp;
        }

        @Override
        protected void onPostExecute(List<Contact.Phones> phones) {
            super.onPostExecute(phones);
            AfterFinish.finish();
        }

        public void setAfterFinish(ME.AfterFinish afterFinish) {
            AfterFinish = afterFinish;
        }
    }


    // private String receivedMsg = StringUtils.EMPTY;

    public static void getOtherAsync(final int mNet, final int mPos, final IAsyncFinish handler) {
        final AsyncTask task = new AsyncTask() {
            IAsyncFinish m;

            @Override
            protected Object doInBackground(Object[] params) {
                m = handler;
                final String key = ((Contact) $[mNet].get(mPos)).Lookup;
                List<Contact.Phones> temp = new ArrayList<>();
                if (mNet != 1) {


                    for (Contact var14 : SudaniList)

                        if (var14.Lookup.startsWith(key)) {

                            temp.addAll(var14.Phone);

                        }


                }


                if (mNet != 0) {


                    for (Contact var11 : ZainiList)
                        if (var11.Lookup.startsWith(key))
                            temp.addAll(var11.Phone);


                }


                if (mNet != 2) {


                    for (Contact var8 : MtnList)
                        if (var8.Lookup.startsWith(key)) {

                            temp.addAll(var8.Phone);

                        }


                }

                return temp;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                handler.LoadFinnish((List<Contact.Phones>) o);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    public interface IAsyncFinish {
        void LoadFinnish(List<Contact.Phones> r);
    }

}

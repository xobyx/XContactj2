package xobyx.xcontactj.until;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import xobyx.xcontactj.R;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR2;


public class ME {

    public final static ArrayList<Contact> SudaniList = new ArrayList<Contact>();
    public final static ArrayList<Contact> ZainiList = new ArrayList<Contact>();
    public final static ArrayList<Contact> MtnList = new ArrayList<Contact>();
    public final static ArrayList[] $ =
            {
                    ZainiList, SudaniList, MtnList


            };
    static public final int nColors[] = {
    0xFFC10278,
    0xff02a7dd,
    0xFFFFCA08,
    0xFFA093CA,
    };

    static public int getNet(Context mContext, String h) {

        int net = 3;
        if (h.length() > 2) {
            int[] mnet = ME.Mnet;
            for (int i1 = 0; i1 < mnet.length; i1++) {
                int i = mnet[i1];
                for (String s1 : mContext.getResources().getStringArray(i)) {
                    if (h.startsWith(s1)) {
                        net = i1;
                        i1 = mnet.length;//break
                        break;
                    }
                }
            }
        }
        return net;
    }

    public final static int Mnet[] = {R.array.zain_net, R.array.Sudani_net, R.array.mtn_net};
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
            };
    public static final Uri _uri;
    final static String[] ZAIN_NUM = {"+24991%", "+24990%"};
    final static String[] SUDANI_NUM = {"+2491%"};
    final static String[] MTN_NUM = {"+24992%", "+24996%", "+24999%"};
    public final static String getDatabaseArg[][] = {

            ZAIN_NUM,

            SUDANI_NUM,

            MTN_NUM,

    };
    final static String[] UAE_NUM = {"+971%"};
    final static String zain_s = "data4 LIKE ? OR data4 LIKE ? ";
    final static String sudani_s = "data4 LIKE ? ";
    final static String mtn_s = "data4 LIKE ? OR data4 LIKE ? OR data4 LIKE ? ";
    public final static String DatabaseSelector[] = {

            zain_s,

            sudani_s,

            mtn_s
    };
    public  final static String NET_N[]={"Zain","Sudani","MTN","Unknown"};
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


    public static String[] NetNames={"Zain","Sudani","Mtn","Unknown"};
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


    /**
     * return Current Network {@code ArrayList} for the specified object.
     *
     * @param a Context
     *            the object to search for.
     * @return {@code 0} if {@link xobyx.xcontactj.activities.MainActivity.Network} is zain
     *         {@code 1} if {@code Network} is sudani ,  {@code 2} if {@code Network} is Mtn
     *          {@code 3} if Not found..
     */
    public static int getCurrentNetwork(Context a) {


        TelephonyManager d = (TelephonyManager) a.getSystemService(Context.TELEPHONY_SERVICE);

        String net = d.getNetworkOperatorName();
        if(net!=null&&!net.isEmpty()) {
            if (net.matches("[Ss]udani") || net.startsWith("[Oo]ne")) {
                return 1;

            } else if (net.matches("[Zz]ain SDN") || net.startsWith("[Mm]obitel"))

            {
                return 0;
            } else if (net.matches("[Mm]tn") || net.startsWith("[Aa]raeba"))

            {
                return 2;
            }
        }
      //  Toast.makeText(a, "No Network founded..", Toast.LENGTH_SHORT).show();
        return 3;

    }

    public static void setTheme(Context m,int net)
    {
        m.setTheme(net_styles[net]);
    }
    static final int[] net_styles={R.style.zain,R.style.sudani,R.style.mtn,R.style.Base_Theme_AppCompat};


    public static void DrawNetworkLogo(Context m, Canvas canvas, int net) {
        final Drawable drawable = m.getResources().getDrawable(ME.NetDrawables[net][0]);
       // Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
       // Canvas canvas2 = new Canvas(bitmap);
         drawable.setBounds(canvas.getWidth()-20, canvas.getHeight()-20, canvas.getWidth(), canvas.getHeight());
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

            Class c =m.getClass();
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
        if(telephonyService==null)
            createITelephonyImp(b);

        return telephonyService;
    }

    private static void setTelephonyService(ITelephony telephonyService) {
        ME.telephonyService = telephonyService;
    }

    public interface AfterFinish {
        void finish();

    }

    public static class Ad extends AsyncTask<Integer, Integer, List<Contact.PhoneClass>> {
        public int mNet;
        public int mPos;
        private AfterFinish AfterFinish;

        @Override
        protected List<Contact.PhoneClass> doInBackground(Integer... params) {
            mNet = params[0];
            mPos = params[1];
            final String key = ((Contact) $[mNet].get(mPos)).Lookup;
            final List<Contact.PhoneClass> temp = new ArrayList<Contact.PhoneClass>();
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
        protected void onPostExecute(List<Contact.PhoneClass> phoneClasses) {
            super.onPostExecute(phoneClasses);
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
                List<Contact.PhoneClass> temp = new ArrayList<>();
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
                handler.LoadFinnish((List<Contact.PhoneClass>) o);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    public interface IAsyncFinish {
        void LoadFinnish(List<Contact.PhoneClass> r);
    }

}

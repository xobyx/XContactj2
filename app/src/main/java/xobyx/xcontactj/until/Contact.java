package xobyx.xcontactj.until;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xobyx.xcontactj.activities.MainActivity;

/**
 * Created by xobyx on 2/20/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class Contact implements Comparable<Contact> {
    public String Lookup;
    public String Name;
    public ArrayList<PhoneClass> Phone = new ArrayList<>(5);//max 5 Number
    public int Net;
    public String Lable;
    public Uri PhotoUri;
    public Uri LookupUri;
    public Uri PhotoThumbUri;
    public int Nnamber = 0;
    Other otherAccounts = new Other();

    public Contact() {

    }

    boolean MultiNumber() {
        return Phone.size() > 1;
    }

    public char Lable_char() {
        return this.Name.charAt(0);
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return TouchedItem negative integer if this instance is less than {@code another};
     * TouchedItem positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int compareTo(@NonNull Contact another) {

        return Character.compare(this.Lable_char(), another.Lable_char());
    }

    interface OtherAccount {
        String getMainData();

        String getType();

        int getID();

        String getAccountName();

        String getMoreData();
    }

    static public class Other<T extends OtherAccount> {


        HashMap<String, List<T>> types = new HashMap<String, List<T>>();

        public Other() {


        }

        public void Add(T s) {
            if (types.containsKey(s.getAccountName()))
                types.get(s.getAccountName()).add(s);
            else {
                final ArrayList<T> ts = new ArrayList<T>();
                ts.add(s);
                types.put(s.getAccountName(), ts);
            }
        }


    }

    static public class PhoneClass implements Comparable<PhoneClass> {
        public String Fnumber;
        public boolean IsPrimyer;
        public boolean isFavorite = false;
        public MainActivity.Network nNet;
        public String ID;
        public String Type;
        //dep
        public String User;
        public String Account;
        private String Number;


        @Override
        public String toString() {
            return Fnumber;
        }

        public String getCleanNumber() {
            return Fnumber.replace("+249", "0");
        }

        /**
         * Compares this object to the specified object to determine their relative
         * order.
         *
         * @param another the object to compare to this instance.
         * @return TouchedItem negative integer if this instance is less than {@code another};
         * TouchedItem positive integer if this instance is greater than
         * {@code another}; 0 if this instance has the same order as
         * {@code another}.
         * @throws ClassCastException if {@code another} cannot be converted into something
         *                            comparable to {@code this} instance.
         */
        @Override
        public int compareTo(@NonNull PhoneClass another) {
            return this.IsPrimyer ? 1 : -1;
        }

        public String getNumber() {
            return Number;
        }

        public void setNumber(String number) {
            Number = number.replace("-", "").replace(" ", "").replace("+249", "0");



        }

        @Override
        public boolean equals(Object o) {

            final PhoneClass m = (PhoneClass) o;
            return m.Fnumber != null && m.Fnumber.equals(this.Fnumber);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}

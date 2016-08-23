package xobyx.xcontactj.until;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xobyx on 2/20/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class Contact implements Comparable<Contact> {
    public String Lookup;
    public String Name;
    public ArrayList<Phones> Phone = new ArrayList<>();//max 5 Number
    public int Net;
    public String Lable;
    public Uri PhotoUri;
    public Uri LookupUri;
    public Uri PhotoThumbUri;
    public int mNumberCount = 0;
    Other otherAccounts = new Other();

    public Contact() {

    }

    boolean MultiNumber() {
        return Phone.size() > 1;
    }

    public char fChar() {
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

    @Override
    public int compareTo(@NonNull Contact another) {

         return this.fChar()- another.fChar();
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

    static public class Phones implements Comparable<Phones> {
        public String Fnumber;
        public boolean IsPrimyer;
        public boolean isFavorite = false;
        public Network nNet;
        public String ID;
        public String Type;
        //dep
        public String User;
        public String Account;
        private String Number;
        public int TimeConnected;
        public int LastTimeConnected;


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
        public int compareTo(@NonNull Phones another) {
            return this.IsPrimyer ? 1 : -1;
        }

        public String getNumber() {
            return Number;
        }

        public void setNumber(String number) {
            Number = number.replace("-", "").replace(" ", "").replace("+249", "0");



        }
        static PhoneNumberUtil instance = PhoneNumberUtil.getInstance();
        @Override
        public boolean equals(Object o) {



            final Phones m = (Phones) o;

            return m.Fnumber != null &&instance.isNumberMatch(m.Fnumber,this.Fnumber)!= PhoneNumberUtil.MatchType.NO_MATCH ;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}

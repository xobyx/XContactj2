package xobyx.xcontactj.until;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class MDatabase {

    private final SQLiteDatabase me;


    public MDatabase(Context s) {
        DatabaseH d = new DatabaseH(s);


        me = d.getWritableDatabase();


    }

    public void InsertALL(List<Contact> d, boolean drop) {
        int m = 0;
        if (drop) {
            me.delete(DatabaseH.CONTACT_TABLE, null, null);
            me.delete(DatabaseH.NUMBER_TABLE, null, null);
        }
        me.beginTransaction();
        try {


            for (Contact contact : d) {
                Insert(contact, m++);
            }
            me.setTransactionSuccessful();
        } finally {
            me.endTransaction();
        }
    }

    public long Insert(Contact a, int sd) {
        ContentValues r = new ContentValues();
        r.put(DatabaseH.NAME_COL, a.Name);
        r.put(DatabaseH.LOOK_COL, a.Lookup);

        r.put(DatabaseH.NETWORK_COL, a.Net);
        if (a.PhotoThumbUri != null) {
            r.put(DatabaseH.PHOTO_T_COL, a.PhotoThumbUri.toString());
            r.put(DatabaseH.PHOTO_COL, a.PhotoThumbUri.toString());
        }
        r.put(DatabaseH.LABLE_COL, a.Lable);
        long l;
        if (!me.inTransaction()) {
            l = me.insert(DatabaseH.CONTACT_TABLE, null, r);
        } else l = sd;
        for (Contact.Phones u : a.Phone) {
            ContentValues rs = new ContentValues();

            rs.put(DatabaseH.N_CONTACT_ID_COL, sd);
            rs.put(DatabaseH.N_NUMBER_COL, u.getNumber());
            rs.put(DatabaseH.N_FNUMBER_COL, u.Fnumber);
            rs.put(DatabaseH.N_N_ID_COL, u.ID);
            rs.put(DatabaseH.N_NET_COL, u.nNet.getValue());
            rs.put(DatabaseH.N_IS_PRIM_COL, u.IsPrimyer ? 1 : 0);
            me.insert(DatabaseH.NUMBER_TABLE, null, rs);
        }
        r.clear();


        return l;
    }

    public List<Contact> GetContactsFor(int net) {
        List<Contact> i = new ArrayList<Contact>();
        Cursor c = me.rawQuery("SELECT* FROM " + DatabaseH.CONTACT_TABLE + " WHERE " + DatabaseH.NETWORK_COL + " =? ORDER BY % ", new String[]{String.valueOf(net), DatabaseH.NAME_COL});
        while (c.moveToNext()) {
            String b = c.getString(c.getColumnIndex(DatabaseH.NAME_COL));
            Contact x = new Contact();
            x.Lookup = c.getString(c.getColumnIndex(DatabaseH.LOOK_COL));
            x.Name = b;
            int id = c.getInt(c.getColumnIndex(DatabaseH.ID_COL));
            x.Lable = c.getString(c.getColumnIndex(DatabaseH.LABLE_COL));
            x.PhotoThumbUri = Uri.parse(c.getString(c.getColumnIndex(DatabaseH.PHOTO_T_COL)));
            x.PhotoUri = Uri.parse(c.getString(c.getColumnIndex(DatabaseH.PHOTO_COL)));
            x.Net = c.getInt(c.getColumnIndex(DatabaseH.NETWORK_COL));
            Cursor m = me.rawQuery("SELECT* FROM " + DatabaseH.NUMBER_TABLE + "WHERE " + DatabaseH.N_CONTACT_ID_COL + " =? AND " + DatabaseH.N_NET_COL + " =?", new String[]{String.valueOf(id), String.valueOf(net)});
            while (m != null && m.moveToNext()) {
                Contact.Phones d = new Contact.Phones();
                d.nNet = Network.values()[net];
                d.Fnumber = m.getString(m.getColumnIndex(DatabaseH.N_FNUMBER_COL));
                d.ID = m.getString(m.getColumnIndex(DatabaseH.N_N_ID_COL));
                d.IsPrimyer = m.getInt(m.getColumnIndex(DatabaseH.N_IS_PRIM_COL)) == 1;
                x.Phone.add(d);
            }
            if (m != null) {
                m.close();
            }
            i.add(x);
        }
        c.close();
        return i;

    }

    void DeleteAll() {
        me.delete(DatabaseH.CONTACT_TABLE, "1=1", null);
    }

    void Close() {

        this.me.close();
    }
}

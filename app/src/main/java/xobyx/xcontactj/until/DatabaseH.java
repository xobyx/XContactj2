package xobyx.xcontactj.until;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xobyx on 11/30/2014.
 * c# to java
 */
public class DatabaseH extends SQLiteOpenHelper {
    public static final String CONTACT_TABLE = "contact";
    public static final String NAME_COL = "name";
    public static final String DATABASE_NAME = "consv1.db";
    public static final String LOOK_COL = "lookup";
    public static final String NETWORK_COL = "network";
    public static final String LABLE_COL = "lable";
    public static final String PHOTO_T_COL = "photo_thumb";
    public static final String PHOTO_COL = "photo";
    public static final String N_CONTACT_ID_COL = "con_id";
    public static final String N_NUMBER_COL = "Number";
    public static final String N_FNUMBER_COL = "fnumber";
    public static final String N_N_ID_COL = "number_id";
    public static final String N_IS_PRIM_COL = "isprim";
    public static final String N_NET_COL = "net";
    public static final String ID_COL = "id";
    private static final String IS_VISIBLE_COL = "is_visible";
    private static final String CRATE_DATABASE =
            "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE + " ( "
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                    + NAME_COL + " TEXT NOT NULL ,"
                    + LOOK_COL + " TEXT NOT NULL ,"
                    + NETWORK_COL + " INTEGER NOT NULL,"
                    + IS_VISIBLE_COL + " INTEGER DEFAULT 1,"
                    + LABLE_COL + " TEXT NOT NULL ,"
                    + PHOTO_T_COL + " TEXT  ,"
                    + PHOTO_COL + " TEXT "
                    + " );";
    public static String NUMBER_TABLE = "numbers";
    private static final String CRATE_DATABASE_NUMBER =
            "CREATE TABLE IF NOT EXISTS " + NUMBER_TABLE + " ( "
                    + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                    + N_CONTACT_ID_COL + " INTEGER NOT NULL ,"
                    + N_NUMBER_COL + " TEXT NOT NULL ,"
                    + N_FNUMBER_COL + " TEXT NOT NULL,"
                    + N_N_ID_COL + " TEXT NOT NULL,"
                    + N_IS_PRIM_COL + " INTEGER DEFAULT 0 ,"
                    + N_NET_COL + " INTEGER NOT NULL "

                    + " );";


    public DatabaseH(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CRATE_DATABASE);
        db.execSQL(CRATE_DATABASE_NUMBER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NUMBER_TABLE);
        onCreate(db);


    }
}


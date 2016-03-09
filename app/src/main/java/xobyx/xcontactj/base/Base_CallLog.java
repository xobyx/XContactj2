package xobyx.xcontactj.base;

import java.util.Date;

/**
 * Created by xobyx on 2/21/2016.
 * For xobyx.xcontactj.base/XContactj
 */
public class Base_CallLog {


    private String mNumber = "";
    private String mName = "";
    private Date mDate_str = null;
    private String mDuration_str = "";
    private int mDuration_int;
    private int mType;
    private int mDrawable;
    private int mId;
    private long mDateLong;
    private String mAll;
    private String lookup_uri;
    private int mNetworkColor = 0xFFE91E63;
    private int mNetDrawable;


    public String getName() {
        return this.mName;
    }

    public void setID(int var1) {
        this.mId = var1;
    }

    public void setDateLong(long var1) {
        this.mDateLong = var1;
    }

    public void setName(String var1) {
        this.mName = var1;
    }

    public int getID() {
        return this.mId;
    }

    public void setType(int var1) {
        this.mType = var1;
    }

    public void setDurationStr(String var1) {
        this.mDuration_str = var1;
    }

    public long getDateLong() {
        return this.mDateLong;
    }

    public void setDuration_int(int var1) {
        this.mDuration_int = var1;
    }

    public void setNumber(String var1) {
        this.mNumber = var1;
    }

    public int getType() {
        return this.mType;
    }

    public void setDrawableRes(int var1) {
        this.mDrawable = var1;
    }

    public void setDateStr(long var1) {
        this.mDate_str = new Date(var1);
    }

    public void setNetworkColor(int m) {
        this.mNetworkColor = m;
    }

    public void setNetDrawable(int x) {

        mNetDrawable = x;
    }

    public int getNetDrawable() {
        return mNetDrawable;
    }

    public int getNetworkColor() {
        return this.mNetworkColor;
    }

    public String getDuration_str() {
        return this.mDuration_str;
    }

    public void setAll(String var1) {
        this.mAll = var1;
    }

    public int getDuration_int() {
        return this.mDuration_int;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public Date getDate_str() {
        return this.mDate_str;
    }

    public int getDrawableRes() {
        return this.mDrawable;
    }

    public String getAll() {
        return this.mAll;
    }

    public void setLookup_uri(String lookup_uri) {
        this.lookup_uri = lookup_uri;
    }
}

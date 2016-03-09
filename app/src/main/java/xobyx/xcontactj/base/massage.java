package xobyx.xcontactj.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by xobyx on 2/27/2016.
 * For xobyx.xcontactj.base/XContactj
 */
public class massage implements Parcelable {
    public String addres;
    public String body;
    public int type;
    public Date date;
    public int state;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addres);
        dest.writeString(this.body);
        dest.writeInt(this.type);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeInt(this.state);
    }

    public massage() {
    }

    protected massage(Parcel in) {
        this.addres = in.readString();
        this.body = in.readString();
        this.type = in.readInt();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.state = in.readInt();
    }

    public static final Creator<massage> CREATOR = new Creator<massage>() {
        public massage createFromParcel(Parcel source) {
            return new massage(source);
        }

        public massage[] newArray(int size) {
            return new massage[size];
        }
    };
}

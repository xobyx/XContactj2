package xobyx.xcontactj.until;

/**
 * Created by xobyx on 12/14/2014.
 * c# to java
 */
public class WhatsappClass extends Account {
    public final String wNumber;


    final public String WhatsappUserID;

    public WhatsappClass(String Maindata, String Accountname, String moredata, String type, int id) {
        super(Maindata, Accountname, moredata, type, id);
        WhatsappUserID = Maindata;
        wNumber = moredata;
    }
}

package xobyx.xcontactj.until;

/**
 * Created by xobyx on 12/14/2014.
 * c# to java
 */
public class EmailClass extends Account {


    public String Adress;
    public String Type;


    public EmailClass(String data1, String mim, String data3, String data2, int id) {
        super(data1, mim, data3, data2, id);
        Adress = data1;
        int i = Integer.parseInt(data2);
        switch (i) {
            case 1:
                Type = "Home";
            case 2:
                Type = "Work";
            case 3:
                Type = "Other";
            case 4:
                Type = "Mobile";
            default:
                Type = data3;
        }
    }
}

package xobyx.xcontactj.until;

import static xobyx.xcontactj.until.Contact.OtherAccount;

public abstract class Account implements OtherAccount {


    private final String main_data;
    private final String account_name;
    private final String moredata;
    private final String type;
    private final int id;

    public Account(String Maindata, String Accountname, String moredata, String type, int id) {
        main_data = Maindata;
        account_name = Accountname;
        this.moredata = moredata;
        this.type = type;
        this.id = id;
    }


    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getAccountName() {
        return account_name;
    }

    @Override
    public String getMainData() {
        return main_data;
    }

    @Override
    public String getMoreData() {
        return moredata;
    }

    @Override
    public String getType() {
        return type;
    }
}

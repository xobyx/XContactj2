package xobyx.xcontactj.until;

import static xobyx.xcontactj.until.Contact.OtherAccount;

public abstract class Account implements OtherAccount {


    private final String maindata;
    private final String accountname;
    private final String moredata;
    private final String type;
    private final int id;

    public Account(String Maindata, String Accountname, String moredata, String type, int id) {
        maindata = Maindata;
        accountname = Accountname;
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
        return accountname;
    }

    @Override
    public String getMainData() {
        return maindata;
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

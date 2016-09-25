package xobyx.xcontactj.until;

/**
 * Created by xobyx on 3/14/2016.
 * For xobyx.xcontactj.until/XContactj2
 */
public enum Network {
    Zain(0),
    Sudani(1),
    Mtn(2),
    Other(3);
    final private int value;


    Network(int a) {
        value = a;


    }

    public int getValue() {
        return value;
    }


}

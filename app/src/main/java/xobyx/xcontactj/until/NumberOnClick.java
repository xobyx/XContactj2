package xobyx.xcontactj.until;

import android.view.View;

/**
 * Created by xobyx on 3/14/2016.
 * For xobyx.xcontactj.until/XContactj2
 */
public class NumberOnClick implements View.OnClickListener {

    public interface NumberOnClickListener {
        void onClick(int i, Contact.Phones j);
    }

    public NumberOnClickListener listener;
    private Contact.Phones Number;


    public NumberOnClick(Contact.Phones v, NumberOnClickListener listener1) {

        Number = v;
        listener = listener1;
    }


    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.onClick(v.getId(), Number);
    }


}

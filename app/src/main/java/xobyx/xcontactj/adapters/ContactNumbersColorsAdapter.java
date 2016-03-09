package xobyx.xcontactj.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;

/**
 * Created by xobyx on 12/12/2014.
 * c# to java
 */
public class ContactNumbersColorsAdapter extends ContactNumberAdapter {
    public ContactNumbersColorsAdapter(Context con, List<Contact> list) {
        super(con, list);
    }

    @Override
    public View getNumberView(int i, View convertView, ViewGroup viewGroup) {
        final View m = super.getNumberView(i, convertView, viewGroup);
        final ImageView view = (ImageView) m.findViewById(R.id.num_child_image);
        view.setVisibility(View.VISIBLE);
        view.setImageResource(ME.NetDrawables[((Contact.PhoneClass) getItem(i)).nNet.getValue()][0]);
        // ((ViewGroup) m).addView(mInflater.inflate(R.layout.child_contact_detils,null),new LinearLayout.LayoutParams(-2,-2));
        return m;
    }
}

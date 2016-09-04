package xobyx.xcontactj.adapters;

import android.content.Context;

import java.util.ArrayList;

import xobyx.xcontactj.fragments.NetFragment;
import xobyx.xcontactj.until.Contact;

/**
 * Created by xobyx on 9/1/2016.
 * For xobyx.xcontactj.adapters/XContactj2
 */
public class AllContactsAdapter extends ContactsAdapter {
    public AllContactsAdapter(Context con, ArrayList<Contact> contacts, boolean mClisp) {
        super(con, contacts, mClisp);
    }

    @Override
    protected void setupView(int position, NetFragment.ViewHolder holder, Contact contact) {
        holder.Text.setText(contact.Name);

        mTexth.setPrefixText(holder.Text, contact.Name, search_text);
        //ib.DrawImageString(contact, holder.Img, mClip, contact.Net);
        if (contact.Phone.size() != 0)
            //   holder.Number.setText(t.Phone.get(0).Number);
            mTexth.setPrefixText(holder.Number, contact.Phone.get(contact.mNumberCount).getNumber(), search_text);

        holder.Img.setImageDrawable(null);
        if (contact.PhotoThumbUri == null) {
            ib.DrawImageString(contact, holder.Img, mClip,-1 );

        } else
            ib.GetPhoto(contact, holder.Img, mClip,-1);
    }
}

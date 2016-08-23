package xobyx.xcontactj.adapters;

/**
 * Created by xobyx on 12/14/2014.
 * c# to java
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.ME;

import xobyx.xcontactj.until.Contact;


////base and def class ,base class is for showing contacts only without numbers
public class ContactNumberAdapter extends ContactBaseAdapter {


    public ContactNumberAdapter(Context con, List<Contact> list) {
        super(con, list, 0);


    }


    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        for (Contact section : mList) {

            int size = section.Phone.size();
            if (position == 0)
                return 0;
            if (position < size)
                return 1;


            position -= size;
        }
        return -1;

    }

    @Override
    public int getCount() {

        int m = 0;
        for (Contact contact : mList) {
            m += contact.Phone.size() - 1;
        }

        return m + mList.size();


    }

    @Override
    public Object getItem(int i) {

        for (Contact section : mList) { //c{1}-c{1}-c{2}-c
            //i=3
            int size = section.Phone.size();//size=0

            // check if position inside this section
            if (i == 0)
                return section;
            if (i < size)
                return section.Phone.get(i);

            // otherwise jump into next section
            i -= size;
        }
        return null;

    }


    private boolean show_net_icon;

    public View getNumberView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (convertView == null) {
            Holders2 holder = new Holders2();
            view = mInflater.inflate(R.layout.child_number, null);
            holder.Text = (TextView) view.findViewById(R.id.num_child_text);
            holder.Number = (TextView) view.findViewById(R.id.num_child_type);
            holder.Img = (ImageView) view.findViewById(R.id.num_child_image);
            view.setTag(holder);
        }
        final Contact.Phones item = (Contact.Phones) getItem(i);
        Holders2 H = (Holders2) view.getTag();
        // H.Text.setText(item.getNumber());
        mTexth.setPrefixText(H.Text, item.getNumber(), x);
        if (show_net_icon) {

            H.Img.setVisibility(View.VISIBLE);
            H.Img.setImageResource(ME.NetDrawables[((Contact.Phones) getItem(i)).nNet.getValue()][0]);
        } else {
            H.Img.setVisibility(View.GONE);
        }

        H.Number.setText(item.Type);
        return view;

    }


    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final int s = this.getItemViewType(i);
        if (s == 1) {
            return getNumberView(i, convertView, viewGroup);
        }
        return super.getView(i, convertView, viewGroup);
    }

    public void setShow_net_icon(boolean show_net_icon) {
        this.show_net_icon = show_net_icon;
        this.notifyDataSetChanged();
    }

    static class Holders2 {
        public TextView Text;
        public ImageView Img;

        public TextView Number;

    }

}


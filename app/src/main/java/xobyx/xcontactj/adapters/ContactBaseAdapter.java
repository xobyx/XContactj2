package xobyx.xcontactj.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.AsyncDrawer;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.views.LetterImageView;

import static xobyx.xcontactj.adapters.ContactBaseAdapter.Mode.List;

/**
 * Created by xobyx on 12/14/2014.
 * c# to java
 */

public class ContactBaseAdapter extends BaseAdapter implements Filterable {

    private List<Contact> swic;

    enum Mode {
        List(0),
        Grid(1);


        private final int value;

        Mode(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }

    final LayoutInflater mInflater;
   // final List<Contact> temp;
    final TextHighlighter mTexth;
    private final AsyncDrawer ib;
    private final Mode mMode;
    public boolean mClip;
    List<Contact> mList;
    String x = "";

    //public ListFragment
    public ContactBaseAdapter(Context con, List<Contact> list, int mode) {
        mList = list;
        swic=mList;
        mMode = mode == 0 ? List : Mode.Grid;


        mTexth = new TextHighlighter(Typeface.BOLD, 0xff02a7dd);

        ib = new AsyncDrawer(con);
        mInflater = LayoutInflater.from(con);


    }

    @Override
    public boolean isEnabled(int position) {

        return getItemViewType(position) == 0;
    }

    @Override
    public int getCount() {

        return swic.size();

    }

    @Override
    public Object getItem(int i) {
        try {


            return swic.get(i);
        } catch (Exception m) {
           throw new StackOverflowError(String.format("id=%d, type=%d ber %s", i, getItemViewType(i), mList.get(i - 1).Name));
        }

    }

    @Override
    public long getItemId(int i) {
        return i;

    }

    public View getView(int i, View view, ViewGroup viewGroup) {

        Holders hold;
        if (view == null) {
            hold = new Holders();
            if (mMode == List)
                view = mInflater.inflate(R.layout.child_main, null);
            else
                view = mInflater.inflate(R.layout.child_main_graid, null);
            hold.Text = (TextView) view.findViewById(R.id.ptextname);
            hold.Number = (TextView) view.findViewById(R.id.numberc);
            hold.Img = (LetterImageView) view.findViewById(R.id.imagec);
            hold.pos = i;
            view.setTag(hold);
        } else {
            hold = (Holders) view.getTag();//as
        }
        Contact t = (Contact) getItem(i);

//        Log.d("me","number of itemes,.........= "+getCount()+"mlist"+ mList==null?"emtye":"notemty"+"count"+mList.size());
        hold.Text.setText(t.Name);

        mTexth.setPrefixText(hold.Text, t.Name, x);
        ib.DrawImageString(t.Name, hold.Img, mClip, t.Net);
        if (t.Phone.size() != 0)
            //   holder.Number.setText(t.Phone.get(0).Number);
            mTexth.setPrefixText(hold.Number, t.Phone.get(t.mNumberCount).getNumber(), x);
        if (t.PhotoThumbUri == null) {
            //hold.Img.setImageDrawable(null);
            hold.Img.setImageDrawable(null);
            ib.DrawImageString(t.Name, hold.Img, mClip, t.Phone.size()!=0?t.Phone.get(0).nNet.getValue():0);

        } else
            ib.GetPhoto(t, hold.Img, mClip,t.Phone.size()!=0?t.Phone.get(0).nNet.getValue():0);


        return view;

    }





    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                x = (String) constraint;
                List<Contact> y = new ArrayList<Contact>();
                if (constraint.length() == 0) {
                    y = mList;

                } else {

//if(constraint.length()>1){}
                    for (Contact contact : mList) {
                        if (contact.Name.contains(x))//||contact.Name.contains(x.replace('\u0627', '\u0623'))||contact.Name.contains(x.replace('\u0623','\u0627' )))


                        {
                            y.add(contact);

                        } else {
                            List<Contact.Phones> phone = contact.Phone;
                            for (int i = 0; i < phone.size(); i++) {

                                final Contact.Phones p = phone.get(i);
                                contact.mNumberCount = 0;
                                if (p.getCleanNumber().contains(x)) {
                                    y.add(contact);
                                    contact.mNumberCount = i;
                                    break;
                                }

                            }

// if(constraint.charAt(0)== '\u0627')
                            //    performFiltering(((String) constraint).replace('\u0627', '\u0623'));
                        }


                    }
                }
                FilterResults er = new FilterResults();
                er.values = y;
                er.count = y.size();

                return er;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                swic=(List<Contact>)results.values;
                notifyDataSetChanged();
            }


        };
    }


    static class Holders {
        public TextView Text;
        public LetterImageView Img;

        public TextView Number;

        public int pos;
    }
}

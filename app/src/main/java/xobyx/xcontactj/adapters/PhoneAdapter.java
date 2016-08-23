package xobyx.xcontactj.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.NumberOnClick;
import xobyx.xcontactj.until.NumberOnClick.NumberOnClickListener;

/**
 * Created by xobyx on 8/8/2015.
 * For xobyx.xcontactj.adapters/XContactj
 */
public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.NumHolder> {


    private static final int TYPE_ITEM = -1;
    private static final int TYPE_HEADER = -2;
    private  int worked_net;
    private List<Contact.Phones> list;
    private final LayoutInflater mInflater;

    private NumberOnClickListener numberOnClickListener;
    private View vlast;
    private boolean sectioned;
    private int mnet;


    public PhoneAdapter(Context var1, List<Contact.Phones> var2) {

        mInflater = LayoutInflater.from(var1);
        this.list = var2;

    }

    public PhoneAdapter(Context var1, List<Contact.Phones> var2,boolean section) {
        this.worked_net = MainActivity.WN_ID;

        mInflater = LayoutInflater.from(var1);
        this.list = var2;
        sectioned=section;
        Sort();
        if(sectioned)
        {
            PreperSection();
        }
    }

    private void Sort() {
        Collections.sort(list, new Comparator<Contact.Phones>() {
            @Override
            public int compare(Contact.Phones lhs, Contact.Phones rhs) {

                if (sectioned) {
                    return lhs.nNet.getValue() == worked_net ? -1 : rhs.nNet.getValue() == worked_net ? 1 : 0;
                }
               // return lhs.nNet.getValue() - rhs.nNet.getValue();
                return 0;
            }
        });
    }

    public void setList(List<Contact.Phones> i) {
        this.list = i;

        if(sectioned) {
            Sort();
            PreperSection();
        }
        notifyDataSetChanged();

    }

    protected void PreperSection() {

        int m=-1,mm=0;
        ArrayList<Contact.Phones> kf = null;
        for (Contact.Phones phones : list) {

            if(m!=phones.nNet.getValue()) {
              kf = new ArrayList<>();
                 m=phones.nNet.getValue();
                integerHashMap.put(mm,m);
                 mk.put(mm++,kf);


            }
            if (kf!=null)
            kf.add(phones);
        }
    }

    HashMap<Integer,Integer> integerHashMap=new HashMap<>();
    HashMap<Integer, List<Contact.Phones>> mk=new HashMap<>();

    int getSectionCount() {
      return mk.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (!sectioned)
            return super.getItemViewType(position);
        else {
            int j = 0;
            int count = getSectionCount();

            for (int m = 0; m < count; m++) {

                if (position == j)
                    return TYPE_HEADER;

                if (position > j && position < mk.get(m).size() +j+1) {
                    return TYPE_ITEM;
                }
                j += mk.get(m).size() + 1;

            }
            return 0;
        }


    }


    public int getCount() {
        if (!sectioned)
            return this.list.size();
        return getSectionCount() + list.size();
    }



    public Contact.Phones getItem(int var1) {
        return this.list.get(var1);
    }

    @Override
    public NumHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

       // if(getItemViewType(i)
        if(!sectioned)
        return getNumHolder();
        else
        {
            if(i==TYPE_ITEM)
           return getNumHolder();
            return  getHeaderHolder();
        }
    }

    private NumHolder getHeaderHolder() {
        View view = mInflater.inflate(R.layout.r_header, null);

        return new NumHolder(view,false);
    }

    @NonNull
    protected NumHolder getNumHolder() {
        View view = mInflater.inflate(R.layout.child_contact_detils, null);




        return new NumHolder(view,true);
    }

    Contact.Phones d(int pos)
    {
        int mmd = 0;
        int count1 = getSectionCount();

        for (int m = 0; m < count1; m++) {

//  0--(i0)--(i1)  ---1----(i0)
//  0-- 1 -- 2     ---3---- 4
               //3
          //  for (int i=0;i<mk.get(m).size())
                             // 3 <1+3+1
            if (pos > mmd && pos < mk.get(m).size() +mmd+1) {
                return mk.get(m).get(pos-mmd-1);
            }
            mmd += mk.get(m).size() + 1;

        }
        return null;
    }
    @Override
    public void onBindViewHolder(NumHolder numHolder, int pos) {
        Contact.Phones item;
        if (!sectioned) {
            item = list.get(pos);


            bindItem(numHolder, item);

        }
        else if(numHolder.header==null) {


                item=d(pos);
                bindItem(numHolder, item);




        }
        else
        {
            try {
               int m= getHeader(pos);
                bindHeader(numHolder, m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int getHeader(int position) throws Exception {
        int mmd = 0;
        int count1 = getSectionCount();

        for (int m = 0; m < count1; m++) {

            if (position == mmd)
                return m;


            mmd += mk.get(m).size() + 1;
        }
        throw new Exception("invaild index" );
    }



    protected void bindItem(NumHolder numHolder, Contact.Phones item) {
       // numHolder.net_image.setImageResource(ME.NetDrawables[item.nNet.getValue()][0]);
        SetupWidget(numHolder.hiden, item);
        numHolder.type.setText(item.Type);


        numHolder.more_d.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (vlast != null && !v.equals(vlast))
                    close(vlast);
                close(v);
            }
        });

        numHolder.number.setText(item.getNumber());


        if (numberOnClickListener != null) {
            numHolder.sms.setOnClickListener(new NumberOnClick(item, numberOnClickListener));
            numHolder.call_b.setOnClickListener(new NumberOnClick(item, numberOnClickListener));
        }
        s.bottomMargin=5;
        s.topMargin=5;
        numHolder.cont.setLayoutParams(s);
    }

    LinearLayout.MarginLayoutParams s=new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    protected void bindHeader(NumHolder numHolder, int item) {



        numHolder.header.setText(integerHashMap.get(item)==worked_net?"Your Network":ME.NET_N[integerHashMap.get(item)]);
        numHolder.net_image.setImageResource(ME.NetDrawables[integerHashMap.get(item)][0]);



    }
    public long getItemId(int var1) {
        return var1;
    }

Object k = new Object();

    @Override
    public int getItemCount() {

        return !sectioned? list.size():list.size()+getSectionCount();
    }


    private void close(View last) {

        synchronized (k) {
            LinearLayout s = (LinearLayout) ((LinearLayout) last.getParent()).findViewById(R.id.front);
            final View v2 = s.findViewById(R.id.back);
            final View v1 = s.findViewById(R.id.num_front);
            if (v2.getVisibility() == View.INVISIBLE) {

                v2.setVisibility(View.VISIBLE);
                v1.setVisibility(View.INVISIBLE);
            } else {
                v1.setVisibility(View.VISIBLE);
                v2.setVisibility(View.INVISIBLE);
            }
            vlast = last;
        }
    }

    private PhoneAdapter SetupWidget(final LinearLayout v, final Contact.Phones item) {
        final LinearLayout layout = (LinearLayout) v.getChildAt(0);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View L = layout.getChildAt(i);
            if (numberOnClickListener != null && L != null) {
                L.setOnClickListener(new NumberOnClick(item, numberOnClickListener));
            }
        }
        return this;
    }

    public PhoneAdapter SetF(NumberOnClickListener buttonsOnClick) {
        this.numberOnClickListener = buttonsOnClick;
        return this;
    }


    public void Add(Contact.Phones next) {
        list.add(next);


    }


    public class NumHolder extends RecyclerView.ViewHolder {
    public TextView number;
    public ImageButton call_b;
    public TextView type;
    public ImageView net_image;
    public ImageView more_d;
    public LinearLayout cont;

    public LinearLayout hiden;
    public TextView header;
    public ImageButton sms;
    public final View mView;

    public NumHolder(View s,boolean nor) {
        super(s);
        mView = s;
        if (nor) {
            call_b = (ImageButton) s.findViewById(R.id.d_call);
            hiden = (LinearLayout) s.findViewById(R.id.back);
            more_d = (ImageView) s.findViewById(R.id.d_more);
            number = (TextView) s.findViewById(R.id.d_number);
            cont= (LinearLayout) s.findViewById(R.id.to_anm);



            type = (TextView) s.findViewById(R.id.d_type);
            sms = (ImageButton) s.findViewById(R.id.d_sms);
        } else {
            header = (TextView) s.findViewById(android.R.id.text1);
        }
        net_image = (ImageView) s.findViewById(R.id.d_netimge);
    }
}
}

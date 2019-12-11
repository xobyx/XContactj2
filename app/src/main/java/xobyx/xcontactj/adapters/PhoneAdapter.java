package xobyx.xcontactj.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
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
    private  LayoutAnimationController jump_anim;
    //private  LayoutAnimationController salid_anim;
    private  int worked_net;
    private List<Contact.Phones> list;
    private final LayoutInflater mInflater;

    private NumberOnClickListener numberOnClickListener;
    private View vlast;
    private boolean sectioned;
    private int mnet;
    private boolean most_connected=false;


    public PhoneAdapter(Context var1, List<Contact.Phones> var2) {

        mInflater = LayoutInflater.from(var1);
        this.list = var2;
        inti_anim(var1);
    }

    public PhoneAdapter(Context var1, List<Contact.Phones> var2,boolean section) {
        this.worked_net = MainActivity.wn_id;

        mInflater = LayoutInflater.from(var1);
        this.list = var2;
        inti_anim(var1);
        sectioned=section;
        Sort();
        if(sectioned)
        {
            PreperSection();
        }
    }

    private void inti_anim(Context var1) {
        jump_anim = new LayoutAnimationController(AnimationUtils.loadAnimation(var1, R.anim.jump));
        //jump_anim.setInterpolator(new OvershootInterpolator());

        //salid_anim = new LayoutAnimationController(AnimationUtils.loadAnimation(var1, R.anim.abc_slide_in_bottom));
    }
    boolean Network_partitiones=true;
    private void Sort() {
        Collections.sort(list, new Comparator<Contact.Phones>() {
            @Override
            public int compare(Contact.Phones lhs, Contact.Phones rhs) {


                if (sectioned) {
                    if(Network_partitiones) {
                        return lhs.nNet.getValue() == worked_net ? -1 : rhs.nNet.getValue() == worked_net ? 1 : 0;
                    }
                    else
                    {

                        return  lhs.TimeConnected<rhs.TimeConnected?-1:1;
                    }
                }
               // return lhs.nNet.getValue() - rhs.nNet.getValue();
                return  lhs.TimeConnected<rhs.TimeConnected?-1:1;

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
        if(Network_partitiones) {
            int m = -1, mm = 0;
            ArrayList<Contact.Phones> kf = null;
            for (Contact.Phones phones : list) {

                if (m != phones.nNet.getValue()) {
                    kf = new ArrayList<>();
                    m = phones.nNet.getValue();
                    integerHashMap.put(mm, m);
                    mk.put(mm++, kf);


                }
                if (kf != null)
                    kf.add(phones);
            }
        }
        else if(most_connected){
            int i=0;
            while(i<list.size()&&list.get(i).TimeConnected==list.get(0).TimeConnected) {

                i++;
            }
            mk.put(0, list.subList(0, i));
            if(list.size()>i+1)
            mk.put(1, list.subList(i+1, list.size()-1));

            integerHashMap.put(0, 0);
            integerHashMap.put(1, 1);

        }
    }

    private SparseIntArray integerHashMap=new SparseIntArray();
    SparseArray<List<Contact.Phones>> mk= new SparseArray<>();

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

    void toggle_visibility(View g)
    {
        if(g.getVisibility()==View.INVISIBLE)
            g.setVisibility(View.VISIBLE);
        else
            g.setVisibility(View.INVISIBLE);
    }

    protected void bindItem(NumHolder numHolder, Contact.Phones item) {
       // numHolder.net_image.setImageResource(ME.NetDrawables[item.nNet.getValue()][0]);
        SetupWidget(numHolder.hiden, item);
        numHolder.type.setText(item.Type);


        numHolder.more_d.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ViewGroup widget_holder = getParentLayout(v);
                toggle_visibility(widget_holder);
                if (vlast != null && !v.equals(vlast))
                    (getParentLayout(vlast)).setVisibility(View.INVISIBLE);


                vlast=v;


                widget_holder.startLayoutAnimation();
                ((ViewGroup) widget_holder.getChildAt(0)).startLayoutAnimation();
            }
        });

        numHolder.number.setText(item.getNumber());


        if (numberOnClickListener != null) {
            numHolder.sms.setOnClickListener(new NumberOnClick(item, numberOnClickListener));
            numHolder.call_b.setOnClickListener(new NumberOnClick(item, numberOnClickListener));
        }

       // numHolder.cont.setLayoutParams(s);
    }


    protected void bindHeader(NumHolder numHolder, int item) {


        if(Network_partitiones) {
            numHolder.header.setText(integerHashMap.get(item) == worked_net ? "Your Network" : ME.NET_N[integerHashMap.get(item)]);
            numHolder.net_image.setImageResource(ME.NetDrawables[integerHashMap.get(item)][0]);
        }
        else if(most_connected) {
            numHolder.header.setText(integerHashMap.get(item) == 0 ? "Most Connected" : "Normal");
            //numHolder.net_image.setImageResource(ME.NetDrawables[integerHashMap.get(item)][0]);
        }


    }
    public long getItemId(int var1) {
        return var1;
    }



    @Override
    public int getItemCount() {

        return !sectioned? list.size():list.size()+getSectionCount();
    }



    private ViewGroup getParentLayout(View last) {
        return (ViewGroup) ((LinearLayout) last.getParent()).findViewById(R.id.front).findViewById(R.id.back);
    }

    private PhoneAdapter SetupWidget(final LinearLayout v, final Contact.Phones item) {
        final LinearLayout layout = (LinearLayout) v.getChildAt(0);

        layout.setLayoutAnimation(jump_anim);
         //v.setLayoutAnimation(salid_anim);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View L = layout.getChildAt(i);
            if (numberOnClickListener != null && L != null) {
                L.setOnClickListener(new NumberOnClick(item, numberOnClickListener));
            }
        }
        return this;
    }

    public void setNumberClickListener(NumberOnClickListener buttonsOnClick) {
        this.numberOnClickListener = buttonsOnClick;

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

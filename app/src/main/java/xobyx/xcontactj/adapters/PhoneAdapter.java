package xobyx.xcontactj.adapters;

import android.animation.LayoutTransition;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;

/**
 * Created by xobyx on 8/8/2015.
 * For xobyx.xcontactj.adapters/XContactj
 */
public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.NumHolder> {


    private final List<Contact.PhoneClass> list;
    private final LayoutInflater mInflater;

    private NumberOnClickListener numberOnClickListener;
    private View vlast;


    public PhoneAdapter(Context var1, List<Contact.PhoneClass> var2) {

        mInflater = LayoutInflater.from(var1);
        this.list = var2;
    }

    public int getCount() {
        return this.list.size();
    }

    public void AddAll(List<Contact.PhoneClass> i) {
        list.addAll(i);
        notifyDataSetChanged();
    }

    public Contact.PhoneClass getItem(int var1) {
        return this.list.get(var1);
    }

    @Override
    public NumHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view  = mInflater.inflate(R.layout.child_contact_detils, null);
        LayoutTransition l = new LayoutTransition();
        l.enableTransitionType(LayoutTransition.CHANGING);
        l.setAnimator(LayoutTransition.CHANGING,null);
        l.setDuration(500);
        ((LinearLayout) view.findViewById(R.id.to_anm)).setLayoutTransition(l);


        return new NumHolder(view);
    }

    @Override
    public void onBindViewHolder(NumHolder numHolder, int i) {
        final Contact.PhoneClass item = list.get(i);


        numHolder.net_image.setImageResource(ME.NetDrawables[item.nNet.getValue()][0]);
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

    }

    public long getItemId(int var1) {
        return 0L;
    }
    Object k=new Object();
    @Override
    public int getItemCount() {
        return list.size();
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

    private PhoneAdapter SetupWidget(final LinearLayout v, final Contact.PhoneClass item) {
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


    public void Add(Contact.PhoneClass next) {
        list.add(next);


    }

    public interface NumberOnClickListener {
        void onClick(int i, Contact.PhoneClass j);
    }

    private   class NumberOnClick implements View.OnClickListener {

        public NumberOnClickListener listener;
        private Contact.PhoneClass Number;


        public NumberOnClick(Contact.PhoneClass v, NumberOnClickListener listener1) {

            Number = v;
            listener = listener1;
        }


        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onClick(v.getId(), Number);
        }


    }

    public class NumHolder extends RecyclerView.ViewHolder {
        public final TextView number;
        public final ImageButton call_b;
        public final TextView type;
        public final ImageView net_image;
        public final ImageView more_d;

        public final LinearLayout hiden;

        public final ImageButton sms;
        public final View mView;

        public  NumHolder(View s)
        {
            super(s);
            mView = s;
            call_b = (ImageButton) s.findViewById(R.id.d_call);
            hiden = (LinearLayout) s.findViewById(R.id.back);
            more_d = (ImageView) s.findViewById(R.id.d_more);
            number = (TextView) s.findViewById(R.id.d_number);
            net_image = (ImageView) s.findViewById(R.id.d_netimge);
            type = (TextView) s.findViewById(R.id.d_type);
            sms = (ImageButton) s.findViewById(R.id.d_sms);
        }
    }
}

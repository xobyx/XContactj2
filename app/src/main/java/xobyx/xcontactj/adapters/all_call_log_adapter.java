package xobyx.xcontactj.adapters;

/**
 * Created by xobyx on 2/20/2016.
 * For xobyx.xcontactj.adapters/XContactj
 */

import android.content.Context;
import android.provider.CallLog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import xobyx.xcontactj.R;
import xobyx.xcontactj.base.Base_CallLog;


public class all_call_log_adapter extends BaseAdapter {

    public String Search = "";
    public String Search_c = "";
    private ArrayList<Base_CallLog> list;

    private ArrayList<Base_CallLog> list1;
    private CallLogFilter logFilter;
    private Context context;








    public all_call_log_adapter(Context var1, ArrayList<Base_CallLog> var3) {

        this.context = var1;
        this.list1 = var3;

        this.list = list1;
    }



    public Filter getFilter() {
        if (this.logFilter == null) {
            this.logFilter = new CallLogFilter(this);
        }

        return this.logFilter;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int var1, View var2, ViewGroup var3) {
        q var4;
        if (var2 == null) {
            var2 = ((LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.child_all_call_log, (ViewGroup) null);
            var4 = new q();
            var4.number = (TextView) var2.findViewById(R.id.number);
            var4.netdraw = (ImageView) var2.findViewById(R.id.call_log_net_drawable);
            var4.date = (TextView) var2.findViewById(R.id.date);
            var4.dur = (TextView) var2.findViewById(R.id.duration);
            var4.color = var2.findViewById(R.id.call_log_network);
            var4.call_type = (ImageView) var2.findViewById(R.id.imagecc);
            var4.imgkill = (ImageView) var2.findViewById(R.id.imgKill);
            var2.setTag(var4);
        } else {
            var4 = (q) var2.getTag();
        }

        final Base_CallLog obj = this.list.get(var1);
        if (obj.getName() != null
                && !obj.getName().isEmpty()) {
            var4.number.setText(obj.getName());
        } else if (obj.getNumber() != null) {
            var4.number.setText(obj.getNumber());

        }
        var4.color.setBackgroundColor(obj.getNetworkColor());
        var4.netdraw.setImageResource(obj.getNetDrawable());

        var4.dur.setText(obj.getDuration_str());
        if (DateUtils.isToday(obj.getDate_str().getTime()))
            var4.date.setText("Today " + format.format(obj.getDate_str()));
        else
            var4.date.setText(format.format(obj.getDate_str()));

        var4.call_type.setImageResource(obj.getDrawableRes());
        var4.imgkill.setOnClickListener(new f(this, var1));
        return var2;
    }
    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
    int type=0;


    public void setLogType(int position) {

        if(type==position)return;
        type=position;
        if (position == 0) {
            list=list1;

            notifyDataSetChanged();
            return;
        }
        ArrayList<Base_CallLog> u = new ArrayList<>();
        for (Base_CallLog n : list1) {
            if (n.getType() == position)
                u.add(n);


        }
        list=u;
        notifyDataSetChanged();

    }

    class f implements View.OnClickListener {


        final int index;

        final all_call_log_adapter b;


        f(all_call_log_adapter var1, int var2) {
            this.b = var1;
            this.index = var2;
        }

        public void onClick(View var1) {
            try {
                b.context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_ID = " + ((Base_CallLog) b.list.get(this.index)).getID(), (String[]) null);
                //((MenuFragments)e.b(this.b)).a(((fragment_all_call_log.CallLogObj) b.getList().get(this.index)).getDuration_int());
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            b.list.remove(this.index);
            b.notifyDataSetChanged();
        }
    }

    class CallLogFilter extends Filter {


        final all_call_log_adapter logAdapter;


        private CallLogFilter(all_call_log_adapter var1) {
            this.logAdapter = var1;
        }


        private String clean(String var1) {
            return var1.replaceAll("[Ááâã]", "a").replaceAll("[Čč]", "c").replaceAll("[Ďď]", "d").replaceAll("[Ééě]", "e").replaceAll("[Íí]", "i").replaceAll("[ĽĹľĺ]", "l").replaceAll("[Ňň]", "n").replaceAll("[Óóô]", "o").replaceAll("[Ŕŕř]", "r").replaceAll("[Ššś]", "s").replaceAll("[Úú]", "u").replaceAll("[Ýý]", "y").replaceAll("[Žž]", "z");
        }

        protected FilterResults performFiltering(CharSequence var1) {
            Search = var1.toString();
            Search_c = this.clean(var1.toString());
            FilterResults results = new FilterResults();
            if (Search != null && Search.length() != 0) {
                ArrayList<Base_CallLog> arrayList = new ArrayList<>();

                for (Base_CallLog var6 : list1) {
                    if (var6.getNumber().toLowerCase(Locale.getDefault()).contains(Search.toLowerCase()) && (type == 0 || var6.getType() == type)) {
                        arrayList.add(var6);
                    } else if (var6.getNumber().toLowerCase(Locale.getDefault()).contains(Search_c.toLowerCase()) && (type == 0 || var6.getType() == type)) {
                        arrayList.add(var6);
                    } else if (var6.getName().toLowerCase(Locale.getDefault()).contains(Search.toLowerCase()) && (type == 0 || var6.getType() == type)) {
                        arrayList.add(var6);
                    } else if (var6.getName().toLowerCase(Locale.getDefault()).contains(Search_c.toLowerCase()) && (type == 0 || var6.getType() == type)) {
                        arrayList.add(var6);
                    }



                }
                results.values = arrayList;
                results.count = arrayList.size();
            } else {
                list=list1;
                results.values = list;
                results.count = list.size();
            }

            return results;
        }

        protected void publishResults(CharSequence var1, FilterResults results) {
            int var3 = 0;
            if (results.count == 0 && Search.length() != 0) {

                notifyDataSetChanged();


            } else if (results.count == 0) {

                list = list1;
                notifyDataSetChanged();

            } else {

                list = (ArrayList<Base_CallLog>) results.values;
                notifyDataSetChanged();



            }

        }
    }

    public class q {

        public ImageView call_type;
        public ImageView imgkill;
        public ImageView netdraw;
        public TextView number;
        public TextView dur;
        public TextView date;
        public View color;


    }
}

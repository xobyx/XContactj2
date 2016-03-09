package xobyx.xcontactj.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xobyx.xcontactj.fragments.AdapterHelp;

/**
 * Created by xobyx on 2/23/2016.
 * For xobyx.xcontactj.adapters/XContactj
 */
public abstract class BaseRecycleAdapter<T extends RecyclerView.ViewHolder, F> extends RecyclerView.Adapter<T> implements AdapterHelp<T, F> {

    private List<F> objects;
    private int layout;


    private Context context;
    private LayoutInflater layoutInflater;

    private List<F> pak;

    public BaseRecycleAdapter(Context context, List<F> s, int layout) {
        this.context = context;
        pak = s;
        this.layout = layout;

        objects = pak;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public T onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(layout, null);
        return onCreateViewHolder(view,i);


    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        onBindViewHolder(holder, objects.get(position));
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    protected FilterBuilder<F> Filter;
/**

make active List source {@link #objects}  to constructor argument {@link #pak}

 */
    protected void RestartFilter()
    {
        objects=pak;
        notifyDataSetChanged();
    }
    protected void StartFilter()
    {
        ArrayList<F> d=new ArrayList<>();
        for (F f : pak) {
            if(Filter.IsMatch(f))
            {
                d.add(f);
            }

        }
        objects=d;
        notifyDataSetChanged();
    }





    @Override
    public abstract void onBindViewHolder(T holder, F item);

    @Override
    public abstract T onCreateViewHolder(View view,int position);


    protected interface FilterBuilder<F>
    {


        boolean IsMatch(F b);
    }
}

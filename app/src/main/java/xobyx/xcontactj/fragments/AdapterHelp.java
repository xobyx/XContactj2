package xobyx.xcontactj.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xobyx on 2/23/2016.
 * For xobyx.xcontactj.fragments/XContactj
 */
public  interface AdapterHelp<T extends RecyclerView.ViewHolder, M> {


     void onBindViewHolder(T holder, M item);

     T onCreateViewHolder(View view,int position);


}

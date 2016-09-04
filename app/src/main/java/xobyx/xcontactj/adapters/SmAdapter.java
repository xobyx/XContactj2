package xobyx.xcontactj.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by xobyx on 9/1/2016.
 * For xobyx.xcontactj.adapters/XContactj2
 */
public class SmAdapter  {

   private class Sm_data {
        inflater mInflater;
        private List<String> data;
        private int layout;
        Context ma;




    }

    public SmAdapter setInflater(inflater m) {


        build.mInflater=m;
        return this;
    }
    public BaseAdapter BuildAdapter()
    {
        if(this.build.mInflater!=null)
            return new  m();
        else
            return null;

    }
    private Sm_data build;
    public  SmAdapter newInstance(Context m) {

        build =new Sm_data();
        build.ma=m;
        return this;
    }
    private class m extends BaseAdapter
    {


        @Override
        public int getCount() {
            return build.data.size();
        }

        @Override
        public Object getItem(int position) {
            return build.data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(build.ma).inflate(build.layout, null);

            for (int i = 0; i <((ViewGroup) v).getChildCount() ; i++) {
                build.mInflater.inflate(((ViewGroup) v).getChildAt(i),position,getItem(position));
            }

            return v;
        }
    }

    public SmAdapter SetupLayout( int Layout)
    {

        build.layout = Layout;
        return this;
    }
    public SmAdapter SetupItems(int String_array_res)
    {
        if(build.ma!=null)

        build.data= Arrays.asList(build.ma.getResources().getStringArray(String_array_res));

        return this;
    }


    public interface inflater
    {
         void inflate(View n, int item, Object o);
    }
}

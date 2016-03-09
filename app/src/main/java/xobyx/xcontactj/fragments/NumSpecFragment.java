package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.adapters.PhoneAdapter;
import xobyx.xcontactj.until.Contact;

import static xobyx.xcontactj.until.ME.$;

public class NumSpecFragment extends Fragment  {


    private static final String ARG_NET = "net";
    private static final java.lang.String ARG_POS = "pos";
    private static final String ARG_ALL = "all";
    public List<Contact.PhoneClass> other;
    private int mNet;
    private int mPos;
    private NumbSpecAdapter ba;
    private TabLayout pageIndicator;
    private boolean mOther;
    private Contact contact;
    private widget_manger widgetManger;
    private boolean mAll;

    {
        onDestroyOptionsMenu();
    }

    public static NumSpecFragment newInstance(int pos, int net, boolean all) {
        NumSpecFragment var3 = new NumSpecFragment();
        Bundle var4 = new Bundle();
        var4.putInt(ARG_POS, pos);
        var4.putBoolean(ARG_ALL, all);

        var4.putInt(ARG_NET, net);
        var3.setArguments(var4);
        return var3;
    }


    public void onAttach(Activity var1) {
        super.onAttach(var1);
    }

    @Override
    public void onViewCreated(View var1, @Nullable Bundle var2) {
        super.onViewCreated(var1, var2);
      //  final ViewPager vb = (ViewPager) var1.findViewById(R.id.d_viewpager);
       // ba = new NumbSpecAdapter(getChildFragmentManager(), 1);
       // pageIndicator = (android.support.design.widget.TabLayout) var1.findViewById(R.id.d_tabindector);
        //vb.setAdapter(ba);

       // ((ListView) var1.findViewById(R.id.example_lv_list)).setAdapter( new PhoneAdapter(this.getActivity(), contact.Phone).SetF(widgetManger));

        // pageIndicator.setupWithViewPager(vb);
       // ME.getOtherAsync(mNet, mPos, new ME.IAsyncFinish() {
        //    @Override
        //    public void LoadFinnish(List<ME.Contact.PhoneClass> data ) {
          //      if (data != null && data.size() != 0) {
            //        ba.AddOtherPage();
              //      pageIndicator.addTab(pageIndicator.newTab().setText("Other"));
                //    other = data;
               // }
            //}
        ///});


    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);

        if (this.getArguments() != null) {

            this.mNet = this.getArguments().getInt(ARG_NET);
            this.mPos = this.getArguments().getInt(ARG_POS);
            mAll=this.getArguments().getBoolean(ARG_ALL);




                contact =!mAll? (Contact) $[mNet].get(mPos):fragment_all_phones.mList.get(mPos);
                widgetManger =new widget_manger(this.getActivity(),contact);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rv = inflater.inflate(R.layout.fragment_numspecpage, null);
        RecyclerView mv = (RecyclerView) rv.findViewById(R.id.example_lv_list);

        setupRecyclerView(mv);
        return  rv;

    }

    private void setupRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new PhoneAdapter(rv.getContext(),contact.Phone).SetF(widgetManger));

    }


    public void onDetach() {
        super.onDetach();

    }


    public class NumbSpecAdapter extends FragmentPagerAdapter {


        private int mCount;

        public NumbSpecAdapter(FragmentManager var1, int page_count) {
            super(var1);
            mCount = page_count;
        }

        public void AddOtherPage() {
            mCount = 2;
            this.notifyDataSetChanged();
        }

        public int getCount() {
            return this.mCount;
        }

        public Fragment getItem(int var1) {
            return var1 == 0 ? NumSpecPageFragment.NewInstence(mPos, mNet, false) :
                    NumSpecPageFragment.NewInstence(mPos, mNet, true);

        }


        public CharSequence getPageTitle(int var1) {
            return var1 == 0 ? MainActivity.Network.values()[mNet].toString() : "Othor";
        }
    }


}

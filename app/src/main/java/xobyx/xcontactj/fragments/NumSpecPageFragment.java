package xobyx.xcontactj.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.adapters.PhoneAdapter;
import xobyx.xcontactj.until.ME;

import static xobyx.xcontactj.until.ME.$;

import xobyx.xcontactj.until.Contact;
import static xobyx.xcontactj.until.Contact.PhoneClass;


/**
 * A simple {@link Fragment} subclass.
 */
public class NumSpecPageFragment extends Fragment {


    private static final String ARG_NET = "net";
    private static final String ARG_POS = "rpos";
    private static final String ARG_OTHER = "other";
    PhoneAdapter adapter;
    private int mNet;
    private int mPos;
    private boolean mOther;
    private Contact contact;
    private widget_manger widgetManger;


    public NumSpecPageFragment() {
        // Required empty public constructor
    }

    public static NumSpecPageFragment NewInstence(int pos, int net, boolean other) {
        NumSpecPageFragment p = new NumSpecPageFragment();
        Bundle a = new Bundle();
        a.putInt(ARG_NET, net);
        a.putInt(ARG_POS, pos);
        a.putBoolean(ARG_OTHER, other);
        p.setArguments(a);
        return p;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_numspecpage, null);
        setupRecyclerView(rv);
        return  rv;

    }

    private void setupRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(new PhoneAdapter(rv.getContext(),contact.Phone));
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        final Bundle bn = getArguments();
        if (bn != null) {

            mNet = bn.getInt(ARG_NET);
            mPos = bn.getInt(ARG_POS);
            mOther = bn.getBoolean(ARG_OTHER);
            contact = (Contact) $[mNet].get(mPos);
            widgetManger =new widget_manger(this.getActivity(),contact);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final ListView mListView = (ListView) view.findViewById(R.id.example_lv_list);
        if (!mOther) {

            adapter = new PhoneAdapter(this.getActivity(), contact.Phone);
            adapter.SetF(widgetManger);
           // mListView.setAdapter(adapter);

        } else {


            adapter = new PhoneAdapter(getActivity(), new ArrayList<PhoneClass>());
            adapter.SetF(widgetManger);
           // mListView.setAdapter(adapter);
            ME.getOtherAsync(mNet, mPos, new ME.IAsyncFinish() {
                @Override
                public void LoadFinnish(List<PhoneClass> r) {
                    if (r != null)
                        adapter.AddAll(r);
                }
            });

        }
mListView.getParent().requestLayout();
        mListView.getParent().getParent().getParent().getParent().requestLayout();
        //mListView.


    }


}

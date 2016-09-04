package xobyx.xcontactj.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.AllContactSpecificsActivity;
import xobyx.xcontactj.adapters.AllContactsAdapter;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ContactLoader;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.until.XPickDialog;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by xobyx on 1/23/2016.
 * For xobyx.xcontactj.fragments/XContactj
 */
public class NetFragmentAll extends NetFragment {
    private char mLastChar;







    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.all_phone_fragment, menu);
        ((SearchView) menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                try {
                    SearchFor(newText);
                    //  mAdapter...getFilter().filter(newText);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {
        return new ContactLoader(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {

        // list_mode = SettingHelp.getListMode(getActivity().getBaseContext());
        // return list_mode == 0 ? var1.inflate(R.layout.fragment_net_list, null) :
        //       var1.inflate(R.layout.fragment_net_grid, null);
        return var1.inflate(R.layout.fragment_all_phones, null);
    }

    @Override
    public void onViewCreated(View var1, @Nullable Bundle var2) {
        super.onViewCreated(var1, var2);

        // xz = (AZSideBar) var1.findViewById(R.id.dtest);
        // xz.setOnItemTouchListener(this);
        // Me.setOnScrollListener(xz);


        if (getDefaultSharedPreferences(this.getActivity()).getBoolean(getString(R.string.key_enable_list_anim), false))
            listView.setLayoutAnimation(getGridLayoutAnim());
        else
            listView.setLayoutAnimation(null);


    }

    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);

        setHasOptionsMenu(true);
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        ME.setAllList(data);
        mList= ME.getAllList();
        if (mList.size() != 0)
            mStateView.showContent();
        else
            mStateView.showEmpty();

        Collections.sort(mList);
        mShowNumber = SettingHelp.getShowNumb(getActivity().getBaseContext());



        mAdapter = new AllContactsAdapter(getActivity(), mList, SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);


        if (listView != null) {
            setupPinndHeader(true);

            listView.setAdapter(mAdapter);

        }


    /*    m=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                lableIndex = new Hashtable<String, Integer>();

                mLastChar = mList.get(0).Name.charAt(0);
                int t = 0;
                String oldl = "";
                for (int i = 0; i < mList.size(); i++) {
                    Contact b = mList.get(i);


                    String tn = b.Name.substring(0, 1);
                    if (!tn.equals(oldl)) {
                        lableIndex.put(tn, i);
                        oldl = tn;

                        xz.CharsIndex.add(tn);

                    }
                    t += b.Phone.size();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                xz.invalidate();
                xz.mfinishAdapter = true;
                super.onPostExecute(o);


            }


        };
        m.execute(new String[]{});*/
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        try {
            final Contact d = mAdapter.getItem(position);
            if (!this.iDialer.getDialerState()) {
                Intent p = new Intent(this.getActivity(), AllContactSpecificsActivity.class);

                p.putExtra("pos", mList.indexOf(d));

                startActivity(p);
                getActivity().overridePendingTransition(android.support.design.R.anim.abc_slide_in_top, android.support.design.R.anim.abc_slide_out_bottom);
            }
            else {
                if (d.Phone.size() > 1) {
                    final XPickDialog instance = XPickDialog.Instance(getActivity());
                    instance.SetOnFinish(new XPickDialog.OnFinish() {
                        @Override
                        public void DialogFinish(int id, boolean isDefault) {
                            iDialer.getDialerAction().setNumber(d.Phone.get(id).getNumber(), true);
                        }
                    }).setListPhoneList(d.Phone).build().show();
                }
                else {
                    iDialer.getDialerAction().setNumber(d.Phone.get(0).getNumber(), true);
                }
            }
        }
        catch (Exception d) {
            d.printStackTrace();
        }

    }


    public static Fragment cv() {
        return new NetFragmentAll();
    }
}
//margelist
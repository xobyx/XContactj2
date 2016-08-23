package xobyx.xcontactj.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import xobyx.xcontactj.activities.ContactSpecificsActivity;
import xobyx.xcontactj.activities.MainActivity;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ContactLoader;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.until.XPickDialog;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by xobyx on 1/23/2016.
 * For xobyx.xcontactj.fragments/XContactj
 */
public class fragment_all_phones extends NetFragment {
    private char mLastChar;
    public static ArrayList<Contact> mList;



    private AsyncTask m;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.all_phone_fragment,menu);
        ((SearchView) menu.findItem(R.id.action_search).getActionView()).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter(newText);
                try {
                  //  mAdapter...getFilter().filter(newText);
                } catch (Exception e) {
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
        this.getLoaderManager().initLoader(net, null, this);
       // list_mode = SettingHelp.getListMode(getActivity().getBaseContext());
       // return list_mode == 0 ? var1.inflate(R.layout.fragment_net_list, null) :
         //       var1.inflate(R.layout.fragment_net_grid, null);
        return  var1.inflate(R.layout.fragment_all_phones, null);
    }

    @Override
    public void onViewCreated(View var1, @Nullable Bundle var2) {
        super.onViewCreated(var1, var2);

       // xz = (AZSideBar) var1.findViewById(R.id.dtest);
       // xz.setOnItemTouchListener(this);
       // Me.setOnScrollListener(xz);


        if (getDefaultSharedPreferences(this.getActivity()).getBoolean(getString(R.string.key_enable_list_anim), false))
            mlist.setLayoutAnimation(getGridLayoutAnim());
        else
            mlist.setLayoutAnimation(null);





    }

    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);

        setHasOptionsMenu(true);
    }

    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {
        mList = new ArrayList<>(data);
        // m.
        Collections.sort(mList);
        mShowNumber = SettingHelp.getShowNumb(getActivity().getBaseContext());
        if (mShowNumber && list_mode == 0) {
            //mAdapter = new ContactNumberAdapter(getActivity().getBaseContext(), mList);
            //((ContactNumberAdapter)mAdapter).setShow_net_icon(true);
            mAdapter=new ContactsAdapter(getActivity(),mList,SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);

        }
        else
            mAdapter=new ContactsAdapter(getActivity(),mList,SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);

        // mAdapter = new ContactBaseAdapter(getActivity().getBaseContext(), mList, list_mode);

       // mAdapter.mClip = SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0;





        if(mlist !=null)
        {
            setupPinndHeader(true);

            mlist.setAdapter(mAdapter);
            mLoading.hide();
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
            final Contact d = ((Contact) mAdapter.getItem(position));
            if(!this.iDialer.getDialerState()) {
                Intent p = new Intent(this.getActivity(), ContactSpecificsActivity.class);

                p.putExtra("pos", mList.indexOf(d));
                p.putExtra("net", MainActivity.WN_ID);
                p.putExtra("all",true);
                startActivity(p);
            }
            else
            {
                if (d.Phone.size()>1)
                {
                    final XPickDialog instance = XPickDialog.Instance(getActivity());
                    instance.SetOnFinish( new XPickDialog.OnFinish() {
                        @Override
                        public void DialogFinish(int id, boolean isDefault) {
                            iDialer.getDialerAction().setNumber(d.Phone.get(id).getNumber(), true);
                        }
                    }).setListPhoneList(d.Phone).build().show();
                }
                else
                {
                    iDialer.getDialerAction().setNumber(d.Phone.get(0).getNumber(),true);
                }
            }
        } catch (Exception d) {

        }

    }


    public static Fragment cv() {
        return new fragment_all_phones();
    }
}
//margelist
package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.ContactSpecificsActivity;
import xobyx.xcontactj.adapters.ContactsAdapter;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.NetworkContactLoader;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.until.XPickDialog;
import xobyx.xcontactj.until.scale_animation;
import xobyx.xcontactj.views.LetterImageView;
import xobyx.xcontactj.views.ListViewH.PinnedHeaderListView;
import xobyx.xcontactj.views.StateView;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static xobyx.xcontactj.until.SettingHelp.getListMode;

/**
 * Created by xobyx on 12/2/2014.
 * c# to java
 */
public class NetFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Contact>>
        , AbsListView.OnItemClickListener {
    /**
     * The fragment argument representing the section Number for this
     * fragment.
     */
    public ArrayList<Contact> mList;
    protected static final String ARG_SECTION_NUMBER = "section_number";

    static int list_mode = 0;
    static boolean mShowNumber = false;
    public AbsListView listView;
    int net;
    ContactsAdapter mAdapter;
    protected DialerFragment.DialerHandler iDialer;
    public StateView mStateView;
    // public ContentLoadingProgressBar mLoading;

    /**
     * Returns TouchedItem new instance of this fragment for the given section
     * Number.
     */

    public static NetFragment newInstance(int sectionNumber) {
        NetFragment fragment = new NetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static LayoutAnimationController getGridLayoutAnim() {
        LayoutAnimationController controller;
        //Animation anim = new AppAnimations.Rotate3dAnimation(90, 0, 0, 0, 0, true);
        Animation anim = new scale_animation();
        anim.setDuration(500);
        controller = new LayoutAnimationController(anim, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }


    @Override
    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {


        return list_mode == 0 ? var1.inflate(R.layout.fragment_net_list, null) :
                var1.inflate(R.layout.fragment_net_list, null);//var1.inflate(R.layout.fragment_net_grid, null);
    }

    @Override
    public void onViewCreated(View var1, @Nullable Bundle var2) {

        mStateView = StateView.inject(var1);
        mStateView.showLoading();
        listView = (AbsListView) var1.findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
        //  mLoading=(ContentLoadingProgressBar)var1.findViewById(R.id.mloading);
        ///  mLoading.show();


        if (getDefaultSharedPreferences(this.getActivity()).getBoolean(getString(R.string.key_enable_list_anim), false))
            listView.setLayoutAnimation(getGridLayoutAnim());
        else
            listView.setLayoutAnimation(null);


    }


    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);

        ///// FIXME: 2/27/2016  Add Custom SearchView ...
        //setHasOptionsMenu(false);
        if (this.getArguments() != null && this.getArguments().containsKey(ARG_SECTION_NUMBER))
            net = this.getArguments().getInt(ARG_SECTION_NUMBER, 0);


        list_mode = getListModeForFragment();
        this.getLoaderManager().initLoader(net, null, this);


    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.iDialer = ((DialerFragment.DialerHandler) activity);

    }

    protected int getListModeForFragment() {
        return getListMode(getActivity().getBaseContext());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        try {
            final Contact d = ((Contact) mAdapter.getItem(position));
            if (!iDialer.getDialerState()) {
                Intent p = new Intent(this.getActivity(), ContactSpecificsActivity.class);

                p.putExtra("pos", ME.$[net].indexOf(d));
                p.putExtra("net", net);
                startActivity(p);
                getActivity().overridePendingTransition(android.support.design.R.anim.abc_slide_in_top, android.support.design.R.anim.abc_slide_out_bottom);
            } else {
                if (d.Phone.size() > 1) {
                    final XPickDialog instance = XPickDialog.Instance(getActivity());
                    instance.SetOnFinish(new XPickDialog.OnFinish() {
                        @Override
                        public void DialogFinish(int id, boolean isDefault) {
                            iDialer.getDialerAction().setNumber(d.Phone.get(id).getNumber(), true);
                        }
                    }).setListPhoneList(d.Phone).build().show();
                } else {
                    iDialer.getDialerAction().setNumber(d.Phone.get(0).getNumber(), true);
                }
            }
        } catch (Exception d) {

        }

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // this.Me.setAdapter(mAdapter);



    }

    @Override
    public Loader<List<Contact>> onCreateLoader(int id, Bundle args) {


        return new NetworkContactLoader(this.getActivity(), net);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void SearchFor(String Qu)  {

        mAdapter.getFilter().filter(Qu);


    }

    @SuppressWarnings("unchecked assignment")
    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {


        if(data.size()!=0)mStateView.showContent();
        else mStateView.showEmpty();
        Collections.sort(data);
        ME.$[net].clear();ME.$[net].addAll(data);
        mList=ME.$[net];


        mShowNumber = SettingHelp.getShowNumb(getActivity().getBaseContext());

        mAdapter = new ContactsAdapter(getActivity(), mList, SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);


        //  mAdapter.mClip = SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0;


        if (listView != null) {

            // int pinnedHeaderBackgroundColor=getResources().getColor(getResIdFromAttribute(this,android.R.attr.colorBackground));
            setupPinndHeader(false);
            listView.setAdapter(mAdapter);
//           mLoading.hide();


            // Me.setAdapter(mAdapter);

        }


        //mAdapter.notifyDataSetChanged();


    }

    protected void setupPinndHeader(boolean all) {
        mAdapter.setPinnedHeaderBackgroundColor(getResources().getColor(R.color.holo_white));
        mAdapter.setPinnedHeaderTextColor(!all ? ME.nColors2[net] : ME.nColors2[3]);//getResources().getColor(R.color.holo_black));
        ((PinnedHeaderListView) listView).setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.pinned_header_listview_side_header, listView, false));
        ((PinnedHeaderListView) listView).setDivider(null);
        listView.setFastScrollEnabled(true);
        listView.setOnScrollListener(mAdapter);
        ((PinnedHeaderListView) listView).setEnableHeaderTransparencyChanges(true);
    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
        //
        // // FIXME: 2/26/2016 This Make list emtye without reload it..{@link loader}
        // ME.$[net].clear();
        loader = null;
    }


    // /////////////////////////////////////////////////////////////////////////////////////
    // ViewHolder //
    // /////////////
    public static class ViewHolder {
        public TextView Text;
        public LetterImageView Img;

        public TextView Number,
                headerView;


        public int pos;
    }

}

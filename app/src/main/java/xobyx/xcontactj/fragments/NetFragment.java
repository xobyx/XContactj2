package xobyx.xcontactj.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
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
import java.util.Locale;

import xobyx.xcontactj.R;
import xobyx.xcontactj.activities.ContactSpecificsActivity;
import xobyx.xcontactj.adapters.TextHighlighter;
import xobyx.xcontactj.until.AsyncDrawer;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.until.NetworkContactLoader;
import xobyx.xcontactj.until.SettingHelp;
import xobyx.xcontactj.until.XPickDialog;
import xobyx.xcontactj.until.scale_animation;
import xobyx.xcontactj.views.LetterImageView;
import xobyx.xcontactj.views.ListViewH.PinnedHeaderListView;
import xobyx.xcontactj.views.ListViewH.SearchablePinnedHeaderListViewAdapter;
import xobyx.xcontactj.views.ListViewH.StringArrayAlphabetIndexer;

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
    protected static final String ARG_SECTION_NUMBER = "section_number";

    static int list_mode = 0;
    static boolean mShowNumber = false;
    public AbsListView Me;
    int net;
    ContactsAdapter mAdapter;
    protected DialerFragment.DialerHandler iDialer;

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
        Animation anim=new scale_animation();
        anim.setDuration(500);
        controller = new LayoutAnimationController(anim, 0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }


    @Override
    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, @Nullable Bundle var3) {


        return list_mode == 0 ? var1.inflate(R.layout.fragment_net_list, null) :
                var1.inflate(R.layout.fragment_net_grid, null);
    }

    @Override
    public void onViewCreated(View var1, @Nullable Bundle var2) {

        Me = (AbsListView) var1.findViewById(android.R.id.list);
        Me.setOnItemClickListener(this);


        if (getDefaultSharedPreferences(this.getActivity()).getBoolean(getString(R.string.key_enable_list_anim), false))
            Me.setLayoutAnimation(getGridLayoutAnim());
        else
            Me.setLayoutAnimation(null);





    }



    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);

        ///// FIXME: 2/27/2016  Add Custom SearchView ...
        setHasOptionsMenu(false);
if(this.getArguments()!=null&&this.getArguments().containsKey(ARG_SECTION_NUMBER))
        net = this.getArguments().getInt(ARG_SECTION_NUMBER, 0);


        list_mode = getListModeForFragment();
        this.getLoaderManager().initLoader(net, null, this);



    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.iDialer = ((DialerFragment.DialerHandler) activity);

    }

    protected  int getListModeForFragment()
    {
        return getListMode(getActivity().getBaseContext());
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        try {
            final Contact d = ((Contact) mAdapter.getItem(position));
            if(!iDialer.getDialerState()) {
                Intent p = new Intent(this.getActivity(), ContactSpecificsActivity.class);

                p.putExtra("pos", ME.$[net].indexOf(d));
                p.putExtra("net", net);
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

    public void SearchFor(String Qu) throws Exception {

        mAdapter.getFilter().filter(Qu);


    }
    @SuppressWarnings("unchecked assignment")
    @Override
    public void onLoadFinished(Loader<List<Contact>> loader, List<Contact> data) {

        Collections.sort(data);
        ME.$[net].clear();
        ME.$[net].addAll(data);
        mShowNumber = SettingHelp.getShowNumb(getActivity().getBaseContext());
        if (mShowNumber && list_mode == 0) {
          //  mAdapter = new ContactNumberAdapter(getActivity().getBaseContext(), ME.$[net]);
            mAdapter=new ContactsAdapter(getActivity(),ME.$[net],SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);
        }
        else {
           // mAdapter = new ContactBaseAdapter(getActivity().getBaseContext(), ME.$[net], list_mode);

            mAdapter=new ContactsAdapter(getActivity(),ME.$[net],SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0);

        }


      //  mAdapter.mClip = SettingHelp.getPhotoMode(getActivity().getBaseContext()) == 0;


        if(Me!=null)
        {

           // int pinnedHeaderBackgroundColor=getResources().getColor(getResIdFromAttribute(this,android.R.attr.colorBackground));
            mAdapter.setPinnedHeaderBackgroundColor(getResources().getColor(R.color.holo_white));
            mAdapter.setPinnedHeaderTextColor(getResources().getColor(R.color.holo_black));
            ((PinnedHeaderListView) Me).setPinnedHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.pinned_header_listview_side_header, Me, false));
            ((PinnedHeaderListView) Me).setDivider(null);
            Me.setAdapter(mAdapter);

            Me.setFastScrollEnabled(true);
            Me.setOnScrollListener(mAdapter);
            ((PinnedHeaderListView) Me).setEnableHeaderTransparencyChanges(false);
           // Me.setAdapter(mAdapter);

        }



         //mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader<List<Contact>> loader) {
      //
      // // FIXME: 2/26/2016 This Make list emtye without reload it..{@link loder}
      // ME.$[net].clear();
     loader=null;
    }

    public class ContactsAdapter extends SearchablePinnedHeaderListViewAdapter<Contact>
    {
        private final TextHighlighter mTexth;
        private final LayoutInflater mInflater;
        private final AsyncDrawer ib;
        private ArrayList<Contact> mContacts;
        private String x;
        private boolean mClip;
        //private final int CONTACT_PHOTO_IMAGE_SIZE;
        //private final int[] PHOTO_TEXT_BACKGROUND_COLORS;


        @Override
        public CharSequence getSectionTitle(int sectionIndex)
        {
            return ((StringArrayAlphabetIndexer.AlphaBetSection)getSections()[sectionIndex]).getName();
        }

        public ContactsAdapter(Context con,final ArrayList<Contact> contacts,boolean mClisp)
        {
            mClip=mClisp;
            setData(contacts);
            ib = new AsyncDrawer(con);
            mTexth = new TextHighlighter(Typeface.BOLD, 0xff02a7dd);
            mInflater = LayoutInflater.from(con);
        //    PHOTO_TEXT_BACKGROUND_COLORS=getResources().getIntArray(R.array.contacts_text_background_colors);
          //  CONTACT_PHOTO_IMAGE_SIZE=getResources().getDimensionPixelSize(
            //        R.dimen.list_item__contact_imageview_size);
        }

        public void setData(final ArrayList<Contact> contacts)
        {
            this.mContacts=contacts;
            final String[] generatedContactNames=generateContactNames(contacts);
            setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames,true));
        }

        private String[] generateContactNames(final List<Contact> contacts)
        {
            final ArrayList<String> contactNames=new ArrayList<String>();
            if(contacts!=null)
                for(final Contact contactEntity : contacts)
                    contactNames.add(contactEntity.Name);
            return contactNames.toArray(new String[contactNames.size()]);
        }

        @Override
        public View getView(final int position,final View convertView,final ViewGroup parent)
        {
            final ViewHolder holder;
            final View rootView;
            if(convertView==null)
            {
                holder=new ViewHolder();
                rootView= mInflater.inflate(R.layout.child_main, null);
                holder.Text = (TextView) rootView.findViewById(R.id.ptextname);
                holder.Number = (TextView) rootView.findViewById(R.id.numberc);
                holder.Img = (LetterImageView) rootView.findViewById(R.id.imagec);
                holder.headerView=(TextView)rootView.findViewById(R.id.header_text);
                holder.pos = position;
                rootView.setTag(holder);
            }
            else
            {
                rootView=convertView;
                holder=(ViewHolder)rootView.getTag();
            }
            final Contact contact=getItem(position);
            final String displayName=contact.Name;

            holder.Text.setText(contact.Name);

            mTexth.setPrefixText(holder.Text, contact.Name, x);
            ib.DrawImageString(contact.Name, holder.Img, mClip, contact.Net);
            if (contact.Phone.size() != 0)
                //   holder.Number.setText(t.Phone.get(0).Number);
                mTexth.setPrefixText(holder.Number, contact.Phone.get(contact.Nnamber).getNumber(), x);
            if (contact.PhotoThumbUri == null) {
                //hold.Img.setImageDrawable(null);
                holder.Img.setImageDrawable(null);
                ib.DrawImageString(contact.Name, holder.Img, mClip, contact.Phone.size()!=0?contact.Phone.get(0).nNet.getValue():0);

            } else
                ib.GetPhoto(contact, holder.Img, mClip,contact.Phone.size()!=0?contact.Phone.get(0).nNet.getValue():0);
            bindSectionHeader(holder.headerView,null,position);
            return rootView;
        }

        @Override
        public boolean doFilter(final Contact item,final CharSequence constraint)
        {
            x= (String) constraint;
            if(TextUtils.isEmpty(constraint))
                return true;
            final String displayName=item.Name;
            return !TextUtils.isEmpty(displayName)&&displayName.toLowerCase(Locale.getDefault())
                    .contains(constraint.toString().toLowerCase(Locale.getDefault()));
        }

        @Override
        public ArrayList<Contact> getOriginalList()
        {
            return mContacts;
        }


    }

    // /////////////////////////////////////////////////////////////////////////////////////
    // ViewHolder //
    // /////////////
    private static class ViewHolder
    {
        public TextView Text;
        public LetterImageView Img;

        public TextView Number,
        headerView;


        public int pos;
    }

}

package xobyx.xcontactj.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import xobyx.xcontactj.R;
import xobyx.xcontactj.fragments.NetFragment;
import xobyx.xcontactj.until.AsyncDrawer;
import xobyx.xcontactj.until.AsyncPrefixTextNumber;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.views.LetterImageView;
import xobyx.xcontactj.views.ListViewH.SearchablePinnedHeaderListViewAdapter;
import xobyx.xcontactj.views.ListViewH.StringArrayAlphabetIndexer;

/**
 * Created by xobyx on 8/25/2016.
 * For xobyx.xcontactj.adapters/XContactj2
 */
public class ContactsAdapter extends SearchablePinnedHeaderListViewAdapter<Contact> {
    protected final TextHighlighter mTexth;
    protected final LayoutInflater mInflater;
    protected final AsyncDrawer ib;
    private ArrayList<Contact> mContacts;
    protected String search_text;
    protected boolean mClip;

    //private final int CONTACT_PHOTO_IMAGE_SIZE;
    //private final int[] PHOTO_TEXT_BACKGROUND_COLORS;


    @Override
    public CharSequence getSectionTitle(int sectionIndex) {
        return ((StringArrayAlphabetIndexer.AlphaBetSection) getSections()[sectionIndex]).getName();
    }

    public ContactsAdapter(Context con, final ArrayList<Contact> contacts, boolean mClisp) {
        mClip = mClisp;

        setData(contacts);
        ib = new AsyncDrawer(con);
        mTexth = new TextHighlighter(Typeface.BOLD, 0xff02a7dd);
        mInflater = LayoutInflater.from(con);
        //    PHOTO_TEXT_BACKGROUND_COLORS=getResources().getIntArray(R.array.contacts_text_background_colors);
        //  CONTACT_PHOTO_IMAGE_SIZE=getResources().getDimensionPixelSize(
        //        R.dimen.list_item__contact_imageview_size);
    }

    public void setData(final ArrayList<Contact> contacts) {
        this.mContacts = contacts;
        final String[] generatedContactNames = generateContactNames(contacts);
        setSectionIndexer(new StringArrayAlphabetIndexer(generatedContactNames, true));
    }

    private String[] generateContactNames(final List<Contact> contacts) {
        final ArrayList<String> contactNames = new ArrayList<>();
        if (contacts != null)
            for (final Contact contactEntity : contacts)
                contactNames.add(contactEntity.Name);
        return contactNames.toArray(new String[contactNames.size()]);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final NetFragment.ViewHolder holder;
        final View rootView;
        if (convertView == null) {
            holder = new NetFragment.ViewHolder();
            rootView = mInflater.inflate(R.layout.child_main, null);
            holder.Text = (TextView) rootView.findViewById(R.id.ptextname);
            holder.Number = (TextView) rootView.findViewById(R.id.numberc);
            holder.Img = (LetterImageView) rootView.findViewById(R.id.imagec);
            holder.headerView = (TextView) rootView.findViewById(R.id.header_text);
            holder.pos = position;
            rootView.setTag(holder);
        } else {
            rootView = convertView;
            holder = (NetFragment.ViewHolder) rootView.getTag();
        }
        final Contact contact = getItem(position);


        setupView(position, holder, contact);
        bindSectionHeader(holder.headerView, null, position);

        return rootView;
    }
//* must call b
    protected void setupView(int position, NetFragment.ViewHolder holder, Contact contact) {
        holder.Text.setText(contact.Name);

        mTexth.setPrefixText(holder.Text, contact.Name, search_text);
        //ib.DrawImageString(contact, holder.Img, mClip, contact.Net);
        if (contact.Phone.size() != 0&&search_text!=null)
            //   holder.Number.setText(t.Phone.get(0).Number);
            //mTexth.setPrefixTextNumber(holder.Number, contact.Phone, search_text);
        {
            AsyncTask execute = new AsyncPrefixTextNumber(mTexth, holder.Number, contact, search_text);
            execute.execute();
        }
        holder.Img.setImageDrawable(null);
        if (contact.PhotoThumbUri == null) {


            ib.DrawImageString(contact, holder.Img, mClip,contact.Net );

        } else
            ib.GetPhoto(contact, holder.Img, mClip,contact.Net);


    }

    @Override
    public boolean doFilter(final Contact item, final CharSequence constraint) {
        search_text = (String) constraint;

        final String displayName = item.Name;
        if (TextUtils.isEmpty(constraint))
            return true;

        else if (displayName!=null&&!TextUtils.isEmpty(displayName) &&

                FormatUtils.indexOfWordPrefix( displayName.toLowerCase(Locale.getDefault()),search_text.toLowerCase(Locale.getDefault()))!=-1)
                return  true;
        else if(Character.isDigit(search_text.charAt(0)))
        {
            for (Contact.Phones phones : item.Phone) {
                if(FormatUtils.indexOfWordPrefix(phones.getNumber(),search_text)!=-1)
                    return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Contact> getOriginalList() {
        return mContacts;
    }


}

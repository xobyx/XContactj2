package xobyx.xcontactj.adapters;

/**
 * Created by xobyx on 3/12/2016.
 * For xobyx.xcontactj.adapters/XContactj
 */
public class sectioned_phones_sp_ad{ //extends SectionedRecyclerViewAdapter<PhoneAdapter.NumHolder> {

  /*  private final Contact s;
    private final int min;

    sectioned_phones_sp_ad(Contact s,int min)
    {

        this.s = s;
        this.min = min;
    }
    @Override
    public int getSectionCount() {
        int m=0,sa=0;
        Collections.sort(s.Phone, new Comparator<Contact.Phones>() {
            @Override
            public int compare(Contact.Phones lhs, Contact.Phones rhs) {
                return lhs.nNet.getValue()-rhs.nNet.getValue();
            }
        });
        for (Contact.Phones phones : s.Phone) {
            if(phones.nNet.getValue()!=min) {
                sa++;
                m=phones.nNet.getValue();

            }



        }
        return m;
    }

    @Override
    public int getItemCount(int section) {
        if (section % 2 == 0)
            return 4; // even sections get 4 items
        return 8; // odd get 8
    }

    @Override
    public void onBindHeaderViewHolder(PhoneAdapter.NumHolder holder, int section) {
        holder.title.setText(String.format("Section %d", section));
    }

    @Override
    public void onBindViewHolder(PhoneAdapter.NumHolder holder, int section, int relativePosition, int absolutePosition) {
        holder.title.setText(String.format("S:%d, P:%d, A:%d", section, relativePosition, absolutePosition));
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {

        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.list_item_header;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.list_item_main;
                break;
            default:
                layout = R.layout.list_item_main_bold;
                break;
        }

        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new MainVH(v);
    }

    public static class MainVH extends RecyclerView.ViewHolder {

        final TextView title;

        public MainVH(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }*/
}
package xobyx.xcontactj.ui.search;

import android.view.View;

import xobyx.xcontactj.R;
import xobyx.xcontactj.ui.base.ClickyViewHolder;
import xobyx.xcontactj.ui.base.QKActivity;
import xobyx.xcontactj.ui.view.AvatarView;
import xobyx.xcontactj.ui.view.QKTextView;

public class SearchViewHolder extends ClickyViewHolder<SearchData> {

    protected View root;
    protected AvatarView avatar;
    protected QKTextView name;
    protected QKTextView date;
    protected QKTextView snippet;

    public SearchViewHolder(QKActivity context, View view) {
        super(context, view);

        root = view;
        avatar = (AvatarView) view.findViewById(R.id.search_avatar);
        name = (QKTextView) view.findViewById(R.id.search_name);
        date = (QKTextView) view.findViewById(R.id.search_date);
        snippet = (QKTextView) view.findViewById(R.id.search_snippet);
    }
}

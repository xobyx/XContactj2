package xobyx.xcontactj.until;

import android.os.AsyncTask;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import xobyx.xcontactj.adapters.TextHighlighter;

/**
 * Created by xobyx on 9/12/2016.
 * For xobyx.xcontactj.until/XContactj2
 */
public class AsyncPrefixTextNumber extends AsyncTask {

    private TextHighlighter textHighlighter;
    private final String mSearch;
    private final Contact mContact;
    WeakReference<TextView> mText;

    public AsyncPrefixTextNumber(TextHighlighter textHighlighter, TextView b, Contact bs, String mSearch) {
        this.textHighlighter = textHighlighter;
        this.mSearch = mSearch;
        mContact = bs;
        mText = new WeakReference<TextView>(b);

    }

    @Override
    protected void onPreExecute() {
        mText.get().setText("");
    }

    @Override
    protected Object doInBackground(Object[] params) {

        if (mSearch != null && !mSearch.isEmpty()) {

            return textHighlighter.getSpannableStringBuilder(mContact.Phone, mSearch);

        }

       return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        if(o!=null) mText.get().setText((SpannableStringBuilder) o);
        else mText.get().setText("");
    }
}

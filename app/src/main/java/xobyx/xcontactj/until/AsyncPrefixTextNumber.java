package xobyx.xcontactj.until;

import android.os.AsyncTask;
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
    protected Object doInBackground(Object[] params) {
        return textHighlighter.getSpannableStringBuilder(mContact.Phone, mSearch);
    }

    @Override
    protected void onPostExecute(Object o) {
        mText.get().setText((StringBuilder) o);
    }
}

package xobyx.xcontactj.adapters;

import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.util.ArrayList;

import xobyx.xcontactj.until.Contact;

public class TextHighlighter {

    private final String TAG = TextHighlighter.class.getSimpleName();
    private int mHighlightColor;
    private CharacterStyle mTextColorStyleSpan;
    private int mTextStyle;


    public TextHighlighter(int var1, int var2) {
        this.mTextStyle = var1;
        this.mHighlightColor = var2;

        this.mTextColorStyleSpan = this.getColorStyleSpan();
    }

    public TextHighlighter(int bold) {
        this.mTextStyle = bold;
    }

    private CharacterStyle getColorStyleSpan() {
        return this.mHighlightColor != -1 ? new ForegroundColorSpan(this.mHighlightColor) : null;
    }

    private CharacterStyle getStyleSpan() {
        return new StyleSpan(this.mTextStyle);

    }

    public void applyMaskingHighlight(SpannableString var1, int var2, int var3) {
        var1.setSpan(this.getStyleSpan(), var2, var3, 0);
        var1.setSpan(this.getColorStyleSpan(), var2, var3, 0);

    }

    public CharSequence applyPrefixHighlight(CharSequence var1, String var2) {
        if (var2 != null) {
            int var3;
            for (var3 = 0; var3 < var2.length() && !Character.isLetterOrDigit(var2.charAt(var3)); ++var3) {
            }

            String var4 = var2.substring(var3);
            int var5 = FormatUtils.indexOfWordPrefix(var1, var4);
            if (var5 != -1) {
                SpannableString var6 = new SpannableString(var1);
                //TODO:this for what//
                var6.setSpan(getStyleSpan(), var5, var5 + var4.length(), 0);
                var6.setSpan(getColorStyleSpan(), var5, var5 + var4.length(), 0);

                return var6;
            }
        }

        return var1;
    }

    public void setPrefixText(TextView var1, String var2, String var3) {
        var1.setText(this.applyPrefixHighlight(var2, var3));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public void setPrefixTextNumber(TextView var1, ArrayList<Contact.Phones> var2, String search) {

        var1.setText("");
        if (search != null && !search.isEmpty()) {

            SpannableStringBuilder sp = getSpannableStringBuilder(var2, search);
            var1.setText(sp);
        }
        else var1.setText("");
    }

    @NonNull
    public SpannableStringBuilder getSpannableStringBuilder(ArrayList<Contact.Phones> var2, String search) {
        SpannableStringBuilder sp = new SpannableStringBuilder();
        int le = 0;
        int var3;
        for (var3 = 0; var3 < search.length() && !Character.isLetterOrDigit(search.charAt(var3)); ++var3) {
        }

        String var4 = search.substring(var3);
        for (Contact.Phones phones : var2) {

            int var5 = FormatUtils.indexOfWordPrefix(phones.getNumber(), var4);
            if (var5 != -1) {

                try {
                    sp.append(new SpannableString(phones.getNumber() + " "));

                    //TODO:this for what//
                    sp.setSpan(getStyleSpan(), var5 + le, var5 + var4.length() + le, 0);
                    sp.setSpan(getColorStyleSpan(), var5 + le, var5 + var4.length() + le, 0);


                    //sp.setSpan(this.mTextStyleSpan, sp.length()-1,sp.length(), 0);
                    le += phones.getNumber().length() + 1;
                }
                catch (Exception y) {
                    y.printStackTrace();
                }
            }


        }
        return sp;
    }

}

package xobyx.xcontactj.until.format;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class TextHighlighter {

    private final String TAG = TextHighlighter.class.getSimpleName();
    private int mHighlightColor;
    private CharacterStyle mTextColorStyleSpan;
    private int mTextStyle;
    private CharacterStyle mTextStyleSpan;


    public TextHighlighter() {
        this.mTextStyle = Typeface.BOLD;
        this.mHighlightColor = 0xff02a7dd;

        this.mTextStyleSpan = this.getStyleSpan();
        this.mTextColorStyleSpan = this.getColorStyleSpan();
    }

    private CharacterStyle getColorStyleSpan() {
        return this.mHighlightColor != -1 ? new ForegroundColorSpan(this.mHighlightColor) : null;
    }

    private CharacterStyle getStyleSpan() {
        return new StyleSpan(this.mTextStyle);
    }

    public void applyMaskingHighlight(SpannableString var1, int var2, int var3) {
        var1.setSpan(this.getStyleSpan(), var2, var3, 0);
        if (this.mTextColorStyleSpan != null) {
            var1.setSpan(this.getColorStyleSpan(), var2, var3, 0);
        }

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
                var6.setSpan(this.mTextStyleSpan, var5, var5 + var4.length(), 0);
                if (this.mTextColorStyleSpan != null) {
                    var6.setSpan(this.mTextColorStyleSpan, var5, var5 + var4.length(), 0);
                }

                return var6;
            }
        }

        return var1;
    }

    public void setPrefixText(TextView var1, String var2, String var3) {
        var1.setText(this.applyPrefixHighlight(var2, var3));
    }
}

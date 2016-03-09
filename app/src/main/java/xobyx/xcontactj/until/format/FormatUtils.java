package xobyx.xcontactj.until.format;

import android.database.CharArrayBuffer;

public class FormatUtils {

    public static String charArrayBufferToString(CharArrayBuffer var0) {
        return new String(var0.data, 0, var0.sizeCopied);
    }

    public static void copyToCharArrayBuffer(String var0, CharArrayBuffer var1) {
        if (var0 == null) {
            var1.sizeCopied = 0;
        } else {
            char[] var2 = var1.data;
            if (var2 != null && var2.length >= var0.length()) {
                var0.getChars(0, var0.length(), var2, 0);
            } else {
                var1.data = var0.toCharArray();
            }

            var1.sizeCopied = var0.length();
        }
    }

    public static int indexOfWordPrefix(CharSequence var0, String var1) {
        int var2;
        if (var1 != null && var0 != null) {
            int var3 = var0.length();
            int var4 = var1.length();
            if (var4 == 0 || var3 < var4) {
                return -1;
            }

            var2 = 0;

            while (true) {
                if (var2 >= var3) {
                    return -1;
                }

                while (var2 < var3 && !Character.isLetterOrDigit(var0.charAt(var2))) {
                    ++var2;
                }

                if (var2 + var4 > var3) {
                    return -1;
                }

                int var5;
                for (var5 = 0; var5 < var4 && Character.toUpperCase(var0.charAt(var2 + var5)) == var1.charAt(var5); ++var5) {
                }

                if (var5 == var4) {
                    break;
                }

                while (var2 < var3 && Character.isLetterOrDigit(var0.charAt(var2))) {
                    ++var2;
                }
            }
        } else {
            var2 = -1;
        }

        return var2;
    }

    public static int overlapPoint(String var0, String var1) {
        return var0 != null && var1 != null ? overlapPoint(var0.toCharArray(), var1.toCharArray()) : -1;
    }

    public static int overlapPoint(char[] var0, char[] var1) {
        int var2;
        if (var0 != null && var1 != null) {
            int var3 = var0.length;

            int var4;
            for (var4 = var1.length; var3 > 0 && var4 > 0 && var0[var3 - 1] == var1[var4 - 1]; --var4) {
                --var3;
            }

            int var5 = var4;
            var2 = 0;

            while (true) {
                if (var2 >= var3) {
                    return -1;
                }

                if (var2 + var5 > var3) {
                    var5 = var3 - var2;
                }

                int var6;
                for (var6 = 0; var6 < var5 && var0[var2 + var6] == var1[var6]; ++var6) {
                }

                if (var6 == var5) {
                    break;
                }

                ++var2;
            }
        } else {
            var2 = -1;
        }

        return var2;
    }
}

package xobyx.xcontactj.until;

public class QueryUtils {

   /* private static Uri a() {
        return Contacts.CONTENT_URI;
    }

    private static String a(ArrayList var0) {
        boolean var1 = LogicManager.getInstance().getSettingsLogic().getShowOnlyContactsWithPhones();
        return var0 != null?b(var0):(var1?String.format("%s = 1 AND %s = 1", new Object[]{"in_visible_group", "has_phone_number"}):String.format("%s = 1", new Object[]{"in_visible_group"}));
    }

    private static String[] a(int var0) {
        String[] var1 = new String[]{"_id", "lookup", c(), b(var0), "has_phone_number", "display_name", "display_name_alt", "last_time_contacted", "photo_uri"};
        return var1;
    }

    private static String b(int var0) {
        return var0 == 3?"times_contacted":(var0 == 5?"last_time_contacted":(LogicManager.getInstance().getSettingsLogic().getSortOrder() == 0?"sort_key":"sort_key_alt"));
    }

    private static String b(ArrayList var0) {
        StringBuilder var1 = new StringBuilder("in_visible_group = 1");
        if(var0.size() > 0) {
            var1.append(" AND _id IN (");

            for(int var3 = 0; var3 < var0.size(); ++var3) {
                var1.append(var0.get(var3));
                if(var3 < -1 + var0.size()) {
                    var1.append(" , ");
                }
            }

            var1.append(")");
        }

        return var1.toString();
    }

    private static String[] b() {
        return null;
    }

    private static String c() {
        return LogicManager.getInstance().getSettingsLogic().getDisplayOrder() == 0?"display_name":"display_name_alt";
    }

    public static Cursor getCallLogsCursor(Context var0, String var1) {
        if(var1 == null) {
            var1 = "0";
        }

        Uri var2 = Calls.CONTENT_URI;
        String[] var3 = new String[]{"_id", "number", "numbertype", "type", "date", "duration", "name"};
        String var4 = String.format("%s > ?", new Object[]{"_id"});
        String[] var5 = new String[]{var1};
        return var0.getContentResolver().query(var2, var3, var4, var5, "date DESC");
    }

    public static Cursor getContactsFragmentCursor(Context var0, ArrayList var1, int var2) {
        if(var0 == null) {
            var0 = SimplerApplication.getContext();
        }

        return var0.getContentResolver().query(a(), a(var2), a(var1), b(), b(var2));
    }

    public static CursorLoader getContactsFragmentCursorLoader(Context var0, ArrayList var1, int var2) {
        String var3;
        if(var2 == 3) {
            var3 = " DESC";
        } else {
            var3 = " ASC";
        }

        return new CursorLoader(var0, a(), a(var2), a(var1), b(), b(var2) + var3);
    }

    public static CursorLoader getGetItFreeCursorLoader(Context var0) {
        String[] var1 = new String[]{"_id", "lookup", c(), "times_contacted", "has_phone_number", "display_name", "display_name_alt", "last_time_contacted", "photo_uri"};
        String var2 = String.format("%s = 1", new Object[]{"has_phone_number"});
        return new CursorLoader(var0, a(), var1, var2, (String[])null, "times_contacted" + " DESC");
    }

    public static Cursor getMostContactedCursor(Context var0) {
        Uri var1 = Contacts.CONTENT_URI;
        String[] var2 = new String[]{"_id", "display_name"};
        String var3 = String.format("%s = 1 AND %s > 0", new Object[]{"in_visible_group", "times_contacted"});
        return var0.getContentResolver().query(var1, var2, var3, (String[])null, "times_contacted DESC LIMIT 50");
    }

    public static Cursor getOrganizationsCursor(Context var0) {
        Uri var1 = Data.CONTENT_URI;
        String[] var2 = new String[]{"vnd.android.cursor.item/organization"};
        String[] var3 = new String[]{"contact_id", "data1", "data4"};
        if(var0 == null) {
            var0 = SimplerApplication.getContext();
        }

        return var0.getContentResolver().query(var1, var3, "mimetype = ? AND in_visible_group=1", var2, "sort_key");
    }

    public static CursorLoader getSelectContactCursorLoader(Context var0) {
        String var1 = String.format("%s = 1", new Object[]{"in_visible_group"});
        return new CursorLoader(var0, a(), a(1), var1, b(), "sort_key");
    }

    public static Cursor getUnusedContactsCursor(Context var0) {
        Uri var1 = Contacts.CONTENT_URI;
        String[] var2 = new String[]{"_id", "last_time_contacted"};
        String var3 = String.format("%s = 1", new Object[]{"in_visible_group"});
        return var0.getContentResolver().query(var1, var2, var3, (String[])null, "last_time_contacted");
    }

    public static String selectArgsFromSameColumn(String var0, String[] var1) {
        Object[] var2 = new Object[]{var0, var1[0]};
        String var3 = String.format("%s LIKE \'%s\'", var2);
        String var4;
        if(var1.length > 1) {
            var4 = var3;

            for(int var5 = 1; var5 < var1.length; ++var5) {
                Object[] var6 = new Object[]{var0, var1[var5]};
                String var7 = String.format("OR %s LIKE \'%s\'", var6);
                var4 = var4 + var7;
            }
        } else {
            var4 = var3;
        }

        return var4;
    }*/
}

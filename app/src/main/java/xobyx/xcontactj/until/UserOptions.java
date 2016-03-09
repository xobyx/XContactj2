package xobyx.xcontactj.until;

import android.content.Context;
import android.provider.ContactsContract;

import static xobyx.xcontactj.until.Contact.PhoneClass;

/**
 * Created by xobyx on 5/2/2015.
 * c# to java
 */
public class UserOptions {
    int DeleteDuplicatedContact(final Contact u, Context m) {
        for (PhoneClass ps : u.Phone)
            for (PhoneClass phoneClass : u.Phone) {
                if (ps.getNumber().equals(phoneClass.getNumber())) {

                    final int i = m.getContentResolver().delete(ContactsContract.Contacts.CONTENT_URI, "ID=?", new String[]{phoneClass.ID});
                    u.Phone.remove(phoneClass);
                }
            }
        return 0;
    }
}

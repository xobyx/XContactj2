package xobyx.xcontactj.until;

import android.content.Context;
import android.provider.ContactsContract;

import static xobyx.xcontactj.until.Contact.Phones;

/**
 * Created by xobyx on 5/2/2015.
 * c# to java
 */
public class UserOptions {
    int DeleteDuplicatedContact(final Contact u, Context m) {
        for (Phones ps : u.Phone)
            for (Phones phones : u.Phone) {
                if (ps.getNumber().equals(phones.getNumber())) {

                    final int i = m.getContentResolver().delete(ContactsContract.Contacts.CONTENT_URI, "ID=?", new String[]{phones.ID});
                    u.Phone.remove(phones);
                }
            }
        return 0;
    }
}

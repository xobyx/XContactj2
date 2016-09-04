package xobyx.xcontactj.activities;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import xobyx.xcontactj.adapters.onCreateFragmentAdapter;
import xobyx.xcontactj.until.Contact;
import xobyx.xcontactj.until.ME;
import xobyx.xcontactj.views.LetterImageView;

import static xobyx.xcontactj.until.ME.getAllList;

/**
 * Created by xobyx on 9/1/2016.
 * For xobyx.xcontactj.activities/XContactj2
 */
public class AllContactSpecificsActivity extends ContactSpecificsActivity {


    @NonNull
    @Override
    protected onCreateFragmentAdapter getFragmentAdapter() {
        return new onCreateFragmentAdapter(getSupportFragmentManager(), mContact, mPos, -1, lmessage, true);
    }

    @Override
    protected void onApplyCustomTheme() {

    }

    @Override
    protected void SetupContactImage(LetterImageView imageView) {
        if (mContact.PhotoUri != null) {
            imageView.setImageURI(mContact.PhotoUri);
        }
        else {
            imageView.setLetter(mContact.Name.charAt(0));

            imageView.setContact(mContact);


        }
    }

    @Override
    protected Contact getContact() {
        if(mPos!=-1) {
            ArrayList<Contact> list = getAllList();
            if (list != null && list.size() != 0) {

                return list.get(mPos);

            }

        }
        return null;
    }

    @Override
    protected void SetPageParameters(Intent in) {

        mPos=-1;

        if (in.getAction() == Intent.ACTION_VIEW) {

            if (!ME.getAllList().isEmpty()) {
                for (int i = 0; i < ME.getAllList().size(); i++)
                    if (in.getData().getPath().contains(ME.getAllList().get(i).Lookup)) {
                        mPos = i;
                    }
            }
            else {
                Contact contact = ME.getContactFromUri(this, in.getData());
                if(contact!=null) {
                    ME.getAllList().add(contact);
                    SetPageParameters(in);
                }
            }


        }
        else {
            mPos = in.getIntExtra(POS, -1);
            lmessage = in.hasExtra("message") ? in.getParcelableExtra("message") : null;
        }
    }
}




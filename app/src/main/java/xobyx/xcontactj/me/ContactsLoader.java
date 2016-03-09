/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xobyx.xcontactj.me;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.provider.ContactsContract.Profile;
import android.util.Log;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * A loader for use in the default contact list, which will also query for the user's profile
 * if configured to do so.
 */
public class ContactsLoader extends CursorLoader {


    private String[] mProjection;

    public ContactsLoader(Context context) {
        super(context);
    }


    public void setProjection(String[] projection) {
        super.setProjection(projection);
        mProjection = projection;
        for (int i = 0; i < projection.length; i++) {
            Log.d("ContactlistLoader", projection[i]);
        }


    }

    @Override
    public Cursor loadInBackground() {
        // First load the profile, if enabled.

        return super.loadInBackground();


    }

    /**
     * Loads the profile into a MatrixCursor. On failure returns null, which
     * matches the behavior of CursorLoader.loadInBackground().
     *
     * @return MatrixCursor containing profile or null on query failure.
     */
    private MatrixCursor loadProfile() {
        try {


            Cursor cursor = getContext().getContentResolver().query(Profile.CONTENT_URI, mProjection,
                    null, null, null);

            if (cursor == null) {
                return null;
            }
            try {
                MatrixCursor matrix = new MatrixCursor(mProjection);
                Object[] row = new Object[mProjection.length];
                while (cursor.moveToNext()) {
                    for (int i = 0; i < row.length; i++) {
                        row[i] = cursor.getString(i);
                    }
                    matrix.addRow(row);
                }
                return matrix;
            } finally {
                cursor.close();
            }
        } catch (SecurityException d) {
        }
        return null;
    }
}

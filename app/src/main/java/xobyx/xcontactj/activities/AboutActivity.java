package xobyx.xcontactj.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import xobyx.xcontactj.R;

public class AboutActivity extends Activity {

    public static void ShowAboutActivity(Context v) {
        Intent u = new Intent(v, AboutActivity.class);
        v.startActivity(u);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

    }
}

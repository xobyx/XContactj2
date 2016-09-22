package xobyx.xcontactj.fragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import xobyx.xcontactj.until.AsyncLoad;

/**
 * Created by xobyx on 9/6/2016.
 * For xobyx.xcontactj.fragments/XContactj2
 */
public abstract class AsyncLoadFragment<T> extends Fragment implements AsyncLoad.IRun<ArrayList<T>> {

    AsyncLoad<ArrayList<T>> asyncLoad;

    @Override
    public void onResume() {
        super.onResume();

        if (asyncLoad != null && asyncLoad.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLoad.cancel(true);
        }

        asyncLoad = new AsyncLoad<>(this);
        asyncLoad.execute();


    }

    @Override
    public void onPause() {
        super.onPause();
        if (asyncLoad != null && asyncLoad.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLoad.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (asyncLoad != null && this.asyncLoad.getStatus() == AsyncTask.Status.RUNNING) {
            asyncLoad.cancel(true);
        }

    }
}

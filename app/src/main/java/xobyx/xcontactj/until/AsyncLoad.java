package xobyx.xcontactj.until;

import android.os.AsyncTask;

/**
 * Created by xobyx on 2/24/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class AsyncLoad extends AsyncTask {

    public AsyncLoad(IRun m)
    {
        this.What=m;
    }

    public interface IRun
    {
        void Start();

        void doAfterFinish();
    }
    IRun What;
    @Override
    protected Object doInBackground(Object[] params) {
        What.Start();
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        What.doAfterFinish();
    }
}

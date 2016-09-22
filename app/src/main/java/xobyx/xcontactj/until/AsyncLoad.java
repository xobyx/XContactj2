package xobyx.xcontactj.until;

import android.os.AsyncTask;

/**
 * Created by xobyx on 2/24/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class AsyncLoad<T> extends AsyncTask<Object,Object,T> {

    public AsyncLoad(IRun<T> m)
    {
        this.What=m;
    }

    public interface IRun<M>
    {
        M Start();

        void doAfterFinish(M result);
    }
    private IRun<T> What;
    @Override
    protected T doInBackground(Object[] params) {

        return What.Start();
    }

    @Override
    protected void onPostExecute(T o) {
        What.doAfterFinish(o);
    }
}

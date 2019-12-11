package xobyx.xcontactj.until;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class mDownlodService extends Service {
    public mDownlodService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }
}

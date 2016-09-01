package xobyx.xcontactj.until;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class XService extends Service {
    public static final int MSG_SET_VALUE = 1;
    public static final int MSG_REGISTER_CLIENT = 0;

    Messenger y = new Messenger(new F());
    localBind xbinder = new localBind();

    public XService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals("server"))
            return xbinder;
        else
            return y.getBinder();

    }

    static class F extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public class localBind extends Binder {

        XService getService() {
            return XService.this;
        }


        @Override
        public IInterface queryLocalInterface(String descriptor) {
            return super.queryLocalInterface(descriptor);
        }
    }

}

class d {
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    String u = "";
    Context m;
    private IBinder serv;
    private XService service;
    private Messenger mOutMessenger;

    d(final String action) {
        Intent u = new Intent(m, XService.class);
        if (action.equals("server"))
            u.setAction("server");
        ServiceConnection yu = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (action.equals("server"))
                    d.this.serv = service;
                else {
                    mOutMessenger = new Messenger(service);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (d.this.serv != null) {
                    d.this.serv = null;
                }
            }
        };
        m.bindService(u, yu, Service.BIND_AUTO_CREATE);
    }

    void Send(int g) {
        if (mOutMessenger != null)
            try {
                Message msg = Message.obtain(null,
                        XService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mOutMessenger.send(msg);

                // Give it some value as an example.
                msg = Message.obtain(null,
                        XService.MSG_SET_VALUE, this.hashCode(), 0);
                mOutMessenger.send(msg);
            } catch (RemoteException e) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }


    }

    void ty() {
        service = ((XService.localBind) serv).getService();

    }

    static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case XService.MSG_SET_VALUE:
                    // mCallbackText.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
package xobyx.xcontactj.until;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okio.Okio;

/**
 * Created by xobyx on 10/2/2016.
 * for xobyx.xcontactj.until
 * M:10
 */

public class UpdateHandler {

    private static final String UPDATE_TOPIC = "update";

    boolean getIsSubscribe() {
        return false;
    }

    static public boolean ChangeSubscribe(Context v, boolean subscribe) {


        GcmPubSub pubSub = GcmPubSub.getInstance(v);
        final String token = getToken(v);
        if (token != null) {
            try {

                if (subscribe) {
                    pubSub.subscribe(token, "/topics/" + UPDATE_TOPIC, null);
                }
                else {
                    pubSub.unsubscribe(token, "/topics/" + UPDATE_TOPIC);
                }
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;

    }

    private static String getToken(Context v) {
        final SharedPreferences m = v.getSharedPreferences("RegistrationToServer", 0);
        if (m.contains("sent_token_to_server") && m.contains("token")) {
            return m.getString("token", null);
        }
        return null;
    }

    public static void handle(int ver, String url) {

    }


    public class UpdateApp extends AsyncTask<String,Void,Void> {
        private Context context;
        public void setContext(Context contextf){
            context = contextf;
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();


                File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();
                File outputFile = new File(file, "update.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = c.getInputStream();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(file, "update.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                context.startActivity(intent);


            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return null;
        }
    }
}

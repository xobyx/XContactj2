package xobyx.xcontactj.until;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSenderException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import xobyx.xcontactj.BuildConfig;

/**
 * Created by xobyx on 8/7/2015.
 * For xobyx.xcontactj.until/XContactj
 */
public class MyEmailReportSender implements org.acra.sender.ReportSender {


    private ACRAConfiguration config;
    private CrashReportData errorContent;

    public MyEmailReportSender(ACRAConfiguration acraConfiguration)
    {

        config = acraConfiguration;
    }

    @Override
    public void send(final Context context, CrashReportData errorContent) throws ReportSenderException {
        final String subject = context.getPackageName() + " Crash Report";
        final String body = buildBody(errorContent);

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.fromParts("mailto", config.mailTo(), null));
        emailIntent.addFlags(0x10000000);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
		if (BuildConfig.DEBUG) {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            //String currentDatedTime = sdf.format(new Date());
            File n = new File(context.getExternalCacheDir() + "/crash/" + new Date().getTime() + ".txt");
            boolean mkdirs = n.mkdirs();
            try {
                OutputStreamWriter y = new OutputStreamWriter(new FileOutputStream(n));
                y.append(body);
                y.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		

        try {
            context.startActivity(emailIntent);
        }
		
        catch (Exception t){

            t.printStackTrace();
            throw new ReportSenderException("");



        }

        
    }
    private String buildBody(CrashReportData errorContent) {
        this.errorContent = errorContent;
        ReportField[] fields = config.customReportContent();
        if(fields.length == 0) {
            fields = ACRAConstants.DEFAULT_REPORT_FIELDS;
        }

        final StringBuilder builder = new StringBuilder();
        for (ReportField field : fields) {
            builder.append(field.toString()).append("=");
            builder.append(errorContent.get(field));
            builder.append('\n');
        }
        return builder.toString();
    }


}

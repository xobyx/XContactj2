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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xobyx on 8/7/2015.
 * For xobyx.xcontactj.until/XContactj
 */
public class D_save_error implements org.acra.sender.ReportSender {


    private ACRAConfiguration cofig;

    public D_save_error(ACRAConfiguration acraConfiguration)
    {

        cofig = acraConfiguration;
    }
    public D_save_error()
    {


    }

    @Override
    public void send(final Context context, CrashReportData errorContent) throws ReportSenderException {
        final String subject = context.getPackageName() + " Crash Report";
        final String body = buildBody(errorContent);

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.fromParts("mailto", cofig.mailTo(), null));
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        try {
            context.startActivity(emailIntent);
        }

        catch (Exception t){





        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDatedTime = sdf.format(new Date());
        File n=new File( context.getExternalCacheDir()+"/"+ currentDatedTime+".txt");
        try {
            OutputStreamWriter y=new OutputStreamWriter(new FileOutputStream(n));
            y.append(body);
            y.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String buildBody(CrashReportData errorContent) {
        ReportField[] fields = cofig.customReportContent();
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

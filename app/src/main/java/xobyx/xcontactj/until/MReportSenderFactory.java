package xobyx.xcontactj.until;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;


/**
 * Created by xobyx on 2/16/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class MReportSenderFactory implements org.acra.sender.ReportSenderFactory {
    @NonNull
    @Override
    public ReportSender create(Context context, ACRAConfiguration config) {
        //return new MyEmailReportSender(acraConfiguration);
        return new A(config);

    }
}

package xobyx.xcontactj.until;

import android.content.Context;
import android.support.annotation.NonNull;

import org.acra.config.ACRAConfiguration;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderFactory;

/**
 * Created by xobyx on 2/16/2016.
 * For xobyx.xcontactj.until/XContactj
 */
public class ReportSenderFac implements ReportSenderFactory {
    @NonNull
    @Override
    public ReportSender create(Context context, ACRAConfiguration acraConfiguration) {
        return new MyEmailReportSender(acraConfiguration);

    }
}

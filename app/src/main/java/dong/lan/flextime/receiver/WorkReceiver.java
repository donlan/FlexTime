package dong.lan.flextime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import dong.lan.flextime.services.WorkService;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/28/2016  15:49.
 */
public class WorkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, WorkService.class));
    }
}

package dong.lan.flextime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.services.WorkService;

/**
 * Created by 梁桂栋 on 2015/12/17.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
//            context.startService(new Intent(context, WorkService.class));
            if (BuildConfig.DEBUG) Log.d("BootReceiver", "开机启动服务");
        }
    }
}

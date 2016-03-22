package dong.lan.flextime;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.utils.Constant;
import dong.lan.flextime.utils.SP;

/**
 * Created by 梁桂栋 on 2015/12/5.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        Bmob.initialize(this, Constant.BmobKey);
        SP.init(getSharedPreferences(SP.SP_NAME, MODE_PRIVATE));
        DBManager.getManager().init(this);
    }
}

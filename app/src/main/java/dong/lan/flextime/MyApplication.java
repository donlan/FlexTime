package dong.lan.flextime;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.db.TodoRepository;
import dong.lan.flextime.utils.Constant;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.UserManager;

/**
 * Created by 梁桂栋 on 2016年08月31日 19:31.
 * Email:760625325@qq.com
 * github:gitbub.com/donlan
 * description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        Bmob.initialize(this, Constant.BmobKey);
        SP.init(getSharedPreferences(SP.SP_NAME, MODE_PRIVATE));
        TodoRepository.initRealm(this);
        UserManager.getManager().initUser(BmobUser.getCurrentUser(User.class));
    }
}

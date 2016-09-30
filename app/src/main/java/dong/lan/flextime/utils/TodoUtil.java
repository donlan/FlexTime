package dong.lan.flextime.utils;

import android.util.Log;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.Config;
import dong.lan.flextime.bean.RealmToDoItem;
import dong.lan.flextime.bean.Todo;

/**
 * Created by 梁桂栋 on 2016年09月01日 16:01.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class TodoUtil {

    public static String getTodoInfo(Todo todo) {
        StringBuilder sb = new StringBuilder();
        boolean isGoodStatus = SP.getStatus() == Config.GOOD;

        sb.append("<html><body>");
        for(RealmToDoItem item : todo.items){
            sb.append("<h3>");
            sb.append(item.info);
            sb.append("</h3><p>");
            sb.append("● 开始时间　");
            sb.append(TimeUtil.longToString(item.startTime,TimeUtil.FORMAT_DATE_TIME));
            sb.append("</p><p>");
            sb.append("● 剩余时间　");
            sb.append(TimeUtil.getRemainTime(item.finishTime - System.currentTimeMillis()));
            sb.append("</p>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    public static String getTodoItemInfo(RealmToDoItem item){
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("<h3>");
        sb.append(item.info);
        sb.append("</h3><p>");
        sb.append("● 开始时间　");
        sb.append(TimeUtil.longToString(item.startTime,TimeUtil.FORMAT_DATE_TIME));
        sb.append("</p><p>");
        sb.append("● 剩余时间　");
        sb.append(TimeUtil.getRemainTime(item.finishTime - System.currentTimeMillis()));
        sb.append("</p>");
        sb.append("</body></html>");
        return sb.toString();
    }
}

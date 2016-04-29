package dong.lan.flextime.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.List;

import de.greenrobot.event.EventBus;
import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.Config;
import dong.lan.flextime.bean.ToDoEvent;
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.dao.TodoDao;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.receiver.WorkReceiver;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.TodoManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/17  15:48.
 *
 * 执行定时任务的后台服务
 */
public class WorkService extends Service {

    private int  timeDelay = 0;
    private ContentValues values;
    private LatLng latLng;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Todo> todos = TodoManager.get().getTodos();
                if (todos != null) {
                    if (BuildConfig.DEBUG) Log.d("WorkService:", todos.toString());
                    int c = 0;
                    for (Todo todo : todos) {

                        if (c >= Config.LEVEL) {
                            break;
                        }
                        //状态栏提醒，并更新日程的权重（ * 1.5）
                        if (TodoManager.get().isTodoItemStartTips(todo,timeDelay )) {
                            values.put(TodoDao.WEIGHT, String.valueOf((todo.getWeight() * 1.5)));
                            values.put(TodoDao.FLAG, TodoDao.FLAG_DOIT);
                            DBManager.getManager().updateTodo(values, todo.getId());
                            EventBus.getDefault().post(new ToDoEvent(todo,ToDoEvent.TODO_NOTIFY,0));
                            c++;
                        }
                        //将超时日程移动到过期日程列表的事件回调
                        if (TodoManager.get().isTodoTimeOut(todo)) {
                            EventBus.getDefault().post(new ToDoEvent(todo,ToDoEvent.ONTIME_TO_TIMEOUT,0));
                        }
                        SparseArray<ToDoItem> items = todo.getTodos();
                        for(int i = 0;i<items.size();i++) {
                            ToDoItem item = items.get(i);
                            if (item.getPoint() != null) {
                                if (DistanceUtil.getDistance(latLng, new LatLng(item.getPoint().getLatitude(), item.getPoint().getLongitude())) < 1000) {
                                    EventBus.getDefault().post(new ToDoEvent(todo, ToDoEvent.NEAR_NOTIFY, i));
                                }
                            }
                        }
                    }
                        EventBus.getDefault().post(new ToDoEvent(null, ToDoEvent.REFRESH_TODO, 0));
                }
            }
        }).start();
        /*
            执行定时刷新任务
         */
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long trigAtTime  = SystemClock.elapsedRealtime()+ Config.FRESH_GAP;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,new Intent(this,WorkReceiver.class),0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,trigAtTime,pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        timeDelay = SP.getAlertDelay()*60000;
        values = new ContentValues();
    }

    public void onEventMainThread(LatLng latLng) {
        this.latLng = latLng;
        if (BuildConfig.DEBUG) Log.d("WorkService", latLng.toString());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package dong.lan.flextime.presenter.handleTodo;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import dong.lan.flextime.R;
import dong.lan.flextime.bean.KeyWord;
import dong.lan.flextime.event.LocEvent;
import dong.lan.flextime.bean.RealmToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.event.TodoEvent;
import dong.lan.flextime.model.handleTodo.AddTodoModel;
import dong.lan.flextime.presenter.BasePresenter;
import dong.lan.flextime.utils.KeyWordManager;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.view.constom.DateTimePicker;
import dong.lan.flextime.view.handleTodo.AddTodoActivity;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 2016年09月02日 19:19.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class AddTodoPresenter extends BasePresenter<AddTodoActivity, AddTodoModel> implements IAddTodoPresenter {

    private long needTime;
    private long finishTime;
    private long deadTime;
    private AlertDialog dialog;
    private Todo todo;
    Map<String, String> keyWords = new HashMap<>();  //保存获取到的分词结果

    public AddTodoPresenter(AddTodoActivity view, AddTodoModel model) {
        super(view, model);
    }


    @Override
    public void setTodoToView() {

    }

    @Override
    public void setNeedTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view);
        View v = LayoutInflater.from(view).inflate(R.layout.dialog_need_time_picker, null);
        final NumberPicker day = (NumberPicker) v.findViewById(R.id.needTimeDay);
        final NumberPicker hour = (NumberPicker) v.findViewById(R.id.needTimeHours);
        final NumberPicker minute = (NumberPicker) v.findViewById(R.id.needTimeMinute);
        day.setMaxValue(365);
        hour.setMaxValue(24);
        minute.setMaxValue(60);
        TextView done = (TextView) v.findViewById(R.id.needTimeDone);
        hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 24) {
                    hour.setValue(0);
                    day.setValue(day.getValue() + 1);
                }
            }
        });
        minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 60) {
                    minute.setValue(0);
                    hour.setValue(hour.getValue() + 1);
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sb = "估计时长: " +
                        day.getValue() +
                        "天 " +
                        hour.getValue() +
                        "小时 " +
                        minute.getValue() +
                        "分";
                view.setNeedTime(sb);
                needTime = TimeUtil.getNeedLong(day.getValue(), hour.getValue(), minute.getValue());
                dialog.dismiss();
            }
        });
        builder.setView(v);
        dialog = builder.show();
    }

    @Override
    public void setDeadTime() {
        new DateTimePicker(view, new DateTimePicker.CallBack() {
            @Override
            public void onClose(long time) {
                deadTime = time;
                view.setDeadTime("截至完成时间：" + TimeUtil.longToString(time, TimeUtil.FORMAT_DATE_TIME));
            }
        }).show();
    }

    @Override
    public void setFinishTime() {
        new DateTimePicker(view, new DateTimePicker.CallBack() {
            @Override
            public void onClose(long time) {
                finishTime = time;
                view.setFinishTime("最佳完成时间：" + TimeUtil.longToString(time, TimeUtil.FORMAT_DATE_TIME));
            }
        }).show();
    }

    @Override
    public void done(boolean isAdd) {
        if (view.getInfo().equals("")) {
            view.dialog("没有记录事件");
            return;
        }
        if (deadTime == 0) {
            view.dialog("最晚完成时间必需设置 !!");
            return;
        }
        if (needTime == 0) {
            view.dialog("请设置需要完成此事的预期时间");
            return;
        }
        int status = view.getStatus();
        Realm realm = model.getRealm();
        realm.beginTransaction();
        LocEvent loc = null;//no location function yet
        if (isAdd) {
            todo.updateTime = System.currentTimeMillis();
            todo.flag = Todo.FLAG_NO_START;
            todo.id = String.valueOf(view.getInfo().hashCode() + System.currentTimeMillis());
            RealmToDoItem item = new RealmToDoItem();
            item.info = view.getInfo();
            item.id = String.valueOf(todo.id.hashCode() + System.currentTimeMillis());
            item.needTime = needTime;
            if (finishTime == 0)
                item.finishTime = deadTime;
            else
                item.finishTime = finishTime;
            item.deadline = deadTime;
            item.status = status;
            if (loc != null) {
                item.loc = loc.getDes();
                item.lat = loc.getLatLng().latitude;
                item.lng = loc.getLatLng().longitude;
            }
            item.important = view.getImportant();
            item.urgent = view.getUrgent();
            item.remind = view.getRemind();
            item.startTime = finishTime - needTime;
            todo.createTime = System.currentTimeMillis();
            todo.items.add(item);
            todo.type = Todo.TYPE_SINGLE;

            /*
            保存当前日程内容的分词结果到数据库
             */
            for (String key : keyWords.keySet()) {
                KeyWord keyWord = new KeyWord();
                keyWord.setWord(key);
                keyWord.setImp(view.getImportant());
                keyWord.setUrg(view.getUrgent());
                keyWord.setTime(needTime);
                realm.copyToRealm(keyWord);
            }
        } else {
            todo.updateTime = System.currentTimeMillis();
            RealmToDoItem item = todo.items.get(0);
            item.info = view.getInfo();
            item.needTime = needTime;
            if (finishTime == 0)
                item.finishTime = deadTime;
            else
                item.finishTime = finishTime;
            item.deadline = deadTime;
            item.status = status;
            item.startTime = finishTime - needTime;
            if (loc != null) {
                item.loc = loc.getDes();
                item.lat = loc.getLatLng().latitude;
                item.lng = loc.getLatLng().longitude;
            }
            item.important = view.getImportant();
            item.urgent = view.getUrgent();
            item.remind = view.getRemind();
        }
        realm.copyToRealmOrUpdate(todo);
        realm.commitTransaction();
        EventBus.getDefault().post(new TodoEvent(null,TodoEvent.EVENT_SINGLE_ADD));
        view.finish();
    }

    @Override
    public void init(Todo todo) {
        this.todo = todo;
        if (todo.items != null && !todo.items.isEmpty()) {
            RealmToDoItem item = todo.items.get(0);
            setLongTime(item);
        }
    }

    @Override
    public Todo getTodo(String id) {
        init(model.getNotNullTodo(id));
        return todo;
    }

    @Override
    public void getKeyWordRecommand(String info, TextView showText) {
        KeyWordManager.getManager().searchRecom(keyWords, info, showText);
    }

    @Override
    public void setLongTime(RealmToDoItem item) {
        needTime = item.needTime;
        if (item.finishTime == 0)
            finishTime = item.deadline;
        else
            finishTime = item.finishTime;
        deadTime = item.deadline;
    }

    @Override
    public void detach() {
        super.detach();
        dialog = null;
        keyWords = null;
    }
}

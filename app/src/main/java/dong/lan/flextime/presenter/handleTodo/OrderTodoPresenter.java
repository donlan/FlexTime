package dong.lan.flextime.presenter.handleTodo;

import android.app.AlertDialog;
import android.util.Log;
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
import dong.lan.flextime.view.handleTodo.AddOrderTodoActivity;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 2016年09月03日 14:13.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class OrderTodoPresenter extends BasePresenter<AddOrderTodoActivity, AddTodoModel> implements IOrderTodoPresenter {


    private long needTime;
    private long finishTime;
    private long deadTime;
    private AlertDialog dialog;
    private Todo todo;
    Map<String, String> keyWords = new HashMap<>();  //保存获取到的分词结果

    public OrderTodoPresenter(AddOrderTodoActivity view, AddTodoModel model) {
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
        int count = view.getCount();
        if (count <= 1 && !isTodoInfoComplete())
            return;
        Realm realm = model.getRealm();
        realm.beginTransaction();
        if (isAdd) {
            todo.id = String.valueOf(view.getInfo().hashCode() + System.currentTimeMillis());
            todo.type = Todo.TYPE_MUTIL_SORT;
            todo.createTime = System.currentTimeMillis();
            todo.flag = Todo.FLAG_NO_START;
        } else {
            todo.updateTime = System.currentTimeMillis();
        }
        realm.copyToRealmOrUpdate(todo);
        realm.commitTransaction();
        EventBus.getDefault().post(new TodoEvent(null, TodoEvent.EVENT_SINGLE_ADD));
        view.finish();
    }

    @Override
    public void init(Todo todo) {
        this.todo = todo;
        if (todo.items.size() > 0) {
            setLongTime(todo.items.get(0));
        }

    }


    @Override
    public Todo getTodo(String id) {
        todo = model.getNotNullTodo(id);
        return todo;
    }

    @Override
    public void getKeyWordRecommand(String info, TextView showText) {
        KeyWordManager.getManager().searchRecom(keyWords, info, showText);
    }

    @Override
    public void setLongTime(RealmToDoItem item) {
        if (item == null) {
            needTime = 0;
            finishTime = 0;
            deadTime = 0;
        } else {
            needTime = item.needTime;
            if (item.finishTime == 0)
                finishTime = item.deadline;
            else
                finishTime = item.finishTime;
            deadTime = item.deadline;
        }
    }

    @Override
    public void detach() {
        super.detach();
        dialog = null;
        keyWords = null;
    }

    @Override
    public void addOne() {
        if (!isTodoInfoComplete())
            return;
        RealmToDoItem item = new RealmToDoItem();
        item.info = view.getInfo();
        item.needTime = needTime;
        if (finishTime == 0)
            item.finishTime = deadTime;
        else
            item.finishTime = finishTime;
        item.deadline = deadTime;
        item.status = view.getStatus();
        item.id = String.valueOf(item.info.hashCode() + System.currentTimeMillis());
        LocEvent loc = null;
        if (loc != null) {
            item.loc = loc.getDes();
            item.lat = loc.getLatLng().latitude;
            item.lng = loc.getLatLng().longitude;
        }
        item.important = view.getImportant();
        item.urgent = view.getUrgent();
        item.remind = view.getRemind();
        item.startTime = finishTime - needTime;
        int count = view.getCount();
        Log.d("OrderTodoPresenter", "count:" + count+" size :"+todo.items.size());
        if (count > todo.items.size()) {
            Realm realm = model.getRealm();
            realm.beginTransaction();
            todo.items.add(item);
            realm.commitTransaction();
        } else
            todo.items.add(item);
        count++;
        view.updateCountView(count);
        view.show("添加成功");
    }

    @Override
    public void updateOne(RealmToDoItem item) {
        Realm realm = model.getRealm();
        realm.beginTransaction();
        item.info = view.getInfo();
        item.needTime = needTime;
        item.deadline = deadTime;
        if (finishTime == 0)
            item.finishTime = deadTime;
        else
            item.finishTime = finishTime;
        item.status = view.getStatus();

        LocEvent loc = null;
        if (loc != null) {
            item.loc = loc.getDes();
            item.lat = loc.getLatLng().latitude;
            item.lng = loc.getLatLng().longitude;
        }
        item.important = view.getImportant();
        item.urgent = view.getUrgent();
        item.remind = view.getRemind();
        item.startTime = finishTime - needTime;
        realm.commitTransaction();
        view.show("更新成功");
    }

    @Override
    public void saveAndTipKeyword(String info, TextView TipTextView) {
        Realm realm = model.getRealm();
        realm.beginTransaction();
        for (String key : keyWords.keySet()) {
            KeyWord keyWord = new KeyWord();
            keyWord.setImp(view.getImportant());
            keyWord.setUrg(view.getUrgent());
            keyWord.setTime(needTime);
            keyWord.setWord(key);
            realm.copyToRealm(keyWord);
        }
        realm.commitTransaction();
    }

    @Override
    public boolean isTodoInfoComplete() {
        if (view.getInfo().equals("")) {
            view.dialog("没有记录事件");
            return false;
        }
        if (deadTime == 0) {
            view.dialog("最晚完成时间必需需要设置 !!");
            return false;
        }
        if (needTime == 0) {
            view.dialog("请设置需要完成此事的预期时间");
            return false;
        }
        return true;
    }
}

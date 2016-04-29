package dong.lan.flextime.utils;

import android.util.SparseArray;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.datatype.BmobGeoPoint;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.Config;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.LocDes;
import dong.lan.flextime.bean.ToDoEvent;
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.dao.TodoDao;
import dong.lan.flextime.db.DBManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/26/2016  14:37.
 */
public class TodoManager {
    private volatile static TodoManager todoManager;

    private List<Todo> todos = null;    //需要执行的日程
    private List<Todo> timeoutTodos = null;//超过设定完成时间的日程
    int levelFirst;

    public int getLevelSecond() {
        return levelSecond;
    }

    public void setLevelSecond(int levelSecond) {
        this.levelSecond = levelSecond;
    }

    public int getLevelFirst() {
        return levelFirst;
    }

    public void setLevelFirst(int levelFirst) {
        this.levelFirst = levelFirst;
    }

    int levelSecond;

    public List<Todo> getTimeoutTodos() {
        return timeoutTodos;
    }

    public void setTimeoutTodos(List<Todo> timeoutTodos) {
        this.timeoutTodos = timeoutTodos;
    }

    public static TodoManager getTodoManager() {
        return todoManager;
    }

    public static void setTodoManager(TodoManager todoManager) {
        TodoManager.todoManager = todoManager;
    }

    public static TodoManager get() {
        if (todoManager == null) {
            synchronized (TodoManager.class) {
                if (todoManager == null)
                    todoManager = new TodoManager();
            }
        }
        return todoManager;
    }

    public void initLevel(int levelFirst,int levelSecond)
    {
        this.levelFirst = levelFirst;
        this.levelSecond = levelSecond;
    }
    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    /*
    添加一个日程的条目
     */
    public ToDoItem addTodoItem(Todo todo, String info, String bestTime, String deadline, String needTime, int status,
                                LocDes loc, int imp, int urg, int seq, boolean isRemind) {

        needTime = needTime.substring(needTime.lastIndexOf(">") + 2);

        if (bestTime.length() > 10)
            bestTime = bestTime.substring(bestTime.lastIndexOf(">") + 2);
        else
            bestTime = "9999年12月31日 23:59";

        if (deadline.length() > 10)
            deadline = deadline.substring(deadline.lastIndexOf(">") + 2);
        else
            deadline = "9999年12月31日 23:59";


        ToDoItem item = new ToDoItem();
        item.setSubId(String.valueOf(System.currentTimeMillis() + 1000));
        item.setDoneOnTime(false);
        item.setImportant(imp);
        item.setUrgent(urg);
        item.setId(todo.getId());
        item.setSeq(seq);
        item.setStatus(status);
        if (loc != null) {
            item.setLoc(loc.getDes());
            item.setPoint(new BmobGeoPoint(loc.getLatLng().longitude, loc.getLatLng().latitude));
        }
        item.setFlag(TodoDao.FLAG_ON);
        item.setInfo(info);
        item.setRemind(isRemind ? 1 : 0);
        item.setContinueDo(false);
        long bTime = TimeUtil.getStartTime(needTime, bestTime);
        long dTime = TimeUtil.getStartTime(needTime, deadline);

        item.setStartTime(bTime - dTime >= 0 ? dTime : bTime);
        item.setWeight(SortManager.getSortWeight(imp, urg, item.getStatus()));
        item.setFinishTime(TimeUtil.defaultTime(bestTime));
        item.setDeadline(TimeUtil.defaultTime(deadline));
        item.setNeedTime(TimeUtil.getLongNeedTime(needTime));
        String createTime = new SimpleDateFormat(TimeUtil.FORMAT_DATA_TIME_SECOND, Locale.CHINA).format(new Date());
        item.setCreateTime(createTime);
        todo.addTodoItem(item);
        return item;
    }


    /*
    添加一个日程
     */
    public void addTodo(Todo todo, int pos) {
        todo = caluTodoWeight(todo);
        DBManager.getManager().addAToDo(todo);
        EventBus.getDefault().post(new ToDoEvent(todo, ToDoEvent.EVENT_ADD, pos));
    }

    /*
    添加分步日程
     */
    public void addOrderTodo(Todo todo, int pos) {
        todo = caluTodoWeight(todo);
        DBManager.getManager().addAToDo(todo);
        EventBus.getDefault().post(new ToDoEvent(todo,ToDoEvent.EVENT_ORDER_DONE,pos));
    }

    /*
    更新一个只有一个日程条目的的日程信息
     */
    public void updateSingleTodo(Todo todo, String info, String bestTime, String deadline, String needTime, int status,
                                 LocDes loc, int imp, int urg, int seq, boolean isRemind) {
        ToDoItem item = todo.getTodos().get(0);
        todo.getTodos().setValueAt(0, updateTodoItem(item, info, bestTime, deadline, needTime, status, loc, imp, urg, seq, isRemind));
        todo.setFlag(item.getFlag());
        DBManager.getManager().updateTodo(todo);
    }

    /*
    只更新日程条目的信息,并保存到数据库
     */
    public ToDoItem updateTodoItem(ToDoItem item, String info, String bestTime, String deadline, String needTime, int status,
                                   LocDes loc, int imp, int urg, int seq, boolean isRemind) {
        needTime= needTime.substring(needTime.indexOf(">")+2);
        bestTime= bestTime.substring(bestTime.indexOf(">")+2);
        deadline= deadline.substring(deadline.indexOf(">")+2);
        item.setInfo(info);
        if(loc!=null) {
            item.setLoc(loc.getDes());
            item.setPoint(new BmobGeoPoint(loc.getLatLng().longitude, loc.getLatLng().latitude));
        }
        long bTime = TimeUtil.getStartTime(needTime, bestTime);
        long dTime = TimeUtil.getStartTime(needTime, deadline);
        item.setStartTime(bTime - dTime >= 0 ? dTime : bTime);
        item.setNeedTime(TimeUtil.getLongNeedTime(needTime));
        item.setSeq(seq);
        item.setStatus(status);
        item.setRemind(isRemind ? 1 : 0);
        item.setImportant(imp);
        item.setUrgent(urg);
        item.setFlag(System.currentTimeMillis()-item.getDeadline()>0?TodoDao.FLAG_TIME_OUT:TodoDao.FLAG_ON);
        item.setFinishTime(TimeUtil.defaultTime(deadline));
        item.setDeadline(TimeUtil.defaultTime(bestTime));
        DBManager.getManager().updateTodoItem(item);
        return item;
    }


    /*
    修改一个日程体条目的信息
     */
    public ToDoItem modefyTodoItem(ToDoItem item, String info, String bestTime, String deadline, String needTime, int status,
                                   LocDes loc, int imp, int urg, int seq, boolean isRemind) {
        needTime= needTime.substring(needTime.indexOf(">")+2);
        bestTime= bestTime.substring(bestTime.indexOf(">")+2);
        deadline= deadline.substring(deadline.indexOf(">")+2);
        item.setInfo(info);
        if(loc!=null) {
            item.setLoc(loc.getDes());
            item.setPoint(new BmobGeoPoint(loc.getLatLng().longitude, loc.getLatLng().latitude));
        }
        item.setNeedTime(TimeUtil.getLongNeedTime(needTime));
        item.setSeq(seq);
        item.setStatus(status);
        item.setRemind(isRemind ? 1 : 0);
        item.setImportant(imp);
        item.setUrgent(urg);
        item.setFlag(TodoDao.FLAG_UPDATE);
        item.setFinishTime(TimeUtil.defaultTime(deadline));
        item.setDeadline(TimeUtil.defaultTime(bestTime));
        return item;
    }

    /*
    更新一个日程的信息
     */
    public Todo updateTodo(Todo todo) {
        SparseArray<ToDoItem> items = todo.getTodos();
        ToDoItem item = null;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            if (item.getFlag() == TodoDao.FLAG_UPDATE) {
                item.setFlag(isTodoItemTimeout(item)?TodoDao.FLAG_TIME_OUT:TodoDao.FLAG_ON);
                DBManager.getManager().updateTodoItem(item);
            }
        }
        todo = updateTodoWeight(todo);
        return todo;
    }

    public Todo updateTodoWeight(Todo todo) {
        SparseArray<ToDoItem> items = todo.getTodos();
        ToDoItem item = null;
        double w = 0;
        int outCounter = 0;
        for (int i = 0; i < items.size(); i++) {
            item = items.get(i);
            w+=item.getWeight();
            if(item.getFlag()==TodoDao.FLAG_TIME_OUT)
                outCounter++;
        }
        if(outCounter==items.size()-1)
            todo.setFlag(TodoDao.FLAG_TIME_OUT);
        else
        todo.setFlag(TodoDao.FLAG_ON);
        todo.setWeight(w);
        DBManager.getManager().updateTodoWeight(todo);
        return todo;
    }



    /*
    更新一个日程条目的信息
     */
    public ToDoItem updateTodoItem(ToDoItem item) {
        DBManager.getManager().updateTodoItem(item);
        return item;
    }


    /*
    将一个日程条目的信息设置到指定的View中
     */
    public void setTodoItemToView(ToDoItem item, EditText info, TextView needTime, TextView bestTime, TextView deadline,
                                  TextView loc, CheckBox checkBox, SeekBar imp, SeekBar urg,
                                  RadioButton high, RadioButton mid, RadioButton low) {
        StringBuilder sb = new StringBuilder();
        info.setText(item.getInfo());
        sb.append("最佳完成时间-> ");
        sb.append(TimeUtil.defaultFormat(item.getFinishTime()));
        bestTime.setText(sb.toString());
        sb.delete(0, sb.length());
        sb.append("最晚完成时间-> ");
        sb.append(TimeUtil.defaultFormat(item.getDeadline()));
        deadline.setText(sb.toString());
        sb.delete(0, sb.length());
        sb.append("所需时长-> ");
        sb.append(TimeUtil.defaultNeedFormat(item.getNeedTime()));
        needTime.setText(sb.toString());


        checkBox.setChecked(item.getRemind().equals(1));
        info.setText(item.getInfo());
        loc.setText(item.getLoc());
        imp.setProgress(item.getImportant());
        urg.setProgress(item.getUrgent());

        if (item.getStatus() == Config.LEVEL_HIGH)
            high.setChecked(true);
        else if (item.getStatus() == Config.LEVEL_NORMAL)
            mid.setChecked(true);
        else
            low.setChecked(true);
    }

    /*
    计算一个日程的总权重
     */
    public Todo caluTodoWeight(Todo todo) {

        SparseArray<ToDoItem> items = todo.getTodos();
        double weight = 0;
        for (int i = 0; i < items.size(); i++) {
            weight += SortManager.getSortWeight(items.get(i));
        }
        todo.setWeight(weight);
        return todo;
    }

    /*
    判断一个日程条目的开始执行时间是否已经超过当前时间
     */
    public boolean isTodoItemTimeout(ToDoItem item) {
        if(Config.STATUS==Config.GOOD)
        return item.getFinishTime()- System.currentTimeMillis() < 0;
        else
            return item.getDeadline()- System.currentTimeMillis() < 0;
    }

    /*
    判断一个日程条目是否已经开始
     */
    public boolean isTodoItemStart(ToDoItem item) {
        return item.getStartTime()- System.currentTimeMillis() > 0;
    }

    /*
    判断一个日程的结束时间是否超过当前时间
     */
    public boolean isTodoTimeOut(Todo todo) {
        SparseArray<ToDoItem> items = todo.getTodos();
        int s = items.size();
        for (int i = 0; i < s; i++) {
           if(isTodoItemTimeout(items.get(i)) )
               return true;
        }

        return false;
    }

    /*
    判断一个日程条目是否需要开始提醒
    timeDelay：提醒的延时
     */
    public boolean isTodoItemStartTips(Todo todo, int timeDelay) {
        SparseArray<ToDoItem> items = todo.getTodos();
        for (int i = 0; i < items.size(); i++) {
            if (Math.abs(items.get(i).getStartTime()- System.currentTimeMillis()) < timeDelay)
                return true;
        }
        return false;
    }

    /*
    改变使用的模式
     */
    public void changeMode(int checkedId) {
        switch (checkedId) {
            case R.id.mode_busy:
                SP.setMode(Config.BUSY);
                Config.MODE = Config.BUSY;
                break;
            case R.id.mode_normal:
                SP.setMode(Config.NORMAL);
                Config.MODE = Config.NORMAL;
                break;
            case R.id.mode_work:
                SP.setMode(Config.WORK);
                Config.MODE = Config.WORK;
                break;
        }
    }

    /*
    改变应用中用户的个人状态信息
     */
    public void changeStatus(int checkedId) {
        switch (checkedId) {
            case R.id.status_good:
                SP.setStatus(Config.GOOD);
                Config.STATUS = Config.GOOD;
                break;
            case R.id.status_bad:
                SP.setStatus(Config.BAD);
                Config.STATUS = Config.BAD;
                break;
        }
    }


    public void setUserHeadInfo(TextView userInfo)
    {

        StringBuffer stringBuilder = new StringBuffer();
        stringBuilder.append("共有 ");
        stringBuilder.append(todos.size());
        stringBuilder.append(" 个未完成日程.\n");
        stringBuilder.append(timeoutTodos.size());
        stringBuilder.append(" 个超时未完成日程。");
        userInfo.setText(stringBuilder.toString());
    }
}

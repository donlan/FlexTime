package dong.lan.flextime.bean;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/12  05:30.
 *
 *
 * EventBus的事件封装类
 */
public class ToDoEvent {

    public static final int EVENT_ADD=0;            //普通日程添加
    public static final int EVENT_UPDATE=1;         //普通日程的更新
    public static final int EVENT_ORDER_DONE=2;     //分步日程添加完成
    public static final int EVENT_ORDER_UPDATE=3;   //分步日程的更新
    public static final int REFRESH_TODO=4;         //刷新日程列表
    public static final int ONTIME_TO_TIMEOUT =5;   //将超时日程移动到超时列表
    public static final int TODO_NOTIFY=6;          //日程状态栏提醒
    public static final int NEAR_NOTIFY=7;          //附近日程提醒
    private Todo todo; //事件传递的日程
    private int type;  //事件类型
    private int pos;   //对应RecycleView的layout position

    public ToDoEvent(Todo todo, int type, int pos)
    {
        this.todo = todo;
        this.type =type;
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public Todo getTodo()
    {
        return todo;
    }
    public int getType()
    {
        return type;
    }

}

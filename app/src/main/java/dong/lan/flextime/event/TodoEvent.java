package dong.lan.flextime.event;

import dong.lan.flextime.bean.Todo;

/**
 * Created by 梁桂栋 on 2016年09月03日 20:40.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class TodoEvent {


    public static final int EVENT_SINGLE_ADD = 0;
    public static final int EVENT_MUTIL_ADD = 1;
    public static final int EVENT_SINGLE_UPDATE = 2;
    public static final int EVENT_MUTIL_UPDATE = 3;
    public static final int EVENT_TODO_DELETE = 4;
    public static final int EVENT_TODO_REFRESH =5;

    private Todo todo;
    private int eventType;


    public TodoEvent(Todo todo, int eventType) {
        this.todo = todo;
        this.eventType = eventType;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}

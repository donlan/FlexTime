package dong.lan.flextime.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dong.lan.flextime.Interface.ScheduleDeliver;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2016/2/10  12:21.
 */
public class ScheduleManager {
    public static final int ALERT = 0;
    public static final int TIMEOVER = 1;
    private int total;
    private int level_first_size;
    private int level_second_size;
    private List<ToDoItem> toDoItems = new ArrayList<>();
    private ScheduleDeliver deliver;

    public List<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public ScheduleManager(int first, int second, ScheduleDeliver deliver) {
        level_first_size = first;
        level_second_size = second;
        this.deliver = deliver;
    }

    public ScheduleManager(ScheduleDeliver deliver) {
        this(10, 20, deliver);
    }

    public void init(List<ToDoItem> toDoItems) {
        Collections.sort(toDoItems);
        total = toDoItems.size();
        this.toDoItems = toDoItems;
    }


    public void looping()
    {
        for(int i=0;i<level_first_size;i++)
        {
            if (Math.abs( toDoItems.get(i).getStartTime()
                    - System.currentTimeMillis()) < 600000) {
                deliver.onScheduleDelivering(ALERT, toDoItems.get(i));
            }
            if (toDoItems.get(i).getFinishTime()- System.currentTimeMillis() < 0) {
               deliver.onScheduleDelivering(TIMEOVER, toDoItems.get(i));
            }
        }
    }
    public void add(ToDoItem toDoItem) {
        toDoItems.add(toDoItem);
        total++;
        Collections.sort(toDoItems);
    }

    public int getTotal() {
        return total;
    }
}

package dong.lan.flextime.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dong.lan.flextime.Interface.ScheduleDeliver;
import dong.lan.flextime.utils.TimeUtil;

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
    private List<ToDo> toDos = new ArrayList<>();
    private ScheduleDeliver deliver;

    public List<ToDo> getToDos() {
        return toDos;
    }

    public ScheduleManager(int first, int second, ScheduleDeliver deliver) {
        level_first_size = first;
        level_second_size = second;
        this.deliver = deliver;
    }

    public ScheduleManager(ScheduleDeliver deliver) {
        this(10, 20, deliver);
    }

    public void init(List<ToDo> toDos) {
        Collections.sort(toDos);
        total = toDos.size();
        this.toDos = toDos;
    }


    public void looping()
    {
        for(int i=0;i<level_first_size;i++)
        {
            if (Math.abs(TimeUtil.getStartTime(toDos.get(i).getNeedTime(), toDos.get(i).getFinishTime())
                    - System.currentTimeMillis()) < 600000) {
                deliver.onScheduleDelivering(ALERT,toDos.get(i));
            }
            if (TimeUtil.stringToLong(toDos.get(i).getFinishTime(), TimeUtil.FORMAT_DATA_TIME_SECOND)
                    - System.currentTimeMillis() < 0) {
               deliver.onScheduleDelivering(TIMEOVER,toDos.get(i));
            }
        }
    }
    public void add(ToDo toDo) {
        toDos.add(toDo);
        total++;
        Collections.sort(toDos);
    }

    public int getTotal() {
        return total;
    }
}

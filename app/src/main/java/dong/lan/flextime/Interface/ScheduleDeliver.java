package dong.lan.flextime.Interface;

import dong.lan.flextime.bean.RealmToDoItem;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2016/2/10  14:13.
 */
public interface ScheduleDeliver {
    void onScheduleDelivering(int tag,RealmToDoItem toDoItem);
}

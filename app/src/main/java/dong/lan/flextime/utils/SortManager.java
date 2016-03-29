package dong.lan.flextime.utils;

import dong.lan.flextime.Config;
import dong.lan.flextime.bean.ToDoItem;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/12  07:02.
 */
public class SortManager {
    public static double IMP=3.5; //默认的重要性权重因子
    public static double URG =2.5;//默认的紧急性权重因子

    /*
    根据 紧急性 重要性 用户状态 设置权重因子
     */
    public static double getSortWeight(int imp,int urg,int status)
    {
        return (imp*IMP+URG*urg)* Config.getLevelFactor(status);
    }

    /*
    根据 紧急性 重要性 用户状态 日程时间（开始时间，所需时间） 设置权重因子
     */
    public static double getSortWeight(ToDoItem toDoItem)
    {
        double d = (toDoItem.getStartTime()-System.currentTimeMillis())/toDoItem.getNeedTime();
        return (toDoItem.getImportant()*IMP+URG*toDoItem.getUrgent())* Config.getLevelFactor(toDoItem.getStatus())*d;
    }

    public static void init(double imp,double urg)
    {
        IMP = imp;
        URG = urg;
    }
}

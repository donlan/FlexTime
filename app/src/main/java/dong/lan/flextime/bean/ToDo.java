package dong.lan.flextime.bean;

import android.support.annotation.NonNull;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 2015/12/7.
 */
public class ToDo extends BmobObject  implements Comparable<ToDo>{

    private User user;              //所属用户
    private String info;            //事件描述
    private String finishTime;      //最佳完成时间
    private String Deadline;        //最晚完成时间
    private String startTime;       //开始时间
    private String needTime;        //估计需要的完成的时长
    private String level;           //事件等级
    private BmobGeoPoint point;     //位置
    private String loc;             //位置描述
    private Double weight;          //计算得到的权重（事件排序的关键）
    private Boolean doneOnTime;     //是否按时完成
    private String createTime;      //建立事件的时间
    private Boolean continueDo;     //事件是否需要连续进行
    private Integer remind;         //是否需要提醒，以及提醒类型
    private Integer perContinueTime;//每次执行持续时间
    private String lastDoTime;      //上次停止执行的时间
    private Integer important;       //事件紧急性
    private Integer urgent;          //事件重要性
    private Integer status;          //日程效果


    public Integer getImportant() {
        return important;
    }

    public void setImportant(Integer important) {
        this.important = important;
    }

    public Integer getUrgent() {
        return urgent;
    }

    public void setUrgent(Integer urgent) {
        this.urgent = urgent;
    }

    public String getNeedTime() {
        return needTime;
    }

    public void setNeedTime(String needTime) {
        this.needTime = needTime;
    }

    public Integer getRemind() {
        return remind;
    }

    public void setRemind(Integer remind) {
        this.remind = remind;
    }

    public void setDoneOnTime(Boolean doneOnTime) {
        this.doneOnTime = doneOnTime;
    }

    public Boolean getContinueDo() {
        return continueDo;
    }

    public void setContinueDo(Boolean continueDo) {
        this.continueDo = continueDo;
    }

    public Integer getPerContinueTime() {
        return perContinueTime;
    }

    public void setPerContinueTime(Integer perContinueTime) {
        this.perContinueTime = perContinueTime;
    }


    public String getLastDoTime() {
        return lastDoTime;
    }

    public void setLastDoTime(String lastDoTime) {
        this.lastDoTime = lastDoTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BmobGeoPoint getPoint() {
        return point;
    }

    public void setPoint(BmobGeoPoint point) {
        this.point = point;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean isDoneOnTime() {
        return doneOnTime;
    }

    @Override
    public int compareTo(@NonNull ToDo another) {
        if(this.weight>another.weight )
        {
            String thisTime = this.getFinishTime();
            String anoTime = another.getFinishTime();
            if(thisTime.substring(0,4).equals("9999"))
                thisTime= this.getDeadline();
            if(anoTime.substring(0,4).equals("9999"))
                anoTime= another.getDeadline();
            if(TimeUtil.stringToLong(thisTime,TimeUtil.FORMAT_DATA_TIME_SECOND)
                    -TimeUtil.stringToLong(anoTime,TimeUtil.FORMAT_DATA_TIME_SECOND)< SP.getAlertDelay()*60000)
            {
                return 1;
            }else
            {
                return -1;
            }
        }
        else if(this.weight.equals(another.weight)) {
            return this.getStartTime().compareTo(another.getStartTime());
        }
        else
        return 1;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

package dong.lan.flextime.bean;

import android.support.annotation.NonNull;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import dong.lan.flextime.utils.SP;

/**
 * Created by 梁桂栋 on 2015/12/7.
 */
public class ToDoItem extends BmobObject implements Comparable<ToDoItem> {


    private String id;              //ID与父日程ID一致
    private String subId;           //子id用于区分同一个日程下的不同分步日程
    private String info;            //事件描述
    private Long finishTime;        //最佳完成时间
    private Long Deadline;          //最晚完成时间
    private Long startTime;         //开始时间
    private Long needTime;          //估计需要的完成的时长
    private Integer flag;           //事件等级
    private String loc;             //位置描述
    private String createTime;      //建立事件的时间
    private Boolean doneOnTime;     //是否按时完成
    private Boolean continueDo;     //事件是否需要连续进行
    private Double weight;          //计算得到的权重（事件排序的关键）
    private Integer remind;         //是否需要提醒，以及提醒类型
    private Integer important;      //事件紧急性
    private Integer urgent;         //事件重要性
    private Integer status;         //日程效果
    private BmobGeoPoint point;     //位置
    private Integer seq;            //分部日程的安排顺序

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }


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

    public Long getNeedTime() {
        return needTime;
    }

    public void setNeedTime(Long needTime) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getDoneOnTime() {
        return doneOnTime;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
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
    public int compareTo(@NonNull ToDoItem another) {
        if (this.weight > another.weight) {
            if (this.getDeadline()
                    - another.getDeadline() < SP.getAlertDelay() * 60000) {
                return 1;
            } else {
                return -1;
            }
        } else if (this.weight.equals(another.weight)) {
            return this.getStartTime().compareTo(another.getStartTime());
        } else
            return 1;
    }

    public Long getDeadline() {
        return Deadline;
    }

    public void setDeadline(Long deadline) {
        Deadline = deadline;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

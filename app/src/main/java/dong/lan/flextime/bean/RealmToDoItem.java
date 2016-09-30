package dong.lan.flextime.bean;

import android.support.annotation.NonNull;


import dong.lan.flextime.utils.SP;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 2016年09月02日 08:18.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class RealmToDoItem extends RealmObject implements Comparable<RealmToDoItem> {

    @PrimaryKey
    public String id;              //ID与父日程ID一致

    public String subId;           //子id用于区分同一个日程下的不同分步日程

    public String info;            //事件描述
    public long createTime;         //建立事件的时间
    public long finishTime;        //最佳完成时间
    public long deadline;          //最晚完成时间
    public long startTime;         //开始时间
    public long needTime;          //估计需要的完成的时长
    public int level;               //事件等级
    public String loc;             //位置描述
    public boolean doneOnTime;     //是否按时完成
    public boolean continueDo;     //事件是否需要连续进行
    public double weight;          //计算得到的权重（事件排序的关键）
    public int remind;          //是否需要提醒，以及提醒类型
    public int important;       //事件紧急性
    public int urgent;          //事件重要性
    public int status;          //日程效果
    public double lat;          //位置维度
    public double lng;          //位置经度
    public int index;           //分部日程的安排顺序
    public int flag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getNeedTime() {
        return needTime;
    }

    public void setNeedTime(long needTime) {
        this.needTime = needTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public boolean isDoneOnTime() {
        return doneOnTime;
    }

    public void setDoneOnTime(boolean doneOnTime) {
        this.doneOnTime = doneOnTime;
    }

    public boolean isContinueDo() {
        return continueDo;
    }

    public void setContinueDo(boolean continueDo) {
        this.continueDo = continueDo;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int compareTo(@NonNull RealmToDoItem another) {
        if (this.weight > another.weight) {
            if (deadline
                    - another.deadline < SP.getAlertDelay() * 60000) {
                return 1;
            } else {
                return -1;
            }
        } else if (this.weight==(another.weight)) {
            if(this.startTime >another.startTime)
                return -1;
            else if(this.startTime < another.startTime)
                return 1;
            return  0;
        } else
            return 1;
    }


}

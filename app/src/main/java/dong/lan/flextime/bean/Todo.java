package dong.lan.flextime.bean;

import android.support.annotation.NonNull;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/26/2016  11:09.
 */
public class Todo extends RealmObject implements Comparable<Todo> {

    public static final int FLAG_DONE = -2;
    public static final int FLAG_OVER = -1;
    public static final int FLAG_STARTING = -4;
    public static final int FLAG_NO_START = -3;


    public static final int TYPE_SINGLE = 1;
    public static final int TYPE_MUTIL = 2;
    public static final int TYPE_MUTIL_SORT = 3;

    public static final String FLAG = "flag";
    public static final String ID = "id";
    public static final String WEIGHT = "weight";
    public static final String UPDATE_TIME="updateTime";

    @PrimaryKey
    public String id;
    public long createTime;
    public long updateTime;
    public int type;
    public int flag;
    public double weight;
    public RealmUser user;
    public RealmList<RealmToDoItem> items;

    public Todo(){
        items =new RealmList<>();
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public RealmUser getUser() {
        return user;
    }

    public void setUser(RealmUser user) {
        this.user = user;
    }

    public RealmList<RealmToDoItem> getItems() {
        return items;
    }

    public void setItems(RealmList<RealmToDoItem> items) {
        this.items = items;
    }

    @Override
    public int compareTo(@NonNull Todo another) {
        if (this.weight > another.weight)
            return -1;
        else if (this.weight == another.weight)
            return 0;
        return 1;
    }
}

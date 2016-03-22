package dong.lan.flextime.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by 梁桂栋 on 2015/12/7.
 */
public class User extends BmobUser {
    private boolean sex;        //性别
    private String des;         //用户描述
    private String tag;         //用户标签

    public User(){}
    public User(String name)
    {
        setUsername(name);
    }
    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}

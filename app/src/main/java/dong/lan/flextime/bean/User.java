package dong.lan.flextime.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by 梁桂栋 on 2016年09月01日 00:51.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
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

    public User(String id,String name,String password,boolean sex, String des, String tag) {
        setObjectId(id);
        setPassword(password);
        setUsername(name);
        this.sex = sex;
        this.des = des;
        this.tag = tag;
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

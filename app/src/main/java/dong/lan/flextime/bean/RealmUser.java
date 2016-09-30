package dong.lan.flextime.bean;

import io.realm.RealmObject;

/**
 * Created by 梁桂栋 on 2016年09月01日 23:43.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class RealmUser extends RealmObject {
    public String id;
    public String username;
    public String password;
    public boolean sex;        //性别
    public String des;         //用户描述
    public String tag;         //用户标签


    public RealmUser(){}

    public RealmUser(String id, String username, String password, boolean sex, String des, String tag) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.des = des;
        this.tag = tag;
    }

    public User toBmobUser(){
        return new User(id,username,password,sex,des,tag);
    }
}

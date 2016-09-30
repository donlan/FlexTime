package dong.lan.flextime.bean;

import cn.bmob.v3.BmobObject;
import io.realm.RealmObject;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/13/2016  15:10.
 */
public class KeyWord extends RealmObject {

    public static final String WORD = "word";

    private String word;
    private long time;
    private int imp;
    private int urg;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getImp() {
        return imp;
    }

    public void setImp(int imp) {
        this.imp = imp;
    }

    public int getUrg() {
        return urg;
    }

    public void setUrg(int urg) {
        this.urg = urg;
    }
}

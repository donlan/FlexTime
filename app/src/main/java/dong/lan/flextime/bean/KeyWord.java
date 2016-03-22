package dong.lan.flextime.bean;

import cn.bmob.v3.BmobObject;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/13/2016  15:10.
 */
public class KeyWord extends BmobObject {
    private String word;
    private String time;
    private String imp;
    private String urg;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImp() {
        return imp;
    }

    public void setImp(String imp) {
        this.imp = imp;
    }

    public String getUrg() {
        return urg;
    }

    public void setUrg(String urg) {
        this.urg = urg;
    }
}

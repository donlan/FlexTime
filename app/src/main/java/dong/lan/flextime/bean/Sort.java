package dong.lan.flextime.bean;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 4/29/2016  12:53.
 */
public class Sort {
    private boolean isSelect;
    private String method;
    private int tag;



    public Sort(){}
    public Sort(boolean isSelect, String method) {
        this.isSelect = isSelect;
        this.method = method;
    }

    public Sort(int tag, String method, boolean isSelect) {
        this.tag = tag;
        this.method = method;
        this.isSelect = isSelect;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

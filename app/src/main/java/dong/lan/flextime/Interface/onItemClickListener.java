package dong.lan.flextime.Interface;

import dong.lan.flextime.bean.Todo;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/28/2016  12:12.
 */
public interface onItemClickListener {
    void itemClick(Todo todo, int pos, int type, boolean isMain);
}

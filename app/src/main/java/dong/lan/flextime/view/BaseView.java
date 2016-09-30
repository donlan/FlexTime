package dong.lan.flextime.view;

import android.support.v4.app.Fragment;

/**
 * Created by 梁桂栋 on 2016年08月31日 22:57.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface BaseView {
    String VIEW_TITTLE="VIEW_TITTLE";
    String VIEW_INDEX="VIEW_INDEX";

    void show(String s);
    void dialog(String text);
}

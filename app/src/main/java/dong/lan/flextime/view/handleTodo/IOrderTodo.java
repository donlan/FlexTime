package dong.lan.flextime.view.handleTodo;

import android.widget.TextView;

/**
 * Created by 梁桂栋 on 2016年09月03日 14:10.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IOrderTodo extends IAddTodo {

    void updateCountView(int count);
    int getCount();
}

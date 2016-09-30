package dong.lan.flextime.presenter.handleTodo;

import android.widget.TextView;

import dong.lan.flextime.bean.RealmToDoItem;

/**
 * Created by 梁桂栋 on 2016年09月03日 14:12.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IOrderTodoPresenter extends IAddTodoPresenter {


    void addOne();

    void updateOne(RealmToDoItem item);


    void saveAndTipKeyword(String info, TextView TipTextView);

    boolean isTodoInfoComplete();

}

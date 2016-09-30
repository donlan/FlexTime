package dong.lan.flextime.view.home;

import dong.lan.flextime.view.BaseView;

/**
 * Created by 梁桂栋 on 2016年09月01日 08:38.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IMainTodoView<T> extends BaseView {

    void setUpTodos();

    void refresh();

    void updateItem(int location);

    void deleteItem(int location);

    T getItem();

}

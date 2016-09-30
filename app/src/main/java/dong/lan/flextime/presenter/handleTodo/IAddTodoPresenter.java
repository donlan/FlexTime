package dong.lan.flextime.presenter.handleTodo;

import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import dong.lan.flextime.bean.RealmToDoItem;
import dong.lan.flextime.bean.Todo;

/**
 * Created by 梁桂栋 on 2016年09月02日 19:18.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IAddTodoPresenter {

    void setTodoToView();

    void setNeedTime();

    void setDeadTime();

    void setFinishTime();

    void done(boolean isAdd);

    void init(Todo todo);

    Todo getTodo(String id);

    void getKeyWordRecommand( String info, TextView showText);

    void setLongTime(RealmToDoItem item);

}

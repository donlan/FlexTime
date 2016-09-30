package dong.lan.flextime.view.handleTodo;

import dong.lan.flextime.bean.Todo;

/**
 * Created by 梁桂栋 on 2016年09月02日 18:57.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IAddTodo {



    void setTodoToView(Todo todo);

    void setNeedTime(String text);

    void setDeadTime(String text);

    void setFinishTime(String text);


    String getInfo();

    int getStatus();

    int getImportant();

    int getUrgent();

    int getRemind();


}

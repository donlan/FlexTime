package dong.lan.flextime.model.home;

import android.content.Context;

import java.util.List;

import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.db.TodoRepository;
import dong.lan.flextime.model.BaseTodoModel;

/**
 * Created by 梁桂栋 on 2016年09月01日 15:00.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class OverTodoModel extends BaseTodoModel {


    public OverTodoModel(Context context) {
        super(context);
        initData();
    }

    @Override
    public void initData() {
        todos = TodoRepository.getAllTimeOutTodo(realm);
    }

    public List<Todo> getAllTodos() {
        return todos;
    }
}

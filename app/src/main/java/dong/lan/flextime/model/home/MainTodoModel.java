package dong.lan.flextime.model.home;

import android.content.Context;
import android.util.Log;

import java.util.List;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.db.TodoRepository;
import dong.lan.flextime.model.BaseTodoModel;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 梁桂栋 on 2016年09月01日 08:31.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class MainTodoModel extends BaseTodoModel {


    public MainTodoModel(Context context) {
        super(context);
        initData();
    }


    public List<Todo> getAllTodo() {
        return todos;
    }

    @Override
    public void initData() {
        todos = TodoRepository.getAllTimeOnTodo(realm);
    }
}

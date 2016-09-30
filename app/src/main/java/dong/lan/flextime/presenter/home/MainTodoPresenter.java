package dong.lan.flextime.presenter.home;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.view.handleTodo.AddOrderTodoActivity;
import dong.lan.flextime.view.handleTodo.AddTodoActivity;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.model.home.MainTodoModel;
import dong.lan.flextime.presenter.BasePresenter;
import dong.lan.flextime.utils.TodoUtil;
import dong.lan.flextime.view.home.MainTodoFragment;

/**
 * Created by 梁桂栋 on 2016年09月01日 08:32.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class MainTodoPresenter extends BasePresenter<MainTodoFragment,MainTodoModel> {

    public static final int FLAG_ITEM_CLICK =1;

    public MainTodoPresenter(MainTodoFragment view, MainTodoModel model) {
        super(view, model);
    }

    public void loadTodoDatas(RecyclerView list){
        model.getAllTodo();
        list.setAdapter(view.adapter);
    }

    public void refresh(){

    }

    public void updateTodo(int location){

    }

    public void deleteTodo(int location){

    }

    public int getTodoCount(){
        if (BuildConfig.DEBUG) Log.d("MainTodoPresenter", "model.size():" + model.size());
        return model.size();
    }

    public void itemClick(int location,int flag){
        Todo todo = model.getItem(location);
        Intent intentTodo;
        if(todo.type == Todo.TYPE_SINGLE)
            intentTodo = new Intent(view.getActivity(), AddTodoActivity.class);
        else
            intentTodo = new Intent(view.getActivity(), AddOrderTodoActivity.class);
        intentTodo.putExtra("TODO_ID",todo.id);
        intentTodo.putExtra("POS",location);
        view.getActivity().startActivity(intentTodo);
    }

    public String getTodoInfo(int location){
        Todo todo = model.getItem(location);
        return TodoUtil.getTodoInfo(todo);
    }
    public Todo getItem(){
        return null;
    }


}

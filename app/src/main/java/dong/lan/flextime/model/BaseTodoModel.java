package dong.lan.flextime.model;

import android.content.Context;

import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.db.TodoRepository;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 2016年09月01日 15:35.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public abstract class BaseTodoModel implements IModel {
    protected  RealmResults<Todo> todos;
    protected  Realm realm ;

    public BaseTodoModel(Context context){
        realm = Realm.getDefaultInstance();
    }

    private void check(int location){
        if(todos==null)
            throw new NullPointerException("todos is null");
        if(location<0 || location >= size())
            throw new IndexOutOfBoundsException("index at "+location+" but size is "+ todos.size());
    }

    public abstract void initData();

    public void addTodo(Todo todo){
        TodoRepository.addTodo(todo,realm);
    }

    public boolean remove(int location){
        todos.deleteFromRealm(location);
        return true;
    }


    public int size(){
        return todos==null?0:todos.size();
    }

    public Todo getItem(int location){
        check(location);
        return todos.get(location);
    }
}

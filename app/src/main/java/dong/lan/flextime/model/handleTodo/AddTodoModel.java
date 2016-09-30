package dong.lan.flextime.model.handleTodo;

import android.content.Context;

import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.model.IModel;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 2016年09月02日 19:20.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class AddTodoModel implements IModel {
    Realm realm;

    public AddTodoModel(Context context) {
        realm = Realm.getDefaultInstance();
    }

    public Realm getRealm() {
        return realm;
    }

    public Todo getTodo(String id) {
        return realm.where(Todo.class).equalTo(Todo.ID, id).findFirst();
    }

    public Todo getTodo(){
        return new Todo();
    }
    public Todo getNotNullTodo(String id) {
        Todo todo = realm.where(Todo.class).equalTo(Todo.ID, id).findFirst();
        if (todo == null)
            todo = new Todo();
        return todo;
    }
}

package dong.lan.flextime.db;

import android.content.Context;

import dong.lan.flextime.bean.Todo;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 梁桂栋 on 2016年09月02日 08:27.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public final class TodoRepository {

    public static Realm initRealm(Context context) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).migration(new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            }
        }).schemaVersion(2).build();

        Realm.setDefaultConfiguration(realmConfig);
        return Realm.getDefaultInstance();
    }

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public static RealmResults<Todo> getAllTodo(Context context, Realm realm) {
        RealmQuery<Todo> query = realm.where(Todo.class);
        return query.findAll();
    }

    public static RealmResults<Todo> getAllTimeOnTodo(Realm realm) {
        RealmQuery<Todo> query = realm.where(Todo.class);
        query.lessThan(Todo.FLAG, Todo.FLAG_DONE);
        return query.findAllSorted(Todo.WEIGHT, Sort.DESCENDING);
    }

    public static RealmResults<Todo> getAllTimeOutTodo(Realm realm) {
        RealmQuery<Todo> query = realm.where(Todo.class);
        query.equalTo(Todo.FLAG, Todo.FLAG_OVER);
        return query.findAllSorted(Todo.WEIGHT, Sort.DESCENDING);
    }

    public static RealmResults<Todo> getAllDoneTodo(Realm realm) {
        RealmQuery<Todo> query = realm.where(Todo.class);
        query.equalTo(Todo.FLAG, Todo.FLAG_OVER);
        return query.findAllSorted(Todo.UPDATE_TIME, Sort.DESCENDING);
    }

    public static Todo addTodo(Todo todo, Realm realm) {
        realm.beginTransaction();
        Todo realmTodo = realm.copyToRealm(todo);
        realm.commitTransaction();
        return realmTodo;
    }

}
package dong.lan.flextime.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;
import dong.lan.flextime.bean.KeyWord;
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.dao.KeyWordDao;
import dong.lan.flextime.dao.ToDoItemDao;
import dong.lan.flextime.dao.TodoDao;
import dong.lan.flextime.utils.SortManager;

/**
 * Created by 梁桂栋 on 2015/12/8.
 */
public class DBManager {

    private static DBManager manager = new DBManager();
    private static DBHelper helper;

    public void init(Context context) {
        helper = DBHelper.getInstance(context);
    }

    public static synchronized DBManager getManager() {
        return manager;
    }

    /*

    添加一个todo事件
     */
    synchronized public void addAToDo(Todo todo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(TodoDao.ID, todo.getId());
            values.put(TodoDao.CREATE_TIME, todo.getCreateTime());
            values.put(TodoDao.TYPE, todo.getType());
            values.put(TodoDao.USER, todo.getUser().getUsername());
            values.put(TodoDao.WEIGHT,todo.getWeight());
            values.put(TodoDao.FLAG,todo.getFlag());
            db.replace(TodoDao.TABLE_NAME, null, values);

            SparseArray<ToDoItem> items = todo.getTodos();
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    values.clear();
                    ToDoItem toDoItem = items.get(i);
                    values.put(ToDoItemDao.COLUMN_INFO, toDoItem.getInfo());
                    values.put(ToDoItemDao.COLUMN_DONE_ONTIME, toDoItem.isDoneOnTime() ? "1" : "0");
                    values.put(ToDoItemDao.COLUMN_FINISH_TIME, toDoItem.getFinishTime());
                    values.put(ToDoItemDao.COLUMN_START_TIME, toDoItem.getStartTime());
                    values.put(ToDoItemDao.COLUMN_WEIGHT, toDoItem.getWeight());
                    values.put(ToDoItemDao.COLUMN_LOC, toDoItem.getLoc());
                    values.put(ToDoItemDao.FLAG, toDoItem.getFlag());
                    values.put(ToDoItemDao.COLUMN_STATUS,toDoItem.getStatus());
                    if (toDoItem.getPoint() != null) {
                        values.put(ToDoItemDao.COLUMN_LNG, toDoItem.getPoint().getLongitude() + "");
                        values.put(ToDoItemDao.COLUMN_LAT, toDoItem.getPoint().getLatitude() + "");
                    }
                    values.put(ToDoItemDao.COLUMN_NEED_TIME, toDoItem.getNeedTime());
                    values.put(ToDoItemDao.COLUMN_IMPORTANCE, String.valueOf(toDoItem.getImportant()));
                    values.put(ToDoItemDao.COLUMN_URGENT, String.valueOf(toDoItem.getUrgent()));
                    values.put(ToDoItemDao.COLUMN_CREATE_TIME, toDoItem.getCreateTime());
                    values.put(ToDoItemDao.ID, toDoItem.getId());
                    values.put(ToDoItemDao.SUB_ID, toDoItem.getSubId());
                    values.put(ToDoItemDao.SEQ, toDoItem.getSeq());
                    values.put(ToDoItemDao.COLUMN_CONTINUE_DO, toDoItem.getContinueDo() ? "1" : "0");
                    values.put(ToDoItemDao.COLUMN_LAST_DO_TIME, toDoItem.getSubId());
                    values.put(ToDoItemDao.COLUMN_REMIND, toDoItem.getRemind() + "");
                    values.put(ToDoItemDao.COLUMN_DEADLINE, toDoItem.getDeadline());
                    db.replace(ToDoItemDao.COLUMN_TABLE_NAME, null, values);
                }
            }

        }
    }

    synchronized public SparseArray<ToDoItem> getTodoItems(String id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()){
            Cursor cursor = db.rawQuery("select * from todo_item where id=? order by seq asc",new String[]{id});
            if (cursor.moveToFirst()){
                SparseArray<ToDoItem> items = new SparseArray<>();
                int i = 0;
                do{
                    ToDoItem toDoItem = new ToDoItem();
                    toDoItem.setDoneOnTime(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_DONE_ONTIME)).equals("1"));
                    toDoItem.setFinishTime(cursor.getLong(cursor.getColumnIndex(ToDoItemDao.COLUMN_FINISH_TIME)));
                    toDoItem.setInfo(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_INFO)));
                    toDoItem.setFlag(cursor.getInt(cursor.getColumnIndex(ToDoItemDao.FLAG)));
                    toDoItem.setStatus(cursor.getInt(cursor.getColumnIndex(ToDoItemDao.COLUMN_STATUS)));
                    toDoItem.setLoc(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_LOC)));
                    String lat = cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_LAT));
                    if (lat != null && !lat.equals(""))
                        toDoItem.setPoint(new BmobGeoPoint(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_LNG)))
                                , Double.valueOf(lat)));
                    toDoItem.setNeedTime(cursor.getLong(cursor.getColumnIndex(ToDoItemDao.COLUMN_NEED_TIME)));
                    toDoItem.setImportant(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_IMPORTANCE))));
                    toDoItem.setUrgent(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_URGENT))));
                    toDoItem.setStartTime(cursor.getLong(cursor.getColumnIndex(ToDoItemDao.COLUMN_START_TIME)));
                    toDoItem.setWeight(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_WEIGHT))));
                    toDoItem.setCreateTime(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_CREATE_TIME)));
                    toDoItem.setId(cursor.getString(cursor.getColumnIndex(ToDoItemDao.ID)));
                    toDoItem.setSubId(cursor.getString(cursor.getColumnIndex(ToDoItemDao.SUB_ID)));
                    toDoItem.setSeq(cursor.getInt(cursor.getColumnIndex(ToDoItemDao.SEQ)));
                    toDoItem.setRemind(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_REMIND))));
                    toDoItem.setContinueDo(cursor.getString(cursor.getColumnIndex(ToDoItemDao.COLUMN_CONTINUE_DO)).equals("1"));
                    toDoItem.setDeadline(cursor.getLong(cursor.getColumnIndex(ToDoItemDao.COLUMN_DEADLINE)));
                    items.put(i,toDoItem);
                    i++;

                }while (cursor.moveToNext());
                cursor.close();

                return items;
            }
            cursor.close();

        }
        return null;
    }
    /*

    获取数据库的所有todo事件
     */
    synchronized public List<Todo> getAllTodos() {

        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TodoDao.TABLE_NAME
                    + " order by " + ToDoItemDao.COLUMN_WEIGHT + " desc", null);
            List<Todo> todos = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do{
                    Todo todo = new Todo();
                    todo.setCreateTime(cursor.getString(cursor.getColumnIndex(TodoDao.CREATE_TIME)));
                    todo.setId(cursor.getString(cursor.getColumnIndex(TodoDao.ID)));
                    todo.setType(cursor.getInt(cursor.getColumnIndex(TodoDao.TYPE)));
                    todo.setFlag(cursor.getInt(cursor.getColumnIndex(TodoDao.FLAG)));
                    todo.setWeight(cursor.getDouble(cursor.getColumnIndex(TodoDao.WEIGHT)));
                    todo.setUser(new User(cursor.getString(cursor.getColumnIndex(TodoDao.USER))));
                    todo.setTodos(getTodoItems(cursor.getString(cursor.getColumnIndex(TodoDao.ID))));
                    todos.add(todo);
                }while(cursor.moveToNext());
                cursor.close();
                return todos;
            }
        }
        return null;
    }


    /*
    查询指定ID的Todo
     */
    synchronized public Todo getTodoByID(String id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from todo where id = ?", new String[]{id});
            if (cursor.moveToFirst())
            {
                Todo todo = new Todo();
                todo.setCreateTime(cursor.getString(cursor.getColumnIndex(TodoDao.CREATE_TIME)));
                todo.setId(cursor.getString(cursor.getColumnIndex(TodoDao.ID)));
                todo.setType(cursor.getInt(cursor.getColumnIndex(TodoDao.TYPE)));
                todo.setFlag(cursor.getInt(cursor.getColumnIndex(TodoDao.FLAG)));
                todo.setWeight(cursor.getDouble(cursor.getColumnIndex(TodoDao.WEIGHT)));
                todo.setUser(new User(cursor.getString(cursor.getColumnIndex(TodoDao.USER))));
                todo.setTodos(getTodoItems(cursor.getString(cursor.getColumnIndex(TodoDao.ID))));
                cursor.close();
               return todo;
            }else{
                cursor.close();
                return null;
            }
        }
        return null;
    }

    /*
    查询本地所有的日程
     */
    synchronized public List<Todo> getAllTodoLable()
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TodoDao.TABLE_NAME, null);
            List<Todo> todos = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do{
                    Todo todo = new Todo();
                    todo.setCreateTime(cursor.getString(cursor.getColumnIndex(TodoDao.CREATE_TIME)));
                    todo.setId(cursor.getString(cursor.getColumnIndex(TodoDao.ID)));
                    todo.setType(cursor.getInt(cursor.getColumnIndex(TodoDao.TYPE)));
                    todo.setFlag(cursor.getInt(cursor.getColumnIndex(TodoDao.FLAG)));
                    todo.setWeight(cursor.getDouble(cursor.getColumnIndex(TodoDao.WEIGHT)));
                    todo.setUser(new User(cursor.getString(cursor.getColumnIndex(TodoDao.USER))));
                    todos.add(todo);
                }while(cursor.moveToNext());
                cursor.close();
                return todos;
            }
        }
        return null;
    }

    synchronized public void updateTodoLable(ContentValues values, String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
            db.update(TodoDao.TABLE_NAME, values, TodoDao.ID + " =? ", new String[]{id});
    }

    synchronized public void addTodoLable(Todo todo)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(TodoDao.ID, todo.getId());
            values.put(TodoDao.CREATE_TIME, todo.getCreateTime());
            values.put(TodoDao.TYPE, todo.getType());
            values.put(TodoDao.USER, todo.getUser().getUsername());
            values.put(TodoDao.WEIGHT,todo.getWeight());
            db.insert(TodoDao.TABLE_NAME, null, values);
        }

    }
    synchronized public List<Todo> getAllTimeOutTodos() {

        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TodoDao.TABLE_NAME
                    + " where flag = ?  order by " + TodoDao.WEIGHT + " desc", new String[]{""+TodoDao.FLAG_TIME_OUT});
            List<Todo> todos = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do{
                    Todo todo = new Todo();
                    todo.setCreateTime(cursor.getString(cursor.getColumnIndex(TodoDao.CREATE_TIME)));
                    todo.setId(cursor.getString(cursor.getColumnIndex(TodoDao.ID)));
                    todo.setType(cursor.getInt(cursor.getColumnIndex(TodoDao.TYPE)));
                    todo.setFlag(cursor.getInt(cursor.getColumnIndex(TodoDao.FLAG)));
                    todo.setUser(new User(cursor.getString(cursor.getColumnIndex(TodoDao.USER))));
                    todo.setWeight(cursor.getDouble(cursor.getColumnIndex(TodoDao.WEIGHT)));
                    todo.setTodos(getTodoItems(cursor.getString(cursor.getColumnIndex(TodoDao.ID))));
                    todos.add(todo);
                }while(cursor.moveToNext());
                cursor.close();
                return todos;
            }
        }
        return null;
    }


    synchronized public List<Todo> getAllTimeOnTodos() {

        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + TodoDao.TABLE_NAME
                    + " where flag <= ?  order by " + TodoDao.WEIGHT + " desc", new String[]{""+TodoDao.FLAG_ON});
            List<Todo> todos = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do{
                    Todo todo = new Todo();
                    todo.setCreateTime(cursor.getString(cursor.getColumnIndex(TodoDao.CREATE_TIME)));
                    todo.setId(cursor.getString(cursor.getColumnIndex(TodoDao.ID)));
                    todo.setType(cursor.getInt(cursor.getColumnIndex(TodoDao.TYPE)));
                    todo.setFlag(cursor.getInt(cursor.getColumnIndex(TodoDao.FLAG)));
                    todo.setWeight(cursor.getDouble(cursor.getColumnIndex(TodoDao.WEIGHT)));
                    todo.setUser(new User(cursor.getString(cursor.getColumnIndex(TodoDao.USER))));
                    todo.setTodos(getTodoItems(cursor.getString(cursor.getColumnIndex(TodoDao.ID))));
                    todos.add(todo);
                }while(cursor.moveToNext());
                cursor.close();
                return todos;
            }
        }
        return null;
    }



    synchronized public void updateTodo(Todo todo)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            for (int i = 0; i < todo.getTodos().size(); i++) {
                updateTodoItem(db, todo.getTodos().get(i));
            }
            updateTodoWeight(db, todo);
        }
    }

    synchronized public void updateTodoWeight(Todo todo)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put(TodoDao.WEIGHT,todo.getWeight());
            values.put(TodoDao.FLAG,todo.getFlag());
            db.update(TodoDao.TABLE_NAME,values,"id = ? ",new String[]{todo.getId()});
        }
    }

    synchronized public void updateTodoWeight(SQLiteDatabase db,Todo todo)
    {
        if (db.isOpen())
        {
            ContentValues values = new ContentValues();
            values.put(TodoDao.WEIGHT,todo.getWeight());
            values.put(TodoDao.FLAG,todo.getFlag());
            db.update(TodoDao.TABLE_NAME,values,"id = ? ",new String[]{todo.getId()});
        }
    }

    synchronized public void updateTodo(ContentValues values, String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
            db.update(TodoDao.TABLE_NAME, values, TodoDao.ID + " =? ", new String[]{id});
    }

    /*
    更新todoitem事件
     */
    synchronized public void updateTodoItem(ContentValues values, String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
            db.update(ToDoItemDao.COLUMN_TABLE_NAME, values, ToDoItemDao.ID + " =? ", new String[]{id});
    }


    synchronized public void updateTodoItem(ToDoItem toDoItem) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ToDoItemDao.COLUMN_INFO, toDoItem.getInfo());
            values.put(ToDoItemDao.COLUMN_DONE_ONTIME, toDoItem.isDoneOnTime() ? "1" : "0");
            values.put(ToDoItemDao.COLUMN_FINISH_TIME, toDoItem.getFinishTime());
            values.put(ToDoItemDao.COLUMN_START_TIME, toDoItem.getStartTime());
            values.put(ToDoItemDao.COLUMN_WEIGHT, toDoItem.getWeight());
            values.put(ToDoItemDao.COLUMN_LOC, toDoItem.getLoc());
            values.put(ToDoItemDao.FLAG, toDoItem.getFlag());
            values.put(ToDoItemDao.COLUMN_DEADLINE, toDoItem.getDeadline());
            values.put(ToDoItemDao.COLUMN_STATUS,toDoItem.getStatus());
            if (toDoItem.getPoint() != null) {
                values.put(ToDoItemDao.COLUMN_LNG, toDoItem.getPoint().getLongitude() + "");
                values.put(ToDoItemDao.COLUMN_LAT, toDoItem.getPoint().getLatitude() + "");
            }
            values.put(ToDoItemDao.COLUMN_WEIGHT, SortManager.getSortWeight(toDoItem));
            values.put(ToDoItemDao.COLUMN_NEED_TIME, toDoItem.getNeedTime());
            values.put(ToDoItemDao.COLUMN_IMPORTANCE, String.valueOf(toDoItem.getImportant()));
            values.put(ToDoItemDao.COLUMN_URGENT, String.valueOf(toDoItem.getUrgent()));
            values.put(ToDoItemDao.COLUMN_CREATE_TIME, toDoItem.getCreateTime());
            values.put(ToDoItemDao.COLUMN_CONTINUE_DO, toDoItem.getContinueDo() ? "1" : "0");
            values.put(ToDoItemDao.COLUMN_LAST_DO_TIME, toDoItem.getSubId());
            values.put(ToDoItemDao.COLUMN_REMIND, toDoItem.getRemind() + "");
            db.update(ToDoItemDao.COLUMN_TABLE_NAME, values, ToDoItemDao.SUB_ID + " =? ", new String[]{toDoItem.getSubId()});
        }
    }


    synchronized public void updateTodoItem(SQLiteDatabase db,ToDoItem toDoItem) {
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ToDoItemDao.COLUMN_INFO, toDoItem.getInfo());
            values.put(ToDoItemDao.COLUMN_DONE_ONTIME, toDoItem.isDoneOnTime() ? "1" : "0");
            values.put(ToDoItemDao.COLUMN_FINISH_TIME, toDoItem.getFinishTime());
            values.put(ToDoItemDao.COLUMN_START_TIME, toDoItem.getStartTime());
            values.put(ToDoItemDao.COLUMN_WEIGHT, toDoItem.getWeight() + "");
            values.put(ToDoItemDao.COLUMN_LOC, toDoItem.getLoc());
            values.put(ToDoItemDao.FLAG, toDoItem.getFlag());
            values.put(ToDoItemDao.COLUMN_STATUS,toDoItem.getStatus());
            values.put(ToDoItemDao.COLUMN_DEADLINE, toDoItem.getDeadline());
            values.put(ToDoItemDao.COLUMN_WEIGHT, SortManager.getSortWeight(toDoItem));
            if (toDoItem.getPoint() != null) {
                values.put(ToDoItemDao.COLUMN_LNG, toDoItem.getPoint().getLongitude() + "");
                values.put(ToDoItemDao.COLUMN_LAT, toDoItem.getPoint().getLatitude() + "");
            }
            values.put(ToDoItemDao.COLUMN_NEED_TIME, toDoItem.getNeedTime());
            values.put(ToDoItemDao.COLUMN_IMPORTANCE, String.valueOf(toDoItem.getImportant()));
            values.put(ToDoItemDao.COLUMN_URGENT, String.valueOf(toDoItem.getUrgent()));
            values.put(ToDoItemDao.COLUMN_CREATE_TIME, toDoItem.getCreateTime());
            values.put(ToDoItemDao.COLUMN_CONTINUE_DO, toDoItem.getContinueDo() ? "1" : "0");
            values.put(ToDoItemDao.COLUMN_LAST_DO_TIME, toDoItem.getSubId());
            values.put(ToDoItemDao.COLUMN_REMIND, toDoItem.getRemind() + "");
            db.update(ToDoItemDao.COLUMN_TABLE_NAME, values, ToDoItemDao.SUB_ID + " =? ", new String[]{toDoItem.getSubId()});
        }
    }

    /*
    删除一个todo事件
     */
    synchronized public void deleteTodo(String id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(TodoDao.TABLE_NAME, TodoDao.ID + " =? ", new String[]{id});
            deleteTodoItemCascade(id,db);
        }
    }


    synchronized public void deleteTodoItem(String id)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
            db.delete(ToDoItemDao.COLUMN_TABLE_NAME, ToDoItemDao.ID + " =? ", new String[]{id});
    }


    synchronized public void deleteTodoItemCascade(String id,SQLiteDatabase db)
    {
        if (db.isOpen())
            db.delete(ToDoItemDao.COLUMN_TABLE_NAME, ToDoItemDao.ID + " =? ", new String[]{id});
    }
    synchronized public KeyWord getKeyword(String word) {
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from keyword where word = ?", new String[]{word});
            int wordIndex = cursor.getColumnIndex(KeyWordDao.WORD);
            int timeIndex = cursor.getColumnIndex(KeyWordDao.TIME);
            int impIndex = cursor.getColumnIndex(KeyWordDao.IMP);
            int urgIndex = cursor.getColumnIndex(KeyWordDao.URG);
            if (cursor.moveToFirst()) {
                KeyWord keyWord = new KeyWord();
                keyWord.setWord(cursor.getString(wordIndex));
                keyWord.setTime(cursor.getString(timeIndex));
                keyWord.setImp(cursor.getString(impIndex));
                keyWord.setUrg(cursor.getString(urgIndex));
                return keyWord;
            }
            cursor.close();
        }

        return null;
    }

    synchronized public List<KeyWord> getAllKeyword() {
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(KeyWordDao.TABLE_NAME, null, null, null, null, null, null);
            if (!cursor.moveToFirst()) {
                return null;
            }
            List<KeyWord> keyWords = new ArrayList<>();
            int wordIndex = cursor.getColumnIndex(KeyWordDao.WORD);
            int timeIndex = cursor.getColumnIndex(KeyWordDao.TIME);
            int impIndex = cursor.getColumnIndex(KeyWordDao.IMP);
            int urgIndex = cursor.getColumnIndex(KeyWordDao.URG);

            do {
                KeyWord keyWord = new KeyWord();
                keyWord.setWord(cursor.getString(wordIndex));
                keyWord.setTime(cursor.getString(timeIndex));
                keyWord.setImp(cursor.getString(impIndex));
                keyWord.setUrg(cursor.getString(urgIndex));
                keyWords.add(keyWord);
            } while (cursor.moveToNext());
            cursor.close();
            return keyWords;
        }

        return null;
    }

    synchronized public List<KeyWord> getLikeKeyword(String word) {
        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from keyword where word = ?", new String[]{word});
            if (!cursor.moveToFirst()) {
                return null;
            }
            List<KeyWord> keyWords = new ArrayList<>();
            int wordIndex = cursor.getColumnIndex(KeyWordDao.WORD);
            int timeIndex = cursor.getColumnIndex(KeyWordDao.TIME);
            int impIndex = cursor.getColumnIndex(KeyWordDao.IMP);
            int urgIndex = cursor.getColumnIndex(KeyWordDao.URG);
            do {
                KeyWord keyWord = new KeyWord();
                keyWord.setWord(cursor.getString(wordIndex));
                keyWord.setTime(cursor.getString(timeIndex));
                keyWord.setImp(cursor.getString(impIndex));
                keyWord.setUrg(cursor.getString(urgIndex));
                keyWords.add(keyWord);
            } while (cursor.moveToNext());
            cursor.close();
            return keyWords;
        }

        return null;
    }

    synchronized public void addKeyword(String word, String time, double imp, double urg) {
        KeyWord keyWord = getKeyword(word);
        if (keyWord == null) {
            SQLiteDatabase db = helper.getReadableDatabase();
            if (db.isOpen()) {
                ContentValues values = new ContentValues();
                values.put("word", word);
                values.put("time", time);
                values.put("imp", imp + "");
                values.put("urg", urg + "");
                db.replace("keyword", null, values);
            }
        } else {
            SQLiteDatabase db = helper.getReadableDatabase();
            if (db.isOpen()) {
                keyWord.setTime(((Long.parseLong(keyWord.getTime()) + Long.parseLong(time)) / 2) + "");
                keyWord.setImp("" + (Double.parseDouble(keyWord.getImp()) + imp) / 2);
                keyWord.setUrg("" + (Double.parseDouble(keyWord.getUrg()) + urg) / 2);
                ContentValues values = new ContentValues();
                values.put("time", keyWord.getTime());
                values.put("imp", keyWord.getImp());
                values.put("urg", keyWord.getUrg());
                db.update("keyword", values, "word = ?", new String[]{word});
            }
        }
    }
}

package dong.lan.flextime.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobGeoPoint;
import dong.lan.flextime.bean.KeyWord;
import dong.lan.flextime.bean.ToDo;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.dao.KeyWordDao;
import dong.lan.flextime.dao.ToDoDao;

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
    synchronized public void addAToDo(ToDo toDo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ToDoDao.COLUMN_INFO, toDo.getInfo());
            values.put(ToDoDao.COLUMN_DONE_ONTIME, toDo.isDoneOnTime() ? "1" : "0");
            values.put(ToDoDao.COLUMN_FINISH_TIME, toDo.getFinishTime());
            values.put(ToDoDao.COLUMN_START_TIME, toDo.getStartTime());
            values.put(ToDoDao.COLUMN_USERNAME, toDo.getUser().getUsername());
            values.put(ToDoDao.COLUMN_WEIGHT, toDo.getWeight() + "");
            values.put(ToDoDao.COLUMN_LOC, toDo.getLoc());
            values.put(ToDoDao.COLUMN_LEVEL, toDo.getStatus() + "");
            if (toDo.getPoint() != null) {
                values.put(ToDoDao.COLUMN_LNG, toDo.getPoint().getLongitude() + "");
                values.put(ToDoDao.COLUMN_LAT, toDo.getPoint().getLatitude() + "");
            }
            values.put(ToDoDao.COLUMN_NEED_TIME, toDo.getNeedTime());
            values.put(ToDoDao.COLUMN_IMPORTANCE, String.valueOf(toDo.getImportant()));
            values.put(ToDoDao.COLUMN_URGENT, String.valueOf(toDo.getUrgent()));
            values.put(ToDoDao.COLUMN_CREATE_TIME, toDo.getCreateTime());
            values.put(ToDoDao.COLUMN_OBJECT_ID, toDo.getObjectId());
            values.put(ToDoDao.COLUMN_CONTINUE_DO, toDo.getContinueDo() ? "1" : "0");
            values.put(ToDoDao.COLUMN_LAST_DO_TIME, toDo.getLastDoTime());
            values.put(ToDoDao.COLUMN_PERCONTINUE_TIME, toDo.getPerContinueTime() + "");
            values.put(ToDoDao.COLUMN_REMIND, toDo.getRemind() + "");
            values.put(ToDoDao.COLUMN_DEADLINE, toDo.getDeadline());
            db.replace(ToDoDao.COLUMN_TABLE_NAME, null, values);
        }
    }

    /*

    获取数据库的所有todo事件
     */
    synchronized public List<ToDo> getAllTodos() {

        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + ToDoDao.COLUMN_TABLE_NAME
                    + " order by " + ToDoDao.COLUMN_WEIGHT + " desc", null);
            List<ToDo> toDos = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    ToDo toDo = new ToDo();
                    toDo.setUser(new User(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_USERNAME))));
                    toDo.setDoneOnTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_DONE_ONTIME)).equals("1"));
                    toDo.setFinishTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_FINISH_TIME)));
                    toDo.setInfo(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_INFO)));
                    toDo.setLevel(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LEVEL)));
                    toDo.setStatus(cursor.getInt(cursor.getColumnIndex(ToDoDao.COLUMN_STATUS)));
                    toDo.setLoc(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LOC)));
                    String lat = cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LAT));
                    if (lat != null && !lat.equals(""))
                        toDo.setPoint(new BmobGeoPoint(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LNG)))
                                , Double.valueOf(lat)));
                    toDo.setNeedTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_NEED_TIME)));
                    toDo.setImportant(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_IMPORTANCE))));
                    toDo.setUrgent(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_URGENT))));
                    toDo.setStartTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_START_TIME)));
                    toDo.setWeight(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_WEIGHT))));
                    toDo.setCreateTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_CREATE_TIME)));
                    toDo.setLastDoTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LAST_DO_TIME)));
                    toDo.setRemind(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_REMIND))));
                    toDo.setContinueDo(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_CONTINUE_DO)).equals("1"));
                    toDo.setPerContinueTime(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_PERCONTINUE_TIME))));
                    toDo.setDeadline(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_DEADLINE)));
                    toDos.add(toDo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return toDos;
        }
        return null;
    }

    synchronized public List<ToDo> getAllTimeOutTodos() {

        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(ToDoDao.COLUMN_TABLE_NAME, null, "level=?", new String[]{"0"}, null, null, " start_time asc ");

            List<ToDo> toDos = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    ToDo toDo = new ToDo();
                    toDo.setUser(new User(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_USERNAME))));
                    toDo.setDoneOnTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_DONE_ONTIME)).equals("1"));
                    toDo.setFinishTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_FINISH_TIME)));
                    toDo.setInfo(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_INFO)));
                    toDo.setLevel(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LEVEL)));
                    toDo.setLoc(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LOC)));
                    toDo.setStatus(cursor.getInt(cursor.getColumnIndex(ToDoDao.COLUMN_STATUS)));
                    String lat = cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LAT));
                    if (lat != null && !lat.equals(""))
                        toDo.setPoint(new BmobGeoPoint(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LNG)))
                                , Double.valueOf(lat)));
                    toDo.setNeedTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_NEED_TIME)));
                    toDo.setImportant(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_IMPORTANCE))));
                    toDo.setUrgent(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_URGENT))));
                    toDo.setStartTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_START_TIME)));
                    toDo.setWeight(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_WEIGHT))));
                    toDo.setCreateTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_CREATE_TIME)));
                    toDo.setLastDoTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LAST_DO_TIME)));
                    toDo.setRemind(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_REMIND))));
                    toDo.setContinueDo(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_CONTINUE_DO)).equals("1"));
                    toDo.setPerContinueTime(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_PERCONTINUE_TIME))));
                    toDo.setDeadline(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_DEADLINE)));
                    toDos.add(toDo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return toDos;
        }
        return null;
    }

    synchronized public List<ToDo> getAllTimeOnTodos() {

        SQLiteDatabase db = helper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.query(ToDoDao.COLUMN_TABLE_NAME, null, "level!=?", new String[]{"0"}, null, null, " weight desc ");

            List<ToDo> toDos = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    ToDo toDo = new ToDo();
                    toDo.setUser(new User(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_USERNAME))));
                    toDo.setDoneOnTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_DONE_ONTIME)).equals("1"));
                    toDo.setFinishTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_FINISH_TIME)));
                    toDo.setInfo(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_INFO)));
                    toDo.setLevel(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LEVEL)));
                    toDo.setLoc(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LOC)));
                    toDo.setStatus(cursor.getInt(cursor.getColumnIndex(ToDoDao.COLUMN_STATUS)));
                    String lat = cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LAT));
                    if (lat != null && !lat.equals(""))
                        toDo.setPoint(new BmobGeoPoint(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LNG)))
                                , Double.valueOf(lat)));
                    toDo.setNeedTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_NEED_TIME)));
                    toDo.setImportant(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_IMPORTANCE))));
                    toDo.setUrgent(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_URGENT))));
                    toDo.setStartTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_START_TIME)));
                    toDo.setWeight(Double.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_WEIGHT))));
                    toDo.setCreateTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_CREATE_TIME)));
                    toDo.setLastDoTime(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_LAST_DO_TIME)));
                    toDo.setRemind(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_REMIND))));
                    toDo.setContinueDo(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_CONTINUE_DO)).equals("1"));
                    toDo.setPerContinueTime(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_PERCONTINUE_TIME))));
                    toDo.setDeadline(cursor.getString(cursor.getColumnIndex(ToDoDao.COLUMN_DEADLINE)));
                    toDos.add(toDo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return toDos;
        }
        return null;
    }


    /*
    更新todo事件
     */
    synchronized public void updateTodo(ContentValues values, String createTime) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
            db.update(ToDoDao.COLUMN_TABLE_NAME, values, ToDoDao.COLUMN_CREATE_TIME + " =? ", new String[]{createTime});
    }


    synchronized public void updateTodo(ToDo toDo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ToDoDao.COLUMN_INFO, toDo.getInfo());
            values.put(ToDoDao.COLUMN_DONE_ONTIME, toDo.isDoneOnTime() ? "1" : "0");
            values.put(ToDoDao.COLUMN_FINISH_TIME, toDo.getFinishTime());
            values.put(ToDoDao.COLUMN_START_TIME, toDo.getStartTime());
            values.put(ToDoDao.COLUMN_USERNAME, toDo.getUser().getUsername());
            values.put(ToDoDao.COLUMN_WEIGHT, toDo.getWeight() + "");
            values.put(ToDoDao.COLUMN_LOC, toDo.getLoc());
            values.put(ToDoDao.COLUMN_LEVEL, toDo.getLevel());
            values.put(ToDoDao.COLUMN_DEADLINE, toDo.getDeadline());
            if (toDo.getPoint() != null) {
                values.put(ToDoDao.COLUMN_LNG, toDo.getPoint().getLongitude() + "");
                values.put(ToDoDao.COLUMN_LAT, toDo.getPoint().getLatitude() + "");
            }
            values.put(ToDoDao.COLUMN_NEED_TIME, toDo.getNeedTime());
            values.put(ToDoDao.COLUMN_IMPORTANCE, String.valueOf(toDo.getImportant()));
            values.put(ToDoDao.COLUMN_URGENT, String.valueOf(toDo.getUrgent()));
            values.put(ToDoDao.COLUMN_CREATE_TIME, toDo.getCreateTime());
            values.put(ToDoDao.COLUMN_OBJECT_ID, toDo.getObjectId());
            values.put(ToDoDao.COLUMN_CONTINUE_DO, toDo.getContinueDo() ? "1" : "0");
            values.put(ToDoDao.COLUMN_LAST_DO_TIME, toDo.getLastDoTime());
            values.put(ToDoDao.COLUMN_PERCONTINUE_TIME, toDo.getPerContinueTime() + "");
            values.put(ToDoDao.COLUMN_REMIND, toDo.getRemind() + "");
            db.update(ToDoDao.COLUMN_TABLE_NAME, values, ToDoDao.COLUMN_CREATE_TIME + " =? ", new String[]{toDo.getCreateTime()});
        }
    }

    /*
    删除一个todo事件
     */
    synchronized public void deleteTodo(String createTime) {
        SQLiteDatabase db = helper.getWritableDatabase();
        if (db.isOpen())
            db.delete(ToDoDao.COLUMN_TABLE_NAME, ToDoDao.COLUMN_CREATE_TIME + " =? ", new String[]{createTime});
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
        KeyWord keyWord= getKeyword(word);
            if( keyWord==null)
            {
                SQLiteDatabase db = helper.getReadableDatabase();
                if(db.isOpen())
                {
                    ContentValues values = new ContentValues();
                    values.put("word",word);
                    values.put("time",time);
                    values.put("imp",imp+"");
                    values.put("urg",urg+"");
                    db.replace("keyword",null,values);
                }
            }else
            {
                SQLiteDatabase db = helper.getReadableDatabase();
                if(db.isOpen()) {
                    keyWord.setTime(((Long.parseLong(keyWord.getTime()) + Long.parseLong(time)) / 2) + "");
                    keyWord.setImp("" + (Double.parseDouble(keyWord.getImp()) + imp) / 2);
                    keyWord.setUrg("" + (Double.parseDouble(keyWord.getUrg()) + urg) / 2);
                    ContentValues values = new ContentValues();
                    values.put("time", keyWord.getTime());
                    values.put("imp", keyWord.getImp());
                    values.put("urg", keyWord.getUrg());
                    db.update("keyword",values,"word = ?",new String[]{word});
                }
            }
    }
}

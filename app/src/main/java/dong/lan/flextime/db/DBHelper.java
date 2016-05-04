package dong.lan.flextime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.dao.ToDoItemDao;

/**
 * Created by 梁桂栋 on 2015/12/7.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "DB_FlexTime.db";
    public static final int VERSION = 2;

    private static final String CREATE_TIME_TABLE = "create table "
            + ToDoItemDao.COLUMN_TABLE_NAME + " ("
            + ToDoItemDao.COLUMN_CREATE_TIME + " TEXT PRIMARY KEY , "
            + ToDoItemDao.ID + " TEXT, "
            + ToDoItemDao.SUB_ID + " TEXT, "
            + ToDoItemDao.SEQ + " TEXT, "
            + ToDoItemDao.COLUMN_START_TIME + " int, "
            + ToDoItemDao.COLUMN_FINISH_TIME + " int, "
            + ToDoItemDao.COLUMN_DEADLINE + " int, "
            + ToDoItemDao.COLUMN_NEED_TIME + " int, "
            + ToDoItemDao.COLUMN_IMPORTANCE + " TEXT, "
            + ToDoItemDao.COLUMN_URGENT + " TEXT, "
            + ToDoItemDao.COLUMN_INFO + " TEXT, "
            + ToDoItemDao.FLAG + " smallint, "
            + ToDoItemDao.COLUMN_STATUS + " TEXT, "
            + ToDoItemDao.COLUMN_WEIGHT + " TEXT, "
            + ToDoItemDao.COLUMN_CONTINUE_DO + " TEXT, "
            + ToDoItemDao.COLUMN_LAST_DO_TIME + " TEXT, "
            + ToDoItemDao.COLUMN_REMIND + " TEXT, "
            + ToDoItemDao.COLUMN_LOC + " TEXT, "
            + ToDoItemDao.COLUMN_DONE_ONTIME + " TEXT, "
            + ToDoItemDao.COLUMN_LAT + " TEXT, "
            + ToDoItemDao.COLUMN_LNG + " TEXT);";

    private static final String TODO_TABLE="create table todo(id text primary key,time text,user text,type smallint,weight float,flag smallint)";
    private static final String CREATE_KEYWORD_TABLE="create table keyword(word text primary key,time text,imp text,urg text);";
    private static final String SORT_TABLE="create table sort(tag int primary key,isSelect int , method text)";


    private DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    private static DBHelper helper;

    public static DBHelper getInstance(Context context) {
        if (helper == null) {
            helper = new DBHelper(context.getApplicationContext());
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TODO_TABLE);
        db.execSQL(CREATE_TIME_TABLE);
        db.execSQL(CREATE_KEYWORD_TABLE);
        try {
            db.execSQL(SORT_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeDB() {
        if (helper != null) {
            try {
                SQLiteDatabase database = helper.getWritableDatabase();
                database.close();
            } catch (Exception E) {
                E.printStackTrace();
            }
        }
        helper = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==2 || oldVersion==1){
            try {
                db.execSQL(SORT_TABLE);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (BuildConfig.DEBUG) Log.d("DBHelper", "oldVersion:" + oldVersion+"   newVersion:"+newVersion);
    }
}

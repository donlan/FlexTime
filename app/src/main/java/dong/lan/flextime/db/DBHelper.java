package dong.lan.flextime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dong.lan.flextime.dao.ToDoDao;

/**
 * Created by 梁桂栋 on 2015/12/7.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "DB_FlexTime.db";
    public static final int VERSION = 1;

    private static final String CREATE_TIME_TABLE = "create table "
            + ToDoDao.COLUMN_TABLE_NAME + " ("
            + ToDoDao.COLUMN_CREATE_TIME + " TEXT PRIMARY KEY , "
            + ToDoDao.COLUMN_OBJECT_ID + " TEXT, "
            + ToDoDao.COLUMN_USERNAME + " TEXT, "
            + ToDoDao.COLUMN_START_TIME + " TEXT, "
            + ToDoDao.COLUMN_FINISH_TIME + " TEXT, "
            + ToDoDao.COLUMN_DEADLINE + " TEXT, "
            + ToDoDao.COLUMN_NEED_TIME + " TEXT, "
            + ToDoDao.COLUMN_IMPORTANCE + " TEXT, "
            + ToDoDao.COLUMN_URGENT + " TEXT, "
            + ToDoDao.COLUMN_INFO + " TEXT, "
            + ToDoDao.COLUMN_LEVEL + " TEXT, "
            + ToDoDao.COLUMN_STATUS + " TEXT, "
            + ToDoDao.COLUMN_WEIGHT + " TEXT, "
            + ToDoDao.COLUMN_CONTINUE_DO + " TEXT, "
            + ToDoDao.COLUMN_LAST_DO_TIME + " TEXT, "
            + ToDoDao.COLUMN_PERCONTINUE_TIME + " TEXT, "
            + ToDoDao.COLUMN_REMIND + " TEXT, "
            + ToDoDao.COLUMN_LOC + " TEXT, "
            + ToDoDao.COLUMN_DONE_ONTIME + " TEXT, "
            + ToDoDao.COLUMN_LAT + " TEXT, "
            + ToDoDao.COLUMN_LNG + " TEXT);";


    private static final String CREATE_KEYWORD_TABLE="create table keyword(word text primary key,time text,imp text,urg text);";
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
        db.execSQL(CREATE_TIME_TABLE);
        db.execSQL(CREATE_KEYWORD_TABLE);
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

    }
}

package dong.lan.flextime.dao;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/26/2016  11:23.
 */
public class TodoDao {

    public static final int TYPE_SINGLE=1;
    public static final int TYPE_MUTIL=2;
    public static final int TYPE_ORDER=3;
    public static final int FLAG_FINISH=8;
    public static final int FLAG_ON=4;
    public static final int FLAG_TIME_OUT=6;
    public static final int FLAG_UPDATE = 3;
    public static final int FLAG_SWAP = 0;
    public static final int FLAG_DONE = 7;
    public static final int FLAG_DOIT =5;
    public static final String TABLE_NAME="todo";
    public static final String USER="user";
    public static final String TYPE="type";
    public static final String ID="id";
    public static final String CREATE_TIME="time";
    public static final String FLAG="flag";
    public static final String WEIGHT="weight";
}

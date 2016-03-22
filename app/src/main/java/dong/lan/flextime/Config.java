package dong.lan.flextime;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2016/2/1  12:27.
 */
public class Config {

    //个人状态
    public static final int BAD = 0;
    public static final int GOOD = 1;

    //使用模式
    public static final int WORK = 2;
    public static final int BUSY = 3;
    public static final int NORMAL = 4;

    //日程效果
    public static final int LEVEL_HIGH = 5;
    public static final int LEVEL_NORMAL = 6;
    public static final int LEVEL_LOW = 7;

    //三种效果对应的时长缩放因子
    public static final float LEVEL_HIGH_SCALE = 1.5f;
    public static final float LEVEL_NORMAL_SCALE = 1;
    public static final float LEVEL_LOW_SCALE= 0.8f;

    //两种个人状态对应的缩放因子
    public static final float BAD_SCALE=1.5F;
    public static final float GOOD_SCALE=1F;

    public static int STATUS = GOOD;
    public static int MODE = NORMAL;



    public static void init(int status,int mode)
    {
        MODE =mode;
        STATUS = status;
    }

    public static double getLevelFactor(int level)
    {
        switch (level)
        {
            case LEVEL_HIGH:
                return  LEVEL_HIGH_SCALE;
            case LEVEL_NORMAL:
                return  LEVEL_NORMAL_SCALE;
            case LEVEL_LOW:
                return LEVEL_LOW_SCALE;
            default:
                return LEVEL_NORMAL_SCALE;
        }
    }

}

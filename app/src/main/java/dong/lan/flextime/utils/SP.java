package dong.lan.flextime.utils;

import android.content.SharedPreferences;

import dong.lan.flextime.Config;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/11/27  07:02.
 */
public class SP {
    public static final String SP_NAME = "FlexTime";
    private static SharedPreferences sharedPreferences;

    public static void init(SharedPreferences sp) {
        sharedPreferences = sp;
    }

    public static boolean islogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public static void setLogin(boolean login) {
        sharedPreferences.edit().putBoolean("isLogin", login).apply();
    }

    /*
    是否第一次开启应用
     */
    public static boolean isWelcome() {
        return sharedPreferences.getBoolean("IS_WELCOME", false);
    }

    public static void setWelcome(boolean wel) {
        sharedPreferences.edit().putBoolean("IS_WELCOME", wel).apply();
    }


    /*
    保存保存的重要性权重因子
     */
    public static void setImp(float imp) {
        sharedPreferences.edit().putFloat("Importance", imp).apply();
    }

    public static float getImp() {
        return sharedPreferences.getFloat("Importance", 3.5f);
    }


    /*
    保存保存的紧急性权重因子
     */
    public static void setUrg(float imp) {
        sharedPreferences.edit().putFloat("Urgent", imp).apply();
    }

    public static float getUrg() {
        return sharedPreferences.getFloat("Urgent", 2.5f);
    }


    /*
    保存用户的使用状态
     */
    public static void setStatus(int status) {
        sharedPreferences.edit().putInt("STATUS", status).apply();
    }

    public static int getStatus() {
        return sharedPreferences.getInt("STATUS", Config.GOOD);
    }

    /*
    保存用户的使用模式
     */
    public static void setMode(int mode) {
        sharedPreferences.edit().putInt("MODE", mode).apply();
    }

    public static int getMode() {
        return sharedPreferences.getInt("MODE", Config.NORMAL);
    }


    /*
    保存提示声音的Uri路劲
     */
    public static void writeSoundPath(String path) {
        sharedPreferences.edit().putString("ALERT_SOUND", path).apply();
    }

    public static String getAlertSoundPath() {
        return sharedPreferences.getString("ALERT_SOUND", "");
    }

    /*
    保存日程提醒的提前间隔
     */
    public static void setAlertDelay(int alertDelay) {
        sharedPreferences.edit().putInt("ALERT_DELAY", alertDelay).apply();
    }

    public static int getAlertDelay() {
        return sharedPreferences.getInt("ALERT_DELAY", 1);
    }

    /*
    保存日程轮询的第一梯队的容量
     */
    public static void setLevelFirst(int first)
    {
        sharedPreferences.edit().putInt("LEVEL_FIRST",first).apply();
    }

    public static int getLevelFirst()
    {
        return sharedPreferences.getInt("LEVEL_FIRST",6);
    }
    /*
   保存日程轮询的第二梯队的容量
    */
    public static void setLevelSecond(int second)
    {
        sharedPreferences.edit().putInt("LEVEL_SECOND",second).apply();
    }

    public static int getLevelSecond()
    {
        return sharedPreferences.getInt("LEVEL_SECOND",12);
    }
}

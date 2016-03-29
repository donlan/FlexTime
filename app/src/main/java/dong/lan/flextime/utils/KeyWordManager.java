package dong.lan.flextime.utils;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.bean.KeyWord;
import dong.lan.flextime.db.DBManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/27/2016  15:10.
 */
public class KeyWordManager {
    private static Map<String, String> map = new HashMap<>();
    private volatile static KeyWordManager manager;

    private KeyWordManager(){}

    public static KeyWordManager getManager(){
        if(manager==null)
            synchronized (KeyWordManager.class){
                if(manager==null)
                    manager = new KeyWordManager();
            }
        return manager;
    }

    /*
        用BreakIterator实现的简单分词
     */
    public Map<String,String> searchRecom(String text,TextView textView) {
        map.clear();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.CHINA);
        iterator.setText(text);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            if (text.substring(start, end).length() > 1) {
                map.put(text.substring(start, end), "");
            }
        }

        if (!map.isEmpty()) {
            showRecommend(map, textView);
        }
        if (BuildConfig.DEBUG) Log.d("KeyWordManager", map.toString());
        return map;
    }

    /*
    根据分词结果，获取数据库中保存的关键词数据并显示
     */
    public void showRecommend(Map<String, String> map, TextView textView) {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            KeyWord keyword = DBManager.getManager().getKeyword(key);
            if (keyword != null) {
                sb.append(keyword.getWord());
                sb.append("  平均时长： ");
                sb.append(TimeUtil.defaultNeedFormat(Long.parseLong(keyword.getTime())));
                sb.append("\n");
            }
        }
        textView.setText(sb.toString());
        textView.setVisibility(View.VISIBLE);

    }
}

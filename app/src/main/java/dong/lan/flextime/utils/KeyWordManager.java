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
import io.realm.Realm;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/27/2016  15:10.
 */
public class KeyWordManager {
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
    public Map<String,String> searchRecom(Map<String,String> map,String text,TextView textView) {
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
        return map;
    }

    /*
    根据分词结果，获取数据库中保存的关键词数据并显示
     */
    public void showRecommend(Map<String, String> map, TextView textView) {
        StringBuilder sb = new StringBuilder();
        Realm realm = Realm.getDefaultInstance();
        for (String key : map.keySet()) {
            KeyWord keyword = realm.where(KeyWord.class).equalTo(KeyWord.WORD,key).findFirst();
            if (keyword != null) {
                sb.append(keyword.getWord());
                sb.append("  平均时长： ");
                sb.append(TimeUtil.defaultNeedFormat(keyword.getTime()));
                sb.append("\n");
            }
        }
        textView.setText(sb.toString());
        textView.setVisibility(View.VISIBLE);

    }
}

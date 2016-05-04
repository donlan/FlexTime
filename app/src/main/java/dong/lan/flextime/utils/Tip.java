package dong.lan.flextime.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 5/4/2016  21:25.
 */
public class Tip {
    public static  void Show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    public static void Show(View parent,String str){
        Snackbar snackbar = Snackbar.make(parent,str,Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}

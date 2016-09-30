package dong.lan.flextime.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.R;
import dong.lan.flextime.view.BaseView;

/**
 * Created by 梁桂栋 on 2016年08月31日 22:48.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class BaseActivity extends AppCompatActivity implements BaseView {
    FrameLayout container;
    View content;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        container = (FrameLayout) findViewById(R.id.base_container);
    }

    protected void setUpView(View view){
        container.addView(view);
    }
    protected void setUpView(int layoutId){
        content = LayoutInflater.from(this).inflate(layoutId,null);
        container.addView(content);
    }
    protected View findView(int id){
        return content.findViewById(id);
    }
    @Override
    public void show(String text){
        Snackbar.make(container,text,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void dialog(String text) {
        new AlertDialog.Builder(this).setMessage(text).setPositiveButton("OK",null).show();
    }
}

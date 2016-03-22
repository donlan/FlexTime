package dong.lan.flextime.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by 梁桂栋 on 2015/11/27.
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.support.design.R.anim.abc_fade_in,android.support.design.R.anim.abc_fade_out);
    }

    public void Show(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

}

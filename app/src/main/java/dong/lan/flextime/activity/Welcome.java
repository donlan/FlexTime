package dong.lan.flextime.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import dong.lan.flextime.R;
import dong.lan.flextime.adapter.MyPagerAdapter;
import dong.lan.flextime.utils.SP;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/11/26  03:13.
 *
 *
 * 引导页
 *
 */
public class Welcome extends BaseActivity {
    @Bind(R.id.welcome_logo)
    TextView logo;
    @Bind(R.id.welcome_pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        if(SP.isWelcome()) {
            ObjectAnimator.ofFloat(logo, "alpha", 0.1f, 1f).setDuration(1500).start();
            pager.setVisibility(View.GONE);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
//                    if (SP.islogin())
                        startActivity(new Intent(Welcome.this, MainActivity.class));
//                    else
//                        startActivity(new Intent(Welcome.this, LoginActivity.class));
                    finish();
                }
            }, 2000);
        }else
        {
            logo.setVisibility(View.GONE);
            List<View> views =new ArrayList<>();
            views.add(LayoutInflater.from(this).inflate(R.layout.welcome_pager1,null));
            views.add(LayoutInflater.from(this).inflate(R.layout.welcome_pager2,null));
            views.add(LayoutInflater.from(this).inflate(R.layout.welcome_pager3,null));
            views.get(2).findViewById(R.id.come).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Welcome.this, MainActivity.class));
                    SP.setWelcome(true);
                    finish();
                }
            });
            pager.setAdapter(new MyPagerAdapter(views));
        }
    }


}

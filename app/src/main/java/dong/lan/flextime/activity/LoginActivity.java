package dong.lan.flextime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import dong.lan.flextime.R;
import dong.lan.flextime.fragment.Fragment_Login;
import dong.lan.flextime.fragment.Fragment_Register;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/12/2016  15:42.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.left)
    TextView back;
    @Bind(R.id.text)
    TextView tittle;
    @Bind(R.id.right)
    TextView right;


    int index=1;
    Fragment tab[] = new Fragment[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tittle.setText("登录");
        right.setText("注册");
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index==0)
                {
                    index=1;
                    right.setText("登录");
                    getSupportFragmentManager().beginTransaction().replace(R.id.login_container,tab[0]).commit();
                }else
                {
                    index=0;
                    right.setText("注册");
                    getSupportFragmentManager().beginTransaction().replace(R.id.login_container,tab[1]).commit();
                }
            }
        });

        tab[0] = new Fragment_Login();
        tab[1] = new Fragment_Register();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_container,tab[0]).commit();

    }
}

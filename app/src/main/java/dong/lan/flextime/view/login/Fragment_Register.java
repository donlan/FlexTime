package dong.lan.flextime.view.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.utils.UserManager;
import dong.lan.flextime.view.BaseFragment;
import rx.functions.Action1;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/12/2016  21:06.
 *
 * 用户注册
 *
 */
public class Fragment_Register extends BaseFragment {
    @Bind(R.id.login_password)
    EditText pass;
    @Bind(R.id.login_username)
    EditText name;
    @Bind(R.id.login_btn)
    Button btn;
    @OnClick(R.id.login_btn)
    public void login()
    {
        String n = name.getText().toString();
        String p = pass .getText().toString();
        if(n.equals(""))
        {
            show("用户名不能为空！");
            return;
        }
        if(p.equals("")){
            show("密码不能为空！");
            return;
        }
        final BmobUser user  = new BmobUser();
        user.setUsername(n);
        user.setPassword(p);
        user.signUpObservable(User.class).subscribe(new Action1<User>() {
            @Override
            public void call(User user) {
                if(user==null){
                    show("登陆失败");
                    return;
                }
                user.signUpObservable(User.class).subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        if(user!=null){
                            UserManager.getManager().initUser(BmobUser.getCurrentUser( User.class));
                            getActivity().finish();
                            show(BmobUser.getCurrentUser().getUsername()+"注册成功");
                        }
                        show("登陆失败");
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,view);
        btn.setText("注册");
        btn.setBackgroundResource(R.drawable.circle_rect_red);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

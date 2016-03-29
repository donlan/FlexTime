package dong.lan.flextime.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.utils.UserManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/12/2016  20:53.
 *
 * 用户登录
 *
 */
public class Fragment_Login extends BaseFragment {
    @Bind(R.id.login_password)
    EditText pass;
    @Bind(R.id.login_username)
    EditText name;
    @OnClick(R.id.login_btn)
    public void login()
    {
        String n = name.getText().toString();
        String p = pass .getText().toString();
        if(n.equals(""))
        {
            Show("用户名不能为空！");
            return;
        }
        if(p.equals("")){
            Show("密码不能为空！");
            return;
        }
        BmobUser user  = new BmobUser();
        user.setUsername(n);
        user.setPassword(p);
        user.login(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                UserManager.getManager().initUser(BmobUser.getCurrentUser(getActivity(), User.class));
                getActivity().finish();
            }

            @Override
            public void onFailure(int i, String s) {
                Show(s);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

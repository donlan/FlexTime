package dong.lan.flextime.view;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import dong.lan.flextime.view.BaseView;

/**
 * Created by 梁桂栋 on 2016年09月01日 00:24.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class BaseFragment extends Fragment implements BaseView {
    @Override
    public void show(String s) {
        if(getView()!=null)
        Snackbar.make(getView(),s,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void dialog(String text) {
        new AlertDialog.Builder(getActivity()).setMessage(text).setPositiveButton("OK",null).show();
    }

}

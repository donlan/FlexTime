package dong.lan.flextime.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/12/2016  21:03.
 */
public class BaseFragment extends Fragment {

    public void Show(String s)
    {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}

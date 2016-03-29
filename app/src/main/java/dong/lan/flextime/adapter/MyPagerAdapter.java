package dong.lan.flextime.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/7  05:37.
 *
 *
 * 引导页的滑动 PagerAdapter
 *
 */
public class MyPagerAdapter extends PagerAdapter {


    List<View> views ;

    public MyPagerAdapter(List<View> v)
    {
        views =v;
    }


    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position), 0);
        return views.get(position);
    }
}

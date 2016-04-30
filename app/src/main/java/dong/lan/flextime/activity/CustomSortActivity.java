package dong.lan.flextime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.flextime.Interface.SortSelectListener;
import dong.lan.flextime.R;
import dong.lan.flextime.adapter.SortAdapter;
import dong.lan.flextime.bean.Sort;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.utils.SortManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 4/29/2016  09:41.
 */
public class CustomSortActivity extends BaseActivity implements SortSelectListener {

    @Bind(R.id.list)
    RecyclerView recyclerView;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.collapsingText)
    TextView collapsingText;


    @OnClick(R.id.back)
    public void back() {
        finish();
    }
    @OnClick(R.id.float_add)
    public void add() {
        Snackbar.make(coordinatorLayout,"自定义排序算法为开放", Snackbar.LENGTH_SHORT).show();
    }

    SortAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sort);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<Sort> sorts = DBManager.getManager().getAllSortItem();
        if(sorts==null)
        {
            Sort sort = new Sort(0, SortManager.SORT_0,false);
            Sort sort1 = new Sort(1,SortManager.SORT_1,true);
            Sort sort2 = new Sort(2,SortManager.SORT_2,false);
            sorts = new ArrayList<>();
            sorts.add(sort);
            sorts.add(sort1);
            sorts.add(sort2);
            DBManager.getManager().addSortItem(sorts);
        }
        adapter = new SortAdapter(this, sorts);
        for (int i = 0; i < sorts.size(); i++) {
            if(sorts.get(i).isSelect()) {
                collapsingText.setText(sorts.get(i).getMethod());
                adapter.setDefault(sorts.get(i));
            }
        }
        adapter.setSelectListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        DBManager.getManager().setSortItemSelect(SortManager.TAG,true);

    }

    @Override
    public void onSortSelect(Sort sort) {
        SortManager.TAG = sort.getTag();
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, sort.getMethod(), Snackbar.LENGTH_SHORT);
        snackbar.setAction("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        collapsingText.setText(sort.getMethod());
    }
}

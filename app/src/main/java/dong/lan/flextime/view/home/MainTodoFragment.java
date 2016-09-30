package dong.lan.flextime.view.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;

import dong.lan.flextime.*;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.model.home.MainTodoModel;
import dong.lan.flextime.presenter.home.MainTodoPresenter;
import dong.lan.flextime.view.BaseView;

/**
 * Created by 梁桂栋 on 2016年08月31日 23:11.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class MainTodoFragment extends dong.lan.flextime.view.BaseFragment implements IMainTodoView<Todo> {

    private MainTodoPresenter presenter;
    SwipeRefreshLayout refreshLayout;

    public MainTodoAdapter adapter;
    private RecyclerView mainTodoList;
    private View emptyView;

    public static Fragment newInstance(String tittle, int index) {
        MainTodoFragment mainTodoFragment = new MainTodoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BaseView.VIEW_TITTLE, tittle);
        bundle.putInt(BaseView.VIEW_INDEX, index);
        mainTodoFragment.setArguments(bundle);
        return mainTodoFragment;
    }

    public MainTodoFragment() {
        presenter = new MainTodoPresenter(this, new MainTodoModel(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_todo, container, false);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_srl);
        mainTodoList = (RecyclerView) view.findViewById(R.id.main_todo_list);
        mainTodoList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        presenter.attach();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        setUpTodos();

    }

    @Override
    public void show(String s) {
        if (getView() != null)
            Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void setUpTodos() {
        adapter = new MainTodoAdapter();
        presenter.loadTodoDatas(mainTodoList);
        if(presenter.getTodoCount()==0){
            ViewStub stub = (ViewStub) getView().findViewById(R.id.empty_stub);
            emptyView = stub.inflate();
        }
    }

    @Override
    public void refresh() {
        adapter.notifyDataSetChanged();
        if(emptyView!=null)
            emptyView.setVisibility(View.GONE);
        presenter.refresh();
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        },1000);
    }

    @Override
    public void updateItem(int location) {

    }

    @Override
    public void deleteItem(int location) {

    }

    public MainTodoPresenter getPresenter(){
        return presenter;
    }

    @Override
    public Todo getItem() {
        return null;
    }

    public class MainTodoAdapter extends RecyclerView.Adapter<MainTodoAdapter.Holder> {


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_todo, null);
            final Holder holder = new Holder(v);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.itemClick(holder.getLayoutPosition(),MainTodoPresenter.FLAG_ITEM_CLICK);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.info.setText(Html.fromHtml(presenter.getTodoInfo(position)));
        }

        @Override
        public int getItemCount() {
            return presenter.getTodoCount();
        }

        class Holder extends RecyclerView.ViewHolder {

            TextView info;
            public Holder(View itemView) {
                super(itemView);
                info = (TextView) itemView.findViewById(R.id.item_todo_Info);
            }
        }
    }
}

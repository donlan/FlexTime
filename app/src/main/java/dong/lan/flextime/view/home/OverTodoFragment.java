package dong.lan.flextime.view.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dong.lan.flextime.*;
import dong.lan.flextime.model.home.OverTodoModel;
import dong.lan.flextime.presenter.home.OverTodoPresenter;
import dong.lan.flextime.view.BaseView;

/**
 * Created by 梁桂栋 on 2016年08月31日 23:11.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class OverTodoFragment extends dong.lan.flextime.view.BaseFragment implements IOverTodoView {

    private OverTodoPresenter presenter;
    public RecyclerView overTodoList;
    public View emptyVIew;
    public OverTodoAdapter adapter;
    public static Fragment newInstance(String tittle,int index){
        OverTodoFragment overTodoFragment = new OverTodoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BaseView.VIEW_TITTLE,tittle);
        bundle.putInt(BaseView.VIEW_INDEX,index);
        overTodoFragment.setArguments(bundle);
        return overTodoFragment;
    }

    public OverTodoFragment(){
        presenter = new OverTodoPresenter(this,new OverTodoModel(getActivity()));
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_overtiem_todo,container,false);
        overTodoList = (RecyclerView) view.findViewById(R.id.over_todo_list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.attach();
        setTodoDatas();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void setTodoDatas() {
        adapter =new OverTodoAdapter();
        presenter.loadData();
    }

    @Override
    public void itemClick(int location) {

    }

    @Override
    public void itemClickToMain(int location) {

    }

    @Override
    public void itemDelete(int location) {

    }

    @Override
    public void updateItem(int location) {

    }

    public OverTodoPresenter getPresenter(){
        return presenter;
    }

    public class OverTodoAdapter extends RecyclerView.Adapter<OverTodoAdapter.Holder> {


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_todo, null);
            final Holder holder = new Holder(v);
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.itemClick(holder.getLayoutPosition());
                }
            });
            holder.itemRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.deleteItem(holder.getLayoutPosition());
                }
            });
            holder.itemLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.itemBack(holder.getLayoutPosition());
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
            TextView itemLeft;
            TextView itemRight;
            public Holder(View itemView) {
                super(itemView);
                info = (TextView) itemView.findViewById(R.id.item_todo_Info);
                itemLeft = (TextView) itemView.findViewById(R.id.item_left);
                itemRight = (TextView) itemView.findViewById(R.id.item_right);
            }
        }
    }
}

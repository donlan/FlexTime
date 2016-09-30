package dong.lan.flextime.presenter.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewStub;

import dong.lan.flextime.R;
import dong.lan.flextime.view.handleTodo.AddTodoActivity;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.model.home.OverTodoModel;
import dong.lan.flextime.presenter.BasePresenter;
import dong.lan.flextime.utils.TodoUtil;
import dong.lan.flextime.view.home.OverTodoFragment;

/**
 * Created by 梁桂栋 on 2016年09月01日 15:00.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class OverTodoPresenter extends BasePresenter<OverTodoFragment,OverTodoModel> {
    public OverTodoPresenter(OverTodoFragment view, OverTodoModel model) {
        super(view, model);
    }

    public void loadData(){
        view.overTodoList.setLayoutManager(new LinearLayoutManager(view.getActivity(),LinearLayoutManager.VERTICAL,false));
        view.overTodoList.setAdapter(view.adapter);
        if(model.size()==0 && view.getView()!=null){
            ViewStub v = (ViewStub) view.getView().findViewById(R.id.empty_stub);
            view.emptyVIew = v.inflate();
        }
    }



    public void itemClick(int location){
        Todo todo = model.getItem(location);
        Intent intentTodo = new Intent(view.getActivity(), AddTodoActivity.class);
        intentTodo.putExtra("TODO_ID",todo.id);
        intentTodo.putExtra("POS",location);
        view.getActivity().startActivity(intentTodo);
    }

    public void itemBack(int location){

    }

    public void deleteItem(int location){
        model.remove(location);
        view.adapter.notifyItemRemoved(location);
    }

    public void updateItem(int location){

    }

    public int getTodoCount() {
        return model.size();
    }

    public String getTodoInfo(int position) {
        Todo todo = model.getItem(position);
        return TodoUtil.getTodoInfo(todo);
    }
}

package dong.lan.flextime.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dong.lan.flextime.Config;
import dong.lan.flextime.Interface.ItemTouchListener;
import dong.lan.flextime.Interface.onItemClickListener;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.dao.TodoDao;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.utils.TodoManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/10  05:38.
 *
 *
 * 日程信息的 RecyclerView Adapter
 *
 */
public class MainTodoAdapter extends RecyclerView.Adapter<MainTodoAdapter.VHolder> implements ItemTouchListener {

    public static final int CLICK = 0;          //单击
    public static final int LONG_CLICK = 1;     //长按
    private boolean isMain;                     //是否是主日程列表的日程
    private boolean isGoodStatus;               //用户状态
    List<Todo> todos = new ArrayList<>();       //日程
    Context context;
    StringBuffer sb = new StringBuffer();
    DecimalFormat decimalFormat = new DecimalFormat("######.00"); //数字格式化为两位小数

    public MainTodoAdapter(Context context, List<Todo> todos, boolean isMain) {
        this.context = context;
        this.todos = todos;
        this.isMain = isMain;
        isGoodStatus = SP.getStatus()==Config.GOOD;
    }


    /*
    设置RecycleView的点击事件
     */
    onItemClickListener listener;
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHolder(LayoutInflater.from(context).inflate(R.layout.item_todo, null));
    }

    @Override
    public void onBindViewHolder(final VHolder holder, int position) {
        final Todo todo = todos.get(position);

        /*
        为不同次序的日程设置不同背景颜色，便于用户分辨轻重缓急
         */
        if(holder.getLayoutPosition()<3)
            holder.parent.setBackgroundResource(R.drawable.circle_rect_level_5);
        else if(holder.getLayoutPosition()<6)
            holder.parent.setBackgroundResource(R.drawable.circle_rect_level_4);
        else if(holder.getLayoutPosition()<9)
            holder.parent.setBackgroundResource(R.drawable.circle_rect_level_3);
        else if(holder.getLayoutPosition()<12)
            holder.parent.setBackgroundResource(R.drawable.circle_rect_level_2);
        else if(holder.getLayoutPosition()>11)
            holder.parent.setBackgroundResource(R.drawable.circle_rect_level_1);

        sb.delete(0, sb.length());
        sb.append("<html><body>");
        if(todo.getType()==TodoDao.TYPE_SINGLE){
            ToDoItem item = todo.getTodos().get(0);
            long time = isGoodStatus ? item.getFinishTime(): item.getDeadline();
            sb.append("<h5>");
            sb.append(item.getInfo());
            sb.append("</h5>");
            sb.append("<p>");
            sb.append(TimeUtil.getRemainTime(time));
            sb.append("</p>");
            sb.append(TimeUtil.getStartTimeGap(item.getStartTime(),item.getNeedTime()));
            sb.append("</p>");
            sb.append("权重（测试时可见）");
            sb.append(decimalFormat.format(todo.getWeight()));
            sb.append("重要");
            sb.append(item.getImportant());
            sb.append("紧急");
            sb.append(item.getUrgent());
            sb.append("</p>");
        }else if(todo.getType()==TodoDao.TYPE_ORDER)
        {
            SparseArray<ToDoItem> items = todo.getTodos();
            for(int i = 0;i<items.size();i++)
            {
                ToDoItem item = items.get(i);
                long time = item.getFinishTime()- item.getDeadline()<=0? item.getFinishTime(): item.getDeadline();
                sb.append("<h2>序号：");
                sb.append(i+1);
                sb.append("</h2><h5>");
                sb.append(item.getInfo());
                sb.append("</h5>");
                sb.append("<p>");
                sb.append(TimeUtil.getRemainTime(time));
                sb.append("</p>");
                sb.append(TimeUtil.getStartTimeGap(item.getStartTime(),item.getNeedTime()));

            }
        }else
        {
            SparseArray<ToDoItem> items = todo.getTodos();
            for(int i = 0;i<items.size();i++)
            {
                ToDoItem item = items.get(i);
                long time = item.getFinishTime()-item.getDeadline()<=0? item.getFinishTime(): item.getDeadline();
                sb.append("<h2>◆</h2><h5>");
                sb.append(item.getInfo());
                sb.append("</h5>");
                sb.append("<p>");
                sb.append(TimeUtil.getRemainTime(time));
                sb.append("</p>");
                sb.append(TimeUtil.getStartTimeGap(item.getStartTime(),item.getNeedTime()));

            }
        }
        sb.append("</body></html>");
        holder.info.setText(Html.fromHtml(sb.toString()));
        if (listener != null) {
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(todo, holder.getLayoutPosition(), CLICK,isMain);
                }
            });
        }
    }

    public void updateTodo(Todo todo, int pos)
    {
        todos.set(pos, todo);
        Collections.sort(todos);
        notifyDataSetChanged();
    }
    public List<Todo> getTodos() {
        return todos;
    }

    public void deleteTodo(int pos) {
        todos.remove(pos);
        notifyItemRemoved(pos);
    }

    public void deleteTodo(Todo todo) {
        todos.remove(todo);
        notifyDataSetChanged();
    }

    public void addTodo(int pos, Todo todo) {
        todos.add(pos, todo);
        notifyItemInserted(pos);
    }

    public void setGoodStatus(boolean goodStatus)
    {
        this.isGoodStatus = goodStatus;
    }
    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public void onItemMoved(int fromPos, int toPos) {
        Collections.swap(todos, fromPos, toPos);
        notifyItemMoved(fromPos, toPos);
        TodoManager.resortWithSwap(todos.get(fromPos),todos.get(toPos));
    }

    @Override
    public void onItemSwiped(final int pos) {
        new AlertDialog.Builder(context, R.style.MyDialogStyleTop)
                .setTitle("你确定删除此个日程安排吗？")
                .setMessage("删除后将不能恢复！！")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.getManager().deleteTodo(todos.get(pos).getId());
                        deleteTodo(pos);
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyItemChanged(pos);
                    }
                })
                .show();
    }

    static class VHolder extends RecyclerView.ViewHolder {

        RelativeLayout parent;
        TextView info;

        public VHolder(View v) {
            super(v);
            parent = (RelativeLayout) v.findViewById(R.id.item_todo_parent);
            info = (TextView) v.findViewById(R.id.item_todo_Info);
        }
    }
}

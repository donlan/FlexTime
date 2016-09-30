package dong.lan.flextime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dong.lan.flextime.Config;
import dong.lan.flextime.Interface.OrderTodoItemClick;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.RealmToDoItem;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.utils.TodoUtil;
import io.realm.RealmList;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/27/2016  16:08.
 *
 * 分步日程的适配器，使用场景是用户不断添加新的分步日程，用户可以快速查看到已经添加的分步日程
 *
 */
public class OrderTodoAdapter extends RecyclerView.Adapter<OrderTodoAdapter.Holder> {

    LayoutInflater inflater ;
    RealmList<RealmToDoItem> items;
    OrderTodoItemClick orderTodoItemClick;

    public void setOnOrderTodoClick(OrderTodoItemClick itemClick)
    {
        orderTodoItemClick  = itemClick;
    }


    public OrderTodoAdapter(Context context, RealmList<RealmToDoItem> items)
    {
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_todo_order,null));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.seq.setText(String.valueOf(position+1));

        final RealmToDoItem toDoItem = items.get(position);
        long time = Config.STATUS==Config.GOOD ? toDoItem.finishTime: toDoItem.deadline;
        time = time - System.currentTimeMillis();

        holder.info.setText(Html.fromHtml(TodoUtil.getTodoItemInfo(toDoItem)));

        if(orderTodoItemClick!=null)
        {
            holder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderTodoItemClick.onItemClick(toDoItem,holder.getLayoutPosition());
                }
            });
        }

    }

    @Override
    public int getItemCount() {

        return items==null ? 0 : items.size();
    }
    public void add(RealmToDoItem item)
    {
        items.add(items.size(),item);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder
    {
        TextView seq;
        TextView info;
        public Holder(View itemView) {
            super(itemView);
            seq = (TextView) itemView.findViewById(R.id.item_todo_order_seq);
            info = (TextView) itemView.findViewById(R.id.item_todo_order_info);
        }
    }
}

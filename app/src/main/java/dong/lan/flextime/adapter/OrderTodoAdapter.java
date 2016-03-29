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
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.utils.TimeUtil;

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
    SparseArray<ToDoItem> items;
    StringBuilder sb;
    OrderTodoItemClick orderTodoItemClick;

    public void setOnOrderTodoClick(OrderTodoItemClick itemClick)
    {
        orderTodoItemClick  = itemClick;
    }


    public OrderTodoAdapter(Context context, SparseArray<ToDoItem> items)
    {
        this.items = items;
        inflater = LayoutInflater.from(context);
        sb = new StringBuilder();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_todo_order,null));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.seq.setText(String.valueOf(position+1));

        final ToDoItem toDoItem = items.get(position);
        long time = Config.STATUS==Config.GOOD ? toDoItem.getFinishTime(): toDoItem.getDeadline();
        sb.delete(0, sb.length());
        sb.append("<html><body><h5>");
        sb.append(toDoItem.getInfo());
        sb.append("</h5>");
        sb.append("<p>");
        sb.append(TimeUtil.getRemainTime(time));
        sb.append("</p>");
        sb.append(TimeUtil.getStartTimeGap(toDoItem.getStartTime(),toDoItem.getNeedTime()));
        sb.append("</p>");
        sb.append("重要  ");
        sb.append(toDoItem.getImportant());
        sb.append("  紧急  ");
        sb.append(toDoItem.getUrgent());
        sb.append("</p>");
        sb.append("</body></html>");

        holder.info.setText(Html.fromHtml(sb.toString()));


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

        return items.size();
    }


    public void add(ToDoItem item)
    {

        items.put(items.size(),item);
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

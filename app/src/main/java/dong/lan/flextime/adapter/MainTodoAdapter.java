package dong.lan.flextime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dong.lan.flextime.R;
import dong.lan.flextime.bean.ToDo;
import dong.lan.flextime.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 2015/12/10.
 */
public class MainTodoAdapter extends RecyclerView.Adapter<MainTodoAdapter.VHolder> {

    public static final int CLICK = 0;
    public static final int LONG_CLICK = 1;
    public static final int DRAG = 2;
    private boolean isMain;
    float x;
    float y;
    List<ToDo> toDos = new ArrayList<>();
    Context context;
    StringBuffer sb = new StringBuffer();
    DecimalFormat decimalFormat = new DecimalFormat("######.00");

    public MainTodoAdapter(Context context, List<ToDo> toDos,boolean isMain) {
        this.context = context;
        this.toDos = toDos;
        this.isMain = isMain;
    }

    public interface onItemClickListener {
        void itemClick(ToDo toDo, int pos, int type,boolean isMain);
    }

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
        final ToDo toDo = toDos.get(position);
        sb.delete(0, sb.length());
        sb.append("<html><body><h5>");
        sb.append(toDo.getInfo());
        sb.append("</h5>");
        sb.append("<p>");
        sb.append(TimeUtil.getRemainTime(toDo.getFinishTime()));
        sb.append("</p>");
        sb.append(TimeUtil.getStartTimeGap(toDo.getNeedTime(),toDo.getFinishTime()));
        sb.append("</p>");
        sb.append("权重（测试时可见）");
        sb.append(decimalFormat.format(toDo.getWeight()));
        sb.append("重要");
        sb.append(toDo.getImportant());
        sb.append("紧急");
        sb.append(toDo.getUrgent());
        sb.append("</p>");
        sb.append("</body></html>");

        holder.info.setText(Html.fromHtml(sb.toString()));
        if (listener != null) {
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(toDo, holder.getLayoutPosition(), CLICK,isMain);
                }
            });

            holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.itemClick(toDo, holder.getLayoutPosition(), LONG_CLICK, isMain);
                    return false;
                }
            });
        }
    }

    public void updateTodo(ToDo toDo,int pos)
    {
        toDos.set(pos, toDo);
        Collections.sort(toDos);
        notifyDataSetChanged();
    }
    public List<ToDo> getToDos() {
        return toDos;
    }

    public void deleteTodo(int pos) {
        toDos.remove(pos);
        notifyItemRemoved(pos);
    }

    public void deleteTodo(ToDo toDo) {
        toDos.remove(toDo);
        notifyDataSetChanged();
    }

    public void addTodo(int pos, ToDo toDo) {
        toDos.add(pos, toDo);
        notifyItemInserted(pos);
    }

    @Override
    public int getItemCount() {
        return toDos.size();
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

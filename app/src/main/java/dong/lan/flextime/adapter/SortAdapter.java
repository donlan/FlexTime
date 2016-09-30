package dong.lan.flextime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import dong.lan.flextime.Interface.SortSelectListener;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.Sort;
import io.realm.Realm;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 4/29/2016  12:55.
 */
public class SortAdapter extends RecyclerView.Adapter<SortAdapter.Holder> {

    LayoutInflater inflater;
    List<Sort> sorts;

    Sort last;

    public SortAdapter(Context context, List<Sort> list) {
        inflater = LayoutInflater.from(context);
        sorts = list;
    }

    SortSelectListener listener;

    public void setSelectListener(SortSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_sort_method, null));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final Sort sort = sorts.get(position);
        holder.method.setText(sort.getMethod());
        holder.checkBox.setChecked(sort.isSelect());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkBox.isChecked()) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    sort.setSelect(true);
                    last.setSelect(false);
                    notifyItemChanged(sorts.indexOf(last));
                    last = sort;
                    realm.commitTransaction();
                    if (listener != null)
                        listener.onSortSelect(sort);
                }
//                    for (int i = 0; i < sorts.size(); i++) {
//                        if (sorts.get(i) != sort) {
//                            sorts.get(i).setSelect(false);
//                            if(listener!=null)
//                                listener.onSortSelect(sort);
//                            sort.setSelect(true);
//
//
//                            notifyDataSetChanged();
//                        }
//                    }
//                }
            }
        });
    }


    public Sort getLastSort()
    {
        return last;
    }
    public void setDefault(Sort sort) {
        this.last = sort;
    }

    @Override
    public int getItemCount() {
        return sorts.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView method;

        public Holder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.method_check);
            method = (TextView) itemView.findViewById(R.id.method_text);
        }
    }
}

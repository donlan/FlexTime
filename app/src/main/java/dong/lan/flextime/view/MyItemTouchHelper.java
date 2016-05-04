package dong.lan.flextime.view;

import android.support.v7.widget.RecyclerView;

import dong.lan.flextime.Interface.ItemTouchListener;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 5/4/2016  18:53.
 */
public class MyItemTouchHelper extends android.support.v7.widget.helper.ItemTouchHelper.Callback {




    private ItemTouchListener touchListener;


    public MyItemTouchHelper(ItemTouchListener touchListener) {
        this.touchListener = touchListener;
        System.out.println(touchListener==null);
    }


    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = android.support.v7.widget.helper.ItemTouchHelper.DOWN | android.support.v7.widget.helper.ItemTouchHelper.UP;
        int swipeFlag = android.support.v7.widget.helper.ItemTouchHelper.START | android.support.v7.widget.helper.ItemTouchHelper.END;

        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        touchListener.onItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        touchListener.onItemSwiped(viewHolder.getAdapterPosition());
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return super.getSwipeThreshold(viewHolder);
    }
}

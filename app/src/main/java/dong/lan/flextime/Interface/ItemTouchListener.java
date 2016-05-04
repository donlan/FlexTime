package dong.lan.flextime.Interface;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 5/4/2016  20:00.
 */
public interface ItemTouchListener {
        void onItemMoved(int fromPos, int toPos);

        void onItemSwiped(int pos);

}

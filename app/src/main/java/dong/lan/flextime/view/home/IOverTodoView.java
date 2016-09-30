package dong.lan.flextime.view.home;

/**
 * Created by 梁桂栋 on 2016年09月01日 15:16.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IOverTodoView {

    void setTodoDatas();

    void itemClick(int location);

    void itemClickToMain(int location);

    void itemDelete(int location);

    void updateItem(int location);


}

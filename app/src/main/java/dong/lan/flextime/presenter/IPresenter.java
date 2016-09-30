package dong.lan.flextime.presenter;

/**
 * Created by 梁桂栋 on 2016年08月31日 20:56.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public interface IPresenter<V,M> {
     void bind(V view,M model);
     void attach();
     void detach();
}

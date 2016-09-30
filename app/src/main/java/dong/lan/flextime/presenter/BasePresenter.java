package dong.lan.flextime.presenter;

/**
 * Created by 梁桂栋 on 2016年09月01日 08:24.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class BasePresenter<V,M> implements IPresenter<V,M> {
    protected V view;
    protected M model;
    public BasePresenter(V view,M model){
        bind(view,model);
    }
    @Override
    public void bind(V view,M model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}

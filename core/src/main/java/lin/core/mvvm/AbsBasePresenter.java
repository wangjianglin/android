package lin.core.mvvm;

/**
 * Created by lin on 23/11/2016.
 */

public abstract class AbsBasePresenter<V extends BaseView> implements BasePresenter<V> {

    public void start(){};

    protected V mView;

    public void setView(V view){
        this.mView = view;
    }

    public V getView(){
        return this.mView;
    }
}

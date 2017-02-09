package lin.core.mvvm;

/**
 * Created by lin on 23/11/2016.
 */

public interface BasePresenter<V extends BaseView> {

    void start();

    void setView(V view);
}

package lin.core.mvvm;

/**
 * Created by lin on 23/11/2016.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}

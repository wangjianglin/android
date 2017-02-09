package lin.core.mvvm;

/**
 * Created by lin on 07/02/2017.
 */

public interface AbsActionHandler<T extends BasePresenter> extends ActionHandler<T> {
    void setPresenter(T presenter);
}

package lin.core.mvvm;

/**
 * Created by lin on 07/02/2017.
 */

public interface ActionHandler<T extends BasePresenter> {
    void setPresenter(T presenter);
}

package io.cess.core.mvvm;

/**
 * @author lin
 * @date 07/02/2017.
 */

public interface ActionHandler<T extends BasePresenter> {
    void setPresenter(T presenter);
}

package io.cess.core.mvvm;

/**
 * @author lin
 * @date 23/11/2016.
 */

public interface BasePresenter<V extends BaseView> {

    void start();

    void setView(V view);
}

package io.cess.core.mvvm;

/**
 * @author lin
 * @date 08/02/2017.
 */

public interface BaseBindPresenter<V extends BaseView,VM extends BaseViewModel> extends BasePresenter<V> {

    void setViewModel(VM viewModel);
}
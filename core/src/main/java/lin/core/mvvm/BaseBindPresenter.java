package lin.core.mvvm;

/**
 * Created by lin on 08/02/2017.
 */

public interface BaseBindPresenter<V extends BaseView,VM extends BaseViewModel> extends BasePresenter<V> {

    void setViewModel(VM viewModel);
}
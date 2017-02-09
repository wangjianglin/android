package lin.core.mvvm;

/**
 * Created by lin on 08/02/2017.
 */

public interface BaseViewModelPresenter<V extends BaseView,VM extends BaseViewModel> extends BasePresenter<V> {

    void setViewModel(VM viewModel);
}
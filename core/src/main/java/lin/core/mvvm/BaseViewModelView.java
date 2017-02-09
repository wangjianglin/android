package lin.core.mvvm;

/**
 * Created by lin on 23/11/2016.
 */

public interface BaseViewModelView<T extends BasePresenter,VM extends BaseViewModel> extends BaseView<T> {

    void setViewModel(VM viewModel);

}

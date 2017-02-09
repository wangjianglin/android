package lin.core.mvvm;

/**
 * Created by lin on 08/02/2017.
 */

public abstract class AbsBaseViewModelPresenter<V extends BaseView,VM extends BaseViewModel> extends AbsBasePresenter<V> implements BaseViewModelPresenter<V,VM> {

    protected VM mViewModel;

    public void setViewModel(VM viewModel) {
        this.mViewModel = viewModel;
    }

    public VM getViewModel() {
        return this.mViewModel;
    }
}
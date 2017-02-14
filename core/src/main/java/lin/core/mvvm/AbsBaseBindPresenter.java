package lin.core.mvvm;

/**
 * Created by lin on 08/02/2017.
 */

public abstract class AbsBaseBindPresenter<V extends BaseView,VM extends BaseViewModel> extends AbsBasePresenter<V> implements BaseBindPresenter<V,VM> {

    protected VM mViewModel;

    public void setViewModel(VM viewModel) {
        this.mViewModel = viewModel;
    }

    public VM getViewModel() {
        return this.mViewModel;
    }
}
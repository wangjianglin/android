package lin.core.mvvm;

/**
 * Created by lin on 23/11/2016.
 */

public interface BaseBindView<T extends BasePresenter,VM extends BaseViewModel,H extends ActionHandler> extends BaseView<T> {

    void setViewModel(VM viewModel);

    void setHandler(H handler);

}

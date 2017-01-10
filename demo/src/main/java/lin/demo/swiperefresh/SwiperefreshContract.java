package lin.demo.swiperefresh;

import android.content.Context;

import java.util.List;

import lin.core.mvvm.BasePresenter;
import lin.core.mvvm.BaseView;

/**
 * Created by lin on 06/01/2017.
 */

public class SwiperefreshContract {
    interface View extends BaseView<Presenter> {
        void show(List<Data> datas);

        Context getContext();
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void clean();
    }
}

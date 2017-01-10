package lin.demo.main;

import android.content.Context;

import java.util.List;

import lin.core.mvvm.BasePresenter;
import lin.core.mvvm.BaseView;

/**
 * Created by lin on 23/11/2016.
 */

public interface MainContract {

    interface View extends BaseView<Presenter> {

//        void showEmptyTaskError();
//
//        void showTasksList();
//
//        void setTitle(String title);
//
//        void setDescription(String description);

        void show(List<Data> datas);

        Context getContext();
    }

    interface Presenter extends BasePresenter {

//        void saveTask(String title, String description);
        void loadData();
    }
}

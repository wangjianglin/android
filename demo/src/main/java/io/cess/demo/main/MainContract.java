package io.cess.demo.main;

import android.content.Context;

import java.util.List;

import io.cess.core.mvvm.BasePresenter;
import io.cess.core.mvvm.BaseView;

/**
 * @author lin
 * @date 23/11/2016.
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

    interface Presenter extends BasePresenter<View> {

//        void saveTask(String title, String description);
        void loadData();
    }
}

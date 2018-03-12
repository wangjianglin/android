package io.cess.core.mvvm;

import android.content.Context;
import android.databinding.BaseObservable;

/**
 * @author lin
 * @date 23/11/2016.
 */

public abstract class AbsBasePresenter<V extends BaseView> extends BaseObservable implements BasePresenter<V> {

    public void start(){};

    protected V mView;

    public void setView(V view){
        this.mView = view;
    }

    public V getView(){
        return this.mView;
    }

    public Context getContext(){
        return this.mView.getContext();
    }
}

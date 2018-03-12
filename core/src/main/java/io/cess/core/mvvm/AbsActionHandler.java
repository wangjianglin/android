package io.cess.core.mvvm;

/**
 * @author lin
 * @date 07/02/2017.
 */

public abstract class AbsActionHandler<T extends BasePresenter> implements ActionHandler<T> {

    protected T mPresenter;
    public void setPresenter(T presenter){
        mPresenter = presenter;
    }
}

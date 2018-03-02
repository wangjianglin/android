package io.cess.core.mvvm;

import android.content.Context;

/**
 * Created by lin on 23/11/2016.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

    Context getContext();
}

package io.cess.core;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import io.cess.core.annotation.ToolsBindCls;


/**
 * @author lin
 * @date 30/12/2016.
 */

public class ResToolsFragment<T extends ViewDataBinding> extends ResFragment {

    private Class<T> mToolsCls;
    private T mToolsBind;

    public ResToolsFragment() {
        super();
    }

    protected ResToolsFragment(int resId) {
        super(resId);
    }

    protected ResToolsFragment(int resId,Class<T> toolsCls) {
        super(resId);
        this.mToolsCls = toolsCls;
    }

    protected T getToolsBind(){
        return mToolsBind;
    }

    @Override
    protected View onCreateToolsViewInternal(LayoutInflater inflater, @Nullable ViewGroup container) {

        if(mToolsCls == null){
            ToolsBindCls bindCls = this.getClass().getAnnotation(ToolsBindCls.class);
            if(bindCls != null){
                mToolsCls = (Class<T>) bindCls.value();
            }
        }
        try {
            Method method = mToolsCls.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            mToolsBind = (T) method.invoke(null,inflater,container,false);
            final View view = mToolsBind.getRoot();


            return view;

        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }
}


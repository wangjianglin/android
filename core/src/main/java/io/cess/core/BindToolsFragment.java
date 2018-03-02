package io.cess.core;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import io.cess.core.annotation.BindCls;
import io.cess.core.annotation.ToolsBindCls;
import io.cess.util.Tools;

/**
 * Created by lin on 18/01/2017.
 */
public class BindToolsFragment<V extends ViewDataBinding,T extends ViewDataBinding> extends BindFragment<V>{


    private Class<T> mToolsCls;
    private T mToolsBind;

    public BindToolsFragment() {
        super();
    }

    protected BindToolsFragment(Class<V> cls) {
        super(cls);
    }

    protected BindToolsFragment(Class<V> cls,Class<T> toolsCls) {
        super(cls);
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
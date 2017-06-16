package lin.core;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import lin.core.annotation.BindCls;
import lin.core.annotation.OptionsMenu;

/**
 * Created by lin on 18/01/2017.
 */
public class BindFragment <T extends ViewDataBinding> extends AbsFragment{
    private Class<T> cls = null;
    public BindFragment() {
        super();
    }

    protected BindFragment(Class<T> cls) {
        this.cls = cls;
    }

    @Nullable
    @Override
    final public View onCreateViewInternal(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getFragmentView(inflater,container);
    }

    public View getFragmentView(LayoutInflater inflater,ViewGroup parent) {
        if(cls == null){
            BindCls bindCls = this.getClass().getAnnotation(BindCls.class);
            if(bindCls != null){
                cls = (Class<T>) bindCls.value();
            }
        }
        try {
            Method method = cls.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (T) method.invoke(null,inflater,parent,false);
            final View view = binding.getRoot();


            return view;

        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }

    private T binding;
    public T getBinding(){
        return binding;
    }
}
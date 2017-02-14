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

public class BindFragment <T extends ViewDataBinding> extends android.support.v4.app.Fragment {
    private Class<T> cls = null;
    public BindFragment() {
        this.setHasOptionsMenu(this.defaultOptionsMenu());
    }

    protected BindFragment(Class<T> cls) {
        this.setHasOptionsMenu(this.defaultOptionsMenu());
        this.cls = cls;
    }

    protected boolean defaultOptionsMenu(){
        OptionsMenu optionsMenu = this.getClass().getAnnotation(OptionsMenu.class);
        if(optionsMenu != null){
            return optionsMenu.value();
        }
        return this.hasOptionsMenu();
    }

    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return LayoutInflaterFactory.setFactory2(super.getLayoutInflater(savedInstanceState));
    }

    private View mView;
    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = getFragmentView(inflater,container);
        Views.process(this);
        lin.core.mvvm.Utils.processViewModel(this);
        this.onCreateView();
        return mView;
    }

    @Nullable
    @Override
    public View getView() {
        return mView;
    }

    protected void onCreateView(){}

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

//            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//                @Override
//                public void onViewAttachedToWindow(View v) {
//                    view.removeOnAttachStateChangeListener(this);
//                    onInited();
//                }
//
//                @Override
//                public void onViewDetachedFromWindow(View v) {
//
//                }
//            });

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
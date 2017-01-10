package lin.core;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * Created by lin on 01/01/2017.
 */

public abstract class BindView<T extends ViewDataBinding> extends ResView {
    public BindView(Class<T> cls,Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(cls);
    }

    public BindView(Class<T> cls,Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(cls);
    }

    public BindView(Class<T> cls,Context context) {
        super(context);
        this.init(cls);
    }

    private T binding = null;
    public T getBinding(){
        return binding;
    }
    private void init(Class<T> cls){
        try {
            Method method = cls.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (T) method.invoke(null,LayoutInflater.from(getContext()),this,false);
            this.addView(binding.getRoot());
        }catch (Throwable e){}
    }
}

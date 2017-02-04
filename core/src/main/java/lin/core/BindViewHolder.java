package lin.core;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import lin.core.annotation.BindCls;

/**
 * Created by lin on 01/01/2017.
 */

public abstract class BindViewHolder<T extends ViewDataBinding> extends ViewHolder {
    private Class<T> cls = null;
    public BindViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected BindViewHolder(Class<T> cls,Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cls = cls;
    }

    @Override
    public View getView(ViewGroup parent) {
//        return super.getView(parent);
        if(cls == null){
            BindCls bindCls = this.getClass().getAnnotation(BindCls.class);
            if(bindCls != null){
                cls = (Class<T>) bindCls.value();
            }
        }
        try {
            Method method = cls.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            binding = (T) method.invoke(null,LayoutInflaterFactory.from(this.getContext()),parent,false);
            final View view = binding.getRoot();

            Views.process(this,view);

            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    view.removeOnAttachStateChangeListener(this);
                    onInited();
                }

                @Override
                public void onViewDetachedFromWindow(View v) {

                }
            });

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

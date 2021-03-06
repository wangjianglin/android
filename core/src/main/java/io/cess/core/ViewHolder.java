package io.cess.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author lin
 * @date 31/12/2016.
 */

public abstract class ViewHolder implements AttrsView {


    private Context context;
    private int resId;
    private Attrs attrs;
    public ViewHolder(Context context, AttributeSet attrs){
        this.context = context;
        this.attrs = new Attrs(context,attrs);

    }

    protected ViewHolder(int resId,Context context, AttributeSet attrs){
        this.context = context;
        this.resId = resId;
        this.attrs = new Attrs(context,attrs);
    }
    public View getView(ViewGroup parent){
        final View view = Views.loadView(this,this.context,parent,resId,false);

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
        io.cess.core.mvvm.Utils.processViewModel(this);
        return view;
    }

    @Override
    public Attrs getAttrs() {
        return attrs;
    }

    protected void onInited(){};

    public Context getContext() {
        return context;
    }
}

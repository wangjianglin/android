package io.cess.core.form;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;

import io.cess.core.AttrType;
import io.cess.core.Attrs;
import io.cess.core.Nav;
import io.cess.core.R;
import io.cess.core.annotation.Click;

/**
 * Created by lin on 18/01/2017.
 */

public class Segue extends Row {

    public Segue(Context context) {
        super(context);
    }

    public Segue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Segue(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Class<?> mCls;

    @Override
    protected void onCreate() {
        super.onCreate();
        Attrs attrs = this.getAttrs();

        if(attrs.hasValue(R.styleable.form,R.styleable.form_form_row_segue)){
            this.setSegue(attrs.getString(R.styleable.form,R.styleable.form_form_row_segue));
        }
    }

    @Click
    private void click(){
        Nav.push(this.getActivity(),mCls,null);
    }

    public void setSegue(Class<?> cls){
        this.mCls = cls;
    }


    public void setSegue(CharSequence className){
        try{
            mCls = Class.forName(className.toString(),false,this.getClass().getClassLoader());
        }catch (Throwable e){
            throw new RuntimeException("class don't exit.",e);
        }
    }

    @BindingAdapter("form_row_segue")
    public static void setSegue(Segue segue,CharSequence className){
        segue.setSegue(className);
    }
    @BindingAdapter("form_row_segue")
    public static void setSegue(Segue segue,Class<?> cls){
        segue.setSegue(cls);
    }

    @Override
    protected void genAttrs() {
        super.genAttrs();
        this.addAttr(R.styleable.form,R.styleable.form_form_row_segue, AttrType.String);
    }
}

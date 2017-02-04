package lin.core.form;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.util.AttributeSet;

import lin.core.AttrType;
import lin.core.Attrs;
import lin.core.Nav;
import lin.core.R;
import lin.core.annotation.Click;

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


    public void setSegue(String className){
        try{
            mCls = Class.forName(className,false,this.getClass().getClassLoader());
        }catch (Throwable e){
            throw new RuntimeException("class don't exit.",e);
        }
    }

    @BindingAdapter("form_row_segue")
    public static void setSegue(Segue segue,String className){
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

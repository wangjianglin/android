package io.cess.core.form;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.TextViewBindingAdapter;
import android.util.AttributeSet;
import android.widget.TextView;

import io.cess.core.AttrType;
import io.cess.core.Nav;
import io.cess.core.R;
import io.cess.core.SwitchButton;
import io.cess.core.annotation.Click;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;

/**
 * @author lin
 * @date 18/01/2017.
 */
@BindingMethods({
        @BindingMethod(type = Switch.class, attribute = "form_row_switch", method = "setSwitch")
})

//@InverseBindingMethods({
//        @InverseBindingMethod(type = AView.class, attribute = "name")//这里有个event属性，如果不填默认event为 attribute+"AttrChanged" ,比如这里就是nameAttrChanged
//})
@ResId(id="io_cess_core_form_switch")
public class Switch extends Row {

    private boolean mSwitch = false;

    @ViewById(id="io_cess_core_form_row_switch")
    private SwitchButton button;

    public Switch(Context context) {
        super(context);
    }

    public Switch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onInited() {
        super.onInited();
        this.setSwitch(this.getAttrs().getBoolean(R.styleable.form,R.styleable.form_form_row_switch,mSwitch));
    }

    private InverseBindingListener mRowSwitchAttrChanged;

    @Click(id="io_cess_core_form_row_switch")
    private void click(SwitchButton button){
        setSwitch(button.isChecked());
    }

    public boolean isSwitch() {
        return mSwitch;
    }

    public void setSwitch(boolean v) {
        if(this.mSwitch == v){
            return;
        }
        this.mSwitch = v;
        if(button != null) {
            button.setChecked(v);
        }
        if(mRowSwitchAttrChanged != null){
            mRowSwitchAttrChanged.onChange();
        }
    }

    @Override
    protected void genAttrs() {
        super.genAttrs();
        addAttr(R.styleable.form,R.styleable.form_form_row_switch, AttrType.Boolean);
    }

    //    @BindingAdapter("form_row_text")
//    public static void setText(Text text,String val){
//        text.setText(val);
//    }

    @InverseBindingAdapter(attribute = "form_row_switch", event = "rowSwithAttrChanged")
    public static boolean getText(Switch row){
        return row.isSwitch();
    }

    @BindingAdapter(value = {"rowSwithAttrChanged"}, requireAll = false)
    public static void setTextWatcher(Switch view, final InverseBindingListener rowSwitchAttrChanged) {
        view.mRowSwitchAttrChanged = rowSwitchAttrChanged;
    }
}

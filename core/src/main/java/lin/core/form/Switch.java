package lin.core.form;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.adapters.TextViewBindingAdapter;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.AttrType;
import lin.core.Nav;
import lin.core.R;
import lin.core.SwitchButton;
import lin.core.annotation.Click;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;

/**
 * Created by lin on 18/01/2017.
 */
@BindingMethods({
        @BindingMethod(type = Text.class, attribute = "form_row_switch", method = "setSwitch")
})

//@InverseBindingMethods({
//        @InverseBindingMethod(type = AView.class, attribute = "name")//这里有个event属性，如果不填默认event为 attribute+"AttrChanged" ,比如这里就是nameAttrChanged
//})
@ResId(id="lin_core_form_switch")
public class Switch extends Row {

    private boolean mSwitch = false;

    @ViewById(id="lin_core_form_row_switch")
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
        this.setSwitch(this.getAttrs().getBoolean(R.styleable.form,R.styleable.form_form_row_switch,false));
    }

    private InverseBindingListener mRowSwitchAttrChanged;

    @Click(id="lin_core_form_row_switch")
    private void click(SwitchButton button){
        if(button.isChecked() == this.mSwitch){
            return;
        }
        this.mSwitch = button.isChecked();
        if(mRowSwitchAttrChanged != null){
            mRowSwitchAttrChanged.onChange();
        }
//        Nav.push(this.getActivity(),mCls,null);
//        Nav.push(this.getActivity(), TextEdit.class, new Nav.Result() {
//            @Override
//            public void result(Object... result) {
//                if(result == null || result.length == 0
//                        || !(result[0] instanceof CharSequence)){
//                    return;
//                }
//                CharSequence s = (CharSequence) result[0];
//                if(s == null || "".equals(s)){
//                    return;
//                }
//                setText(s);
//                if(mRowTextAttrChanged != null){
//                    mRowTextAttrChanged.onChange();
//                }
//            }
//        },this.hint,this.getText()).setTitle(navTitle);

    }

    public boolean isSwitch() {
        return mSwitch;
    }

    public void setSwitch(boolean v) {
        this.mSwitch = v;
        if(button != null) {
            button.setChecked(v);
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
    public static CharSequence getText(Text text){
        return text.getText();
    }

    @BindingAdapter(value = {"rowSwithAttrChanged"}, requireAll = false)
    public static void setTextWatcher(Switch view, final InverseBindingListener rowSwitchAttrChanged) {
        view.mRowSwitchAttrChanged = rowSwitchAttrChanged;
    }
}

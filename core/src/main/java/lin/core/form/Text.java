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
import lin.core.annotation.Click;

/**
 * Created by lin on 18/01/2017.
 */
@BindingMethods({
        @BindingMethod(type = Text.class, attribute = "form_row_text_hint", method = "setHint"),
        @BindingMethod(type = Text.class, attribute = "form_row_text_title", method = "setNavTitle")
})

//@InverseBindingMethods({
//        @InverseBindingMethod(type = AView.class, attribute = "name")//这里有个event属性，如果不填默认event为 attribute+"AttrChanged" ,比如这里就是nameAttrChanged
//})
public class Text extends Row {

    private String hint;
    private String navTitle;

    public Text(Context context) {
        super(context);
    }

    public Text(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Text(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        this.setHint(this.getAttrs().getString(R.styleable.form,R.styleable.form_form_row_text_hint));
        this.setNavTitle(this.getAttrs().getString(R.styleable.form,R.styleable.form_form_row_text_navtitle));
    }

    private InverseBindingListener mRowTextAttrChanged;
    @Click
    private void click(){
//        Nav.push(this.getActivity(),mCls,null);
        Nav.push(this.getActivity(), TextEdit.class, new Nav.Result() {
            @Override
            public void result(Object... result) {
                if(result == null || result.length == 0
                        || !(result[0] instanceof String)){
                    return;
                }
                String s = (String) result[0];
                if(s == null || "".equals(s)){
                    return;
                }
                setText(s);
                if(mRowTextAttrChanged != null){
                    mRowTextAttrChanged.onChange();
                }
            }
        },this.hint).setTitle(navTitle);

    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getNavTitle() {
        return navTitle;
    }

    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }

    @Override
    protected void genAttrs() {
        super.genAttrs();
        addAttr(R.styleable.form,R.styleable.form_form_row_text_hint, AttrType.String);
        addAttr(R.styleable.form,R.styleable.form_form_row_text_navtitle, AttrType.String);
    }

    //    @BindingAdapter("form_row_text")
//    public static void setText(Text text,String val){
//        text.setText(val);
//    }

    @InverseBindingAdapter(attribute = "form_row_text", event = "rowTextAttrChanged")
    public static String getText(Text text){
        return text.getText();
    }

    @BindingAdapter(value = {"rowTextAttrChanged"}, requireAll = false)
    public static void setTextWatcher(Text view, final InverseBindingListener rowTextAttrChanged) {
        view.mRowTextAttrChanged = rowTextAttrChanged;
    }
}

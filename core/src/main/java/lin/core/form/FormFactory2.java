package lin.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import lin.core.LayoutInflaterFactory;

/**
 * Created by lin on 19/01/2017.
 */

public class FormFactory2 extends LayoutInflaterFactory.AbsFactory2 {
    @Override
    protected String tag() {
        return "form";
    }

    @Override
    protected View createView(View parent, Context context, AttributeSet attributeSet) {
        return new Form(context,attributeSet);
    }
}

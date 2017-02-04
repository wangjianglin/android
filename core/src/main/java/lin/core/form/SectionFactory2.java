package lin.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import lin.core.LayoutInflaterFactory;

/**
 * Created by lin on 19/01/2017.
 */

public class SectionFactory2 extends LayoutInflaterFactory.AbsFactory2 {
    @Override
    protected String tag() {
        return "form.section";
    }

    @Override
    protected View createView(View parent, Context context, AttributeSet attributeSet) {
        return new Section(context,attributeSet);
    }
}

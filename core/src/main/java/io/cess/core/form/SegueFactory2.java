package io.cess.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import io.cess.core.LayoutInflaterFactory;

/**
 * @author lin
 * @date 19/01/2017.
 */

public class SegueFactory2 extends LayoutInflaterFactory.AbsFactory2 {
    @Override
    protected String tag() {
        return "form.segue";
    }

    @Override
    protected View createView(View parent, Context context, AttributeSet attributeSet) {
        return new Segue(context,attributeSet);
    }
}

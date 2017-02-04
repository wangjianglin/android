package lin.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import lin.core.ReView;

/**
 * Created by lin on 12/01/2017.
 */

public class Form extends ReView {
    public Form(Context context) {
        super(context);
    }

    public Form(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Form(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    @Override
//    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
//        return p instanceof LinearLayout.LayoutParams;
//    }
//
//    @Override
//    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new LinearLayout.LayoutParams(this.getContext(), attrs);
//    }
//
//
//    @Override
//    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
//        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//    }
//
//    @Override
//    protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
//        return new LinearLayout.LayoutParams(lp);
//    }
}

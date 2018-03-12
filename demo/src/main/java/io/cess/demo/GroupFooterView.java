package io.cess.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import io.cess.core.ResView;
import io.cess.core.annotation.ViewById;

/**
 * @author lin
 * @date 11/11/15.
 */
public class GroupFooterView extends ResView {
    public GroupFooterView(Context context) {
        super(context);
    }

    public GroupFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @ViewById(R.id.textViewId)
    private TextView textView;
}

package io.cess.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import io.cess.core.ResView;
import io.cess.core.annotation.ViewById;

/**
 * Created by lin on 11/11/15.
 */
public class GroupHeaderView extends ResView {
    public GroupHeaderView(Context context) {
        super(context);
    }

    public GroupHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GroupHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById(R.id.textViewId)
    private TextView textView;
}

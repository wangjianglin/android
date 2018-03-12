package io.cess.demo;

import android.content.Context;
import android.util.AttributeSet;

import io.cess.core.annotation.ResId;

/**
 * @author lin
 * @date 9/6/15.
 */
@ResId(R.layout.activity_merge_view)
public class MergeView extends io.cess.core.ResView {
    public MergeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MergeView(Context context) {
        super(context);
    }
}

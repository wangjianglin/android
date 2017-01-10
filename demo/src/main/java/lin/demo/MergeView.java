package lin.demo;

import android.content.Context;
import android.util.AttributeSet;

import lin.core.annotation.ResId;

/**
 * Created by lin on 9/6/15.
 */
@ResId(R.layout.activity_merge_view)
public class MergeView extends lin.core.ResView {
    public MergeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MergeView(Context context) {
        super(context);
    }
}

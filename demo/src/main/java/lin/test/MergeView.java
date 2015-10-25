package lin.test;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by lin on 9/6/15.
 */
@lin.core.annotation.ResourceId(R.layout.activity_merge_view)
public class MergeView extends lin.core.ResourceView {
    public MergeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MergeView(Context context) {
        super(context);
    }
}

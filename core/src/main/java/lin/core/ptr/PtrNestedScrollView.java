package lin.core.ptr;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by lin on 10/01/2017.
 */

public class PtrNestedScrollView extends PtrViewBase<NestedScrollView> {
    public PtrNestedScrollView(Context context) {
        super(context);
    }

    public PtrNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected NestedScrollView genContentView() {
        return new NestedScrollView(this.getContext());
    }
}

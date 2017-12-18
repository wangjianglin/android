package lin.core;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by lin on 16/10/2017.
 *
 * 实现  GridView 中自动布局
 */

public class AtMostGridView  extends GridView {

    public AtMostGridView(Context context) {
        super(context);
    }

    public AtMostGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AtMostGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
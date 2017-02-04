package lin.core.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by lin on 10/01/2017.
 */

public class PtrScrollView extends PtrViewBase<ScrollView> {
    public PtrScrollView(Context context) {
        super(context);
    }

    public PtrScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PtrScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected ScrollView genContentView() {
        ScrollView scrollView = new ScrollView(this.getContext());
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        return scrollView;
    }

    @Override
    public void setMode(Mode mode) {
        if(mode == Mode.Both
                || mode == Mode.Refresh){
            super.setMode(Mode.Refresh);
        }else{
            super.setMode(Mode.Disable);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        getContentView().addView(child, index, params);
    }
}

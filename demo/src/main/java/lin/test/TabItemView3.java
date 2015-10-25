package lin.test;

import android.content.Context;
import android.util.AttributeSet;

import lin.core.ResourceView;

/**
 * Created by lin on 9/6/15.
 */
@lin.core.annotation.ResourceId(R.layout.tabbar_item)
public class TabItemView3 extends ResourceView {
    public TabItemView3(Context context) {
        super(context);
    }

    public TabItemView3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("========  333   ========");
    }
}

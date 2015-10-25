package lin.test;

import android.content.Context;
import android.util.AttributeSet;

import lin.core.ResourceView;

/**
 * Created by lin on 9/6/15.
 */
@lin.core.annotation.ResourceId(R.layout.tabbar_item)
public class TabItemView1 extends ResourceView {
    public TabItemView1(Context context) {
        super(context);
    }

    public TabItemView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("========  111   ========");
    }
}

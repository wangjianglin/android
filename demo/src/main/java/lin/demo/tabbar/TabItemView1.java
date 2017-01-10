package lin.demo.tabbar;

import android.content.Context;
import android.util.AttributeSet;

import lin.core.ResView;
import lin.core.annotation.ResId;
import lin.demo.R;

/**
 * Created by lin on 9/6/15.
 */
@ResId(R.layout.activity_tabbar_tabbar_item)
public class TabItemView1 extends ResView {
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

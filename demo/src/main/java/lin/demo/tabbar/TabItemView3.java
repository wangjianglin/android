package lin.demo.tabbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.ResView;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;
import lin.demo.R;

/**
 * Created by lin on 9/6/15.
 */
@ResId(R.layout.activity_tabbar_tabbar_item)
public class TabItemView3 extends ResView {
    public TabItemView3(Context context) {
        super(context);
    }

    public TabItemView3(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @ViewById(R.id.tabbar_item_checkbox_text)
    private TextView textView;

    @Override
    protected void onInited() {
        textView.setText(this.getClass().getName());
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("========  333   ========");
    }
}

package lin.demo.tabbar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.CheckBox;

import lin.core.ResView;
import lin.core.annotation.ResId;
import lin.demo.R;

/**
 * Created by lin on 9/6/15.
 */
@ResId(R.layout.activity_tabbar_tabbar_item)
public class TabItemView4 extends ResView {
    public TabItemView4(Context context) {
        super(context);
    }

    public TabItemView4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("========  444   ========");
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        checkBox.setChecked(false);
    }

    @lin.core.annotation.ViewById(R.id.tabbar_item_checkbox_id)
    private CheckBox checkBox;
}

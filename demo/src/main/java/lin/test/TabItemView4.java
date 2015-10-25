package lin.test;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.CheckBox;

import lin.core.ResourceView;

/**
 * Created by lin on 9/6/15.
 */
@lin.core.annotation.ResourceId(R.layout.tabbar_item)
public class TabItemView4 extends ResourceView {
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

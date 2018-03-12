package io.cess.demo.tabbar;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import io.cess.core.ResView;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;
import io.cess.demo.R;

/**
 * @author lin
 * @date 9/6/15.
 */
@ResId(R.layout.activity_tabbar_tabbar_item)
public class TabItemView2 extends ResView {
    public TabItemView2(Context context) {
        super(context);
    }

    public TabItemView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(View.VISIBLE == visibility){
            checkBox.setChecked(false);
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(View.VISIBLE == visibility){
            checkBox.setChecked(false);
        }
    }
    @ViewById(R.id.tabbar_item_checkbox_text)
    private TextView textView;

    @Override
    protected void onInited() {
        textView.setText(this.getClass().getName());
    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        checkBox.setChecked(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("========  222   ========");
    }

    @ViewById(R.id.tabbar_item_checkbox_id)
    private CheckBox checkBox;
}

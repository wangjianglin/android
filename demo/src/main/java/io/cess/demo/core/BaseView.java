package io.cess.demo.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Toast;

import io.cess.core.ResView;
import io.cess.core.annotation.Click;
import io.cess.core.annotation.ResId;
import io.cess.demo.R;

/**
 * @author lin
 * @date 14/01/2017.
 */
@ResId(R.layout.activity_view_base)
public abstract class BaseView extends ResView {
    public BaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context) {
        super(context);
    }

    @Click(R.id.activity_view_base_button)
    private void click(){
        Toast toast = Toast.makeText(getContext(),
                "base view click", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

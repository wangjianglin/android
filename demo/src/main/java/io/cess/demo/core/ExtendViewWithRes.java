package io.cess.demo.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Toast;

import io.cess.core.annotation.Click;
import io.cess.core.annotation.ResId;
import io.cess.demo.R;

/**
 * @author lin
 * @date 14/01/2017.
 */
@ResId(R.layout.activity_view_extend)
public class ExtendViewWithRes extends BaseView {
    public ExtendViewWithRes(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ExtendViewWithRes(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendViewWithRes(Context context) {
        super(context);
    }

    @Click(R.id.activity_view_extend_button)
    private void click(){
        Toast toast = Toast.makeText(getContext(),
                "extend view click", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

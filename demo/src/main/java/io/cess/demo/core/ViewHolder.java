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
@ResId(R.layout.activity_view_holder_item)
public class ViewHolder extends io.cess.core.ViewHolder {

    public ViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Click(R.id.activity_view_holder_item_button)
    private void click(){
        Toast toast = Toast.makeText(getContext(),
                "view holder click", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

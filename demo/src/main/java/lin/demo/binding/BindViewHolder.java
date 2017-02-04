package lin.demo.binding;

import android.content.Context;
import android.util.AttributeSet;

import lin.core.annotation.BindCls;

/**
 * Created by lin on 16/01/2017.
 */

@BindCls(lin.demo.databinding.ActivityBindViewBinding.class)
public class BindViewHolder extends lin.core.BindViewHolder<lin.demo.databinding.ActivityBindViewBinding> {


    public BindViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onInited() {
        User user = new User("fire name","last name");

        this.getBinding().setData(user);
    }
}

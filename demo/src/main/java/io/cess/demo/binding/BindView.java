package io.cess.demo.binding;

import android.content.Context;
import android.util.AttributeSet;

import io.cess.core.annotation.BindCls;

/**
 * Created by lin on 16/01/2017.
 */

@BindCls(io.cess.demo.databinding.ActivityBindViewBinding.class)
public class BindView extends io.cess.core.BindView<io.cess.demo.databinding.ActivityBindViewBinding> {
    public BindView(Context context) {
        super(context);
    }

    public BindView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BindView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onInited() {
        User user = new User("fire name","last name");

        this.getBinding().setData(user);
    }
}

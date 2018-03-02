package io.cess.core.form;

import android.content.Context;
import android.util.AttributeSet;

import io.cess.core.annotation.Click;

/**
 * Created by lin on 18/01/2017.
 * <io.cess.core.form.Button app:onClick="" />
 */

public class Button extends Row {
    public Button(Context context) {
        super(context);
    }

    public Button(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}

package lin.core;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;

/**
 * Created by lin on 01/01/2017.
 */

public abstract class BindViewHolder<T extends ViewDataBinding> extends ViewHolder {
    public BindViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

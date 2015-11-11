package lin.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.ResourceView;
import lin.core.annotation.ViewById;

/**
 * Created by lin on 11/11/15.
 */
public class GroupFooterView extends ResourceView {
    public GroupFooterView(Context context) {
        super(context);
    }

    public GroupFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GroupFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @ViewById(R.id.textViewId)
    private TextView textView;
}

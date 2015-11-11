package lin.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.ResourceView;
import lin.core.annotation.ViewById;

/**
 * Created by lin on 11/11/15.
 */
public class GroupRowView extends ResourceView {
    public GroupRowView(Context context) {
        super(context);
    }

    public GroupRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GroupRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById(R.id.textViewId)
    private TextView textView;
}

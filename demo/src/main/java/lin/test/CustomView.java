package lin.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.ResourceView;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.annotation.ViewById;
import lin.core.annotation.ViewModel;
import lin.test.binding.User;

/**
 * Created by lin on 7/8/16.
 */
@ResourceClass(R.class)
@ResourceId(R.layout.custome_view)
public class CustomView extends ResourceView {

    @ViewModel
    private User user = new User("fei", "Liang", 27);

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @ViewById()
    private TextView textView2;

    @Override
    protected void onInited() {
        System.out.println("text view:"+textView2);
    }
}

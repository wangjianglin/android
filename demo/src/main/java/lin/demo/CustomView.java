package lin.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.ResView;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.ViewById;

/**
 * Created by lin on 7/8/16.
 */
@ResCls(R.class)
@ResId(R.layout.custome_view)
public class CustomView extends ResView {

//    @ViewModel
//    private User user = new User("fei", "Liang", 27);

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
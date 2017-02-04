package lin.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import lin.core.Nav;
import lin.core.R;
import lin.core.ResView;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.Touch;
import lin.core.annotation.ViewById;

/**
 * Created by lin on 20/01/2017.
 */
@ResCls(R.class)
@ResId(id="lin_core_form_text_edit")
public class TextEdit extends ResView {
    public TextEdit(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @ViewById(id="lin_core_form_row_text_view")
    private TextView hintView;

    @Override
    protected void onInited() {
        Object[] args = Nav.getNav(this).getArgs();
        if(args != null && args.length > 0){
            hintView.setText((String) args[0]);
        }
    }

    @Touch
    private void touch(){
        Nav.getNav(this).pop("ok.");
    }

    public TextEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextEdit(Context context) {
        super(context);
    }
}

package lin.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import lin.core.Nav;
import lin.core.R;
import lin.core.ResFragment;
import lin.core.ResView;
import lin.core.annotation.EditorAction;
import lin.core.annotation.Key;
import lin.core.annotation.OptionsMenu;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;
import lin.core.annotation.Touch;
import lin.core.annotation.ViewById;

/**
 * Created by lin on 20/01/2017.
 */
//ic_menu_save
@ResCls(R.class)
@ResId(id="lin_core_form_text_edit")
@OptionsMenu
public class TextEdit extends ResFragment{
    @ViewById(id="lin_core_form_row_text_view")
    private TextView hintView;

    @ViewById(id="lin_core_form_row_text_edit")
    private TextView textView;

    @EditorAction(id = "lin_core_form_row_text_edit")
    private void action(TextView v,int actionId, KeyEvent event){
        Nav.getNav(this).pop(v.getText());
    }

    @Override
    protected void onCreateView() {
        Object[] args = Nav.getNav(this).getArgs();
        if(args != null){
            if(args.length > 0) {
                hintView.setText((CharSequence) args[0]);
            }
            if(args.length > 1){
                textView.setText((CharSequence) args[1]);
                textView.setSelected(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.lin_core_form_edit_text, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.lin_core_form_edit_text_save_item){
            Nav.getNav(this).pop(textView.getText());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    protected void onInited() {
//        Object[] args = Nav.getNav(this).getArgs();
//        if(args != null && args.length > 0){
//            hintView.setText((String) args[0]);
//        }
//    }
}
//@ResCls(R.class)
//@ResId(id="lin_core_form_text_edit")
//public class TextEdit extends ResView {
//    public TextEdit(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @ViewById(id="lin_core_form_row_text_view")
//    private TextView hintView;
//
//    @Key(id = "lin_core_form_row_text_edit",action = KeyEvent.KEYCODE_ENTER)
//    private void done(EditText editText){
//        Nav.getNav(this).pop(editText.getText());
//    }
//
//    @Override
//    protected void onInited() {
//        Object[] args = Nav.getNav(this).getArgs();
//        if(args != null && args.length > 0){
//            hintView.setText((String) args[0]);
//        }
//    }
//
//    @EditorAction(id = "lin_core_form_row_text_edit")
//    private void action(TextView v,int actionId, KeyEvent event){
//        Nav.getNav(this).pop(v.getText());
//    }
//
//    public TextEdit(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public TextEdit(Context context) {
//        super(context);
//    }
//}

package io.cess.core.form;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import io.cess.core.Nav;
import io.cess.core.R;
import io.cess.core.ResFragment;
import io.cess.core.ResView;
import io.cess.core.annotation.EditorAction;
import io.cess.core.annotation.Key;
import io.cess.core.annotation.OptionsMenu;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.Touch;
import io.cess.core.annotation.ViewById;

/**
 * Created by lin on 20/01/2017.
 */
@ResCls(R.class)
@ResId(id="io_cess_core_form_text_edit")
@OptionsMenu
public class TextEdit extends ResFragment{
    @ViewById(id="io_cess_core_form_row_text_view")
    private TextView hintView;

    @ViewById(id="io_cess_core_form_row_text_edit")
    private EditText editText;

    @EditorAction(id = "io_cess_core_form_row_text_edit")
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
            if(args.length > 1 && args[1] != null){
                editText.setText((CharSequence) args[1]);
                editText.setSelection(((CharSequence) args[1]).length());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.io_cess_core_form_edit_text, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.io_cess_core_form_edit_text_save_item){
            Nav.getNav(this).pop(editText.getText());
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
//@ResId(id="io_cess_core_form_text_edit")
//public class TextEdit extends ResView {
//    public TextEdit(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @ViewById(id="io_cess_core_form_row_text_view")
//    private TextView hintView;
//
//    @Key(id = "io_cess_core_form_row_text_edit",action = KeyEvent.KEYCODE_ENTER)
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
//    @EditorAction(id = "io_cess_core_form_row_text_edit")
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

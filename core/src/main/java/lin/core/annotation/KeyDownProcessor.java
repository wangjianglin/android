package lin.core.annotation;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class KeyDownProcessor extends KeyAbsProcessor<KeyDown>{

	@Override
	protected int[] getIds(KeyDown annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(KeyDown annot) {
		return annot.id();
	}


	protected boolean isProcess(KeyDown annot,int keyCode,KeyEvent event){

		return (event.getAction() == KeyEvent.ACTION_DOWN)
				&& (annot.keyCode() == Integer.MIN_VALUE || annot.keyCode() == keyCode);
	}
}

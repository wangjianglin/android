package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class KeyProcessor extends KeyAbsProcessor<Key>{

	@Override
	protected int[] getIds(Key annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(Key annot) {
		return annot.id();
	}

	protected boolean isProcess(Key annot,int keyCode,KeyEvent event){
		return (annot.action() == Integer.MIN_VALUE || annot.action() == event.getAction())
				&& (annot.keyCode() == Integer.MIN_VALUE || annot.keyCode() == keyCode);
	}
}

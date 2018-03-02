package io.cess.core.annotation;

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
public class KeyUpProcessor extends KeyAbsProcessor<KeyUp>{

	@Override
	protected int[] getIds(KeyUp annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(KeyUp annot) {
		return annot.id();
	}

	protected boolean isProcess(KeyUp annot,int keyCode,KeyEvent event){
		return (event.getAction() == KeyEvent.ACTION_UP)
				&& (annot.keyCode() == Integer.MIN_VALUE || annot.keyCode() == keyCode);
	}
}

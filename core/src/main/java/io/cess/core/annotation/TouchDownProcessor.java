package io.cess.core.annotation;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lin
 * @date Jul 28, 2015 6:19:12 PM
 *
 */
public class TouchDownProcessor extends TouchAbsProcessor<TouchDown>{

	@Override
	protected boolean isProcess(MotionEvent event,TouchDown annot) {
		return event.getAction() == MotionEvent.ACTION_DOWN;
	}

	@Override
	protected int[] getIds(TouchDown annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(TouchDown annot) {
		return annot.id();
	}
}
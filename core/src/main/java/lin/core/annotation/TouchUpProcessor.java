package lin.core.annotation;

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
public class TouchUpProcessor extends TouchAbsProcessor<TouchUp> {

	@Override
	protected boolean isProcess(MotionEvent event,TouchUp annot) {
		return event.getAction() == MotionEvent.ACTION_UP;
	}

	@Override
	protected int[] getIds(TouchUp annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(TouchUp annot) {
		return annot.id();
	}
}
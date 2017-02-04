package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 
 * @author lin
 * @date Jul 28, 2015 6:19:12 PM
 *
 */
public class TouchProcessor extends TouchAbsProcessor<Touch>{

	@Override
	protected boolean isProcess(MotionEvent event,Touch annot) {
		return annot.action() == Integer.MIN_VALUE
				|| annot.action() == event.getAction();
	}

	@Override
	protected int[] getIds(Touch annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(Touch annot) {
		return annot.id();
	}
}
//public class TouchProcessor extends AbstractMethodProcessor{
//	@Override
//	protected int[] getIds(Annotation annotation) {
//		return ((Touch)annotation).value();
//	}
//
//	@Override
//	protected String[] getStringIds(Annotation annotation) {
//		return ((Touch)annotation).id();
//	}
//
//	@Override
//	protected void processFieldItem(Object target, Method method, View itemView){
//
//		Class<?> viewClass = null;
//		Class<?>[] clcikMethodParams = method.getParameterTypes();
//		if(clcikMethodParams != null){
//			if(clcikMethodParams.length > 3){
//				return;
//			}
//			boolean iv = false;
//			boolean ie = false;
//			for(Class<?> clcikMethodParam : clcikMethodParams){
//				if(clcikMethodParam == null){
//					return;
//				}
//				if(View.class.isAssignableFrom(clcikMethodParam)){
//					if(iv){return;}
//					viewClass = clcikMethodParam;
//					iv = true;
//				}
//				else if(MotionEvent.class.isAssignableFrom(clcikMethodParam)){
//					if(ie){return;}
//					ie = true;
//				}else{
//					return;
//				}
//			}
//		}
//
//		if(!(viewClass == null || viewClass.isAssignableFrom(itemView.getClass()))){
//			return;
//		}
//		itemView.setOnTouchListener(new ViewOnTouchListener(target,method,clcikMethodParams));
//	}
//
//	private class ViewOnTouchListener implements OnTouchListener{
//
//		private Object view;
//		private Class<?>[] clcikMethodParams;
//		private Method method;
//		ViewOnTouchListener(Object view,Method method,Class<?>[] clcikMethodParams){
//			this.view = view;
//			this.clcikMethodParams = clcikMethodParams;
//			this.method = method;
//		}
//
//
//		@SuppressLint("ClickableViewAccessibility")
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//		try{
//			method.setAccessible(true);
//			Object result = null;
//			if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
//				result = method.invoke(this.view);
//			}else{
//				Class<?> clcikMethodParam = null;
//				List<Object> args = new ArrayList<Object>();
//				for(int n=0;n<clcikMethodParams.length;n++){
//					clcikMethodParam = clcikMethodParams[n];
//					if(clcikMethodParam == null){
//						args.add(null);
//						continue;
//					}
//					if(View.class.isAssignableFrom(clcikMethodParam)){
//						args.add(v);
//					}
//					else if(MotionEvent.class.isAssignableFrom(clcikMethodParam)){
//						args.add(event);
//					}
//				}
//				result = method.invoke(this.view,args.toArray());
//			}
//
//			if(result instanceof Boolean){
//				return (Boolean)result;
//			}
//		}catch(Throwable e){e.printStackTrace();}
//			return false;
//		}
//	}
//}
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
public abstract class TouchAbsProcessor<T extends Annotation> extends AbstractMethodProcessor<T,View>{

	protected abstract boolean isProcess(MotionEvent event,T annot);
	@Override
	final protected void processMethod(Object target, Method method, View itemView,T annot){

//		Class<?> viewClass = null;
		Class<?>[] methodParams = method.getParameterTypes();
		if(!Utils.validate(methodParams,itemView.getClass(),MotionEvent.class)){
			return;
		}
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
		itemView.setOnTouchListener(new ViewOnTouchListener(target,method,methodParams,annot));
	}

	private class ViewOnTouchListener implements OnTouchListener{

		private Object view;
		private Class<?>[] methodParams;
		private Method method;
		T annot;
		ViewOnTouchListener(Object view,Method method,Class<?>[] methodParams,T annot){
			this.view = view;
			this.methodParams = methodParams;
			this.method = method;
			this.annot = annot;
		}
		

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!isProcess(event,annot)){
				return false;
			}
		try{
			method.setAccessible(true);
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
			Object result = method.invoke(this.view,Utils.args(methodParams,v,event));
			
			if(result instanceof Boolean){
				return (Boolean)result;
			}
		}catch(Throwable e){e.printStackTrace();}
			return false;
		}		
	}
}
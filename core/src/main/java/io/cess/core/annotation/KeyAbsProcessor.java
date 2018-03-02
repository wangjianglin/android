package io.cess.core.annotation;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public abstract class KeyAbsProcessor<T extends Annotation> extends AbstractMethodProcessor<T,View>{


	@Override
	protected void processMethod(Object target, Method method, View itemView,T annot){


//		Class<?> viewClass = null;
		Class<?>[] methodParams = method.getParameterTypes();
		if(!Utils.validate(methodParams,itemView.getClass(),int.class,KeyEvent.class)){
			return;
		}
//		if(clcikMethodParams != null){
//			if(clcikMethodParams.length > 3){
//				return;
//			}
//			boolean vc = false;
//			boolean ic = false;
//			boolean ec = false;
//			for(Class<?> clcikMethodParam : clcikMethodParams){
//				if(clcikMethodParam == null){
//					return;
//				}
//				if(View.class.isAssignableFrom(clcikMethodParam)){
//					if(vc){return;}
//					viewClass = clcikMethodParam;
//					vc = true;
//				}
//				else if(int.class.isAssignableFrom(clcikMethodParam)){
//					if(ic){return;}
//					ic = true;
//				}
//				else if(KeyEvent.class.isAssignableFrom(clcikMethodParam)){
//					if(ec){return;}
//					ec = true;
//				}else{
//					return;
//				}
//			}
//		}
//
//		if(!(viewClass == null || viewClass.isAssignableFrom(itemView.getClass()))){
//			return;
//		}

		itemView.setOnKeyListener(new ViewOnKeyListener(target,method,methodParams,annot));
	}

	protected abstract boolean isProcess(T annot,int keyCode,KeyEvent event);

	private class ViewOnKeyListener implements OnKeyListener{

		private Object view;
		private Class<?>[] methodParams;
		private Method method;
		private T annot;
		ViewOnKeyListener(Object view,Method method,Class<?>[] methodParams,T annot){
			this.view = view;
			this.methodParams = methodParams;
			this.method = method;
			this.annot = annot;
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (!isProcess(annot,keyCode,event)){
				return false;
			}
			try{
				method.setAccessible(true);
//				Object result = null;
//				if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
//					result = method.invoke(this.view);
//				}else{
//					Class<?> clcikMethodParam = null;
//					List<Object> args = new ArrayList<Object>();
//					for(int n=0;n<clcikMethodParams.length;n++){
//						clcikMethodParam = clcikMethodParams[n];
//						if(clcikMethodParam == null){
//							args.add(null);
//							continue;
//						}
//						if(View.class.isAssignableFrom(clcikMethodParam)){
//							args.add(v);
//						}
//						else if(int.class.isAssignableFrom(clcikMethodParam)){
//							args.add(keyCode);
//						}
//						else if(KeyEvent.class.isAssignableFrom(clcikMethodParam)){
//							args.add(event);
//						}
//					}
//					result = method.invoke(this.view,args.toArray());
//				}

				Object result = method.invoke(this.view,Utils.args(methodParams,v,keyCode,event));

				if(result instanceof Boolean){
					return (Boolean)result;
				}
			}catch(Throwable e){e.printStackTrace();}
			return false;
		}
		
	}
}

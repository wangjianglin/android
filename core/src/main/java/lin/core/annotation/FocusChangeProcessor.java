package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnFocusChangeListener;

/**
 * 
 * @author lin
 * @date Jul 30, 2015 4:52:01 PM
 *
 */
public class FocusChangeProcessor extends AbstractMethodProcessor<FocusChange,View>{

	@Override
	protected int[] getIds(FocusChange annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(FocusChange annot) {
		return annot.id();
	}

	@Override
	protected void processMethod(Object target, Method method, View itemView,FocusChange annot){

		Class<?>[] methodParams = method.getParameterTypes();
		if(!Utils.validate(methodParams,itemView.getClass(),boolean.class)){
			return;
		}
//		if(clcikMethodParams != null){
//			if(clcikMethodParams.length > 3){
//				return;
//			}
//			boolean iv = false;
//			boolean ib = false;
//			for(Class<?> clcikMethodParam : clcikMethodParams){
//				if(clcikMethodParam == null){
//					return;
//				}
//				if(clcikMethodParam.isAssignableFrom(itemView.getClass())){
//					if(iv){return;}
//					iv = true;
//				}
//				else if(clcikMethodParam.isAssignableFrom(boolean.class)){
//					if(ib){return;}
//					ib = true;
//				}else{
//					return;
//				}
//			}
//		}
		itemView.setOnFocusChangeListener(new ViewOnFocusChangeListener(target,method,methodParams));
	}

	private class ViewOnFocusChangeListener implements OnFocusChangeListener{

		private Object view;
		private Class<?>[] methodParams;
		private Method method;
		ViewOnFocusChangeListener(Object view,Method method,Class<?>[] methodParams){
			this.view = view;
			this.methodParams = methodParams;
			this.method = method;
		}
		

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
		try{
			method.setAccessible(true);
//			if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
//				method.invoke(this.view);
//			}else{
//				Class<?> clcikMethodParam = null;
//				List<Object> args = new ArrayList<Object>();
//				for(int n=0;n<clcikMethodParams.length;n++){
//					clcikMethodParam = clcikMethodParams[n];
//					if(clcikMethodParam == null){
//						args.add(null);
//						continue;
//					}
//					if(clcikMethodParam.isAssignableFrom(View.class)){
//						args.add(v);
//					}
//					else if(boolean.class.isAssignableFrom(clcikMethodParam)){
//						args.add(hasFocus);
//					}
//				}
			method.invoke(this.view,Utils.args(methodParams,v,hasFocus));
//			}
		}catch(Throwable e){e.printStackTrace();}
		}		
	}
}
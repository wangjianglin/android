package lin.core.annotation;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lin.core.Nav;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class EditorActionProcessor extends AbstractMethodProcessor<EditorAction,TextView>{

	@Override
	protected int[] getIds(EditorAction annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(EditorAction annot) {
		return annot.id();
	}

	@Override
	protected void processMethod(Object target, Method method, TextView itemView,EditorAction annot){


		Class<?>[] methodParams = method.getParameterTypes();

		if(!Utils.validate(methodParams,itemView.getClass(),KeyEvent.class,int.class)){
			return;
		}
//		if(clcikMethodParams != null){
//			if(clcikMethodParams.length > 3){
//				return;
//			}
//			boolean iv = false;
//			boolean ik = false;
//			boolean ii = false;
//			for(Class<?> clcikMethodParam : clcikMethodParams){
//				if(clcikMethodParam == null){
//					return;
//				}
//				if(clcikMethodParam.isAssignableFrom(itemView.getClass())){
//					if(iv){return;}
//					iv = true;
//				}
//				else if(clcikMethodParam.isAssignableFrom(KeyEvent.class)){
//					if(ik){return;}
//					ik = true;
//				}else if(clcikMethodParam.isAssignableFrom(int.class)
//						|| Integer.class.isAssignableFrom(clcikMethodParam)){
//					if(ii){return;}
//					ii = true;
//				}else{
//					return;
//				}
//			}
//		}

		itemView.setOnEditorActionListener(new ViewOnEditorActionListener(target,method,methodParams,annot));
	}



	private class ViewOnEditorActionListener implements TextView.OnEditorActionListener{

		private Object view;
		private Method method;
		private Class<?>[] methodParams;
		private EditorAction annot;

		ViewOnEditorActionListener(Object view,Method method,Class<?>[] methodParams,EditorAction annot){
			this.view = view;
			this.method = method;
			this.methodParams = methodParams;
			this.annot = annot;
		}

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(this.annot.action() != Integer.MIN_VALUE
					&& this.annot.action() != event.getAction()){
				return false;
			}
			try{
				method.setAccessible(true);
//				Object result = null;
//				if(clcikMethodParams == null || clcikMethodParams.length == 0){
//					result = method.invoke(this.view);
//				}else{
//					Class<?> clickMethodParam = null;
//					List<Object> args = new ArrayList<>();
//					for(int n=0;n<clcikMethodParams.length;n++){
//						clickMethodParam = clcikMethodParams[n];
//						if(clickMethodParam == null){
//							args.add(null);
//						}else if(clickMethodParam.isAssignableFrom(view.getClass())){
//							args.add(v);
//						}else if(clickMethodParam.isAssignableFrom(KeyEvent.class)){
//							args.add(event);
//						}else{
//							args.add(actionId);
//						}
//					}
//					result = method.invoke(this.view,args);
//				}
				Object result = method.invoke(this.view,Utils.args(methodParams,v,actionId,event));
				if(result != null && result instanceof Boolean){
					return (Boolean)result;
				}
			}catch(Throwable e){e.printStackTrace();}
			return false;
		}

	}
}

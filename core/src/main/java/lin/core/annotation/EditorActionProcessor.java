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
public class EditorActionProcessor extends AbstractMethodProcessor<EditorAction>{

	@Override
	protected int[] getIds(EditorAction annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(EditorAction annot) {
		return annot.id();
	}

	@Override
	protected void processMethod(Object target, Method method, View itemView,EditorAction annot){

		if(!(itemView instanceof EditText)){
			return;
		}

		EditText editText = (EditText) itemView;

		Class<?>[] clcikMethodParams = method.getParameterTypes();
		Class<?> viewClass = null;
		if(clcikMethodParams != null){
			if(clcikMethodParams.length > 3){
				return;
			}
			boolean iv = false;
			boolean ik = false;
			boolean ii = false;
			for(Class<?> clcikMethodParam : clcikMethodParams){
				if(clcikMethodParam == null){
					return;
				}
				if(View.class.isAssignableFrom(clcikMethodParam)){
					if(iv){return;}
					viewClass = clcikMethodParam;
					iv = true;
				}
				else if(KeyEvent.class.isAssignableFrom(clcikMethodParam)){
					if(ik){return;}
					ik = true;
				}else if(int.class.isAssignableFrom(clcikMethodParam)
						|| Integer.class.isAssignableFrom(clcikMethodParam)){
					if(ii){return;}
					ii = true;
				}else{
					return;
				}
			}
		}

		if(!(viewClass == null || viewClass.isAssignableFrom(itemView.getClass()))
				|| viewClass == View.class){
			return;
		}

		editText.setOnEditorActionListener(new ViewOnEditorActionListener(target,method,clcikMethodParams,annot));
	}



	private class ViewOnEditorActionListener implements TextView.OnEditorActionListener{

		private Object view;
		private Method method;
		private Class<?>[] clcikMethodParams;
		private EditorAction annot;

		ViewOnEditorActionListener(Object view,Method method,Class<?>[] clcikMethodParams,EditorAction annot){
			this.view = view;
			this.method = method;
			this.clcikMethodParams = clcikMethodParams;
			this.annot = annot;
		}
		@Override
//		public void onClick(View v) {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(this.annot.action() != Integer.MIN_VALUE
					&& this.annot.action() != event.getAction()){
				return false;
			}
			try{
				method.setAccessible(true);
				Object result = null;
				if(clcikMethodParams == null || clcikMethodParams.length == 0){
					result = method.invoke(this.view);
				}else{
					Class<?> clickMethodParam = null;
					List<Object> args = new ArrayList<>();
					for(int n=0;n<clcikMethodParams.length;n++){
						clickMethodParam = clcikMethodParams[n];
						if(clickMethodParam == null){
							args.add(null);
						}else if(View.class.isAssignableFrom(clickMethodParam)){
							args.add(v);
						}else if(KeyEvent.class.isAssignableFrom(clickMethodParam)){
							args.add(event);
						}else{
							args.add(actionId);
						}
					}
					result = method.invoke(this.view,args);
				}
				if(result != null && result instanceof Boolean){
					return (Boolean)result;
				}
			}catch(Throwable e){e.printStackTrace();}
			return false;
		}

//		@Override
//		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//			try{
//				method.setAccessible(true);
//				Object result = null;
//				if(this.method.getParameterTypes() == null || this.method.getParameterTypes().length == 0){
//					method.invoke(this.view);
//				}else{
//
//					method.invoke(this.view,v);
//				}
//			}catch(Throwable e){e.printStackTrace();}
////			for(int n=0;n<clcikMethodParams.length;n++){
////				clcikMethodParam = clcikMethodParams[n];
////				if(clcikMethodParam == null){
////					args.add(null);
////					continue;
////				}
////				if(View.class.isAssignableFrom(clcikMethodParam)){
////					args.add(v);
////				}
////				else if(MotionEvent.class.isAssignableFrom(clcikMethodParam)){
////					args.add(event);
////				}
////			}
//			return false;
//		}
	}
}

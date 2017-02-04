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
public class FocusChangeProcessor extends AbstractMethodProcessor<FocusChange>{

	@Override
	protected int[] getIds(FocusChange annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(FocusChange annot) {
		return annot.id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processMethod(Object target, Method method, View itemView,FocusChange annot){

//		FocusChange item = (FocusChange)annotation;
//
//		int viewId = 0;
//		if(item.value() != 0){
//			viewId = item.value();
//		}else if(!"".equals(item.id())){
//			try{
//				Field f = idClass.getDeclaredField(item.id());
//				viewId = f.getInt(null);
//			}catch(Throwable e){}
//		}else{
//			try{
//				Field f = idClass.getDeclaredField(method.getName());
//				viewId = f.getInt(null);
//			}catch(Throwable e){}
//		}

		Class<?> viewClass = null;
		Class<?>[] clcikMethodParams = method.getParameterTypes();
		if(clcikMethodParams != null){
			if(clcikMethodParams.length > 3){
				return;
			}
			boolean iv = false;
			boolean ib = false;
			for(Class<?> clcikMethodParam : clcikMethodParams){
				if(clcikMethodParam == null){
					return;
				}
				if(View.class.isAssignableFrom(clcikMethodParam)){
					if(iv){return;}
					viewClass = clcikMethodParam;
					iv = true;
				}
				else if(boolean.class.isAssignableFrom(clcikMethodParam)){
					if(ib){return;}
					ib = true;
				}else{
					return;
				}
			}
		}
//		View itemView = view;
//		if(viewId != 0){
//			itemView = view.findViewById(viewId);
//		}
//		if(itemView == null){
//			return;
//		}
		if(!(viewClass == null || viewClass.isAssignableFrom(itemView.getClass()))){
			return;
		}
//			itemView.setOnClickListener(new ViewOnClickListener(itemView,method,view));
		itemView.setOnFocusChangeListener(new ViewOnFocusChangeListener(target,method,clcikMethodParams));
	}

	private class ViewOnFocusChangeListener implements OnFocusChangeListener{

		private Object view;
		private Class<?>[] clcikMethodParams;
		private Method method;
		ViewOnFocusChangeListener(Object view,Method method,Class<?>[] clcikMethodParams){
			this.view = view;
			this.clcikMethodParams = clcikMethodParams;
			this.method = method;
		}
		

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
		try{
			method.setAccessible(true);
			if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
				method.invoke(this.view);
			}else{
				Class<?> clcikMethodParam = null;
				List<Object> args = new ArrayList<Object>();
				for(int n=0;n<clcikMethodParams.length;n++){
					clcikMethodParam = clcikMethodParams[n];
					if(clcikMethodParam == null){
						args.add(null);
						continue;
					}
					if(View.class.isAssignableFrom(clcikMethodParam)){
						args.add(v);
					}
					else if(boolean.class.isAssignableFrom(clcikMethodParam)){
						args.add(hasFocus);
					}
				}
				method.invoke(this.view,args.toArray());
			}
		}catch(Throwable e){e.printStackTrace();}
		}		
	}
}
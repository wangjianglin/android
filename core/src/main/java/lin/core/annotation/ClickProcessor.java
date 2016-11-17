package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class ClickProcessor extends AbstractMethodProcessor{

	@Override
	protected int[] getIds(Annotation annotation) {
		return ((Click)annotation).value();
	}

	@Override
	protected String[] getStringIds(Annotation annotation) {
		return ((Click)annotation).id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processFieldItem(Object target, Method method, View itemView){
//	Click item = (Click)annotation;
//		if(item == null){
//			return;
//		}
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

		Class<?>[] clcikMethodParams = method.getParameterTypes();
		if(clcikMethodParams != null && clcikMethodParams.length > 1){
			return;
		}
//		View itemView = view;
//		if(viewId != 0){
//			itemView = view.findViewById(viewId);
//		}
		
//		if(itemView == null){
//			return;
//		}
		if(!(clcikMethodParams.length == 0 || (clcikMethodParams.length == 1 && clcikMethodParams[0].isAssignableFrom(itemView.getClass())))){
			return;
		}
		itemView.setOnClickListener(new ViewOnClickListener(target,method));
		
	}



	private class ViewOnClickListener implements OnClickListener{

		private Object view;
		private Method method;
		ViewOnClickListener(Object view,Method method){
			this.view = view;
			this.method = method;
		}
		@Override
		public void onClick(View v) {
			try{
				method.setAccessible(true);
				if(this.method.getParameterTypes() == null || this.method.getParameterTypes().length == 0){
					method.invoke(this.view);
				}else{
					method.invoke(this.view,v);
				}
			}catch(Throwable e){e.printStackTrace();}
		}
		
	}
}

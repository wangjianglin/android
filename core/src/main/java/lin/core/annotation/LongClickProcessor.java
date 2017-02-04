package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class LongClickProcessor extends AbstractMethodProcessor<ListItemClick>{

	@Override
	protected int[] getIds(ListItemClick annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(ListItemClick annot) {
		return annot.id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processMethod(Object target, Method method, View itemView,ListItemClick annot){

//		LongClick item = (LongClick)annotation;
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
//
//		if(itemView == null){
//			return;
//		}
		if(!(clcikMethodParams.length == 0 || (clcikMethodParams.length == 1 && clcikMethodParams[0].isAssignableFrom(itemView.getClass())))){
			return;
		}
//		itemView.setOnClickListener(new ViewOnClickListener(view,method));
		itemView.setOnLongClickListener(new ViewOnLongClickListener(target,method));
	}

	private class ViewOnLongClickListener implements OnLongClickListener{

		private Object view;
		private Method method;
		ViewOnLongClickListener(Object view,Method method){
			this.view = view;
			this.method = method;
		}
		@Override
		public boolean onLongClick(View v) {
			try{
				method.setAccessible(true);
				Object result = null;
				if(this.method.getParameterTypes() == null || this.method.getParameterTypes().length == 0){
					result = method.invoke(this.view);
				}else{
					result = method.invoke(this.view,v);
				}
				if(result instanceof Boolean){
					return (Boolean) result;
				}
			}catch(Throwable e){}
			return true;
		}
		
	}
}

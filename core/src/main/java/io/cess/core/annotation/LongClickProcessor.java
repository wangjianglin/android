package io.cess.core.annotation;

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
public class LongClickProcessor extends AbstractMethodProcessor<LongClick,View>{

	@Override
	protected int[] getIds(LongClick annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(LongClick annot) {
		return annot.id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processMethod(Object target, Method method, View itemView,LongClick annot){


		Class<?>[] methodParams = method.getParameterTypes();
		if(methodParams != null && methodParams.length > 1){
			return;
		}
		if(!(methodParams.length == 0 || (methodParams.length == 1 && methodParams[0].isAssignableFrom(itemView.getClass())))){
			return;
		}
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

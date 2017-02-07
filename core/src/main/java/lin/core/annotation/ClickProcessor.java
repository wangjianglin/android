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
public class ClickProcessor extends AbstractMethodProcessor<Click,View>{

	@Override
	protected int[] getIds(Click annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(Click annot) {
		return annot.id();
	}

	@Override
	protected void processMethod(Object target, Method method, View itemView,Click annot){


		Class<?>[] clcikMethodParams = method.getParameterTypes();
		if(clcikMethodParams != null && clcikMethodParams.length > 1){
			return;
		}

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

package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */

public class CheckedChangeProcessor extends AbstractMethodProcessor<CheckedChange,CompoundButton>{

	@Override
	protected int[] getIds(CheckedChange annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(CheckedChange annot) {
		return annot.id();
	}
	@Override
	protected void processMethod(Object target, Method method, CompoundButton itemView,CheckedChange annot) {

		Class<?>[] methodParams = method.getParameterTypes();
		if(!Utils.validate(methodParams,itemView.getClass(),boolean.class)){
			return;
		}
//		if(clcikMethodParams != null){
//			if(clcikMethodParams.length > 3){
//				return;
//			}
//			boolean vc = false;
//			boolean ib = false;
//			for(Class<?> clcikMethodParam : clcikMethodParams){
//				if(clcikMethodParam == null){
//					return;
//				}
//				if(clcikMethodParam.isAssignableFrom(itemView.getClass())){
//					if(vc){return;}
//					vc = true;
//				}
//				else if(clcikMethodParam.isAssignableFrom(boolean.class)){
//					if(ib){return;}
//					ib = true;
//				}else{
//					return;
//				}
//			}
//		}

		itemView.setOnCheckedChangeListener(new ViewOnCheckedChangeListener(target,method,methodParams));
	}

	private class ViewOnCheckedChangeListener implements OnCheckedChangeListener{

		private Object view;
		private Class<?>[] methodParams;
		private Method method;
		ViewOnCheckedChangeListener(Object view,Method method,Class<?>[] methodParams){
			this.view = view;
			this.methodParams = methodParams;
			this.method = method;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)  {
			try{
				method.setAccessible(true);
				method.invoke(this.view,Utils.args(methodParams,buttonView,isChecked));
//				if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
//					method.invoke(this.view);
//				}else{
//					Class<?> clcikMethodParam = null;
//					List<Object> args = new ArrayList<Object>();
//					for(int n=0;n<clcikMethodParams.length;n++){
//						clcikMethodParam = clcikMethodParams[n];
//						if(clcikMethodParam == null){
//							args.add(null);
//							continue;
//						}
//						if(clcikMethodParam.isAssignableFrom(view.getClass())){
//							args.add(buttonView);
//						}
//						else if(clcikMethodParam.isAssignableFrom(boolean.class)){
//							args.add(isChecked);
//						}
//					}
//					method.invoke(this.view,args.toArray());
//				}
			}catch(Throwable e){e.printStackTrace();}
		}
		
	}
}

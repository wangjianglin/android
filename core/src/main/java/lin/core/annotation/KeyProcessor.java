package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class KeyProcessor extends AbstractMethodProcessor<Key>{

	@Override
	protected int[] getIds(Key annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(Key annot) {
		return annot.id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processMethod(Object target, Method method, View itemView,Key annot){


		Class<?> viewClass = null;
		Class<?>[] clcikMethodParams = method.getParameterTypes();
		if(clcikMethodParams != null){
			if(clcikMethodParams.length > 3){
				return;
			}
			boolean vc = false;
			boolean ic = false;
			boolean ec = false;
			for(Class<?> clcikMethodParam : clcikMethodParams){
				if(clcikMethodParam == null){
					return;
				}
				if(View.class.isAssignableFrom(clcikMethodParam)){
					if(vc){return;}
					viewClass = clcikMethodParam;
					vc = true;
				}
				else if(int.class.isAssignableFrom(clcikMethodParam)){
					if(ic){return;}
					ic = true;
				}
				else if(KeyEvent.class.isAssignableFrom(clcikMethodParam)){
					if(ec){return;}
					ec = true;
				}else{
					return;
				}
			}
		}

		if(!(viewClass == null || viewClass.isAssignableFrom(itemView.getClass()))){
			return;
		}

		itemView.setOnKeyListener(new ViewOnKeyListener(target,method,clcikMethodParams,annot));
	}

	private class ViewOnKeyListener implements OnKeyListener{

		private Object view;
		private Class<?>[] clcikMethodParams;
		private Method method;
		private Key annot;
		ViewOnKeyListener(Object view,Method method,Class<?>[] clcikMethodParams,Key annot){
			this.view = view;
			this.clcikMethodParams = clcikMethodParams;
			this.method = method;
			this.annot = annot;
		}

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (annot.action() != Integer.MIN_VALUE
					&& annot.action() != keyCode){
				return false;
			}
			try{
				method.setAccessible(true);
				Object result = null;
				if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
					result = method.invoke(this.view);
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
						else if(int.class.isAssignableFrom(clcikMethodParam)){
							args.add(keyCode);
						}
						else if(KeyEvent.class.isAssignableFrom(clcikMethodParam)){
							args.add(event);
						}
					}
					result = method.invoke(this.view,args.toArray());
				}
				
				if(result instanceof Boolean){
					return (Boolean)result;
				}
			}catch(Throwable e){e.printStackTrace();}
			return false;
		}
		
	}
}

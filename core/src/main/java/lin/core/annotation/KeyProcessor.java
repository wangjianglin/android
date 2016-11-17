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
public class KeyProcessor extends AbstractMethodProcessor{

	@Override
	protected int[] getIds(Annotation annotation) {
		return ((Key)annotation).value();
	}

	@Override
	protected String[] getStringIds(Annotation annotation) {
		return ((Key)annotation).id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processFieldItem(Object target, Method method, View itemView){

//		Key item = (Key)annotation;
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
		itemView.setOnKeyListener(new ViewOnKeyListener(target,method,clcikMethodParams));
	}

	private class ViewOnKeyListener implements OnKeyListener{

		private Object view;
		private Class<?>[] clcikMethodParams;
		private Method method;
		ViewOnKeyListener(Object view,Method method,Class<?>[] clcikMethodParams){
			this.view = view;
			this.clcikMethodParams = clcikMethodParams;
			this.method = method;
		}
//		@Override
//		public void onClick(View v) {
//			try{
//				method.setAccessible(true);
//				if(this.method.getParameterTypes() == null || this.method.getParameterTypes().length == 0){
//					method.invoke(this.view);
//				}else{
//					method.invoke(this.view,this.item);
//				}
//			}catch(Throwable e){e.printStackTrace();}
//		}
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
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

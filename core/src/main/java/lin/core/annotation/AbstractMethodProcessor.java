package lin.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:57 PM
 *
 */
public abstract class AbstractMethodProcessor implements MethodProcessor {

	private void process(Annotation annotation, Package pack){
		String[] idStrings = this.getStringIds(annotation);
		int[] ids = this.getIds(annotation);
		if(idStrings == null || ids == null
				|| idStrings.length == 0 || ids.length == 0){
			processItem(0);
			return;
		}
		if(ids.length != 1 || ids[0] != 0) {
			for (int id : ids) {
				processItem(id);
			}
		}else if(idStrings.length != 1 || !"".equals(idStrings[0])){
			for (String id : idStrings) {
				processStringItem(id, pack);
			}
		}else{
			processItem(0);
		}
	}

	private void processStringItem(String stringId,
								   Package pack){

		int viewId = 0;
		if(stringId != null && !"".equals(stringId)){
//			try{
//				Field f = idClass.getDeclaredField(stringId);
////                if(f != null) {正常情况下，都不会为null，只有当代码不误时才会为null
//				viewId = f.getInt(null);
////                }
//			}catch(Throwable e){}
			viewId = Utils.getId(pack,stringId);
		}

		if(viewId != -1) {
			processItem(viewId);
		}
	}
	private Method method;
	private View view;
	private View targetView;
	private Object target;


	private void processItem(int viewId) {
		method.setAccessible(true);
		if(viewId == 0) {
			if(targetView != null) {
				processFieldItem(target, method, targetView);
			}
		}else{
			View itemView = view.findViewById(viewId);
			if(itemView != null) {
				processFieldItem(target, method, itemView);
			}
		}
	}

	@Override
	public void process(Object target,View view, Method method, Annotation annotation, Package pack) {
		this.view = view;
		this.target = target;
		if(target instanceof View){
			targetView = (View)target;
		}else{
			targetView = null;
		}
		this.method = method;
		this.process(annotation,pack);
	}


	protected abstract void processFieldItem(Object target, Method method, View itemView);

	protected abstract int[] getIds(Annotation annotation);

	protected abstract String[] getStringIds(Annotation annotation);
}

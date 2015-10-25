package lin.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:57 PM
 *
 */
public abstract class AbstractMethodProcessor implements MethodProcessor {
//	public void process(View view, Method method, Annotation annotation, Class<?> idClass){
//
//	}

	private void process(Annotation annotation, Class<?> idClass){
//		CheckedChange item = (CheckedChange) annotation;
//		String[] idStrings = item.id();
//		int[] ids = item.value();
		String[] idStrings = this.getStringIds(annotation);
		int[] ids = this.getIds(annotation);
		if(idStrings == null || ids == null
				|| idStrings.length == 0 || ids.length == 0){
//                ||
//                ((idStrings.length == 1 && "".equals(idStrings[0]))
//                && (ids.length == 1 && ids[0] == 0))){
			processItem(0);
			return;
		}
		if(ids.length != 1 || ids[0] != 0) {
			for (int id : ids) {
				processItem(id);
			}
		}else if(idStrings.length != 1 || !"".equals(idStrings[0])){
			for (String id : idStrings) {
				processStringItem(id, idClass);
			}
		}else{
			processItem(0);
		}
	}

	private void processStringItem(String stringId,
								   Class<?> idClass){

		int viewId = 0;
		if(stringId != null && !"".equals(stringId)){
			try{
				Field f = idClass.getDeclaredField(stringId);
//                if(f != null) {正常情况下，都不会为null，只有当代码不误时才会为null
				viewId = f.getInt(null);
//                }
			}catch(Throwable e){}
		}
//		else {
//			try {
//				String defaultName = this.getDefaultName();
//				if(defaultName != null && !"".equals(defaultName)) {
//					Field f = idClass.getDeclaredField(defaultName);
////                    if(f!= null) {
//					viewId = f.getInt(null);
////                    }
//				}
//			} catch (Throwable e) {
//			}
//		}
		processItem(viewId);
	}
	private Method method;
	private View view;


	private void processItem(int viewId) {
		method.setAccessible(true);
		if(viewId == 0) {
			processFieldItem(view, method, view);
		}else{
			View itemView = view.findViewById(viewId);
			if(itemView != null) {
				processFieldItem(view, method, itemView);
			}
		}
	}

	@Override
	public void process(View view, Method method, Annotation annotation, Class<?> idClass) {
		this.view = view;
		this.method = method;
		this.process(annotation,idClass);
	}


	protected abstract void processFieldItem(View view,Method method,View itemView);

	protected abstract int[] getIds(Annotation annotation);

	protected abstract String[] getStringIds(Annotation annotation);
}

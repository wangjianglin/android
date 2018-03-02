package io.cess.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:20:57 PM
 *
 */
public abstract class AbstractMethodProcessor<T extends Annotation,V extends View> implements MethodProcessor<T> {

	private void process(Package pack,T annot){
		String[] idStrings = this.getStringIds(annot);
		int[] ids = this.getIds(annot);
		if(idStrings == null || ids == null
				|| idStrings.length == 0 || ids.length == 0){
			processItem(0,annot);
			return;
		}
		if(ids.length != 1 || ids[0] != 0) {
			for (int id : ids) {
				processItem(id,annot);
			}
		}else if(idStrings.length != 1 || !"".equals(idStrings[0])){
			for (String id : idStrings) {
				processStringItem(id, pack,annot);
			}
		}else{
			processItem(0,annot);
		}
	}

	private void processStringItem(String stringId,
								   Package pack,T annot){

		int viewId = 0;
		if(stringId != null && !"".equals(stringId)){
			viewId = Utils.getId(pack,stringId);
		}

		if(viewId != -1) {
			processItem(viewId,annot);
		}
	}
	private Method method;
	private View view;
	private View targetView;
	private Object target;


	private void processItem(int viewId,T annot) {
		method.setAccessible(true);
		if(viewId == 0) {
			if(targetView != null) {
				processViewMethod(target, method, targetView,annot);
			}
		}else{
			View itemView = view.findViewById(viewId);
			if(itemView != null) {
				processViewMethod(target, method, itemView,annot);
			}
		}
	}

	@Override
	public void process(Object target,View view, Method method, T annotation, Package pack) {
		this.view = view;
		this.target = target;
		if(target instanceof View){
			targetView = (View)target;
		}else{
			targetView = null;
		}
		this.method = method;
		this.process(pack,annotation);
	}


	private void processViewMethod(Object target, Method method, View itemView,T annot){

		try {
			processMethod(target, method, (V) itemView, annot);
		}catch (ClassCastException e){

		}
	}
	protected abstract void processMethod(Object target, Method method, V itemView,T annot);

	protected abstract int[] getIds(T annot);

	protected abstract String[] getStringIds(T annot);
}

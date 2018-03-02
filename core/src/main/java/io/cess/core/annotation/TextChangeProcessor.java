package io.cess.core.annotation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class TextChangeProcessor extends AbstractMethodProcessor<TextChange,EditText>{

	@Override
	protected int[] getIds(TextChange annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(TextChange annot) {
		return annot.id();
	}

	@Override
	protected void processMethod(Object target, Method method, EditText itemView,TextChange annot){

		Class<?> viewClass = null;
		Class<?>[] methodParams = method.getParameterTypes();
		if(annot.state() == TextChangeState.After){
			if(!Utils.validate(methodParams,Editable.class)){
				return;
			}
		}
		else if(!Utils.validate(methodParams,CharSequence.class,int.class,int.class,int.class)){
			return;
		}
//		if(methodParams != null){
//			if(annot.state() == TextChangeState.After){
//				if(methodParams.length > 1){
//					return;
//				}
//				if(methodParams.length == 1
//						&& !methodParams[0].isAssignableFrom(Editable.class)){
//					return;
//				}
//			}else {
//				if (methodParams.length > 3) {
//					return;
//				}
//				boolean cc = false;
//				int ic = 0;
//				for (Class<?> methodParam : methodParams) {
//					if (methodParam == null) {
//						return;
//					}
//					if (methodParam.isAssignableFrom(CharSequence.class)) {
//						if (cc) {
//							return;
//						}
//						cc = true;
//					} else if (methodParam.isAssignableFrom(int.class)) {
//						ic += 1;
//						if (ic > 3) {
//							return;
//						}
//					}
//				}
//			}
//		}

		itemView.addTextChangedListener(new EditTextWatcher(itemView,annot,methodParams,method));
	}




	private class EditTextWatcher implements TextWatcher {
		private TextChange annot;
		private Class<?>[] methodParams;
		private Method method;
		private Object obj;
		private EditTextWatcher(Object obj,TextChange annot,Class<?>[] methodParams,Method method){
			this.annot = annot;
			this.methodParams = methodParams;
			this.method = method;
			method.setAccessible(true);
			this.obj = obj;
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			if(annot.state() == TextChangeState.Before){
				invokeMethod(s,start,count,after);
			}
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(annot.state() == TextChangeState.Changed){
				invokeMethod(s,start,before,count);
			}
		}

		private void invokeMethod(CharSequence s, int start, int before, int count) {
				try {
					method.invoke(obj,Utils.args(methodParams,s,start,before,count));
//					if (this.methodParams == null || methodParams.length == 0) {
//						method.invoke(obj);
//					} else {
//
//						List<Object> args = new ArrayList<Object>();
//						int pi = 0;
//						int[] pv = new int[]{start,before,count};
//						for(int n=0;n<methodParams.length;n++){
//							if (methodParams[n].isAssignableFrom(CharSequence.class)){
//								args.add(s);
//							}else{
//								args.add(pv[pi]);
//								pi += 1;
//							}
//						}
//						method.invoke(obj,args);
//					}
				}catch (Throwable e){}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (annot.state() == TextChangeState.After) {
				try {
					method.invoke(obj,Utils.args(methodParams,s));
//					if (this.methodParams == null || methodParams.length == 0) {
//						method.invoke(obj);
//					} else {
//						method.invoke(obj, s);
//					}
				}catch (Throwable e){}
			}
		}
		
	}
}

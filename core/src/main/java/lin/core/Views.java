package lin.core;

import java.lang.reflect.Method;

import android.content.Context;
import android.view.View;
import android.view.ViewParent;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 12:55:16 AM
 *
 */
public class Views {

	@SuppressWarnings("unchecked")
	public static <T extends View> T parentView(View view,Class<T> c){
		if(c == null){
			return null;
		}
		ViewParent parent = view.getParent();
		while(parent != null){
			if(parent.getClass() == c){
				return (T) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}
	
	public static void segue(View view,String action){
		if(view == null){
			return;
		}
		ViewParent parent = view.getParent();
		Method method = null;
		while(parent != null){
			try {
				method = parent.getClass().getDeclaredMethod("segue", String.class);
				if(method != null){
					Object result = method.invoke(parent, action);
					if(Boolean.TRUE.equals(result)){
						return;
					}
				}
			} catch (Throwable e) {
//				e.printStackTrace();
			}
			parent = parent.getParent();

		}
		Context contetn = view.getContext();
		try {
			method = contetn.getClass().getDeclaredMethod("segue", String.class);
			if(method != null){
				Object result = method.invoke(contetn, action);
				if(Boolean.TRUE.equals(result)){
					return;
				}
			}
		} catch (Throwable e) {
//			e.printStackTrace();
		}
	}
}

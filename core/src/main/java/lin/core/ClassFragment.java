package lin.core;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

/**
 * 
 * @author lin
 * @date May 7, 2015 12:55:33 PM
 *
 */
public class ClassFragment extends android.support.v4.app.Fragment {

	private Class<?> cls;

	public ClassFragment(){

	}

	public ClassFragment(Class<?> cls){
		this.cls = cls;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return Views.genView(this.getContext(),cls,container);
	}

//	private View genView(Class<?> cls, ViewGroup container){
//		try {
//			if (View.class.isAssignableFrom(cls)) {
//				Constructor<?> con = getConstructor(cls, Context.class);
//				if (con != null) {
//					return (View) con.newInstance(this.getContext());
//				}
//
//				con = getConstructor(cls, Context.class, AttributeSet.class);
//
//				if (con != null) {
//					return (View) con.newInstance(this.getContext(), null);
//				}
//				con = getConstructor(cls, Context.class, AttributeSet.class, int.class);
//				if (con != null) {
//					return (View) con.newInstance(this.getContext(), null, 0);
//				}
//			} else if (ViewHolder.class.isAssignableFrom(cls)) {
//				Constructor<?> con = getConstructor(cls, Context.class, AttributeSet.class);
//				ViewHolder viewHolder = null;
//				if (con != null) {
//					viewHolder = (ViewHolder) con.newInstance(this.getContext(), null);
//				}
//
//				con = getConstructor(cls, Context.class);
//
//				if (con != null) {
//					viewHolder = (ViewHolder) con.newInstance(this.getContext());
//				}
//
//				return viewHolder.getView(container);
//			}
//		}catch (Throwable e){}
//		return null;
//	}
//
//	private Constructor<?> getConstructor(Class<?> cls, Class<?> ... parameterTypes){
//		try {
//			return cls.getConstructor(parameterTypes);
//		} catch (NoSuchMethodException e) {
//		}
//		return null;
//	}
}

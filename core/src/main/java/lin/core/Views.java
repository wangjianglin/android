package lin.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.*;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import lin.core.annotation.ClassProcessor;
import lin.core.annotation.FieldProcessor;
import lin.core.annotation.MethodProcessor;
import lin.core.annotation.ProcessorClass;
import lin.core.annotation.ResCls;
import lin.core.annotation.ResId;

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

	public static View loadView(View view){
		return loadView(view,view.getContext(),null,0,false);
	}

	public static View loadView(View view,int resId){
		return loadView(view,view.getContext(),null,resId,false);
	}

	public static View loadView(Activity activity,int resId){
		return loadView(activity,activity.getApplicationContext(),null,resId,false);
	}
	public static View loadView(Activity activity){
		return loadView(activity,activity.getApplicationContext(),null,0,false);
	}

	public static View loadView(Context context, ViewGroup root,int resourceId, boolean attachToRoot){
		if(resourceId == 0){
			return null;
		}
		return loadView(null,context,root,resourceId,attachToRoot);
	}

	private static Class<?> getResCls(Object holder,Context context){
		ResCls rc = holder.getClass().getAnnotation(ResCls.class);
		Class<?> resCls = null;

		if(rc != null){
			resCls = rc.value();
		}else if(context != null){
			try {
				resCls = Class.forName(context.getPackageName() + ".R");
			}catch (Throwable e){}
		}
		return resCls;
	}
	public static View loadView(Object holder, Context context, ViewGroup root, int resId, boolean attachToRoot){

		Class<?> resCls = getResCls(holder,context);
		if(resId == 0 && holder != null){

			ResId ri = holder.getClass().getAnnotation(ResId.class);
			if(ri != null){
				if(ri.value() != 0){
					resId = ri.value();
				}else if(!"".equals(ri.id())){
					try{
						Class<?> layoutClass = Class.forName(resCls.getName() + "$layout");
						Field f = layoutClass.getDeclaredField(ri.id());
						resId = f.getInt(null);
					}catch(Exception e){}
				}
			}
		}
		if(resId == 0) {
			return null;
		}
//			return DataBindingUtil.inflate(LayoutInflater.from(context),resourceId,null,false);
		if(root == null && holder instanceof ViewGroup){
			root = (ViewGroup) holder;
		}
		View view = LayoutInflaterFactory.from(context).inflate(resId, root, attachToRoot);
		processAnnotation(view,holder,resCls);
		return view;
	}

	public static int layoutId(Object holder){
		return layoutId(holder,null);
	}

	public static int layoutId(Activity holder){
		return layoutId(holder,holder.getApplicationContext());
	}

	public static int layoutId(View holder){
		return layoutId(holder,holder.getContext());
	}

	public static int layoutId(android.support.v4.app.Fragment holder){
		return layoutId(holder,holder.getContext());
	}

	public static int layoutId(Object holder,Context context){
		int resId = 0;
		ResId ri = holder.getClass().getAnnotation(ResId.class);
		if(ri != null){
			if(ri.value() != 0){
				resId = ri.value();
			}else if(!"".equals(ri.id())){
				try{
					Class<?> resCls = getResCls(holder,context);
					Class<?> layoutClass = Class.forName(resCls.getName() + "$layout");
					Field f = layoutClass.getDeclaredField(ri.id());
					resId = f.getInt(null);
				}catch(Exception e){}
			}
		}
		return resId;
	}
	public static android.support.v4.app.Fragment genFragment(Context context, String clsName, ViewGroup container) {
		try {
			return genFragment(context,Class.forName(clsName),container);
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	public static android.support.v4.app.Fragment genFragment(Context context, Class<?> cls, ViewGroup container){
		try {
			if(android.support.v4.app.Fragment.class.isAssignableFrom(cls)){
				return (android.support.v4.app.Fragment) cls.newInstance();
			}else{
				return new ViewFragment(genView(context,cls,container));
			}

		} catch (Throwable e) {
		}
		return null;
	}
	public static View genView(Context context, Class<?> cls, ViewGroup container){
		try {
			if (View.class.isAssignableFrom(cls)) {
				Constructor<?> con = getConstructor(cls, Context.class);
				if (con != null) {
					return (View) con.newInstance(context);
				}

				con = getConstructor(cls, Context.class, AttributeSet.class);

				if (con != null) {
					return (View) con.newInstance(context, null);
				}
				con = getConstructor(cls, Context.class, AttributeSet.class, int.class);
				if (con != null) {
					return (View) con.newInstance(context, null, 0);
				}
			} else if (ViewHolder.class.isAssignableFrom(cls)) {
				Constructor<?> con = getConstructor(cls, Context.class, AttributeSet.class);
				ViewHolder viewHolder = null;
				if (con != null) {
					viewHolder = (ViewHolder) con.newInstance(context, null);
				}

				con = getConstructor(cls, Context.class);

				if (con != null) {
					viewHolder = (ViewHolder) con.newInstance(context);
				}

				return viewHolder.getView(container);
			}
		}catch (Throwable e){}
		return null;
	}

	public static void process(View view){
		process(view,view);
	}
	public static void process(android.support.v4.app.Fragment fragment){
		process(fragment,fragment.getView());
	}
	public static void process(Activity activity){
		process(activity,activity.getWindow().getDecorView());
	}
	public static void process(Object holder,View view){
		Class<?> resCls = getResCls(holder,view.getContext());
		processAnnotation(view,holder,resCls);
	}
	private static void processAnnotation(final View view,final Object target,Class<?> resCls) {

		if(target == null){
			return;
		}
		Class<?> cls = target.getClass();
		while (cls != null && cls != Object.class){
			initClassAnnotationWithCls(cls,target,view,resCls.getPackage());
			initFieldAnnotationWithCls(cls,target,view,resCls.getPackage());
			initMethodAnnotationWithCls(cls,target,view,resCls.getPackage());
			cls = cls.getSuperclass();
		}

	}

	private static Object getProcessor(Annotation annon){
		ProcessorClass processorClass = annon.annotationType().getAnnotation(ProcessorClass.class);
		Object processorObj = null;
		if(processorClass != null) {
			try {
				processorObj = processors.get(processorClass.value());
				if (processorObj == null) {
					processorObj = processorClass.value().newInstance();
					processors.put(processorClass.value(), processorObj);
				}
			}catch (Throwable e){}
		}
		return processorObj;
	}
	private static Map<Class<?>,Object> processors = new HashMap<Class<?>,Object>();

	private static void initClassAnnotationWithCls(Class<?> cls,Object target,View view,Package pack){

		Annotation[] items = cls.getAnnotations();

		if(items != null){
			ProcessorClass processorClass = null;
			Object processorObj = null;

			for(Annotation item : items){
				processorObj = getProcessor(item);
				if (processorObj instanceof ClassProcessor) {
					((ClassProcessor) processorObj).process(target,view, item, pack);
				}
			}

		}

	}

	private static void initFieldAnnotationWithCls(Class<?> cls,Object target,View view,Package pack){


		Field[] fs = cls.getDeclaredFields();
		Annotation[] items = null;
		ProcessorClass processorClass = null;
		Object processorObj = null;
		for(Field f : fs){
			items = f.getAnnotations();
			if(items == null){
				continue;
			}
			for(Annotation item : items){

				processorObj = getProcessor(item);
				if(processorObj instanceof FieldProcessor){
					((FieldProcessor)processorObj).process(target,view,f, item, pack);
				}

			}

		}

	}

//	private static void initMethodAnnotation(Object target,View view,Package pack) {
//
//		if(target == null){
//			return;
//		}
//		Class<?> cls = target.getClass();
//		while (cls != null && cls != Object.class){
//			initMethodAnnotationWithCls(cls,target,view,pack);
//			cls = cls.getSuperclass();
//		}
//	}

	private static void initMethodAnnotationWithCls(Class<?> cls,Object target,View view,Package pack){
		Method[] ms = cls.getDeclaredMethods();
		Annotation[] items = null;
		ProcessorClass processorClass = null;
		Object processorObj = null;
		for(Method m : ms){
			items = m.getAnnotations();
			if(items == null){
				continue;
			}
			for(Annotation item : items){

				processorObj = getProcessor(item);
				if(processorObj instanceof MethodProcessor){
					((MethodProcessor)processorObj).process(target,view,m, item, pack);
				}

			}

		}

	}

	private static Constructor<?> getConstructor(Class<?> cls, Class<?> ... parameterTypes){
		try {
			return cls.getConstructor(parameterTypes);
		} catch (NoSuchMethodException e) {
		}
		return null;
	}
}

package lin.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import lin.core.annotation.ClassProcessor;
import lin.core.annotation.FieldProcessor;
import lin.core.annotation.MethodProcessor;
import lin.core.annotation.Opportunity;
import lin.core.annotation.OpportunityAnnotation;
import lin.core.annotation.ProcessorClass;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;

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


	public static View loadViewByAnnot(Object obj){
		if(obj instanceof View) {
			return getViewByAnnotation((View)obj, 0, ((View)obj).getContext());
		}else if(obj instanceof Activity){
			return getViewByAnnotation((Activity)obj, 0, ((Activity)obj).getApplicationContext());
		}else{
			return getViewByAnnotation((Activity)obj, 0, null);
		}
	}
	public static View loadView(Context context,int resourceId){
		if(resourceId == 0){
			return null;
		}
		return getViewByAnnotation(null,resourceId,context);
	}

	private static View getViewByAnnotation(Object view,int resourceId,Context context){

		if(resourceId == 0 && view != null){
			ResourceClass rc = view.getClass().getAnnotation(ResourceClass.class);
			Class<?> layoutClass = null;

			if(rc != null){
				try{
					Class<?> rClass = rc.value();
					layoutClass = Class.forName(rClass.getName()+"$layout");
				}catch(Throwable e){
				}
			}else if(context != null){
				try {
					Class<?> rClass = Class.forName(context.getPackageName() + "R");
					layoutClass = Class.forName(context.getPackageName() + "R$layout");
				}catch (Throwable e){}
			}

			ResourceId ri = view.getClass().getAnnotation(ResourceId.class);
			if(ri != null){
				if(ri.value() != 0){
					resourceId = ri.value();
				}else if(!"".equals(ri.id())){
					try{
						Field f = layoutClass.getDeclaredField(ri.id());
						resourceId = f.getInt(null);
					}catch(Exception e){}
				}
			}
		}
		if(resourceId != 0){
//			return DataBindingUtil.inflate(LayoutInflater.from(context),resourceId,null,false);
			return LayoutInflater.from(context).inflate(resourceId, null, false);
		}
		return null;
	}

	public static void processAnnotation(View view,Object target){
//		processAnnotationImpl(new CanFindViewByIdIdViewByView(view,target),view.getContext(), Opportunity.OnCreate);
		processAnnotationImpl(target,view, Opportunity.OnCreate);
	}
	public static void processAnnotation(View view){
//		processAnnotationImpl(new CanFindViewByIdIdViewByView(view,view),view.getContext(), Opportunity.OnCreate);
		processAnnotationImpl(view,view, Opportunity.OnCreate);
	}

	public static void processAnnotation(View view,Object target,Opportunity opportunity){
//		processAnnotationImpl(new CanFindViewByIdIdViewByView(view,target),view.getContext(), opportunity);
		processAnnotationImpl(target,view, opportunity);
	}
	public static void processAnnotation(View view,Opportunity opportunity){
//		processAnnotationImpl(new CanFindViewByIdIdViewByView(view,view),view.getContext(), opportunity);
		processAnnotationImpl(view,view, opportunity);
	}


//	public static void processAnnotation(Activity activity){
//		processAnnotationImpl(new CanFindViewByIdIdViewByActivity(activity,activity),activity.getApplicationContext(), Opportunity.OnCreate);
//		processAnnotationImpl(new CanFindViewByIdIdViewByActivity(activity,activity),activity.getApplicationContext(), Opportunity.OnAttached);
//	}

	private static void processAnnotationImpl(Object target,View view,Opportunity opportunity){

		ResourceClass rc = target.getClass().getAnnotation(ResourceClass.class);
		Class<?> rCls = null;
		if(rc != null){
			rCls = rc.value();
		}else {
			try{
				rCls = Class.forName(view.getContext().getPackageName()+".R");
			}catch (Throwable e){}
		}
		if(rCls == null){
			return;
		}
//		view.bind();
//		DataBindingUtil.findBinding()

		try {
			View v = view;
			if(v == target && v instanceof ResourceView){
				v = ((ResourceView) v).mRootView;
			}
			DataBindingUtil.bind(v);
		}catch (Throwable e){
//			e.printStackTrace();
		}

		initClassAnnotation(target,view,rCls.getPackage(), opportunity);
		initFieldAnnotation(target,view,rCls.getPackage(), opportunity);
		initMethodAnnotation(target,view,rCls.getPackage(), opportunity);
	}

	private static Object getProcessor(Annotation annot,Opportunity opportunity){
		ProcessorClass processorClass = annot.annotationType().getAnnotation(ProcessorClass.class);
		Object processorObj = null;
		if(processorClass != null) {
			try {
				OpportunityAnnotation oa = annot.annotationType().getAnnotation(OpportunityAnnotation.class);
				if((oa == null && opportunity == Opportunity.OnCreate) || oa.value() == opportunity) {
					processorObj = processors.get(processorClass.value());
					if (processorObj == null) {
						processorObj = processorClass.value().newInstance();
						processors.put(processorClass.value(), processorObj);
					}
				}
			}catch (Throwable e){}
		}
		return processorObj;
	}
	private static Map<Class<?>,Object> processors = new HashMap<Class<?>,Object>();

	private static void initClassAnnotation(Object target,View view,Package pack,Opportunity opportunity){

		Annotation[] items = target.getClass().getAnnotations();

		if(items != null){
			ProcessorClass processorClass = null;
			Object processorObj = null;

			for(Annotation item : items){
//				try{
//					processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
//					if(processorClass != null) {
//						processorObj = processors.get(processorClass.value());
//						if (processorObj == null) {
//							processorObj = processorClass.value().newInstance();
//							processors.put(processorClass.value(), processorObj);
//						}
				processorObj = getProcessor(item,opportunity);
				if (processorObj instanceof ClassProcessor) {
					((ClassProcessor) processorObj).process(target,view, item, pack);
				}
//					}
//				}catch(Throwable e){
//					e.printStackTrace();
//				}
			}

		}

	}

	private static void initFieldAnnotation(Object target,View view,Package pack,Opportunity opportunity){


		Field[] fs = target.getClass().getDeclaredFields();
		Annotation[] items = null;
		ProcessorClass processorClass = null;
		Object processorObj = null;
		for(Field f : fs){
			items = f.getAnnotations();
			if(items == null){
				continue;
			}
			for(Annotation item : items){
//				try{
//					processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
////					processorObj = processors.get(processorClass);
////					processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
//					processorObj = processors.get(processorClass.value());
//					if(processorObj == null){
//						processorObj = processorClass.value().newInstance();
//						processors.put(processorClass.value(), processorObj);
//					}
				processorObj = getProcessor(item,opportunity);
					if(processorObj instanceof FieldProcessor){
						((FieldProcessor)processorObj).process(target,view,f, item, pack);
					}
//				}catch(Throwable e){
//					e.printStackTrace();
//				}
			}

		}

	}

	private static void initMethodAnnotation(Object target,View view,Package pack,Opportunity opportunity){
//		Click item = null;
//		View itemView = null;
//		Class<?>[] clcikMethodParams = null;
		Method[] ms = target.getClass().getDeclaredMethods();
		Annotation[] items = null;
		ProcessorClass processorClass = null;
		Object processorObj = null;
		for(Method m : ms){
			items = m.getAnnotations();
			if(items == null){
				continue;
			}
			for(Annotation item : items){
//				try{
//					processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
//					if(processorClass == null){
//						continue;
//					}
//					processorObj = processors.get(processorClass.value());
//					if(processorObj == null){
//						processorObj = processorClass.value().newInstance();
//						processors.put(processorClass.value(), processorObj);
//					}
					processorObj = getProcessor(item,opportunity);
					if(processorObj instanceof MethodProcessor){
						((MethodProcessor)processorObj).process(target,view,m, item, pack);
					}
//				}catch(Throwable e){
//					e.printStackTrace();
//				}
			}

		}

//		for(Method m : ms){
//			item = m.getAnnotation(Click.class);
//			if(item == null){
//				continue;
//			}
//			clcikMethodParams = m.getParameterTypes();
//			if(!(clcikMethodParams == null || clcikMethodParams.length == 0 ||
//				clcikMethodParams.length == 1)){
//				continue;
//			}
//			if(item.value() != 0){
//				itemView = this.findViewById(item.value());
//				if(itemView == null){
//					continue;
//				}
//				if(!(clcikMethodParams.length == 0 || (clcikMethodParams.length == 1 && clcikMethodParams[0].isAssignableFrom(itemView.getClass())))){
//					continue;
//				}
//				itemView.setOnClickListener(new ViewOnClickListener(itemView,m));
//			}
//		}
	}


//	static class CanFindViewByIdIdViewByView implements CanFindViewByIdIdView2 {
//
//		private View view;
//		private Object target;
//		CanFindViewByIdIdViewByView(View view,Object target){
//			this.view = view;
//			this.target = target;
//		}
//		@Override
//		public View findViewById(int id) {
//			return view.findViewById(id);
//		}
//
//		@Override
//		public ViewDataBinding getBinding() {
//			if(target == view && this.view instanceof ResourceView){
//				return DataBindingUtil.getBinding(((ResourceView) this.view).mRootView);
//			}
//			return DataBindingUtil.getBinding(this.view);
//		}
//
//		@Override
//		public Resources getResources() {
//			return view.getResources();
//		}
//
//		@Override
//		public Object getTarget() {
//			return this.target;
//		}
//
//		@Override
//		public View getView() {
//			return view;
//		}
//	}
//
//	static interface CanFindViewByIdIdView2 extends AnnotRes {
//		View getView();
//	}
//	static class CanFindViewByIdIdViewByActivity implements CanFindViewByIdIdView2 {
//
//		private Activity activity;
//		private Object target;
//		CanFindViewByIdIdViewByActivity(Activity activity,Object target){
//			this.activity = activity;
//			this.target = target;
//		}
//		@Override
//		public View findViewById(int id) {
//			return activity.findViewById(id);
//		}
//
//		@Override
//		public ViewDataBinding getBinding() {
//			return DataBindingUtil.getBinding(this.activity.getWindow().getDecorView());
//		}
//
//		@Override
//		public Resources getResources() {
//			return activity.getResources();
//		}
//
//		@Override
//		public Object getTarget() {
//			return target;
//		}
//
//		@Override
//		public View getView() {
//			return this.activity.getWindow().getDecorView();
//		}
//	}

}

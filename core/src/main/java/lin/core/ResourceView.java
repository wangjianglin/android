package lin.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import lin.core.annotation.FieldProcessor;
import lin.core.annotation.MethodProcessor;
import lin.core.annotation.ProcessorClass;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.log.Log;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 12:19:35 AM
 *
 */
public class ResourceView extends ContentView implements ActivieyLifeCycle{


	public ResourceView(int resourceId,Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.resourceId = resourceId;
		this.initWithAnnotation();
	}

	public ResourceView(int resourceId,Context context, AttributeSet attrs) {
		super(context, attrs);
		this.resourceId = resourceId;
		this.initWithAnnotation();
	}

	public ResourceView(int resourceId,Context context) {
		super(context);
		this.resourceId = resourceId;
		this.initWithAnnotation();
	}
	public ResourceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initWithAnnotation();
	}

	public ResourceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initWithAnnotation();
	}

	public ResourceView(Context context) {
		super(context);
		this.initWithAnnotation();
	}
	
	private Class<?> rClass = null;
	private Class<?> layoutClass = null;
	private Class<?> idClass = null;

	private Log log;
	
	protected void init(int resourceId){
		this.resourceId = resourceId;
		this.init();
	}

	private void initWithAnnotation(){
		log = new Log("view "+this.getClass().getName());
		log.info("create");
		ResourceClass rc = this.getClass().getAnnotation(ResourceClass.class);
		if(rc != null){
			try{
				rClass = rc.value();
				layoutClass = Class.forName(rClass.getName()+"$layout");
				idClass = Class.forName(rClass.getName()+"$id");
			}catch(Throwable e){
//				e.printStackTrace();
			}
		}
		
		if(this.resourceId == 0){
			ResourceId ri = this.getClass().getAnnotation(ResourceId.class);
			if(ri != null){
				if(ri.value() != 0){
					this.resourceId = ri.value();
				}else if(!"".equals(ri.id())){
					try{
						Field f = layoutClass.getDeclaredField(ri.id());
						this.resourceId = f.getInt(null);
					}catch(Exception e){}
				}
			}
		}
		this.init();
		//this.onInited();
	}
	
	protected View rootView;
	private int resourceId;


	private void init(){
		// 导入布局
		if(resourceId == 0){
			return;
		}
		rootView = LayoutInflater.from(this.getContext()).inflate(this.resourceId, this, false);
		this.addView(rootView);
		
//		Class<?> cls = this.getClass();
		
//		ResourceClass rc = cls.getAnnotation(ResourceClass.class);
//		Class<?> rCls = null;
//		if(rc != null){
//			rCls = rc.value();
//		}
		this.initAnnotation();
	}
//	private boolean isInitAnnotation;
//	private boolean isInited;
	private Handler mHandler = new Handler();
	private void initAnnotation(){
//		if(isInitAnnotation){
//			return;
//		}
//		isInitAnnotation = true;
		initFieldAnnotation();
		initMethodAnnotation();
//		this.onViewCreate();
//		mHandler.post(new Runnable(){
//
//			@Override
//			public void run() {
//				if(isAttached){
//					return;
//				}
//				isInited = true;
//				onInited();
//			}});
	}
	
	private static Map<Class<?>,Object> processors = new HashMap<Class<?>,Object>();
	private void initFieldAnnotation(){
		
		
		
		
		Field[] fs = this.getClass().getDeclaredFields();
		Annotation[] items = null;
		ProcessorClass processorClass = null;
		Object processorObj = null;
		for(Field f : fs){
			items = f.getAnnotations();
			 if(items == null){
				 continue;
			 }
			 for(Annotation item : items){
				 try{
					 processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
					 processorObj = processors.get(processorClass);
					 processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
					 processorObj = processors.get(processorClass.value());
					 if(processorObj == null){
						 processorObj = processorClass.value().newInstance();
						 processors.put(processorClass.value(), processorObj);
					 }
					 if(processorObj instanceof FieldProcessor){
						 ((FieldProcessor)processorObj).process(this,f, item, idClass);
					 }
				 }catch(Throwable e){
					 e.printStackTrace();
				 }
			 }

		}
		
	}
	
	private void initMethodAnnotation(){
//		Click item = null;
//		View itemView = null;
//		Class<?>[] clcikMethodParams = null;
		Method[] ms = this.getClass().getDeclaredMethods();
		Annotation[] items = null;
		ProcessorClass processorClass = null;
		Object processorObj = null;
		for(Method m : ms){
			items = m.getAnnotations();
			 if(items == null){
				 continue;
			 }
			 for(Annotation item : items){
				 try{
					 processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
					 if(processorClass == null){
						 continue;
					 }
					 processorObj = processors.get(processorClass.value());
					 if(processorObj == null){
						 processorObj = processorClass.value().newInstance();
						 processors.put(processorClass.value(), processorObj);
					 }
					 if(processorObj instanceof MethodProcessor){
						 ((MethodProcessor)processorObj).process(this,m, item, idClass);
					 }
				 }catch(Throwable e){
					 e.printStackTrace();
				 }
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
	

	protected void onInited(){}
//	protected void onViewCreate(){}
	
	@Override
	public void addView(View child) {
		if(rootView != null && child != rootView){
			addViewItem(child);
			return;
		}
		super.addView(child);
	}
	
	@Override
	public void addView(View child, int index) {
		if(rootView != null && child != rootView){
			addViewItem(child);
			return;
		}
		super.addView(child, index);
	}
	
	@Override
	public void addView(View child, int index,
			LayoutParams params) {
		if(rootView != null && child != rootView){
			addViewItem(child);
			return;
		}
		super.addView(child, index, params);
	}
	
	@Override
	public void addView(View child, int width, int height) {
		if(rootView != null && child != rootView){
			addViewItem(child);
			return;
		}
		super.addView(child, width, height);
	}
	
	@Override
	public void addView(View child, LayoutParams params) {
		if(rootView != null && child != rootView){
			addViewItem(child);
			return;
		}
		super.addView(child, params);
	}
	
	protected void addViewItem(View item){
	
	}
	
	private boolean isAttached = false;
	@Override
	protected void onAttachedToWindow() {
		log.info("attached window");
		//if(isInitAnnotation && isInited == false){
			//isInited = true;
		if(!isAttached){
//			this.init();
			this.onInited();
		}
		
		super.onAttachedToWindow();
		
		
		if(!isAttached){
			isAttached = true;
			this.initAnnotation();
			this.onFirstAttachedToWindow();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		log.info("detached window");
	}

	@Deprecated
	protected void onFirstAttachedToWindow(){}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onResume() {
		
	}

	@Override
	public void onPause() {
		
	}

	@Override
	public void onStop() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		
	}



	//
//
//	public static <T extends ResourceView> T newInstanceFromLayout(Class<T> cls){
//		return null;
//	}
}

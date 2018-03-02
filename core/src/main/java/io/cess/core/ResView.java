package io.cess.core;

//import io.cess.core.log.Log;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 12:19:35 AM
 *
 */
public abstract class ResView extends ContentView{


	protected ResView(int resId, Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.resId = resId;
		this.initWithAnnotation();
		this.onCreate();
	}

	protected ResView(int resId, Context context, AttributeSet attrs) {
		super(context, attrs);
		this.resId = resId;
		this.initWithAnnotation();
		this.onCreate();
	}

	protected ResView(int resId, Context context) {
		super(context);
		this.resId = resId;
		this.initWithAnnotation();
		this.onCreate();
	}
	public ResView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initWithAnnotation();
		this.onCreate();
	}

	public ResView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initWithAnnotation();
		this.onCreate();
	}

	public ResView(Context context) {
		super(context);
		this.initWithAnnotation();
		this.onCreate();
	}

	protected void init(int resId){
		this.resId = resId;
		View view = Views.loadView(this,this.resId);
		if(view != null){
			this.addViewPrivate(view);
		}
	}

	private void initWithAnnotation(){

		View view = Views.loadView(this,this.resId);
		if(view != null){
			this.addViewPrivate(view);
		}
	}
	
	private View mRootView;
	private int resId;

	public View getNodeView(){
		return mRootView;
	}


//	private Handler mHandler = new Handler();


	protected void onCreate(){};

	protected void onInited(){}
	
	@Override
	public void addView(View child, int index,
						ViewGroup.LayoutParams params) {
		this.addViewItemPrivate(child,index,params);
	}

	private void addViewPrivate(View child){
		ViewGroup.LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = generateDefaultLayoutParams();
		}
		this.addViewItemPrivate(child,-1,params);
	}

	private void addViewItemPrivate(View child,int index,ViewGroup.LayoutParams params){
		if(mRootView != null && child != mRootView){
			addViewItem(child,index,params);
			return;
		}
		mRootView = child;
		super.addView(child, index, params);
	}
	

	
	protected void addViewItem(View item, int index,ViewGroup.LayoutParams params){
	
	}
	
	private boolean isAttached = false;
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		if(!isAttached){
			isAttached = true;
			this.onInited();
		}
	}

}






//public class ResView extends ContentView{
//
//
//	public ResView(int resourceId, Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		this.resourceId = resourceId;
//		this.initWithAnnotation();
//	}
//
//	public ResView(int resourceId, Context context, AttributeSet attrs) {
//		super(context, attrs);
//		this.resourceId = resourceId;
//		this.initWithAnnotation();
//	}
//
//	public ResView(int resourceId, Context context) {
//		super(context);
//		this.resourceId = resourceId;
//		this.initWithAnnotation();
//	}
//	public ResView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		this.initWithAnnotation();
//	}
//
//	public ResView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		this.initWithAnnotation();
//	}
//
//	public ResView(Context context) {
//		super(context);
//		this.initWithAnnotation();
//	}
//
//	private Class<?> rClass = null;
//	private Class<?> layoutClass = null;
//	private Class<?> idClass = null;
//	private Package aPackage;
//
////	private Log log;
//
//	protected void init(int resourceId){
//		this.resourceId = resourceId;
//		View view = Views.loadView(this.getContext(),this.resourceId);
//		if(view != null){
//			this.addView(view);
//		}
//	}
//
//	private void initWithAnnotation(){
//
//		View view = null;
//		if(resourceId != 0){
//			view = Views.loadView(this.getContext(),this.resourceId);
//		}else {
//			view = Views.loadViewByAnnot(this);
//		}
//		if(view != null){
//			this.addView(view);
//		}
//	}
//
//	protected View mRootView;
//	private int resourceId;
//
//	public View getNodeView(){
//		return mRootView;
//	}
//
//	//	private void init(){
////		// 导入布局
//////		if(resourceId == 0){
//////			return;
//////		}
//////		mRootView = LayoutInflater.from(this.getContext()).inflate(this.resourceId, this, false);
//////		this.addView(mRootView);
////
//////		Class<?> cls = this.getClass();
////
//////		ResCls rc = cls.getAnnotation(ResCls.class);
//////		Class<?> rCls = null;
//////		if(rc != null){
//////			rCls = rc.value();
//////		}
//////		this.initAnnotation();
////	}
////	private boolean isInitAnnotation;
////	private boolean isInited;
//	private Handler mHandler = new Handler();
////	private void initAnnotation(){
////
////		DataBindingUtil.bind(this);
////
////		initFieldAnnotation();
////		initMethodAnnotation();
////
////	}
//
////	private static Map<Class<?>,Object> processors = new HashMap<Class<?>,Object>();
////	private void initFieldAnnotation(){
////
////
////
////
////		Field[] fs = this.getClass().getDeclaredFields();
////		Annotation[] items = null;
////		ProcessorClass processorClass = null;
////		Object processorObj = null;
////		for(Field f : fs){
////			items = f.getAnnotations();
////			 if(items == null){
////				 continue;
////			 }
////			 for(Annotation item : items){
////				 try{
////					 processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
////					 processorObj = processors.get(processorClass);
////					 processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
////					 processorObj = processors.get(processorClass.value());
////					 if(processorObj == null){
////						 processorObj = processorClass.value().newInstance();
////						 processors.put(processorClass.value(), processorObj);
////					 }
////					 if(processorObj instanceof FieldProcessor){
////						 ((FieldProcessor)processorObj).process(this,f, item, aPackage);
////					 }
////				 }catch(Throwable e){
////					 e.printStackTrace();
////				 }
////			 }
////
////		}
////
////	}
////
////	private void initMethodAnnotation(){
//////		Click item = null;
//////		View itemView = null;
//////		Class<?>[] clcikMethodParams = null;
////		Method[] ms = this.getClass().getDeclaredMethods();
////		Annotation[] items = null;
////		ProcessorClass processorClass = null;
////		Object processorObj = null;
////		for(Method m : ms){
////			items = m.getAnnotations();
////			 if(items == null){
////				 continue;
////			 }
////			 for(Annotation item : items){
////				 try{
////					 processorClass = item.annotationType().getAnnotation(ProcessorClass.class);
////					 if(processorClass == null){
////						 continue;
////					 }
////					 processorObj = processors.get(processorClass.value());
////					 if(processorObj == null){
////						 processorObj = processorClass.value().newInstance();
////						 processors.put(processorClass.value(), processorObj);
////					 }
////					 if(processorObj instanceof MethodProcessor){
////						 ((MethodProcessor)processorObj).process(this,m, item, aPackage);
////					 }
////				 }catch(Throwable e){
////					 e.printStackTrace();
////				 }
////			 }
////
////		}
////
//////		for(Method m : ms){
//////			item = m.getAnnotation(Click.class);
//////			if(item == null){
//////				continue;
//////			}
//////			clcikMethodParams = m.getParameterTypes();
//////			if(!(clcikMethodParams == null || clcikMethodParams.length == 0 ||
//////				clcikMethodParams.length == 1)){
//////				continue;
//////			}
//////			if(item.value() != 0){
//////				itemView = this.findViewById(item.value());
//////				if(itemView == null){
//////					continue;
//////				}
//////				if(!(clcikMethodParams.length == 0 || (clcikMethodParams.length == 1 && clcikMethodParams[0].isAssignableFrom(itemView.getClass())))){
//////					continue;
//////				}
//////				itemView.setOnClickListener(new ViewOnClickListener(itemView,m));
//////			}
//////		}
////	}
////
//
//	protected void onInited(){}
////	protected void onViewCreate(){}
//
//	@Override
////	public void addView(View child) {
////		if(mRootView != null && child != mRootView){
////			addViewItem(child);
////			return;
////		}
////		mRootView = child;
////		super.addView(child);
////		Views.processAnnotation(this);
////	}
//
////	@Override
////	public void addView(View child, int index) {
////		if(mRootView != null && child != mRootView){
////			addViewItem(child);
////			return;
////		}
////		mRootView = child;
////		super.addView(child, index);
////		Views.processAnnotation(this);
////	}
//
////	@Override
//	public void addView(View child, int index,
//						LayoutParams params) {
//		if(mRootView != null && child != mRootView){
//			addViewItem(child);
//			return;
//		}
//		mRootView = child;
//		super.addView(child, index, params);
//		Views.processAnnotation(this);
//	}
//
////	@Override
////	public void addView(View child, int width, int height) {
////		if(mRootView != null && child != mRootView){
////			addViewItem(child);
////			return;
////		}
////		mRootView = child;
////		super.addView(child, width, height);
////		Views.processAnnotation(this);
////	}
//
////	@Override
////	public void addView(View child, LayoutParams params) {
////		if(mRootView != null && child != mRootView){
////			addViewItem(child);
////			return;
////		}
////		mRootView = child;
////		super.addView(child, params);
////		Views.processAnnotation(this);
////	}
//
//	protected void addViewItem(View item){
//
//	}
//
//	private boolean isAttached = false;
//	@Override
//	protected void onAttachedToWindow() {
////		log.info("attached window");
//		//if(isInitAnnotation && isInited == false){
//		//isInited = true;
////		if(!isAttached){
//////			this.init();
//////			this.onInited();
////		}
//
//		super.onAttachedToWindow();
//
//
//		if(!isAttached){
//			isAttached = true;
////			this.initAnnotation();
//			this.onInited();
//			Views.processAnnotation(this, Opportunity.OnAttached);
////			this.onFirstAttachedToWindow();
//		}
//	}
//
//	@Override
//	protected void onDetachedFromWindow() {
//		super.onDetachedFromWindow();
////		log.info("detached window");
//	}
//
////	@Deprecated
////	protected void onFirstAttachedToWindow(){}
//
//
//
//	@Override
//	public void onConfigurationChanged(Configuration newConfig){}
//
//
//
//
//
//	//
////
////	public static <T extends ResourceView> T newInstanceFromLayout(Class<T> cls){
////		return null;
////	}
//}

package io.cess.core;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;

import io.cess.core.annotation.NavTag;
import io.cess.core.annotation.NavTitle;
import io.cess.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 1:24:56 AM
 *
 */
public class NavActivity extends ViewActivity {

	private Nav nav;
	public Nav getNav(){
		if(nav == null){
			throw new RuntimeException("nav is null in activity");
		}
		return nav;
	}

	@ViewById(id="io_cess_core_nav_toolbar")
	private Toolbar toolbar;

	@ViewById(id="io_cess_core_nav_content_frame")
	private FrameLayout frameLayout;

	@ViewById(id="io_cess_core_nav_view")
	private ViewGroup parentView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		this.supportRequestWindowFeature(Window.FEATURE_SUPPORT_ACTION_BAR);

		Intent intent = this.getIntent();
		String argsId = intent.getAction();
		String className = intent.getStringExtra("cls");
		int layoutId = intent.getIntExtra("layout_id",0);

		nav = Nav.getNav(argsId);
		if(nav == null) {
			this.finish();
			return;
		}
		nav.activity = this;

		ActionBar actionBar = this.getSupportActionBar();
//		if(actionBar == null) {
//			this.setContentViewById(R.layout.io_cess_core_nav_main);
//			this.setSupportActionBar(toolbar);
//			actionBar = this.getSupportActionBar();
//		}else{
//			this.setContentViewById(R.layout.io_cess_core_nav_main_without_toolbar);
//		}
//
//
//
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		Object view = null;
		if(className != null && !"".equals(className)) {

			android.support.v4.app.Fragment fragment = Views.genFragment(this,className,this.parentView);

			setInfoWithView(fragment,fragment.getView());
			view = fragment;
		}else{
			view = genView(layoutId);
			setInfoWithView(view,(View) view);
		}

//		addNavView(view);
		setObjectContent(view);

		if(nav != null) {
			nav.init();
		}
	}

	private void setInfoWithView(Object navObj,View view){

		if(nav == null){
			return;
		}
		if(view != null) {
			if (view.getLayoutParams() instanceof ContentView.LayoutParams) {
				ContentView.LayoutParams lp = (ContentView.LayoutParams) view.getLayoutParams();
				if (nav.getTitle() == null) {
					nav.setTitle(lp.getAttrs().getString(R.styleable.nav, R.styleable.nav_nav_title, nav.getTitle()+""));
				}
				if (nav.getTag() == null) {
					nav.setTag(lp.getAttrs().getString(R.styleable.nav, R.styleable.nav_nav_tag, nav.getTag()+""));
				}
			}
		}

		if(navObj != null){
			if(nav.getTitle() == null) {
				NavTitle navTitle = navObj.getClass().getAnnotation(NavTitle.class);
				if (navTitle != null) {
					nav.setTitle(navTitle.value());
				}
			}
			if(nav.getTag() == null) {
				NavTag navTag = navObj.getClass().getAnnotation(NavTag.class);
				if (navTag != null) {
					nav.setTag(navTag.value());
				}
			}
		}
	}

	private void addNavView(Object item){
		if(item == null){
			return;
		}

		FragmentManager fragmentManager = this.getSupportFragmentManager();

		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(item instanceof View) {
//			transaction.add()
			transaction.add(R.id.io_cess_core_nav_content_frame, new ViewFragment((View) item));
		}else{
			transaction.add(R.id.io_cess_core_nav_content_frame, (android.support.v4.app.Fragment) item);
		}
		transaction.commit();

	}
	private Object genView(int layoutId){
		return LayoutInflater.from(this).inflate(layoutId,parentView,false);
	}
	private Object genView(String className){
		try {
			Class<?> cls = Class.forName(className);
			if(android.support.v4.app.Fragment.class.isAssignableFrom(cls)){
				return cls.newInstance();
			}else{
				return new ClassFragment(cls);
			}

		} catch (Throwable e) {
		}
		return null;
	}

//	@Override
//	public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//		super.onCreate(savedInstanceState, persistentState);
//		System.out.println("save instance state ............................");
//	}

	private boolean isBackKeyDown = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			isBackKeyDown = true;
			return true;
		}
		isBackKeyDown = false;
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && nav != null){
			if(isBackKeyDown) {
				nav.pop();
			}
			isBackKeyDown = false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onSupportNavigateUp() {
		if(nav != null) {
			nav.pop();
		}
		return true;
	}


	private Constructor<?> getConstructor(Class<?> cls,Class<?> ... parameterTypes){
		try {
			return cls.getConstructor(parameterTypes);
		} catch (NoSuchMethodException e) {
		}
		return null;
	}
}

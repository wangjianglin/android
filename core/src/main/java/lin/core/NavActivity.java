package lin.core;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import lin.core.annotation.NavTag;
import lin.core.annotation.NavTitle;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 1:24:56 AM
 *
 */
public class NavActivity extends ViewActivity {

	private Nav nav;
	public Nav getNav(){
		return nav;
	}

	@ViewById(id="lin_core_nav_toolbar")
	private Toolbar toolbar;

	@ViewById(id="lin_core_nav_content_frame")
	private FrameLayout frameLayout;

	@ViewById(id="lin_core_nav_view")
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
		nav.activity = this;

		ActionBar actionBar = this.getSupportActionBar();
		if(actionBar == null) {
			this.setContentViewById(R.layout.lin_core_nav_main);
			this.setSupportActionBar(toolbar);
			actionBar = this.getSupportActionBar();
		}else{
			this.setContentViewById(R.layout.lin_core_nav_main_without_toolbar);
		}



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

		addNavView(view);

		nav.init();
	}

	private void setInfoWithView(Object navObj,View view){

		if(view != null) {
			if (view.getLayoutParams() instanceof ContentView.LayoutParams) {
				ContentView.LayoutParams lp = (ContentView.LayoutParams) view.getLayoutParams();
				if (nav.getTitle() == null) {
					nav.setTitle(lp.getAttrs().getString(R.styleable.nav, R.styleable.nav_nav_title, nav.getTitle()));
				}
				if (nav.getTag() == null) {
					nav.setTag(lp.getAttrs().getString(R.styleable.nav, R.styleable.nav_nav_tag, nav.getTag()));
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
			transaction.replace(R.id.lin_core_nav_content_frame, new ViewFragment((View) item));
		}else{
			transaction.replace(R.id.lin_core_nav_content_frame, (android.support.v4.app.Fragment) item);
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
		if(keyCode == KeyEvent.KEYCODE_BACK){
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
		nav.pop();
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

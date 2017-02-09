package lin.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author lin
 * @date Jul 28, 2015 4:08:20 PM
 *
 */
public class ViewActivity extends AppCompatActivity{
	
	private Bundle savedInstanceState;
	private boolean intercept = false;

	public ViewActivity(){
		activitys.add(new SoftReference<Activity>(this));
	}
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;

		if(this.getSupportActionBar() == null){
			this.setContentViewById(R.layout.lin_core_tools_act);
			Toolbar toolbar = (Toolbar)this.findViewById(R.id.lin_core_tools_act_toolbar);
			this.setSupportActionBar(toolbar);
			intercept = true;
		}

		View view = Views.loadView(this);
		if(view != null){
			this.setContentView(view);
		}else {
			Object obj = lin.core.mvvm.Utils.loadView(this);
			this.addContent(obj);
		}
//		LayoutInflaterFactory.setFactory2(this);
	}

	private static List<SoftReference<Activity>> activitys = new ArrayList<SoftReference<Activity>>();

	/*
	activityManager=(ActivityManager)super.getSystemService(Context.ACTIVITY_SERVICE);



        listActivity();

    }



    public void listActivity(){

    List<RunningTaskInfo> tasks=  activityManager.getRunningTasks(30);

        Iterator<RunningTaskInfo> itInfo=tasks.iterator();
	 */
	public static Activity getActivity(View view){

		for (SoftReference<Activity> item : activitys){
			Activity a = item.get();
			if(a == null){
				continue;
			}
			if(view.getWindowId().equals(a.getWindow().getDecorView().getWindowId())){
				return a;
			}
		}
		return null;
	}

	@Override
	public void setContentView(int layoutResID) {
		View view= Views.loadView(this,layoutResID);
		if(intercept){
			addContent(view);
		}else {
			super.setContentView(view);
		}
		Views.process(this,view);
	}

	@Override
	public void setContentView(View view) {
		if(intercept){
			addContent(view);
		}else {
			super.setContentView(view);
		}
		Views.process(this, view);
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		if(intercept){
			addContent(view);
		}else {
			super.setContentView(view, params);
		}
		Views.process(this, view);
	}

	@Override
	public void addContentView(View view, ViewGroup.LayoutParams params) {
		if(intercept){
			addContent(view);
		}else {
			super.addContentView(view, params);
		}
		Views.process(this, view);
	}
//	@Override
//	public void setContentView(int layoutResID) {
//		View view = Views.loadView(this,layoutResID);
//		super.setContentView(view);
//	}
//
	private void setContentViewById(int layoutResID) {
		super.setContentView(layoutResID);
		Views.process(this,this.getWindow().getDecorView());
	}
//
//	@Override
//	public void setContentView(View view) {
//		super.setContentView(view);
//		Views.process(this,view);
//
//	}
//
//	@Override
//	public void setContentView(View view, LayoutParams params) {
//		super.setContentView(view, params);
//		Views.process(this, view);
//	}
//
//	@Override
//	public void addContentView(View view, ViewGroup.LayoutParams params) {
//		super.addContentView(view, params);
//		Views.process(this, view);
//	}


	public void addContent(Object obj){
		if(obj instanceof View){
			addContent((View)obj);
		}else if(obj instanceof android.support.v4.app.Fragment){
			addContent((android.support.v4.app.Fragment)obj);
		}
	}
	private void addContent(View view) {
		addContent(new ViewFragment(view));
	}
	private void addContent(android.support.v4.app.Fragment fragment) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();

		FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (item instanceof View) {
		transaction.replace(R.id.lin_core_tools_act_layout, fragment);
//        } else {
//            transaction.replace(R.id.lin_core_nav_content_frame, (android.support.v4.app.Fragment) item);
//        }
		transaction.commit();
	}
}

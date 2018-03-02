package io.cess.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
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
	private ViewStub fragmentStud = null;
	private ViewStub viewStud;

	public ViewActivity(){
		activitys.add(new WeakReference<Activity>(this));
	}

	public void hideToolbar(){
		ActionBar actionBar = this.getSupportActionBar();
		if(actionBar != null){
			actionBar.hide();
		}
	}

	public void showToolbar(){
		ActionBar actionBar = this.getSupportActionBar();
		if(actionBar != null){
			actionBar.show();
		}
	}
	private Toolbar toolbar = null;
	protected ViewGroup toolsLayout = null;
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;

		if(this.getSupportActionBar() == null){
			this.setContentViewById(R.layout.io_cess_core_act_tools);
			toolbar = (Toolbar)this.findViewById(R.id.io_cess_core_act_tools_toolbar);
			toolsLayout = (ViewGroup) this.findViewById(R.id.io_cess_core_act_tools_layout);
//			fragmentStud = (ViewStub) this.findViewById(R.id.io_cess_core_act_tools_frame_stud);
//			viewStud = (ViewStub)this.findViewById(R.id.io_cess_core_act_tools_view_stud);
			this.setSupportActionBar(toolbar);
		}else {
			this.setContentViewById(R.layout.io_cess_core_act_no_tools);
		}
		intercept = true;
		fragmentStud = (ViewStub) this.findViewById(R.id.io_cess_core_act_tools_frame_stud);
		viewStud = (ViewStub)this.findViewById(R.id.io_cess_core_act_tools_view_stud);

		View view = Views.loadView(this);
		if(view != null){
			this.setContentView(view);
		}else {
			if(intercept == false){
				this.setContentViewById(R.layout.io_cess_core_act_no_tools);
				fragmentStud = (ViewStub) this.findViewById(R.id.io_cess_core_act_tools_frame_stud);
				viewStud = (ViewStub)this.findViewById(R.id.io_cess_core_act_tools_view_stud);
			}
			Object obj = io.cess.core.mvvm.Utils.loadView(this);
			this.setObjectContent(obj);
		}
//		LayoutInflaterFactory.setFactory2(this);
	}

	private static List<WeakReference<Activity>> activitys = new ArrayList<WeakReference<Activity>>();

	public static Activity getActivity(View view){

		for (WeakReference<Activity> item : activitys){
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
//		View view= Views.loadView(this,layoutResID);
		View view = Views.loadView(this,this,getViewGroup(),layoutResID,false);
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


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		Menus.onCreateOptionsMenu(this, menu, getMenuInflater(),getMenuId());
		return true;
	}

    protected int getMenuId(){
        return 0;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if(Menus.onOptionsItemSelected(this,item)){
			return true;
		}
        return super.onOptionsItemSelected(item);
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


	public void setObjectContent(Object obj){
		if(obj instanceof View){
			addContent((View)obj);
		}else if(obj instanceof android.support.v4.app.Fragment){
			addContent((android.support.v4.app.Fragment)obj);
		}
	}

//	@Override
//	public View findViewById(@IdRes int id){
//		if(intercept){
//			FragmentManager fragmentManager = this.getSupportFragmentManager();
//			return fragment.getView().findViewById(id);
//		}else{
//			return super.findViewById(id);
//		}
//	}

	private ViewGroup getViewGroup(){
		if(intercept){
			if(viewLayout == null) {
				View tview = viewStud.inflate();
				viewLayout = (ViewGroup) tview.findViewById(R.id.io_cess_core_act_view_layout);
			}
			return viewLayout;
		}
		if(this.getWindow().getDecorView() instanceof ViewGroup){
			return (ViewGroup) this.getWindow().getDecorView();
		}
		return null;
	}

	private ViewGroup viewLayout = null;
	private void addContent(View view) {
		if(viewLayout == null){
			View tview = viewStud.inflate();
			viewLayout = (ViewGroup) tview.findViewById(R.id.io_cess_core_act_view_layout);
		}
		fragmentStud.setVisibility(View.GONE);
		viewLayout.removeAllViews();
		viewLayout.addView(view);//,-1,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//		addContent(new ViewFragment(view));
	}

	private void addContent(android.support.v4.app.Fragment fragment) {

		fragmentStud.setVisibility(View.VISIBLE);
		viewStud.setVisibility(View.GONE);
		FragmentManager fragmentManager = this.getSupportFragmentManager();

		FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (item instanceof View) {
		transaction.replace(R.id.io_cess_core_act_fragment_layout, fragment);
//        } else {
//            transaction.replace(R.id.io_cess_core_nav_content_frame, (android.support.v4.app.Fragment) item);
//        }
		transaction.commit();
	}
}

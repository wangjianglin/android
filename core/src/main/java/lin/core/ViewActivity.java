package lin.core;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

	public ViewActivity(){
		activitys.add(new SoftReference<Activity>(this));
	}
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.savedInstanceState = savedInstanceState;
		LayoutInflaterFactory.setFactory2(this);
	}

	private static List<SoftReference<Activity>> activitys = new ArrayList<SoftReference<Activity>>();

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
		super.setContentView(layoutResID);
		Views.process(this,super.getWindow().getDecorView());
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		Views.process(this,view);

	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		Views.process(this, view);
	}

	@Override
	public void addContentView(View view, ViewGroup.LayoutParams params) {
		super.addContentView(view, params);
		Views.process(this, view);
	}
//
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onCreate(savedInstanceState);
//			}});
//	}
//
//	public void onConfigurationChanged(final Configuration newConfig) {
//		super.onConfigurationChanged(newConfig);
//
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onConfigurationChanged(newConfig);
//			}
//		});
//
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onStart();
//			}});
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onResume();
//			}});
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onStop();
//			}});
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onPause();
//			}});
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onDestroy();
//			}});
//	}
//
//	@Override
//	protected void onSaveInstanceState(final Bundle outState){
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onSaveInstanceState(outState);
//			}});
//
//		super.onSaveInstanceState(outState);
//	}
//
//	private void fireActivieyLifeCycle(lin.util.Procedure<ActivieyLifeCycle> procedure){
//		fireActivieyLifeCycleImpl(this.getWindow().getDecorView(),procedure);
//	}
//
//	private void fireActivieyLifeCycleImpl(View v,lin.util.Procedure<ActivieyLifeCycle> procedure){
//
//		if(v == null){
//			return;
//		}
//		if(v instanceof ActivieyLifeCycle){
//			procedure.procedure((ActivieyLifeCycle) v);
//		}
//		if(!(v instanceof ViewGroup)){
//			return;
//		}
//		ViewGroup contentView = (ViewGroup) v;//nav.getContentView();
//		for(int n=0;n<contentView.getChildCount();n++){
//			fireActivieyLifeCycleImpl(contentView.getChildAt(n),procedure);
//		}
//	}
	
}

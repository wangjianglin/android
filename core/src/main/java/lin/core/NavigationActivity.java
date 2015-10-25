package lin.core;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import lin.core.log.Log;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 1:24:56 AM
 *
 */
public class NavigationActivity extends ViewActivity {

	private Navigation nav;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		String className = intent.getAction();
		argsid = intent.getStringExtra("args_id");
		
		nav = new Navigation(this, argsid);
		//this.setContentView(R.layout.navigation_activity);
		this.setContentView(nav);

		//nav = (Navigation) this.findViewById(R.id.nav_activity_nav);
		
		nav.rootView(className);

//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onCreate(savedInstanceState);
//			}});
		
	}

//	@Override
//	protected void onStart() {
//		super.onStart();
//		log.info("start");
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		log.info("resume");
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		log.info("pause");
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		log.info("destroy");
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		log.info("save instance state");
//	}
//
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		log.info("restore instance state");
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		log.info("stop");
//	}

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
//		super.onSaveInstanceState(outState);
//		fireActivieyLifeCycle(new lin.util.Procedure<ActivieyLifeCycle>(){
//
//			@Override
//			public void procedure(ActivieyLifeCycle obj) {
//				obj.onSaveInstanceState(outState);
//			}});
//	}
//	
//	private void fireActivieyLifeCycle(lin.util.Procedure<ActivieyLifeCycle> procedure){
//		ViewGroup contentView = nav.getContentView();
//		ActivieyLifeCycle item = null;
//		for(int n=0;n<contentView.getChildCount();n++){
//			if(contentView.getChildAt(n) instanceof ActivieyLifeCycle){
//				item = (ActivieyLifeCycle)contentView.getChildAt(n);
//				procedure.procedure(item);
//			}
//		}
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
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(isBackKeyDown) {
				nav.popView();
			}
			isBackKeyDown = false;
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private String argsid;
	String argsId(){
		return argsid;
	}
}

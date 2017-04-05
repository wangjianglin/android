package lin.core;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lin.core.annotation.ResCls;
import lin.core.annotation.ViewById;

/**
 *
 * @author lin
 * @date Mar 11, 2015 12:26:14 AM
 *
 */
//考虑Fragment  用 ToolBar 实现导航栏
@SuppressLint("NewApi")
@ResCls(lin.core.R.class)
public class Nav {

	private CharSequence argsId;
	NavActivity activity;
	private Nav preNav;
	private CharSequence tag;
	private CharSequence title;
	private Object[] args;
	private Result result;


	public Object[] getArgs(){
		return this.args;
	}


	private static Map<String,Nav> navsMap = new HashMap<>();


	private static long seq = 1;

	Nav(String argsId,CharSequence preArgsId){
		this.argsId = argsId;
		navsMap.put(argsId,this);
		preNav = navsMap.get(preArgsId);
	}

	void init(){
		this.setTitle(title);
	}


	public Nav push(Class<?> viewCls,Result result,Object ... args){
		return push(this.activity,viewCls,result,args);
	}

	public Nav push(int layoutId,Result result,Object ... args){
		return push(this.activity,layoutId,result,args);
	}

	public void pop(Object ... args){
		this.activity.onBackPressed();
		if(result != null){
			result.result(args);
		}
	}

	public void popToTag(String tag,Object ... args){
		if(tag == null || "".equals(tag)){
			popToRoot(args);
			return;
		}
		Nav preNav = this;
		while(preNav != null && preNav.preNav != null && !tag.equals(preNav.preNav.getTag())){
			preNav.activity.onBackPressed();
			preNav = preNav.preNav;
		}
		preNav.activity.onBackPressed();
		if(preNav.result != null){
			preNav.result.result(args);
		}
	}
	
	public void popToRoot(Object ... args){

		Nav preNav = this;
		while(preNav != null && preNav.preNav != null){
			preNav.activity.onBackPressed();
			preNav = preNav.preNav;
		}
		preNav.activity.onBackPressed();
		if(preNav.result != null){
			preNav.result.result(args);
		}
	}

	public static Nav push(Activity activity, int layoutId, Result result, Object ... args) {
		return pushImpl(activity,null,layoutId,result,args);
	}

	public static Nav push(Activity activity, Class<?> viewCls, Result result, Object ... args) {
		return pushImpl(activity,viewCls,0,result,args);
	}

	private static Nav pushImpl(Activity activity, Class<?> viewCls, int layoutId, Result result, Object ... args){
		Intent intent = new Intent(activity,NavActivity.class);
		
		String argsId = "args_id_" + new Date().getTime() + ":" + seq++ + ":" + Math.round(1000);

		intent.setAction(argsId);

		if(viewCls != null) {
			intent.putExtra("cls", viewCls.getName());
		}
		intent.putExtra("layout_id",layoutId);

		CharSequence preArgsId = null;
		if(activity instanceof NavActivity){
			preArgsId = ((NavActivity) activity).getNav().getArgsId();
		}
		Nav nav = new Nav(argsId,preArgsId);
		nav.args = args;
		nav.result = result;
		activity.startActivity(intent);
		return nav;
	}


	public static void pop(Activity activity,Object ... args){
		popImpl(Nav.getNav(activity.getApplicationContext()),args);
	}

	public static void pop(Context context,Object ... args){
		popImpl(Nav.getNav(context),args);
	}

	public static void pop(android.view.View view, Object ... args){
		popImpl(Nav.getNav(view),args);
	}

	public static boolean pop(android.support.v4.app.Fragment fragment, Object ... args){
		return popImpl(Nav.getNav(fragment),args);
	}
	public static boolean pop(String argsId, Object ... args){
		return popImpl(Nav.getNav(argsId),args);
	}

	private static boolean popImpl(Nav nav,Object ... args){
		if(nav != null){
			nav.pop(args);
			return true;
		}
		return false;
	}
	public CharSequence getArgsId() {
		return argsId;
	}

	public CharSequence getTag() {
		return tag;
	}

	public void setTag(CharSequence tag) {
		this.tag = tag;
	}

	public CharSequence getTitle() {
		return title;
	}

	public void setTitle(CharSequence title) {
		this.title = title;
		if(activity != null) {
			activity.setTitle(title);
		}
	}

	/**
	 * 
	 * @param view
	 * @return
	 */
	public static Nav getNav(android.view.View view) {
		if (view == null) {
			return null;
		}
		Context context = view.getContext();
		return getNav(context);
	}

	public static Nav getNav(Context context){
		if(context instanceof NavActivity){
			return ((NavActivity) context).getNav();
		}
		return null;
	}
	public static Nav getNav(android.support.v4.app.Fragment fragment){
		if(fragment == null){
			return null;
		}
		return getNav(fragment.getContext());
	}

	static Nav getNav(String argsId){
		return navsMap.get(argsId);
	}

	public interface Result{
		void result(Object... result);
	}


	public static class View extends ContentView {
		public View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public View(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public View(Context context) {
			super(context);
		}

		@Override
		protected void genLayoutAttrs() {
			this.addLayoutAttr(R.styleable.nav,R.styleable.nav_nav_show,AttrType.Boolean);
			this.addLayoutAttr(R.styleable.nav,R.styleable.nav_nav_tag,AttrType.String);
			this.addLayoutAttr(R.styleable.nav,R.styleable.nav_nav_title,AttrType.String);
		}
	}
}



//@SuppressLint("NewApi")
//@ResCls(lin.core.R.class)
//public class Nav extends ResView {
//
//	public Nav(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		this.initWithTheme();
//	}
//
//	public Nav(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		this.initWithTheme();
//	}
//
//	public Nav(Context context) {
//		super(context);
//		this.initWithTheme();
//	}
//
//	private String argsId;
//
//	Nav(Context context, String argsId) {
//		super(context);
//		this.argsId = argsId;
//		themeId = R.layout.lin_core_nav_main;
//		if(this.getActivity() instanceof NavActivity){
////			NavActivity activity = (NavActivity) this.getActivity();
////			return argsMap.get(activity.argsId());
////			String argsId = activity.argsId();
//
//			this.preNav = navsMap.get(argsId);
//			if(this.preNav != null){
//				themeId = this.preNav.themeId;
//			}
//			this.preResult = resultsMap.get(argsId);
//			this.args = argsMap.get(argsId);
//
//
//			isNav = true;
//
//		}
//		this.init(themeId);
//	}
//
//	@Override
//	protected void onDetachedFromWindow() {
//		super.onDetachedFromWindow();
//
//		navsMap.remove(argsId);
//		resultsMap.remove(argsId);
//		argsMap.remove(argsId);
//	}
//
//	private int themeId;
//	private void initWithTheme(){
//		//R.layout.navigation_main
//		themeId = this.getAttrs().getInt(R.styleable.lin,R.styleable.lin_view_theme);
//		if(themeId == 0){
//			themeId = R.layout.lin_core_nav_main;
//		}
//		this.init(themeId);
//	}
//
//	@ViewById(id="lin_core_nav_toolbar")
//	Toolbar toolbar;
//
//	@ViewById(id="lin_core_nav_content_frame")
//	private FrameLayout frameLayout;
//
////	private int titleHeight = -1;
////	@Override
////	protected void onInited() {
////		isShow = true;
////		isNav = false;
//////		contentView = (ViewGroup) mRootView.findViewById(R.id.navigation_main_root_view);
//////		titleView = (TextView) this.findViewById(R.id.nav_title);
//////		leftButtonsView = (ViewGroup) this.findViewById(R.id.nav_left_buttons);
//////		rightButtonsView = (ViewGroup) this.findViewById(R.id.nav_right_buttons);
////
////
//////		if(this.getActivity() instanceof NavActivity){
//////			NavActivity activity = (NavActivity) this.getActivity();
////////			return argsMap.get(activity.argsId());
//////			String argsId = activity.argsId();
//////
//////			this.preNav = navsMap.get(argsId);
//////			this.preResult = resultsMap.get(argsId);
//////			this.args = argsMap.get(argsId);
//////
//////
//////			navsMap.remove(argsId);
//////			resultsMap.remove(argsId);
//////			argsMap.remove(argsId);
//////			isNav = true;
//////
//////		}
////		//navsMap
////	}
//
//	private int titleHeidht;
//	@Override
//	protected void onInited() {
//		isShow = true;
//		isNav = false;
//
//
////		if(isSupportAppCompat()) {
////			AppCompatActivityty activity = (AppCompatActivity) this.getActivity();
////			activity.setSupportActionBar(toolbar);
////
////			ActionBar actionBar = activity.getSupportActionBar();
////			actionBar.setDisplayHomeAsUpEnabled(true);
////			actionBar.setDisplayShowHomeEnabled(true);
////			toolbar.setTitle("ok");
////
////		Menu menu = toolbar.getMenu();
////
////		new MenuInflater(this.getContext()).inflate(R.menu.main,menu);
////
////			final TintTypedArray a = TintTypedArray.obtainStyledAttributes(toolbar.getContext(),
////					null, android.support.v7.appcompat.R.styleable.ActionBar, android.support.v7.appcompat.R.attr.actionBarStyle, 0);
////			Drawable mDefaultNavigationIcon = a.getDrawable(android.support.v7.appcompat.R.styleable.ActionBar_homeAsUpIndicator);
////			toolbar.setNavigationIcon(mDefaultNavigationIcon);
////			a.recycle();
////		}
//
//
//		showTitleImpl(isShow);
//	}
//
//	@Override
//	protected void addViewItem(View item,int index,ViewGroup.LayoutParams params) {
//
//		this.addNavView(item);
//
////		if(contentView.getChildCount() == 0){
////			contentView.addView(item);
////			if(item instanceof AttrsView){
////				AttrsView itemView = (AttrsView) item;
////				if(!isSetTitle){
////					titleTextView.setText(itemView.getAttrs().getString(R.styleable.lin_nav_title));
////				}
////
////				Drawable background = itemView.getAttrs().getDrawable(R.styleable.lin_nav_background);
////				if(!isSetShow){
////					this.showTitleImpl(itemView.getAttrs().getBoolean(R.styleable.lin_nav_show_title, this.isShow));
////				}
////				if(background != null){
////					this.titleView.setBackground(background);
////				}
////			}
////		}
//	}
//
//	private boolean isSupportAppCompat(){
//		return this.getActivity() instanceof AppCompatActivity;
//	}
//
//	void rootView(String className){
//		Object obj = gentView(className);
//
//		this.addNavView(obj);
//	}
//
//	private void addNavView(Object item){
//		if(item == null){
//			return;
//		}
//
//		if (isSupportAppCompat()) {
//			AppCompatActivity activity = (AppCompatActivity) this.getActivity();
//			FragmentManager fragmentManager = activity.getSupportFragmentManager();
//
//			FragmentTransaction transaction = fragmentManager.beginTransaction();
//			if(item instanceof View) {
//				transaction.replace(R.id.lin_core_nav_content_frame, new ViewFragment((View) item));
//			}else{
//				transaction.replace(R.id.lin_core_nav_content_frame, (Fragment) item);
//			}
//			transaction.commit();
//		}
//	}
//
//	private boolean isSetTitle;
//	public void setTitle(String title){
//		isSetTitle = true;
////		if(titleTextView != null){
////			titleTextView.setText(title);
////		}
//	}
//
//	private boolean isSetShow;
//	private boolean isShow = true;
//	public void showTitle(boolean isShow){
//		isSetShow = true;
//		this.showTitleImpl(isShow);
//	}
//	private void showTitleImpl(boolean isShow){
//		if(this.isShow == isShow){
//			return;
//		}
//		this.isShow = isShow;
////		if(titleView == null){
////			return;
////		}
////		if(isShow){
////			//titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(45*this.getContext().getResources().getDisplayMetrics().density)));
////			titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,titleHeidht));
////		}else{
////			titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
////		}
//	}
//	private Object gentView(String className){
//		try {
//			Class<?> c = Class.forName(className);
//			if(View.class.isAssignableFrom(c)) {
//				Constructor<?> con = c.getConstructor(Context.class);
//				if (con != null) {
//					return con.newInstance(this.getActivity());
//				}
//
//				con = c.getConstructor(Context.class, AttributeSet.class);
//
//				if (con != null) {
//					return con.newInstance(this.getActivity(), null);
//				}
//				con = c.getConstructor(Context.class, AttributeSet.class, int.class);
//				if (con != null) {
//					return con.newInstance(this.getActivity(), null, 0);
//				}
//			}else if(android.support.v4.app.Fragment.class.isAssignableFrom(c)){
//				return c.newInstance();
//			}
//
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//		return null;
//	}
//
//
//	//	private List<Navigation> navs = new ArrayList<Navigation>();
//	private Nav preNav;
//	private Result preResult;
//	private boolean isNav;
//
//	public void pop(Object ... args){
//		if(preNav != null || isNav){
//			this.getActivity().finish();
//			if(preResult != null){
//				preResult.result(args);
//			}
//		}
//	}
//
//	public void popRoot(Object ... args){
//		Nav tmpNav = this;
//		Result tmpResult = null;
//		while((tmpNav != null && tmpNav.preNav != null) || isNav){
//			if(tmpNav != null){
//				tmpNav.getActivity().finish();
//				tmpResult = tmpNav.preResult;
//				tmpNav = tmpNav.preNav;
//			}else{
//				break;
//			}
//		}
//		if(tmpResult != null){
//			tmpResult.result(args);
//		}
//	}
//
//
//	private static Map<String,Object[]> argsMap = new HashMap<String,Object[]>();
//	private static Map<String, Nav> navsMap = new HashMap<String, Nav>();
//	private static Map<String,Result> resultsMap = new HashMap<String,Result>();
//
//	private Object[] args;
//	public Object[] getArgs(){
//		return args;
//	}
//
//
//	private static long seq = 1;
//	//	//阻止代码重复执行
//////	public void pushView(Object view){
//	public static void push(Activity activity,Class<?> view,Result result,Object ... args){
//		pushViewImpl(activity,null,view,result,args);
//	}
//	public void push(Class<?> view,Result result,Object ... args){
//		pushViewImpl(this.getActivity(),this,view,result,args);
//	}
//
//	private static void pushViewImpl(Activity activity, Nav nav, Class<?> view, Result result, Object ... args){
//
//		Intent intent = new Intent(activity,NavActivity.class);
//
//		String args_id = "args_id_" + new Date().getTime() + ":" + seq++ + ":" + Math.round(1000);
//
//		intent.setAction(view.getName());
//
//		intent.putExtra("args_id", args_id);
//		argsMap.put(args_id, args);
//		navsMap.put(args_id, nav);
//		if(result != null){
//			resultsMap.put(args_id, result);
//		}
//		activity.startActivity(intent);
//////		Fragment fragment = pushViewImpl(view);
//////
//////		if(fragment != null){
//////			if(contentRootView == null){
//////				contentRootView = fragment.getView();
//////				return;
//////			}
//////			View fromView = null;
//////			if(stack.empty()){
//////				fromView = contentRootView;
//////			}else{
//////				fromView = stack.lastElement().getView();
//////			}
//////			stack.push(fragment);
////////			this.geta.getFragmentManager().beginTransaction().replace(R.id.main_layout, new MainFragment()).commit();
//////
//////			//((View)view).setX(this.getMeasuredWidth());
//////			this.pushViewImpl(fromView, fragment.getView());
//////		}
//	}
//
////	public void setLeftButtons(List<NavButton> buttons){
////		this.leftButtonsView.removeAllViews();
////		if(buttons == null){
////			return;
////		}
////		for(NavButton button : buttons){
////			addButton(this.leftButtonsView,button);
////		}
////	}
////
////	public void setLeftButton(NavButton button){
////		this.leftButtonsView.removeAllViews();
////		addButton(this.leftButtonsView,button);
////	}
////
////	public void setRightButtons(List<NavButton> buttons){
////		this.rightButtonsView.removeAllViews();
////		if(buttons == null){
////			return;
////		}
////		for(NavButton button : buttons){
////			addButton(this.rightButtonsView,button);
////		}
////	}
////
////	public void setRightButton(NavButton button){
////		this.rightButtonsView.removeAllViews();
////		addButton(this.rightButtonsView,button);
////	}
//
////	private void addButton(ViewGroup parent,NavButton button){
////		if(button == null){
////			return;
////		}
////		View buttonView = LayoutInflater.from(this.getContext()).inflate(R.layout.lin_core_navigation_botton, parent, true);
////		TextView titleView = (TextView) buttonView.findViewById(R.id.nav_button_title);
////		titleView.setText(button.getTitle());
////		ImageView leftIconView = (ImageView) buttonView.findViewById(R.id.nav_button_left_icon);
////		leftIconView.setMaxWidth((int)(5*this.getActivity().getResources().getDisplayMetrics().density));
////		if(button.getIcon() != null){
////			leftIconView.setImageDrawable(button.getIcon());
//////			leftIconView.setVisibility(View.VISIBLE);
////		}else{
//////			iconView.setVisibility(View.INVISIBLE);
////		}
////
////		ImageView rightIconView = (ImageView) buttonView.findViewById(R.id.nav_button_right_icon);
////		rightIconView.setMinimumWidth((int)(5*this.getActivity().getResources().getDisplayMetrics().density));
////		buttonView.setOnClickListener(button.getOnClickListener());
////	}
//
//
//
////	public ViewGroup getContentView(){
////		return contentView;
////	}
//
//
//	/**
//	 *
//	 * @param view
//	 * @return
//	 */
//	public static Nav getNav(View view){
//		if(view == null){
//			return null;
//		}
//		ViewParent parent = view.getParent();
//		while(parent != null){
//			if(parent instanceof Nav){
//				return (Nav)parent;
//			}
//			parent = parent.getParent();
//		}
//		return null;
//	}
//
//	public interface Result{
//		void result(Object... result);
//	}
//}
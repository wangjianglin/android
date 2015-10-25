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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 12:26:14 AM
 *
 */
@SuppressLint("NewApi")
@ResourceClass(lin.core.R.class)
public class Navigation extends ResourceView {

	public Navigation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.initWithTheme();
	}

	public Navigation(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.initWithTheme();
	}

	public Navigation(Context context) {
		super(context);
		this.initWithTheme();
	}

	private String argsId;
	
	Navigation(Context context,String argsId) {
		super(context);
		this.argsId = argsId;
		themeId = R.layout.lin_core_navigation_main;
		if(this.getActivity() instanceof NavigationActivity){
//			NavigationActivity activity = (NavigationActivity) this.getActivity();
//			return argsMap.get(activity.argsId());
//			String argsId = activity.argsId();
			
			this.preNav = navsMap.get(argsId);
			if(this.preNav != null){
				themeId = this.preNav.themeId;
			}
			this.preResult = resultsMap.get(argsId);
			this.args = argsMap.get(argsId);
			

			isNav = true;
			
		}
		this.init(themeId);
	}

	@Override
	public void onDestroy() {

		navsMap.remove(argsId);
		resultsMap.remove(argsId);
		argsMap.remove(argsId);
	}

	private int themeId;
	private void initWithTheme(){
		//R.layout.navigation_main
		themeId = this.getAttrs().getInteger(R.styleable.lin_view_theme);
		if(themeId == 0){
			themeId = R.layout.lin_core_navigation_main;
		}
		this.init(themeId);
	}
	
	@ViewById(id="navigation_main_root_view")
	private ViewGroup contentView;
	
	@ViewById(id="nav_title")
	private TextView titleTextView;
	
	@ViewById(id="nav_left_buttons")
	private ViewGroup leftButtonsView;
	
	@ViewById(id="nav_right_buttons")
	private ViewGroup rightButtonsView;
	
	@ViewById(id="nav_title_view")
	private View titleView;
	
//	private int titleHeight = -1;
	@Override
	protected void onInited() {
		isShow = true;
		isNav = false;
//		contentView = (ViewGroup) rootView.findViewById(R.id.navigation_main_root_view);
//		titleView = (TextView) this.findViewById(R.id.nav_title);
//		leftButtonsView = (ViewGroup) this.findViewById(R.id.nav_left_buttons);
//		rightButtonsView = (ViewGroup) this.findViewById(R.id.nav_right_buttons);
		
		
//		if(this.getActivity() instanceof NavigationActivity){
//			NavigationActivity activity = (NavigationActivity) this.getActivity();
////			return argsMap.get(activity.argsId());
//			String argsId = activity.argsId();
//			
//			this.preNav = navsMap.get(argsId);
//			this.preResult = resultsMap.get(argsId);
//			this.args = argsMap.get(argsId);
//			
//			
//			navsMap.remove(argsId);
//			resultsMap.remove(argsId);
//			argsMap.remove(argsId);
//			isNav = true;
//			
//		}
		//navsMap
	}
	
	private int titleHeidht;
	@Override
	protected void onFirstAttachedToWindow() {
//		titleHeight = titleView.getMinimumHeight();
		if(titleView != null){
			titleHeidht = titleView.getLayoutParams().height;
//			System.out.println("title heidht:"+titleHeidht);
		}
		showTitleImpl(isShow);
	}
	
	@Override
	protected void addViewItem(View item) {
		
		if(item == null){
			return;
		}
		
		if(contentView.getChildCount() == 0){
			contentView.addView(item);
			if(item instanceof LinView){
				LinView itemView = (LinView) item;
				if(!isSetTitle){
					titleTextView.setText(itemView.getAttrs().getString(R.styleable.lin_nav_title));
				}
				
				Drawable background = itemView.getAttrs().getDrawable(R.styleable.lin_nav_background);
				if(!isSetShow){
					this.showTitleImpl(itemView.getAttrs().getBoolean(R.styleable.lin_nav_show_title, this.isShow));
				}
				if(background != null){
					this.titleView.setBackground(background);
				}
			}
		}
	}
	
	void rootView(String className){
		Object obj = gentView(className);
		
		this.addViewItem((View)obj);
	}
	
	private boolean isSetTitle;
	public void setTitle(String title){
		isSetTitle = true;
		if(titleTextView != null){
			titleTextView.setText(title);
		}
	}

	private boolean isSetShow;
	private boolean isShow = true;
	public void showTitle(boolean isShow){
		isSetShow = true;
		this.showTitleImpl(isShow);
	}
	private void showTitleImpl(boolean isShow){
		if(this.isShow == isShow){
			return;
		}
		this.isShow = isShow;
		if(titleView == null){
			return;
		}
		if(isShow){
			//titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(45*this.getContext().getResources().getDisplayMetrics().density)));
			titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,titleHeidht));
		}else{
			titleView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0));
		}
	}
	private Object gentView(String className){
		try {
			Class<?> c = Class.forName(className);
			Constructor<?> con = getConstructor(c,Context.class);
			if(con == null){
				con = getConstructor(c,Context.class,AttributeSet.class);
			}else{
				return con.newInstance(this.getActivity());
			}
			if(con == null){
				con = getConstructor(c,Context.class,AttributeSet.class,int.class);
			}else{
				return con.newInstance(this.getActivity(),null);
			}
			if(con == null){
				con = getConstructor(c);
			}else{
				return con.newInstance(this.getActivity(),null,0);
			}
			if(con != null){
				return con.newInstance();
			}
			
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	private Constructor<?> getConstructor(Class<?> cls,Class<?>... parameterTypes){
		try{
			return cls.getConstructor(parameterTypes);
		}catch(Throwable e){}
		return null;
	}
	

//	private List<Navigation> navs = new ArrayList<Navigation>();
	private Navigation preNav;
	private Result preResult;
	private boolean isNav;
	
	public void popView(Object ... args){
		if(preNav != null || isNav){
			this.getActivity().finish();
			if(preResult != null){
				preResult.result(args);
			}
		}
	}
	
	public void popRootView(Object ... args){
		Navigation tmpNav = this;
		Result tmpResult = null;
		while((tmpNav != null && tmpNav.preNav != null) || isNav){
			if(tmpNav != null){
				tmpNav.getActivity().finish();
				tmpResult = tmpNav.preResult;
				tmpNav = tmpNav.preNav;
			}else{
				break;
			}
		}
		if(tmpResult != null){
			tmpResult.result(args);
		}
	}
	

	private static Map<String,Object[]> argsMap = new HashMap<String,Object[]>();
	private static Map<String,Navigation> navsMap = new HashMap<String,Navigation>();
	private static Map<String,Result> resultsMap = new HashMap<String,Result>();
	
	private Object[] args;
	public Object[] getArgs(){
		return args;
	}
	
	
	private static long seq = 1;
//	//阻止代码重复执行
////	public void pushView(Object view){
	public static void pushView(Activity activity,Class<?> view,Result result,Object ... args){
		pushViewImpl(activity,null,view,result,args);
	}
	public void pushView(Class<?> view,Result result,Object ... args){
		pushViewImpl(this.getActivity(),this,view,result,args);
	}
	
	private static void pushViewImpl(Activity activity,Navigation nav,Class<?> view,Result result,Object ... args){
			
		Intent intent = new Intent(activity,NavigationActivity.class);
		
		String args_id = "args_id_" + new Date().getTime() + ":" + seq++ + ":" + Math.round(1000);
		
		intent.setAction(view.getName());
		
		intent.putExtra("args_id", args_id);
		argsMap.put(args_id, args);
		navsMap.put(args_id, nav);
		if(result != null){
			resultsMap.put(args_id, result);
		}
		activity.startActivity(intent);
////		Fragment fragment = pushViewImpl(view);
////		
////		if(fragment != null){
////			if(contentRootView == null){
////				contentRootView = fragment.getView();
////				return;
////			}
////			View fromView = null;
////			if(stack.empty()){
////				fromView = contentRootView;
////			}else{
////				fromView = stack.lastElement().getView();
////			}
////			stack.push(fragment);
//////			this.geta.getFragmentManager().beginTransaction().replace(R.id.main_layout, new MainFragment()).commit();
////			
////			//((View)view).setX(this.getMeasuredWidth());
////			this.pushViewImpl(fromView, fragment.getView());
////		}
	}
	
	public void setLeftButtons(List<NavigationButton> buttons){
		this.leftButtonsView.removeAllViews();
		if(buttons == null){
			return;
		}
		for(NavigationButton button : buttons){
			addButton(this.leftButtonsView,button);
		}
	}
	
	public void setLeftButton(NavigationButton button){
		this.leftButtonsView.removeAllViews();
		addButton(this.leftButtonsView,button);
	}
	
	public void setRightButtons(List<NavigationButton> buttons){
		this.rightButtonsView.removeAllViews();
		if(buttons == null){
			return;
		}
		for(NavigationButton button : buttons){
			addButton(this.rightButtonsView,button);
		}
	}
	
	public void setRightButton(NavigationButton button){
		this.rightButtonsView.removeAllViews();
		addButton(this.rightButtonsView,button);
	}
	
	private void addButton(ViewGroup parent,NavigationButton button){
		if(button == null){
			return;
		}
		View buttonView = LayoutInflater.from(this.getContext()).inflate(R.layout.lin_core_navigation_botton, parent, true);
		TextView titleView = (TextView) buttonView.findViewById(R.id.nav_button_title);
		titleView.setText(button.getTitle());
		ImageView leftIconView = (ImageView) buttonView.findViewById(R.id.nav_button_left_icon);
		leftIconView.setMaxWidth((int)(5*this.getActivity().getResources().getDisplayMetrics().density));
		if(button.getIcon() != null){
			leftIconView.setImageDrawable(button.getIcon());
//			leftIconView.setVisibility(View.VISIBLE);
		}else{
//			iconView.setVisibility(View.INVISIBLE);
		}

		ImageView rightIconView = (ImageView) buttonView.findViewById(R.id.nav_button_right_icon);
		rightIconView.setMinimumWidth((int)(5*this.getActivity().getResources().getDisplayMetrics().density));
		buttonView.setOnClickListener(button.getOnClickListener());
	}
	
	
	
	public ViewGroup getContentView(){
		return contentView;
	}
	
	
	/**
	 * 
	 * @param view
	 * @return
	 */
	public static Navigation getNavigation(View view){
		if(view == null){
			return null;
		}
		ViewParent parent = view.getParent();
		while(parent != null){
			if(parent instanceof Navigation){
				return (Navigation)parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public interface Result{
		void result(Object... result);
	}
}

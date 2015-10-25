package lin.core;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import lin.core.annotation.ResourceClass;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 10, 2015 3:18:58 PM
 *
 */


@ResourceClass(R.class)
public class TabBar extends ResourceView {


	@ViewById(id="tabbar_bar_layout")
	private LinearLayout barLayout;



	private List<Fragment> tabItems = new ArrayList<Fragment>();
	private List<TabBarItem> tabs = new ArrayList<TabBarItem>();
	public TabBar(Context context) {
		super(context);
		this.initWithTheme();
	}


	public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		this.initWithTheme();
	}
	public TabBar(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.initWithTheme();
	}

	private void initWithTheme(){
		int r = this.getAttrs().getInteger(R.styleable.lin_view_theme);
		if(r == 0){
			r = R.layout.lin_core_tabbar_main;
		}
		this.init(r);
	}

	private Drawable activateBackground = null;
	private Drawable background = null;

	private Drawable overlayDrawable;

	@SuppressLint("NewApi")
	@Override
	protected void onInited() {

		Attrs attrs = this.getAttrs();


		background = attrs.getDrawable(R.styleable.lin_tab_background);
		if(background == null){
			background = super.getBackground();
		}
		activateBackground = attrs.getDrawable(R.styleable.lin_tab_activate_background);

		if(background != null){
			super.setBackground(background);
		}

		overlayDrawable = attrs.getDrawable(R.styleable.lin_tab_overlay);
//
	}

	@Override
	protected void addViewItem(View item){
		int tabbarItemId = this.getAttrs().getInteger(R.styleable.lin_tab_item_theme);
		if(tabbarItemId == 0){
			tabbarItemId = R.layout.lin_core_tabbar_item_bar;
		}

		TabBarItem barItem = new TabBarItem(this,this.getContext());
		barItem.setBarItemResourceId(tabbarItemId,item);
		//View barView = LayoutInflater.from(this.getContext()).inflate(tabbarItemId, this.barLayout, false);
		this.barLayout.addView(barItem);
		this.tabs.add(barItem);
		barItem.setOnClickListener(new MyOnClickListener(tabItems.size()));
//
//		tabItems.add(item);
		tabItems.add(new ViewFragment(item));
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
//			pager.setCurrentItem(index);
			setTab(index);
		}
	};



	@Override
	protected void onFirstAttachedToWindow() {
		setTab(tabIndex);
	}

	private int preIndex = -1;
	private void setTab(int tabIndex){
		for(int i=0;i<tabs.size();i++){
			this.tabs.get(i).setActivate(false);
		}
		if(tabIndex>=0&&tabIndex<tabs.size()) {
			this.tabs.get(tabIndex).setActivate(true);

			if (preIndex != tabIndex) {
				Activity a = this.getActivity();
				if (a != null) {
					a.getFragmentManager().beginTransaction().replace(R.id.tabbar_content, tabItems.get(tabIndex)).commit();
				}
			}
			preIndex = tabIndex;
		}
	}

	private int tabIndex = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			tabIndex = savedInstanceState.getInt("tab_index");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if(outState != null){
			outState.putInt("tab_index", preIndex);
		}
	}

	public Drawable getOverlayDrawable() {
		return overlayDrawable;
	}

	public void setOverlayDrawable(Drawable overlayDrawable) {
		this.overlayDrawable = overlayDrawable;
	}

	@Override
	public Drawable getBackground() {
		return background;
	}

	@Override
	public void setBackground(Drawable background) {
		this.background = background;
	}

	public Drawable getActivateBackground() {
		return activateBackground;
	}

	public void setActivateBackground(Drawable activateBackground) {
		this.activateBackground = activateBackground;
	}
}


//@ResourceClass(R.class)
//public class TabBar extends ResourceView {
//
//	@ViewById(id="tabbar_tabpager")
//	private ViewPager pager;
//
//	@ViewById(id="tabbar_bar_layout")
//	private LinearLayout barLayout;
//
//	@ViewById(id="tabbar_main_bottom")
//
//
//	private List<View> tabItems = new ArrayList<View>();
//	private List<TabBarItem> tabs = new ArrayList<TabBarItem>();
//	public TabBar(Context context) {
//		super(context);
//		this.initWithTheme();
//	}
//
//
//	public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context,attrs,defStyleAttr);
//		this.initWithTheme();
//	}
//	public TabBar(Context context, AttributeSet attrs) {
//		super(context,attrs);
//		this.initWithTheme();
//	}
//
//	private void initWithTheme(){
//		//R.layout.navigation_main
//		int r = this.getAttrs().getInteger(R.styleable.lin_view_theme);
//		if(r == 0){
//			r = R.layout.tabbar_main;
//		}
//		this.init(r);
//
////		pager.setOnPageChangeListener(new MyOnPageChangeListener());
//	}
//
//	private Drawable activateBackground = null;
//	private Drawable background = null;
//
//	private Drawable overlayDrawable;
//
//	@SuppressLint("NewApi")
//	@Override
//	protected void onInited() {
//
////		this.pager = (ViewPager) rootView.findViewById(R.id.tabpager);
////		this.barLayout = (LinearLayout) rootView.findViewById(R.id.tabbar_bar_layout);
////		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
//		Attrs attrs = this.getAttrs();
//
//
//		background = attrs.getDrawable(R.styleable.lin_tab_background);
//		if(background == null){
//			background = super.getBackground();
//		}
//		activateBackground = attrs.getDrawable(R.styleable.lin_tab_activate_background);
//
//		if(background != null){
//			super.setBackground(background);
//		}
//
//		overlayDrawable = attrs.getDrawable(R.styleable.lin_tab_overlay);
////
//	}
//
//	@Override
//	protected void addViewItem(View item){
//		int tabbarItemId = this.getAttrs().getInteger(R.styleable.lin_tab_item_theme);
//		if(tabbarItemId == 0){
//			tabbarItemId = R.layout.tabbar_item_bar;
//		}
////		View barView = LayoutInflater.from(this.getContext()).inflate(tabbarItemId, this.barLayout, false);
////		this.barLayout.addView(barView);
////		this.tabs.add(barView);
////		if(item instanceof LinView){
////			LinView barItem = (LinView) item;
////			TextView text = (TextView) barView.findViewById(R.id.tabbar_tiem_bar_text_id);
////			text.setText(barItem.getAttrs().getString(R.styleable.lin_tab_name));
////			if(barItem.getAttrs().getDrawable(R.styleable.lin_tab_icon) != null){
////				ImageView image = (ImageView) barView.findViewById(R.id.tabbar_tiem_bar_img_id);
////				image.setImageDrawable(barItem.getAttrs().getDrawable(R.styleable.lin_tab_icon));
////			}
////		}
////		barView.setOnClickListener(new MyOnClickListener(tabItems.size()));
////
////		tabItems.add(item);
//
//		TabBarItem barItem = new TabBarItem(this,this.getContext());
//		barItem.setBarItemResourceId(tabbarItemId,item);
//		//View barView = LayoutInflater.from(this.getContext()).inflate(tabbarItemId, this.barLayout, false);
//		this.barLayout.addView(barItem);
//		this.tabs.add(barItem);
//////		if(item instanceof LinView){
//////			LinView barItem = (LinView) item;
//////			TextView text = (TextView) barView.findViewById(R.id.tabbar_tiem_bar_text_id);
//////			text.setText(barItem.getAttrs().getString(R.styleable.lin_tab_name));
//////			if(barItem.getAttrs().getDrawable(R.styleable.lin_tab_icon) != null){
//////				ImageView image = (ImageView) barView.findViewById(R.id.tabbar_tiem_bar_img_id);
//////				image.setImageDrawable(barItem.getAttrs().getDrawable(R.styleable.lin_tab_icon));
//////			}
//////		}
//		barItem.setOnClickListener(new MyOnClickListener(tabItems.size()));
////
//		tabItems.add(item);
//	}
//
//	public class MyOnClickListener implements View.OnClickListener {
//		private int index = 0;
//
//		public MyOnClickListener(int i) {
//			index = i;
//		}
//		@Override
//		public void onClick(View v) {
//			pager.setCurrentItem(index);
//		}
//	};
//
//	@Override
//	protected void onFirstAttachedToWindow() {
//		PagerAdapter pagerAdapter = new PagerAdapter() {
//
//			@Override
//			public boolean isViewFromObject(View arg0, Object arg1) {
//				return arg0 == arg1;
//			}
//
//			@Override
//			public int getCount() {
//				return tabItems.size();
//			}
//			@Override
//			public void destroyItem(View container, int position, Object object) {
//				((ViewPager)container).removeView(tabItems.get(position));
//			}
//
//			@Override
//			public Object instantiateItem(View container, int position) {
//				((ViewPager)container).addView(tabItems.get(position));
//				return tabItems.get(position);
//			}
//		};
//		this.pager.setAdapter(pagerAdapter);
//
//		listener = new MyOnPageChangeListener();
//		pager.setOnPageChangeListener(listener);
//		if(tabs.size() > 0){
//			if(tabIndex < tabs.size()){
//				setTab(tabIndex);
//				pager.setCurrentItem(tabIndex);
//				listener.currIndex = tabIndex;
//			}else{
//				setTab(0);
//				pager.setCurrentItem(0);
//			}
//		}
//	}
//
//	private void setTab(int n){
//		for(int i=0;i<tabs.size();i++){
//			this.tabs.get(i).setActivate(false);
//		}
//		if(n>=0&&n<tabs.size()){
//			this.tabs.get(n).setActivate(true);
//		}
//	}
//
//	private int tabIndex;
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		if(savedInstanceState != null){
//			tabIndex = savedInstanceState.getInt("tab_index");
//		}
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		if(outState != null && listener != null){
//			outState.putInt("tab_index", listener.currIndex);
//		}
//	}
//
//	private MyOnPageChangeListener listener;
//
////	@Override
////	protected void onLayout(boolean changed, int l, int t, int r, int b) {
////		super.onLayout(changed, l, t, r, b);
//////		int animWidth =  TabBar.this.getMeasuredWidth() / tabItems.size();
//////		int offset = (animWidth - mTabImg.getMeasuredWidth()) / 2;
//////		mTabImg.setTranslationX(offset);
//////		Log.i("Nav","changed:"+changed+"\tl:"+l+"\tt:"+t+"\tr:"+r+"\tb:"+b);
////	}
//	 /* 页卡切换监听
//		 */
//		public class MyOnPageChangeListener implements OnPageChangeListener {
//			private int currIndex = 0;// 当前页卡编号
//			@Override
//			public void onPageSelected(int arg0) {
//				int animWidth =  TabBar.this.getMeasuredWidth() / tabItems.size();// * currIndex;
//				Animation animation = new TranslateAnimation(Animation.ABSOLUTE,animWidth*currIndex,Animation.ABSOLUTE,animWidth*arg0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0);
////				TabBar.this.tabs.get(currIndex).setBackgroundColor(0xeeeeee);
//				TabBar.this.tabs.get(currIndex).setActivate(false);
//				currIndex = arg0;
//				animation.setFillAfter(true);// True:图片停在动画结束位置
//				animation.setDuration(150);
////				mTabImg.startAnimation(animation);
////				TabBar.this.tabItems.get(currIndex).setBackgroundColor(0x08232323);
////				TabBar.this.tabs.get(currIndex).setBackgroundColor(0x30232323);
//				TabBar.this.tabs.get(currIndex).setActivate(true);
////				TabBar.this.tabItems.get(currIndex).set
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//			}
//		}
//
//		public Drawable getActivateBackground() {
//			return activateBackground;
//		}
//
//
//		public void setActivateBackground(Drawable activateBackground) {
//			this.activateBackground = activateBackground;
//		}
//
//
//		public Drawable getBackground() {
//			return background;
//		}
//
//
//		public void setBackground(Drawable background) {
//			this.background = background;
//			super.setBackground(background);
//		}
//
//
//		public Drawable getOverlayDrawable() {
//			return overlayDrawable;
//		}
//
//
//		public void setOverlayDrawable(Drawable overlayDrawable) {
//			this.overlayDrawable = overlayDrawable;
//		}
//
//}

package lin.core;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import lin.core.annotation.ResCls;
import lin.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Mar 10, 2015 3:18:58 PM
 *
 */

@ResCls(R.class)
public class TabBar extends ResView {

	@ViewById(id="tabbar_bar_layout")
	private LinearLayout barLayout;

//	@ViewById(id="tabbar_main_bottom")
//	private RelativeLayout tabbarMainBottom;

	private List<android.support.v4.app.Fragment> tabItems = new ArrayList<android.support.v4.app.Fragment>();
	private List<TabBarItem> tabs = new ArrayList<TabBarItem>();

	public TabBar(Context context) {
		super(context);
		this.initWithTheme();
		this.initDefValue();
	}


	public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context,attrs,defStyleAttr);
		this.initWithTheme();
		this.initDefValue();
	}
	public TabBar(Context context, AttributeSet attrs) {
		super(context,attrs);
		this.initWithTheme();
		this.initDefValue();
	}

	private void initWithTheme() {
		int r = this.getAttrs().getInt(R.styleable.lin, R.styleable.lin_view_theme);
		if (r == 0) {
			r = R.layout.lin_core_tabbar_main;
		}
		this.init(r);
	}

	private void initDefValue(){

		Attrs attrs = this.getAttrs();


		background = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_background);

		if(background != null){
			super.setBackground(background);
		}else{
			background = barLayout.getBackground();
		}


		activateBackground = attrs.getDrawable(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_background);

		textColor = attrs.getColor(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_color,textColor);

		activateTextColor = attrs.getColor(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_activate_color,activateTextColor);

		overlayDrawable = attrs.getDrawable(R.styleable.tabbar_tabbar_overlay);
	}

	private Drawable activateBackground = null;
	private Drawable background = null;

	private int textColor = 0xff343434;
	private int activateTextColor = 0xffffffff;

	private Drawable overlayDrawable;

	@SuppressLint("NewApi")
	@Override
	protected void onInited() {

		barLayout.setBackgroundDrawable(this.background);
		setTab(tabIndex);

	}

	@Override
	protected void addViewItem(View item, int index,ViewGroup.LayoutParams params){
		int tabbarItemId = this.getAttrs().getInt(R.styleable.tabbar,R.styleable.tabbar_tabbar_item_theme);
		if(tabbarItemId == 0){
			tabbarItemId = R.layout.lin_core_tabbar_item_bar;
		}

		TabBarItem barItem = new TabBarItem(this,this.getContext());
		if(params instanceof ContentView.LayoutParams) {
			barItem.setBarItemResource(tabbarItemId, item,((ContentView.LayoutParams) params).getAttrs());
		}else{
			barItem.setBarItemResource(tabbarItemId, item,null);
		}

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
		barItem.setLayoutParams(layoutParams);
		this.barLayout.addView(barItem);
		this.tabs.add(barItem);
		barItem.setOnClickListener(new MyOnClickListener(tabs.size()-1));
		if(isSupportAppCompat()) {
			if (item.getTag() instanceof android.support.v4.app.Fragment) {
				tabItems.add((android.support.v4.app.Fragment) item.getTag());
			} else {
				tabItems.add(new ViewFragment(item));
			}
		}else {//暂不对 android.app.Fragment 兼容

		}
	}

	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			setTab(index);
		}
	};


	private int preIndex = -1;
	private void setTab(int tabIndex){
		if (preIndex == tabIndex){
			return;
		}
		if(preIndex>=0&&preIndex<tabs.size()){
			this.tabs.get(preIndex).setActivate(false);
		}
		if(tabIndex>=0&&tabIndex<tabs.size()) {
			this.tabs.get(tabIndex).setActivate(true);

			if (isSupportAppCompat()) {
				AppCompatActivity activity = (AppCompatActivity) this.getActivity();
				FragmentManager fragmentManager = activity.getSupportFragmentManager();

				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.replace(R.id.lin_core_tabbar_content, tabItems.get(tabIndex));
				transaction.commit();
			}else {//暂不对 android.app.Fragment 兼容

				//a.getFragmentManager().beginTransaction().replace(R.id.tabbar_content, tabItems.get(tabIndex)).commit();
			}
			preIndex = tabIndex;
		}
	}

	private boolean isSupportAppCompat(){
		return this.getActivity() instanceof AppCompatActivity;
	}

	private int tabIndex = 0;

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

	public int getActivateTextColor() {
		return activateTextColor;
	}

	public void setActivateTextColor(int activateTextColor) {
		this.activateTextColor = activateTextColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}


	@Override
	protected void genAttrs() {

		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_background,AttrType.Drawable);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_background,AttrType.Drawable);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_overlay,AttrType.Drawable);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_item_theme,AttrType.Int);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_icon,AttrType.Drawable);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_icon,AttrType.Drawable);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_color,AttrType.Int);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_activate_color,AttrType.Int);
		this.addAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_name,AttrType.String);
		this.addAttr(R.styleable.lin,R.styleable.lin_view_theme,AttrType.Int);
	}

	@Override
	protected void genLayoutAttrs() {
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_background,AttrType.Drawable);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_background,AttrType.Drawable);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_overlay,AttrType.Drawable);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_item_theme,AttrType.Int);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_icon,AttrType.Drawable);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_activate_icon,AttrType.Drawable);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_color,AttrType.Int);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_text_activate_color,AttrType.Int);
		this.addLayoutAttr(R.styleable.tabbar,R.styleable.tabbar_tabbar_name,AttrType.String);
		this.addLayoutAttr(R.styleable.lin,R.styleable.lin_view_theme,AttrType.Int);
	}
}


//@ResCls(R.class)
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
////		this.pager = (ViewPager) mRootView.findViewById(R.id.tabpager);
////		this.barLayout = (LinearLayout) mRootView.findViewById(R.id.tabbar_bar_layout);
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
////		if(item instanceof AttrsView){
////			AttrsView barItem = (AttrsView) item;
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
//////		if(item instanceof AttrsView){
//////			AttrsView barItem = (AttrsView) item;
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

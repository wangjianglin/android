package lin.core;

import lin.core.annotation.ResourceClass;
import lin.core.annotation.ResourceId;
import lin.core.annotation.ViewById;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ResourceClass(value=lin.core.R.class)
@ResourceId(id="lin_core_segmented")
public class Segmented extends ResourceView{

	public Segmented(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initParams();
	}

	public Segmented(Context context, AttributeSet attrs) {
		super(context, attrs);
		initParams();
	}

	public Segmented(Context context) {
		super(context);
		initParams();
	}

	private void initParams(){
		this.setStrokeWidth(1);
		this.setRoundRadius(5);
	}

	@ViewById(id="lin_core_segmented_id")
	private LinearLayout layout;
	public void addItem(String item){
		if(item == null){
			return;
		}
		items.add(item);
		resetViews();
	}

	public void addItems(String[] items){
		if(items == null){
			return;
		}
		for(String item : items) {
			this.items.add(item);
		}
		resetViews();
		setSelectedIndex(0);
	}

	public void addItems(Collection<String> items){
		if(items == null){
			return;
		}
		this.items.addAll(items);
		resetViews();
	}

	public void remvoeItem(String item){
		if(items.contains(item)){
			items.remove(item);
			this.resetViews();
		}
	}

	public void removeItems(String[] items){

	}

	public void removeItems(Collection<String> items){

	}

	private void resetViews(){
		if(layout == null){
			return;
		}
		layout.removeAllViews();
		if(items == null || items.size() == 0){
			return;
		}
		View viewItem = null;
		GradientDrawable drawable = null;

		if(items.size() == 0){
			viewItem = mRootView = LayoutInflater.from(this.getContext()).inflate(R.layout.lin_core_segmented_item, layout, false);
			layout.addView(viewItem);

			drawable = new GradientDrawable();//创建drawable
			drawable.setColor(fillColor);
			drawable.setCornerRadius(roundRadius);
			drawable.setStroke(strokeWidth, strokeColor);

			viewItem.setBackgroundDrawable(drawable);

			setViewItem(viewItem, 0, items.get(0));
			return;
		}
		for(int n=0;n<items.size();n++){


			drawable = new GradientDrawable();//创建drawable

			LayerDrawable ld = new LayerDrawable(new Drawable[]{drawable});


			drawable.setColor(fillColor);
			drawable.setStroke(strokeWidth, strokeColor);

			if(n == 0) {
				viewItem = mRootView = LayoutInflater.from(this.getContext()).inflate(R.layout.lin_core_segmented_item_left, layout, false);
				drawable.setCornerRadii(new float[]{roundRadius, roundRadius, 0, 0, 0, 0, roundRadius, roundRadius});

				ld.setLayerInset(0, 0, 0, -(strokeWidth)/2,0);

//				drawable.setColor(fillColor);
//				drawable.setStroke(strokeWidth, strokeColor);
//				drawable.se
			}else if(n == items.size() - 1){
				viewItem = mRootView = LayoutInflater.from(this.getContext()).inflate(R.layout.lin_core_segmented_item_right, layout, false);
//				drawable.setCornerRadii(new float[]{roundRadius, roundRadius, 0, 0, 0, 0, roundRadius, roundRadius});
				drawable.setCornerRadii(new float[]{0, 0, roundRadius, roundRadius, roundRadius, roundRadius, 0, 0});
				ld.setLayerInset(0, -(strokeWidth+1) / 2,0, 0,0);

			}else{
				viewItem = mRootView = LayoutInflater.from(this.getContext()).inflate(R.layout.lin_core_segmented_item_middle, layout, false);
				ld.setLayerInset(0, -(strokeWidth+1) / 2,0, -(strokeWidth)/2,0);
//				drawable.setStroke(0, strokeColor);
//
//				drawable.setColor(fillColor);
//				drawable.setStroke(strokeWidth, strokeColor);
			}
			layout.addView(viewItem);
//			drawable.setHotspotBounds(0,0,30,0);

//			ChildDrawable childDrawable = new ChildDrawable();
//			childDrawable.
//			viewItem.setBackgroundDrawable(drawable);
			viewItem.setBackgroundDrawable(ld);
//			PaintDrawable p = new PaintDrawable();
//			p.setPadding(0, 0, 10, 0);
////			p.set
//			p.setCornerRadii(new float[]{0, 0, roundRadius, roundRadius, roundRadius, roundRadius, 0, 0});
////			p.setColor(fillColor);
//			p.getPaint().setColor(fillColor);
////			p.setStroke(strokeWidth, strokeColor);
//
////			p.set
////			ShapeDrawable d =  new ShapeDrawable()
////			viewItem.setBackgroundDrawable(p);
//			LayerDrawable d = (LayerDrawable) this.getContext().getResources().getDrawable(R.drawable.lin_core_form_section_separate);
////			viewItem.setBackgroundDrawable(this.getContext().getResources().getDrawable(R.drawable.lin_core_form_section_separate));
////			viewItem.setBackgroundDrawable(d);
//			GradientDrawable dd = (GradientDrawable) d.getDrawable(1);
			setViewItem(viewItem, n, items.get(n));
		}
	}

	private int preIndex = -1;
	public void setSelectedIndex(int index){
		if(index == preIndex){
			return;
		}

		if(index >=0 && index < layout.getChildCount()) {
			((GradientDrawable) ((LayerDrawable) (layout.getChildAt(index).getBackground())).getDrawable(0)).setColor(this.selectedColor);
			((TextView)(layout.getChildAt(index).findViewById(R.id.lin_core_segmented_text_view_id))).setTextColor(this.textSelectedColor);
		}
		if(preIndex >=0 && preIndex < layout.getChildCount()) {
			((GradientDrawable) ((LayerDrawable) (layout.getChildAt(preIndex).getBackground())).getDrawable(0)).setColor(this.fillColor);
			((TextView)(layout.getChildAt(preIndex).findViewById(R.id.lin_core_segmented_text_view_id))).setTextColor(this.textColor);
		}

		preIndex = index;

	}
	private void setViewItem(View viewItem,final int index, final String item){
		TextView textView = (TextView) viewItem.findViewById(R.id.lin_core_segmented_text_view_id);
		textView.setText(item);
		textView.setTextColor(this.textColor);
		viewItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				System.out.println("click:(index:"+index+",item:"+item);
				if(preIndex == index){
					return;
				}
				setSelectedIndex(index);
				if(listener != null){
					listener.selected(Segmented.this,index,item);
				}
			}
		});
	}

	private List<String> items = new ArrayList<String>();
	private int strokeWidth = 5; // 3dp 边框宽度
	private int roundRadius = 15; // 8dp 圆角半径
	private int strokeColor = Color.parseColor("#2011e5");//边框颜色
	private int fillColor = Color.parseColor("#DFDFE0");//内部填充颜色
	private int selectedColor = Color.parseColor("#2011e5");//内部填充颜色
	private int textSize;

	private int textColor = Color.BLACK;
	private int textSelectedColor = Color.WHITE;

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getTextSelectedColor() {
		return textSelectedColor;
	}

	public void setTextSelectedColor(int textSelectedColor) {
		this.textSelectedColor = textSelectedColor;
	}

	public void setStrokeWidth(int strokeWidth){
		this.strokeWidth = (int) (this.getContext().getResources().getDisplayMetrics().density * strokeWidth);
	}

	public void setRoundRadius(int roundRadius){
		this.roundRadius = (int) (this.getContext().getResources().getDisplayMetrics().density * roundRadius);
	}

	public int getFillColor() {
		return fillColor;
	}

	public void setFillColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public int getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
	}

	public int getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(int selectedColor) {
		this.selectedColor = selectedColor;
	}

	public String selectedItem(){
		return "";
	}
	public int selectedIndex(){
		return 0;
	}
	
//	private void fireClick(){
////		Object obj = this.mOnClickListener;
//		performClick();
//	}

	private OnItemSelectedListener listener;
	public void setOnItemSelectedListener(OnItemSelectedListener listener){
		this.listener = listener;
	}

	public static interface OnItemSelectedListener{
		void selected(Segmented segmented,int selectedIndex,String selectedItem);
	}
}

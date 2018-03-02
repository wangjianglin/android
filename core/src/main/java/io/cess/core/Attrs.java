package io.cess.core;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import io.cess.core.annotation.Attr;
import io.cess.core.annotation.ResCls;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 11:23:58 AM
 *
 */

public class Attrs {

	private Map<String, Object> values = new HashMap<String, Object>();


	private Map<Integer,Integer> ids = new HashMap<>();
	private Map<Integer,int[]> idAttrs = new HashMap<>();
	private Map<Integer,AttrType> types = new HashMap<>();

	private Map<int[], TypedArray> attrArrays = new HashMap<>();

	private void recycleAttrArray() {
		for (TypedArray item : attrArrays.values()) {
			item.recycle();
		}
	}

	private TypedArray getAttrArray(Context context, AttributeSet attrs, int[] ids) {
		TypedArray value = attrArrays.get(ids);
		if (value != null) {
			return value;
		}
		return context.obtainStyledAttributes(attrs, ids);
	}


	private void addValue(TypedArray attrArray,int[] ids, int id, AttrType type) {
		if (!attrArray.hasValue(id)) {
			return;
		}
		//String,Drawable,Color,Int,Boolean
		switch (type) {
			case Boolean:
				values.put(ids[id]+"", attrArray.getBoolean(id,false));
				break;
			case String:
				values.put(ids[id]+"", attrArray.getString(id));
				break;
			case Color:
				values.put(ids[id]+"", attrArray.getColor(id,0));
				break;
			case Int:
				values.put(ids[id]+"", attrArray.getInt(id,0));
				break;
			case Drawable:
				values.put(ids[id]+"", attrArray.getDrawable(id));
				break;
			case Float:
				values.put(ids[id]+"",attrArray.getFloat(id,0));
				break;
			case Dimension:
				values.put(ids[id]+"",attrArray.getDimension(id,0));
				values.put(ids[id]+"-offset",attrArray.getDimensionPixelOffset(id,0));
				values.put(ids[id]+"-size",attrArray.getDimensionPixelSize(id,0));
				break;
			case Fraction:
				values.put(ids[id]+"",attrArray.getFraction(id,1,100,0));
				break;
		}
	}

	private AttributeSet attrs = null;
	private Context context;
	Attrs(Context context, AttributeSet attrs) {
		this.context = context;
		this.attrs = attrs;
	}

	void process(){
		if (attrs == null || context == null) {
			return;
		}

		TypedArray attrArray = null;
		int[] attrsId = null;
		for(int id : ids.keySet()){
			attrsId = idAttrs.get(id);
			attrArray = this.getAttrArray(context,attrs,attrsId);
			this.addValue(attrArray,attrsId,ids.get(id),types.get(id));
		}
		this.recycleAttrArray();


	}

	public Drawable getDrawable(int[] ids,int id){
		return getDrawable(ids[id]);
	}

	public Drawable getDrawable(int id){
		return (Drawable) values.get(id+"");
	}

	public String getString(int[] ids,int id,String def){
		return getString(ids[id],def);
	}
	public String getString(int[] ids,int id){
		return getString(ids[id],null);
	}
	public String getString(int id){
		return getString(id,null);
	}

	public String getString(int id,String def){
		String v = (String) values.get(id+"");
		if(v == null){
			return def;
		}
		return v;
	}

	public int getColor(int id){
		return getColor(id,0xffffff);
	}

	public int getColor(int[] ids,int id){
		return getColor(ids[id]);
	}
	public int getColor(int[] ids,int id,int defValue){
		return getColor(ids[id],defValue);
	}
	public int getColor(int id,int defValue){
		Object obj = values.get(id+"");
		if(obj != null){
			return (Integer) obj;
		}
		return defValue;
	}


	public int getInt(int id){
		return getInt(id,0);
	}
	public int getInt(int[] ids,int id){
		return getInt(ids[id]);
	}
	public int getInt(int[] ids,int id,int defValue){
		return getInt(ids[id],defValue);
	}
	public int getInt(int id,int defValue){
		Object obj = values.get(id+"");
		if(obj != null){
			return (Integer)obj;
		}
		return defValue;
	}

	public boolean getBoolean(int id){
		return getBoolean(id,false);
	}
	public boolean getBoolean(int[] ids,int id){
		return getBoolean(ids[id]);
	}
	public boolean getBoolean(int[] ids,int id,boolean defValue){
		return getBoolean(ids[id],defValue);
	}
	public boolean getBoolean(int id,boolean defaultValue){
		Object obj =  values.get(id+"");
		if(obj == null){
			return defaultValue;
		}
		return (Boolean)obj;
	}


	public float getFloat(int id){
		return getFloat(id,0);
	}
	public float getFloat(int[] ids,int id){
		return getFloat(ids[id]);
	}
	public float getFloat(int[] ids,int id,float defValue){
		return getFloat(ids[id],defValue);
	}
	public float getFloat(int id,float defaultValue){
		Object obj =  values.get(id+"");
		if(obj == null){
			return defaultValue;
		}
		return (float)obj;
	}


	public float getFraction(int id){
		return getFraction(id,0);
	}
	public float getFraction(int[] ids,int id){
		return getFraction(ids[id]);
	}
	public float getFraction(int[] ids,int id,float defValue){
		return getFraction(ids[id],defValue);
	}
	public float getFraction(int id,float defaultValue){
		Object obj =  values.get(id+"");
		if(obj == null){
			return defaultValue;
		}
		return (float)obj;
	}

//	Dimension:
//			values.put(ids[id]+"",attrArray.getDimension(id,0));
//	values.put(ids[id]+"",attrArray.getDimensionPixelOffset(id,0));
//	values.put(ids[id]+"",attrArray.getDimensionPixelSize(id,0));
	public float getDimension(int id){
	return getDimension(id,0);
}
	public float getDimension(int[] ids,int id){
		return getDimension(ids[id]);
	}
	public float getDimension(int[] ids,int id,float defValue){
		return getDimension(ids[id],defValue);
	}
	public float getDimension(int id,float defaultValue){
		Object obj =  values.get(id+"");
		if(obj == null){
			return defaultValue;
		}
		return (float)obj;
	}


	public int getDimensionPixelOffset(int id){
		return getDimensionPixelOffset(id,0);
	}
	public int getDimensionPixelOffset(int[] ids,int id){
		return getDimensionPixelOffset(ids[id]);
	}
	public int getDimensionPixelOffset(int[] ids,int id,int defValue){
		return getDimensionPixelOffset(ids[id],defValue);
	}
	public int getDimensionPixelOffset(int id,int defaultValue){
		Object obj =  values.get(id+"-offset");
		if(obj == null){
			return defaultValue;
		}
		return (int)obj;
	}


	public int getDimensionPixelSize(int id){
		return getDimensionPixelSize(id,0);
	}
	public int getDimensionPixelSize(int[] ids,int id){
		return getDimensionPixelSize(ids[id]);
	}
	public int getDimensionPixelSize(int[] ids,int id,int defValue){
		return getDimensionPixelSize(ids[id],defValue);
	}
	public int getDimensionPixelSize(int id,int defaultValue){
		Object obj =  values.get(id+"-size");
		if(obj == null){
			return defaultValue;
		}
		return (int)obj;
	}

	public boolean hasValue(int[] attrs,int id){
		return hasValue(attrs[id]);
	}
	public boolean hasValue(int id){
		return values.containsKey(id+"");
	}




	void addAttr(int[] attrs,int id,AttrType type){
		types.put(attrs[id],type);
		ids.put(attrs[id],id);
		idAttrs.put(attrs[id],attrs);
	}


	private void addAttr(Class<?> rCls, String id,AttrType type) {
		String[] idsArr = id.split("_");
		try {
			Class<?> styleableCls = Class.forName(rCls.getName() + "$styleable");
			Object obj = styleableCls.getDeclaredField(idsArr[0]).get(null);
			int idValue = styleableCls.getDeclaredField(id).getInt(null);
			addAttr((int[])obj,idValue,type);
		} catch (Throwable e) {
		}
	}

	void addAttr(Object holder) {
		io.cess.core.annotation.Attrs attrsAnnon = holder.getClass().getAnnotation(io.cess.core.annotation.Attrs.class);

		if (attrsAnnon != null) {
			this.addAttrs(attrsAnnon.value());
		}
	}
	void addLayoutAttr(Object holder) {

		io.cess.core.annotation.LayoutAttrs attrsAnnon = holder.getClass().getAnnotation(io.cess.core.annotation.LayoutAttrs.class);

		if (attrsAnnon != null) {
			this.addAttrs(attrsAnnon.value());
		}
	}

	private void addAttrs(Attr[] attrs){

		TypedArray attrArray = null;
		int id = 0;
		ResCls resClsAnnon = this.getClass().getAnnotation(ResCls.class);
		Class<?> resCls = null;
		if (resClsAnnon != null) {
			resCls = resClsAnnon.value();
		}
		for (Attr attr : attrs) {
			if (attr.value() != 0) {
				addAttr(attr.attrs(),attr.value(),attr.type());
			} else {
				if (attr.R() != Object.class) {
					addAttr(attr.R(), attr.id(),attr.type());
				} else {
					addAttr(resCls, attr.id(),attr.type());
				}
			}

		}
	}

}

//public class Attrs {
//
//	private Map<Integer, Object> values = new HashMap<Integer, Object>();
//
//
//	private Map<int[], TypedArray> attrArrays = new HashMap<>();
//
//	private void recycleAttrArray() {
//		for (TypedArray item : attrArrays.values()) {
//			item.recycle();
//		}
//	}
//
//	private TypedArray getAttrArray(Context context, AttributeSet attrs, int[] ids) {
//		TypedArray value = attrArrays.get(ids);
//		if (value != null) {
//			return value;
//		}
//		return context.obtainStyledAttributes(attrs, ids);
//	}
//
//	private TypedArray getAttrArray(Class<?> rCls, Context context, AttributeSet attrs, String id) {
//
//		String[] idsArr = id.split("_");
//
//		try {
//			Class<?> styleableCls = Class.forName(rCls.getName() + "$styleable");
//			Object obj = styleableCls.getDeclaredField(idsArr[0]).get(null);
//			return getAttrArray(context, attrs, (int[]) obj);
//		} catch (Throwable e) {
////			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	private int getAttrId(Class<?> rCls, String id) {
//
//		try {
//			Class<?> styleableCls = Class.forName(rCls.getName() + "$styleable");
//			return styleableCls.getDeclaredField(id).getInt(null);
//		} catch (Throwable e) {
////			e.printStackTrace();
//		}
//
//		return 0;
//	}
//
//	private void addValue(TypedArray attrArray, Attr attrAnnon, int id, AttrType type) {
//		if (!attrArray.hasValue(id)) {
//			return;
//		}
//		//String,Drawable,Color,Int,Boolean
//		switch (type) {
//			case Boolean:
//				values.put(id, attrArray.getBoolean(id,false));
//				break;
//			case String:
//				values.put(id, attrArray.getString(id));
//				break;
//			case Color:
//				values.put(id, attrArray.getColor(id,0));
//				break;
//			case Int:
//				values.put(id, attrArray.getInt(id,0));
//				break;
//			case Drawable:
//				values.put(id, attrArray.getDrawable(id));
//				break;
//		}
//	}
//
//	Attrs(View view, AttributeSet attrs) {
//		if (attrs == null) {
//			return;
//		}
//
//		io.cess.core.annotation.Attrs attrsAnnon = this.getClass().getAnnotation(io.cess.core.annotation.Attrs.class);
//
//
//		if (attrsAnnon != null) {
//			Context context = view.getContext();
//			TypedArray attrArray = null;
//			int id = 0;
//			ResCls resClsAnnon = this.getClass().getAnnotation(ResCls.class);
//			Class<?> resCls = null;
//			if (resClsAnnon != null) {
//				resCls = resClsAnnon.value();
//			}
//			for (Attr attr : attrsAnnon.value()) {
//				if (attr.value() != 0) {
//					attrArray = getAttrArray(context, attrs, attr.attrs());
//					id = attr.value();
//				} else {
//					if (attr.R() != Object.class) {
//						attrArray = getAttrArray(attr.R(), context, attrs, attr.id());
//						id = this.getAttrId(attr.R(), attr.id());
//					} else {
//						attrArray = getAttrArray(resCls, context, attrs, attr.id());
//						id = this.getAttrId(resCls, attr.id());
//					}
//				}
//				addValue(attrArray, attr, id, attr.type());
//
//			}
//		}
//
//		recycleAttrArray();
//
//	}
//
//	public Drawable getDrawable(int id){
//		return (Drawable) values.get(id);
//	}
//
//	public String getString(int id){
//		return (String) values.get(id);
//	}
//
//	public int getColor(int id){
//		return getColor(id,0xffffff);
//	}
//
//	public int getColor(int id,int defValue){
//		Object obj = values.get(id);
//		if(obj != null){
//			return (Integer) obj;
//		}
//		return defValue;
//	}
//
//
//	public int getInt(int id){
//		return getInt(id,0);
//	}
//	public int getInt(int id,int defValue){
//		Object obj = values.get(id);
//		if(obj != null){
//			return (Integer)obj;
//		}
//		return defValue;
//	}
//
//	public boolean getBoolean(int id){
//		return getBoolean(id,false);
//	}
//
//	public boolean getBoolean(int id,boolean defaultValue){
//		Object obj =  values.get(id);
//		if(obj == null){
//			return defaultValue;
//		}
//		return (Boolean)obj;
//	}
//
//	public boolean hasValue(int id){
//		return values.containsKey(id);
//	}
//
//}


//public class Attrs {
//
//	private Map<Int, Object> values = new HashMap<Int, Object>();
//	Attrs(View view, AttributeSet attrs) {
//		if (attrs == null) {
//			return;
//		}
//		view.getContext().obtainStyledAttributes()
//		TypedArray attrArray = view.getContext().obtainStyledAttributes(attrs,R.styleable.lin);
//
//
//		attrArray.recycle();
//
//		if(attrArray.hasValue(R.styleable.io_cess_view_theme)){
//			int theme = attrArray.getResourceId(R.styleable.io_cess_view_theme, 0);
//			values.put("id:"+R.styleable.io_cess_view_theme, theme);
//			values.put("name:io_cess_view_theme", theme);
//		}
//
//		//nav相关 开始
//		values.put("id:"+R.styleable.io_cess_nav_background, attrArray.getDrawable(R.styleable.io_cess_nav_background));
//		values.put("name:io_cess_nav_background", attrArray.getDrawable(R.styleable.io_cess_nav_background));
//
//		values.put("id:"+R.styleable.io_cess_nav_title, attrArray.getString(R.styleable.io_cess_nav_title));
//		values.put("name:io_cess_nav_title", attrArray.getString(R.styleable.io_cess_nav_title));
//
//		values.put("id:"+R.styleable.io_cess_nav_title_color, attrArray.getString(R.styleable.io_cess_nav_title_color));
//		values.put("name:io_cess_nav_title_color", attrArray.getString(R.styleable.io_cess_nav_title_color));
//
//		values.put("id:"+R.styleable.io_cess_nav_show_title, attrArray.getBoolean(R.styleable.io_cess_nav_show_title,true));
//		values.put("name:io_cess_nav_show_title", attrArray.getBoolean(R.styleable.io_cess_nav_show_title,true));
//
////		if(attrArray.hasValue(R.styleable.io_cess_nav_background_color)){
////			values.put("id:"+R.styleable.io_cess_nav_background_color, attrArray.getColor(R.styleable.io_cess_nav_background_color,0xffffff));
////			values.put("name:io_cess_nav_background_color", attrArray.getColor(R.styleable.io_cess_nav_background_color,0xffffff));
////		}
//
//		values.put("id:"+R.styleable.io_cess_nav_tag, attrArray.getString(R.styleable.io_cess_nav_tag));
//		values.put("name:io_cess_nav_tag", attrArray.getString(R.styleable.io_cess_nav_tag));
//		//nav相关 结束
//
//		//tab相关 开始
//		values.put("id:"+R.styleable.io_cess_tab_icon, attrArray.getDrawable(R.styleable.io_cess_tab_icon));
//		values.put("name:io_cess_tab_icon", attrArray.getDrawable(R.styleable.io_cess_tab_icon));
//
//		values.put("id:"+R.styleable.io_cess_tab_activate_icon, attrArray.getDrawable(R.styleable.io_cess_tab_activate_icon));
//		values.put("name:io_cess_tab_activate_icon", attrArray.getDrawable(R.styleable.io_cess_tab_activate_icon));
//
//		values.put("id:"+R.styleable.io_cess_tab_background, attrArray.getDrawable(R.styleable.io_cess_tab_background));
//		values.put("name:io_cess_tab_background", attrArray.getDrawable(R.styleable.io_cess_tab_background));
//
//		values.put("id:"+R.styleable.io_cess_tab_activate_background, attrArray.getDrawable(R.styleable.io_cess_tab_activate_background));
//		values.put("name:io_cess_tab_activate_background", attrArray.getDrawable(R.styleable.io_cess_tab_activate_background));
//
//		values.put("id:"+R.styleable.io_cess_tab_name, attrArray.getString(R.styleable.io_cess_tab_name));
//		values.put("name:io_cess_tab_name", attrArray.getString(R.styleable.io_cess_tab_name));
//
//		if(attrArray.hasValue(R.styleable.io_cess_tab_item_theme)){
//			values.put("id:"+R.styleable.io_cess_tab_item_theme, attrArray.getResourceId(R.styleable.io_cess_tab_item_theme,0));
//			values.put("name:io_cess_tab_item_theme", attrArray.getResourceId(R.styleable.io_cess_tab_item_theme,0));
//		}
//
//		values.put("id:"+R.styleable.io_cess_tab_overlay, attrArray.getDrawable(R.styleable.io_cess_tab_overlay));
//		values.put("name:io_cess_tab_overlay", attrArray.getDrawable(R.styleable.io_cess_tab_overlay));
//
//		//tab相关 结束
////		values.put("name:io_cess_tab_theme", attrArray.getInteger(R.styleable.io_cess_nav_theme,0));
////		values.put("id:"+R.styleable.io_cess_tab_theme, attrArray.getInteger(R.styleable.io_cess_nav_theme,0));
//
//
//		//form 相关  开始
//		values.put("id:"+R.styleable.io_cess_form_row_text, attrArray.getString(R.styleable.io_cess_form_row_text));
//		values.put("name:io_cess_form_row_text", attrArray.getString(R.styleable.io_cess_form_row_text));
////
//		values.put("id:"+R.styleable.io_cess_form_row_title, attrArray.getString(R.styleable.io_cess_form_row_title));
//		values.put("name:io_cess_form_row_title", attrArray.getString(R.styleable.io_cess_form_row_title));
//
////		if(attrArray.hasValue(R.styleable.io_cess_form_row_accessory)){
////			values.put("id:"+R.styleable.io_cess_form_row_accessory, attrArray.getBoolean(R.styleable.io_cess_form_row_accessory,false));
////		}else{
////			values.put("id:"+R.styleable.io_cess_form_row_accessory, null);
////		}
////		if(attrArray.hasValue(R.styleable.io_cess_form_row_accessory)){
////			values.put("name:io_cess_form_row_accessory", attrArray.getBoolean(R.styleable.io_cess_form_row_accessory,false));
////		}else{
////			values.put("name:io_cess_form_row_accessory", null);
////		}
//		if(attrArray.hasValue(R.styleable.io_cess_form_row_accessory)){
//			values.put("id:"+R.styleable.io_cess_form_row_accessory, attrArray.getBoolean(R.styleable.io_cess_form_row_accessory,false));
//			values.put("name:io_cess_form_row_accessory", attrArray.getBoolean(R.styleable.io_cess_form_row_accessory,false));
//		}
//		//form 相关  结束
//
//
////		int r = attrArray.getResourceId(R.styleable.io_cess_segmenteds, 0);
//
//
////		String[] values = attrArray.getResources().getStringArray(R.styleable.io_cess_segmenteds);
////		System.out.println("values:"+r);
////		attrArray.
////		values.put("name:io_cess_form_row_accessory", attrArray.getString(R.styleable.io_cess_form_row_accessory));
////		attrArray.
//		attrArray.recycle();
//	}
	
	
//	public Drawable getDrawable(String name){
//		return (Drawable) values.get("name:"+name);
//	}
//	public Drawable getDrawable(int id){
//		return (Drawable) values.get("id:"+id);
//	}
//
//	public void setDrawable(String name,Drawable value){
//		values.put("name:"+name,value);
//	}
//	public void setDrawable(int id,Drawable value){
//		values.put("id:"+id,value);
//	}
//
//	public String getString(String name){
//		return (String) values.get("name:"+name);
//	}
//	public String getString(int id){
//		return (String) values.get("id:"+id);
//	}
//
//	public void setString(String name,String value){
//		values.put("name:"+name,value);
//	}
//	public void setString(int id,String value){
//		values.put("id:"+id,value);
//	}
//
//	public int getColor(String name){
//		return getColor(name,0xffffff);
//	}
//	public int getColor(String name,int defValue){
//		Object obj = values.get("name:"+name);
//		if(obj != null){
//			return (Int) obj;
//		}
//		return defValue;
//	}
//
//	public int getColor(int id){
//		return getColor(id,0xffffff);
//	}
//
//	public int getColor(int id,int defValue){
//		Object obj = values.get("id:"+id);
//		if(obj != null){
//			return (Int) obj;
//		}
//		return defValue;
//	}
//
//	public void setColor(String name,int value){
//		values.put("name:"+name,value);
//	}
//	public void setColor(int id,int value){
//		values.put("id:"+id,value);
//	}
//
//
//	public void setInteger(String name,int value){
//		values.put("name:"+name,value);
//	}
//	public void setInteger(int id,int value){
//		values.put("id:"+id,value);
//	}
//	public int getInteger(String name){
//		return getInteger(name,0);
//	}
//	public int getInteger(String name,int defValue){
//		Object obj = values.get("name:"+name);
//		if(obj != null){
//			return (Int) obj;
//		}
//		return defValue;
//	}
//	public int getInteger(int id){
//		return getInteger(id,0);
//	}
//	public int getInteger(int id,int defValue){
//		Object obj = values.get("id:"+id);
//		if(obj != null){
//			return (Int)obj;
//		}
//		return defValue;
//	}
//
//
//
//
//	public boolean getBoolean(String name){
//		return getBoolean(name,false);
//	}
//	public boolean getBoolean(int id){
//		return getBoolean(id,false);
//	}
//
//	public void setBoolean(String name,Boolean value){
//		values.put("name:"+name,value);
//	}
//	public void setBoolean(int id,Boolean value){
//		values.put("id:"+id,value);
//	}
//
//	public boolean getBoolean(String name,boolean defaultValue){
//		Object obj = values.get("name:"+name);
//		if(obj == null){
//			return defaultValue;
//		}
//		return (Boolean)obj;
//	}
//	public boolean getBoolean(int id,boolean defaultValue){
//		Object obj =  values.get("id:"+id);
//		if(obj == null){
//			return defaultValue;
//		}
//		return (Boolean)obj;
//	}
//
//	public boolean hasValue(int id){
//		return values.containsKey("id:"+id);
//	}
//
//	public boolean hasValue(String name){
//		return values.containsKey("name:"+name);
//	}
//}

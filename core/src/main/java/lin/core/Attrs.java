package lin.core;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 11:23:58 AM
 *
 */
public class Attrs {

	private Map<String,Object> map = new HashMap<String,Object>();
	Attrs(Context context, AttributeSet attrs){
		if(attrs == null){
			return;
		}
		TypedArray attrArray = context.obtainStyledAttributes(attrs,R.styleable.lin);

		if(attrArray.hasValue(R.styleable.lin_view_theme)){
			int theme = attrArray.getResourceId(R.styleable.lin_view_theme, 0);
			map.put("id:"+R.styleable.lin_view_theme, theme);
			map.put("name:lin_view_theme", theme);
		}
		
		//nav相关 开始
		map.put("id:"+R.styleable.lin_nav_background, attrArray.getDrawable(R.styleable.lin_nav_background));
		map.put("name:lin_nav_background", attrArray.getDrawable(R.styleable.lin_nav_background));

		map.put("id:"+R.styleable.lin_nav_title, attrArray.getString(R.styleable.lin_nav_title));
		map.put("name:lin_nav_title", attrArray.getString(R.styleable.lin_nav_title));

		map.put("id:"+R.styleable.lin_nav_title_color, attrArray.getString(R.styleable.lin_nav_title_color));
		map.put("name:lin_nav_title_color", attrArray.getString(R.styleable.lin_nav_title_color));
		
		map.put("id:"+R.styleable.lin_nav_show_title, attrArray.getBoolean(R.styleable.lin_nav_show_title,true));
		map.put("name:lin_nav_show_title", attrArray.getBoolean(R.styleable.lin_nav_show_title,true));

//		if(attrArray.hasValue(R.styleable.lin_nav_background_color)){
//			map.put("id:"+R.styleable.lin_nav_background_color, attrArray.getColor(R.styleable.lin_nav_background_color,0xffffff));
//			map.put("name:lin_nav_background_color", attrArray.getColor(R.styleable.lin_nav_background_color,0xffffff));
//		}
		
		map.put("id:"+R.styleable.lin_nav_tag, attrArray.getString(R.styleable.lin_nav_tag));
		map.put("name:lin_nav_tag", attrArray.getString(R.styleable.lin_nav_tag));
		//nav相关 结束
		
		//tab相关 开始
		map.put("id:"+R.styleable.lin_tab_icon, attrArray.getDrawable(R.styleable.lin_tab_icon));
		map.put("name:lin_tab_icon", attrArray.getDrawable(R.styleable.lin_tab_icon));

		map.put("id:"+R.styleable.lin_tab_activate_icon, attrArray.getDrawable(R.styleable.lin_tab_activate_icon));
		map.put("name:lin_tab_activate_icon", attrArray.getDrawable(R.styleable.lin_tab_activate_icon));

		map.put("id:"+R.styleable.lin_tab_background, attrArray.getDrawable(R.styleable.lin_tab_background));
		map.put("name:lin_tab_background", attrArray.getDrawable(R.styleable.lin_tab_background));

		map.put("id:"+R.styleable.lin_tab_activate_background, attrArray.getDrawable(R.styleable.lin_tab_activate_background));
		map.put("name:lin_tab_activate_background", attrArray.getDrawable(R.styleable.lin_tab_activate_background));
		
		map.put("id:"+R.styleable.lin_tab_name, attrArray.getString(R.styleable.lin_tab_name));
		map.put("name:lin_tab_name", attrArray.getString(R.styleable.lin_tab_name));
		
		if(attrArray.hasValue(R.styleable.lin_tab_item_theme)){
			map.put("id:"+R.styleable.lin_tab_item_theme, attrArray.getResourceId(R.styleable.lin_tab_item_theme,0));
			map.put("name:lin_tab_item_theme", attrArray.getResourceId(R.styleable.lin_tab_item_theme,0));
		}
		
		map.put("id:"+R.styleable.lin_tab_overlay, attrArray.getDrawable(R.styleable.lin_tab_overlay));
		map.put("name:lin_tab_overlay", attrArray.getDrawable(R.styleable.lin_tab_overlay));
		
		//tab相关 结束
//		map.put("name:lin_tab_theme", attrArray.getInteger(R.styleable.lin_nav_theme,0));
//		map.put("id:"+R.styleable.lin_tab_theme, attrArray.getInteger(R.styleable.lin_nav_theme,0));
		
		
		//form 相关  开始
		map.put("id:"+R.styleable.lin_form_row_text, attrArray.getString(R.styleable.lin_form_row_text));
		map.put("name:lin_form_row_text", attrArray.getString(R.styleable.lin_form_row_text));
//		
		map.put("id:"+R.styleable.lin_form_row_title, attrArray.getString(R.styleable.lin_form_row_title));
		map.put("name:lin_form_row_title", attrArray.getString(R.styleable.lin_form_row_title));
		
//		if(attrArray.hasValue(R.styleable.lin_form_row_accessory)){
//			map.put("id:"+R.styleable.lin_form_row_accessory, attrArray.getBoolean(R.styleable.lin_form_row_accessory,false));
//		}else{
//			map.put("id:"+R.styleable.lin_form_row_accessory, null);
//		}
//		if(attrArray.hasValue(R.styleable.lin_form_row_accessory)){
//			map.put("name:lin_form_row_accessory", attrArray.getBoolean(R.styleable.lin_form_row_accessory,false));
//		}else{
//			map.put("name:lin_form_row_accessory", null);
//		}
		if(attrArray.hasValue(R.styleable.lin_form_row_accessory)){
			map.put("id:"+R.styleable.lin_form_row_accessory, attrArray.getBoolean(R.styleable.lin_form_row_accessory,false));
			map.put("name:lin_form_row_accessory", attrArray.getBoolean(R.styleable.lin_form_row_accessory,false));
		}
		//form 相关  结束
		
		
//		int r = attrArray.getResourceId(R.styleable.lin_segmenteds, 0);
		
		
//		String[] values = attrArray.getResources().getStringArray(R.styleable.lin_segmenteds);
//		System.out.println("values:"+r);
//		attrArray.
//		map.put("name:lin_form_row_accessory", attrArray.getString(R.styleable.lin_form_row_accessory));
//		attrArray.
		attrArray.recycle();
	}
	
	
	public Drawable getDrawable(String name){
		return (Drawable) map.get("name:"+name);
	}
	public Drawable getDrawable(int id){
		return (Drawable) map.get("id:"+id);
	}
	
	public void setDrawable(String name,Drawable value){
		map.put("name:"+name,value);
	}
	public void setDrawable(int id,Drawable value){
		map.put("id:"+id,value);
	}

	public String getString(String name){
		return (String) map.get("name:"+name);
	}
	public String getString(int id){
		return (String) map.get("id:"+id);
	}

	public void setString(String name,String value){
		map.put("name:"+name,value);
	}
	public void setString(int id,String value){
		map.put("id:"+id,value);
	}

	public int getColor(String name){
		return getColor(name,0xffffff);
	}
	public int getColor(String name,int defValue){
		Object obj = map.get("name:"+name);
		if(obj != null){
			return (Integer) obj;
		}
		return defValue;
	}
	
	public int getColor(int id){
		return getColor(id,0xffffff);
	}
	
	public int getColor(int id,int defValue){
		Object obj = map.get("id:"+id);
		if(obj != null){
			return (Integer) obj;
		}
		return defValue;
	}

	public void setColor(String name,int value){
		map.put("name:"+name,value);
	}
	public void setColor(int id,int value){
		map.put("id:"+id,value);
	}
	

	public void setInteger(String name,int value){
		map.put("name:"+name,value);
	}
	public void setInteger(int id,int value){
		map.put("id:"+id,value);
	}
	public int getInteger(String name){
		return getInteger(name,0);
	}
	public int getInteger(String name,int defValue){
		Object obj = map.get("name:"+name);
		if(obj != null){
			return (Integer) obj;
		}
		return defValue;
	}
	public int getInteger(int id){
		return getInteger(id,0);
	}
	public int getInteger(int id,int defValue){
		Object obj = map.get("id:"+id);
		if(obj != null){
			return (Integer)obj;
		}
		return defValue;
	}
	

	

	public boolean getBoolean(String name){
		return getBoolean(name,false);
	}
	public boolean getBoolean(int id){
		return getBoolean(id,false);
	}
	
	public void setBoolean(String name,Boolean value){
		map.put("name:"+name,value);
	}
	public void setBoolean(int id,Boolean value){
		map.put("id:"+id,value);
	}
	
	public boolean getBoolean(String name,boolean defaultValue){
		Object obj = map.get("name:"+name);
		if(obj == null){
			return defaultValue;
		}
		return (Boolean)obj;
	}
	public boolean getBoolean(int id,boolean defaultValue){
		Object obj =  map.get("id:"+id);
		if(obj == null){
			return defaultValue;
		}
		return (Boolean)obj;
	}
	
	public boolean hasValue(int id){
		return map.containsKey("id:"+id);
	}
	
	public boolean hasValue(String name){
		return map.containsKey("name:"+name);
	}
}

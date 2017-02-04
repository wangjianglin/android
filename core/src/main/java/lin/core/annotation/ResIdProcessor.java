package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:22:09 PM
 *
 */
public class ResIdProcessor implements FieldProcessor<ResId>{

	@Override
	public void process(Object target, View view, Field field, ResId annot,
						Package pack) {

		field.setAccessible(true);
		
		int itemId = 0;
		 if(annot.value() != 0){
			 itemId = annot.value();
		 }
		 else if(!"".equals(annot.id())){
//			 try{
//				 Field f = idClass.getDeclaredField(item.id());
//				 itemId = f.getInt(null);
//			 }catch(Throwable e){}
			 itemId = Utils.getId(pack,annot.id());
		 }else{
//			 try{
//				 Field f = idClass.getDeclaredField(field.getName());
//				 itemId = f.getInt(null);
//			 }catch(Throwable e){}
			 itemId = Utils.getId(pack,field.getName());
		 }
		 
		 if(itemId != -1){
			 try{
				 Class<?> type = field.getType();
				 if(type == null){
					 return;
				 }
				 if(type.isAssignableFrom(Drawable.class)){
					 field.set(target, view.getResources().getDrawable(itemId));
				 }else if(type.isAssignableFrom(int.class)){
					 try{
						 field.set(target, view.getResources().getColor(itemId));
					 }catch(Throwable e){}
					 field.set(target, view.getResources().getInteger(itemId));
				 }else if(type.isAssignableFrom(String.class)){
					 field.set(target, view.getResources().getString(itemId));
				 }
			 }catch(Throwable e){}
		 }
		 
	}

}

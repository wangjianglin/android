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
public class ResourceIdProcessor implements FieldProcessor{

	@Override
	public void process(View view,Field field, Annotation annotation,
			Class<?> idClass) {
		ResourceId item = (ResourceId)annotation;
		field.setAccessible(true);
		
		int itemId = 0;
		 if(item.value() != 0){
			 itemId = item.value();
		 }
		 else if(!"".equals(item.id())){
			 try{
				 Field f = idClass.getDeclaredField(item.id());
				 itemId = f.getInt(null);
			 }catch(Throwable e){}
		 }else{
			 try{
				 Field f = idClass.getDeclaredField(field.getName());
				 itemId = f.getInt(null);
			 }catch(Throwable e){}
		 }
		 
		 if(itemId != 0){
			 try{
				 Class<?> type = field.getType();
				 if(type == null){
					 return;
				 }
				 if(type.isAssignableFrom(Drawable.class)){
					 field.set(view, view.getResources().getDrawable(itemId));
				 }else if(type.isAssignableFrom(int.class)){
					 try{
						 field.set(view, view.getResources().getColor(itemId));
					 }catch(Throwable e){}
					 field.set(view, view.getResources().getInteger(itemId));
				 }else if(type.isAssignableFrom(String.class)){
					 field.set(view, view.getResources().getString(itemId));
				 }
			 }catch(Throwable e){}
		 }
		 
	}

}

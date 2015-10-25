package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import android.view.View;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:22:09 PM
 *
 */
public class ViewByIdProcessor implements FieldProcessor{
//	@Override
//	protected void processFieldItem(View view, Field field,View itemView) {
//		try{
//			field.set(view,itemView);
//		}catch (Throwable e){}
//	}
//
//	@Override
//	protected int[] getIds(Annotation annotation) {
//		return ((ViewById)annotation).value();
//	}
//
//	@Override
//	protected String[] getStringIds(Annotation annotation) {
//		return ((ViewById)annotation).id();
//	}

	@Override
	public void process(View view,Field field, Annotation annotation,
			Class<?> idClass) {
		ViewById item = (ViewById)annotation;
		field.setAccessible(true);

		int viewId = 0;
		 if(item.value() != 0){
			viewId = item.value();
		 }
		 else if(!"".equals(item.id())){
			 try{
				 Field f = idClass.getDeclaredField(item.id());
				 viewId = f.getInt(null);
			 }catch(Throwable e){}
		 }else{
			 try{
				 Field f = idClass.getDeclaredField(field.getName());
				 viewId = f.getInt(null);
			 }catch(Throwable e){}
		 }

		 if(viewId != 0){
			 try{
				 field.set(view, view.findViewById(viewId));
			 }catch(Throwable e){}
		 }

	}

}

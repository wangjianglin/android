package lin.core.annotation;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
	public void process(Object target, View view, Field field, Annotation annotation,
						Package pack) {
		ViewById item = (ViewById)annotation;
		field.setAccessible(true);

		int viewId = 0;
		 if(item.value() != 0){
			viewId = item.value();
		 }
		 else if(!"".equals(item.id())){
//			 try{
//				 Field f = idClass.getDeclaredField(item.id());
//				 viewId = f.getInt(null);
//			 }catch(Throwable e){}
			 viewId = Utils.getId(pack,item.id());
		 }else{
//			 try{
//				 Field f = idClass.getDeclaredField(field.getName());
//				 viewId = f.getInt(null);
//			 }catch(Throwable e){}
			 viewId = Utils.getId(pack,field.getName());
		 }

		 if(viewId != -1){
			 try{
				 field.set(target, view.findViewById(viewId));
			 }catch(Throwable e){
				 e.printStackTrace();
			 }
		 }

	}

}

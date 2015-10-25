//package lin.core.annotation;
//
//import android.view.View;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
///**
// *
// * @author lin
// * @date Jun 14, 2015 5:20:31 PM
// *
// */
//public abstract class AbstractFieldProcessor extends AbstractProcessor implements FieldProcessor{
//
//	private Field field;
//	private View view;
//	@Override
//	final protected void processItem(int viewId) {
//		if(viewId == 0) {
//			return;
//		}
//		field.setAccessible(true);
//		processFieldItem(view, field,view.findViewById(viewId));
//	}
//
//	@Override
//	public void process(View view, Field field, Annotation annotation, Class<?> idClass) {
//		this.view = view;
//		this.field = field;
//		this.process(annotation,idClass);
//	}
//
//	@Override
//	final protected String getDefaultName() {
//		return field.getName();
//	}
//
//	protected abstract void processFieldItem(View view,Field field,View itemView);
//
////	public void process(View view, Field field, Annotation annotation, Class<?> idClass){
//////		CheckedChange item = (CheckedChange) annotation;
//////		String[] idStrings = item.id();
//////		int[] ids = item.value();
////		String[] idStrings = this.getStringIds(annotation);
////		int[] ids = this.getIds(annotation);
////		if(idStrings == null || ids == null
////				|| idStrings.length == 0 || ids.length == 0
////				||(idStrings.length == 1 && "".equals(idStrings[0]))
////				|| (ids.length == 1 && ids[0] == 0)){
////			processItem(view,method,0,idClass);
////		}
////		if(ids.length != 1 || ids[0] != 0) {
////			for (int id : ids) {
////				processItem(view, method, id, idClass);
////			}
////		}else{
////			for (String id : idStrings) {
////				processStringItem(view, method, id, idClass);
////			}
////		}
////	}
////
////	private void processStringItem(View view,Method method, String stringId,
////								   Class<?> idClass){
////
////		int viewId = 0;
////		if("".equals(stringId) || stringId == null){
////			try{
////				Field f = idClass.getDeclaredField(stringId);
////				viewId = f.getInt(null);
////			}catch(Throwable e){}
////		}else {
////			try {
////				Field f = idClass.getDeclaredField(method.getName());
////				viewId = f.getInt(null);
////			} catch (Throwable e) {
////			}
////		}
////		processItem(view, method, viewId, idClass);
////	}
////
////	protected abstract int[] getIds(Annotation annotation);
////
////	protected abstract String[] getStringIds(Annotation annotation);
//}

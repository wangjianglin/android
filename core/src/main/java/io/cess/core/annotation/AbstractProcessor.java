//package io.cess.core.annotation;
//
//import android.view.View;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//
///**
// * @author lin
// * @date 10/21/15.
// */
//public abstract class AbstractProcessor {
//
//    public void process(Annotation annotation, Class<?> idClass){
////		CheckedChange item = (CheckedChange) annotation;
////		String[] idStrings = item.id();
////		int[] ids = item.value();
//        String[] idStrings = this.getStringIds(annotation);
//        int[] ids = this.getIds(annotation);
//        if(idStrings == null || ids == null
//                || idStrings.length == 0 || ids.length == 0){
////                ||
////                ((idStrings.length == 1 && "".equals(idStrings[0]))
////                && (ids.length == 1 && ids[0] == 0))){
//            processItem(0);
//            return;
//        }
//        if(ids.length != 1 || ids[0] != 0) {
//            for (int id : ids) {
//                processItem(id);
//            }
//        }else if(idStrings.length != 1 || !"".equals(idStrings[0])){
//            for (String id : idStrings) {
//                processStringItem(id, idClass);
//            }
//        }else{
//            processItem(0);
//        }
//    }
//
//    private void processStringItem(String stringId,
//                                   Class<?> idClass){
//
//        int viewId = 0;
//        if(stringId != null && !"".equals(stringId)){
//            try{
//                Field f = idClass.getDeclaredField(stringId);
////                if(f != null) {正常情况下，都不会为null，只有当代码不误时才会为null
//                    viewId = f.getInt(null);
////                }
//            }catch(Throwable e){}
//        }else {
//            try {
//                String defaultName = this.getDefaultName();
//                if(defaultName != null && !"".equals(defaultName)) {
//                    Field f = idClass.getDeclaredField(defaultName);
////                    if(f!= null) {
//                        viewId = f.getInt(null);
////                    }
//                }
//            } catch (Throwable e) {
//            }
//        }
//        processItem(viewId);
//    }
//
//    protected abstract String getDefaultName();
//
//    protected abstract int[] getIds(Annotation annotation);
//
//    protected abstract String[] getStringIds(Annotation annotation);
//
//    protected abstract void processItem(int viewId);
//}

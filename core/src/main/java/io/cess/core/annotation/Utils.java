package io.cess.core.annotation;

import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 * @date 7/6/16.
 */
class Utils {

    static int getBindingId(Package pack,String name){

        try {
            Class<?> cls = Class.forName(pack.getName() + ".BR");
            Field f = cls.getDeclaredField(name);
            return f.getInt(null);
        } catch (Throwable e) {
        }
        return -1;
    }

    static int getId(Package pack,String name){

        try {
            Class<?> cls = Class.forName(pack.getName() + ".R$id");
            Field f = cls.getDeclaredField(name);
            return f.getInt(null);
        } catch (Throwable e) {
        }
        return -1;
    }

    static boolean validate(Class<?>[] params,Class<?> ... cls){
        if(params == null || params.length == 0){
            return true;
        }
        if(cls == null || cls.length == 0
                || params.length > cls.length){
            return false;
        }
        boolean[] use = new boolean[cls.length];
        for(Class<?> param : params){
            if(!validateItem(param,cls,use)){
                return false;
            }
        }
        return true;
    }

    private static boolean validateItem(Class<?> param,Class<?>[] cls,boolean[] use){
        for(int n=0;n<cls.length;n++){
            if(use[n] == false &&
                    (param.isAssignableFrom(cls[n]) || numberTypeCompare(param,cls[n]))){
                use[n] = true;
                return true;
            }
        }
        return false;
    }

    static Object[] args(Class<?>[] params,Object ... objs){

        if(params == null){
            return null;
        }
        List<Object> argsResult = new ArrayList<Object>();
        Class<?> param = null;
        boolean[] use = null;
        if(objs != null){
            use = new boolean[objs.length];
        }
        for(int n=0;n<params.length;n++){
            param = params[n];
            if(param == null
                    || objs == null
                    || objs.length <= n){
                argsResult.add(null);
                continue;
            }
            argsResult.add(getObj(objs,param,use));
        }

        return argsResult.toArray();
    }

    private static Object getObj(Object[] objs,Class<?> cls,boolean[] use){
        for(int n=0;n<objs.length;n++){
            if(objs[n] == null){
                continue;
            }
            if(use[n] == false &&
                    cls.isAssignableFrom(objs[n].getClass()) || numberTypeCompare(cls,objs[n].getClass())){
                use[n] = true;
                return objs[n];
            }
        }
        return null;
    }

    private static boolean numberTypeCompare(Class<?> cls,Class<?> oCls){
        if(cls == oCls){
            return true;
        }
        if(cls == int.class && oCls == Integer.class){
            return true;
        }

        if(cls == long.class && oCls == Long.class){
            return true;
        }

        if(cls == byte.class && oCls == Byte.class){
            return true;
        }

        if(cls == short.class && oCls == Short.class){
            return true;
        }

        if(cls == float.class && oCls == Float.class){
            return true;
        }

        if(cls == double.class && oCls == Double.class){
            return true;
        }

        if(cls == Integer.class && oCls == int.class){
            return true;
        }

        if(cls == Long.class && oCls == long.class){
            return true;
        }

        if(cls == Byte.class && oCls == byte.class){
            return true;
        }

        if(cls == Short.class && oCls == short.class){
            return true;
        }

        if(cls == Float.class && oCls == float.class){
            return true;
        }

        if(cls == Double.class && oCls == double.class){
            return true;
        }

        return false;
    }

}

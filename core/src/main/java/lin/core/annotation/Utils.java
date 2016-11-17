package lin.core.annotation;

import java.lang.reflect.Field;

/**
 * Created by lin on 7/6/16.
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


}

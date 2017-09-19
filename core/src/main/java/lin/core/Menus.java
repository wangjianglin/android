package lin.core;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lin.core.annotation.ResCls;

/**
 * Created by lin on 01/03/2017.
 */

public class Menus {


    public static void onCreateOptionsMenu(Activity holder, Menu menu, MenuInflater inflater,int menuId) {
        onCreateOptionsMenuImpl(holder,holder,menu,inflater,menuId);
    }


    public static void onCreateOptionsMenu(android.support.v4.app.Fragment holder, Menu menu, MenuInflater inflater,int menuId) {
        onCreateOptionsMenuImpl(holder,holder.getContext(),menu,inflater,menuId);
    }

    private static void onCreateOptionsMenuImpl(Object holder,Context context,Menu menu, MenuInflater inflater,int menuId) {

        lin.core.annotation.MenuId menuAnnot = holder.getClass().getAnnotation(lin.core.annotation.MenuId.class);
        if(menuAnnot == null && menuId < 0) {
            return;
        }
        if(menuAnnot != null) {
            processClsMenuAnnot(holder.getClass(), context, menuAnnot, menu, inflater);
        }

        if(menuId > 0){
            inflater.inflate(menuId,menu);
        }

        Method[] ms = holder.getClass().getDeclaredMethods();
        Map<Integer,Method> map = getMenuItemMaps(holder);
        for(Method m : ms){
            menuAnnot = m.getAnnotation(lin.core.annotation.MenuId.class);
            if(menuAnnot == null){
                continue;
            }
            processMethodMenuAnnot(holder.getClass(),context,map,m,menuAnnot,menu,inflater);
        }
    }

//    private static Map<Integer,Method> menuItemMaps = new HashMap<>();
    private static Map<WeakReference<Object>,Map<Integer,Method>> menuObjectMaps = new HashMap<>();

    private static Map<Integer,Method> getMenuItemMaps(Object holder){
        List<WeakReference<Object>> keys = new ArrayList<>();
        Map<Integer,Method> map = null;
        for(Map.Entry<WeakReference<Object>,Map<Integer,Method>> entry : menuObjectMaps.entrySet()){
            if(entry.getKey().get() == null){
                keys.add(entry.getKey());
                continue;
            }
            if(entry.getKey().get() == holder){
                map = entry.getValue();
            }
        }
        for(WeakReference<Object> key : keys) {
            menuObjectMaps.remove(key);
        }
        if(map != null){
            return map;
        }
        map = new HashMap<Integer,Method>();
        menuObjectMaps.put(new WeakReference<Object>(holder),map);
        return map;
    }

    private static void processMethodMenuAnnot(Class<?> cls, Context context,Map<Integer,Method> map,Method method,lin.core.annotation.MenuId menuAnnot,Menu menu, MenuInflater inflater){

        for(int id : menuAnnotIds(cls,context,menuAnnot,"$id")){
            map.put(id,method);
        }
    }

    private static void processClsMenuAnnot(Class<?> cls, Context context,lin.core.annotation.MenuId menuAnnot,Menu menu, MenuInflater inflater){
        for(int id : menuAnnotIds(cls,context,menuAnnot,"$menu")){
            menu.removeGroup(id);
            menu.removeItem(id);
            inflater.inflate(id,menu);
        }
    }

    private static int[] menuAnnotIds(Class<?> cls, Context context,lin.core.annotation.MenuId menuAnnot, String clsSuff){
        int[] idsValue = menuAnnot.value();
        if(idsValue != null &&
                (idsValue.length > 1 || idsValue[0] != 0)){
            return menuAnnot.value();
        }
        List<Integer> ids = new ArrayList<>();
        if(menuAnnot.id() != null && menuAnnot.id().length > 0){
            ResCls resCls = cls.getAnnotation(ResCls.class);
            Class<?> rCls = null;
            if(resCls == null){
                try {
                    rCls = Class.forName(context.getPackageName()+".R");
                } catch (ClassNotFoundException e) {
                }
            }else{
                rCls = resCls.value();
            }
            if(rCls != null){
                Class<?> mCls = null;
                try {
                    mCls = Class.forName(rCls.getName() + clsSuff);
                } catch (ClassNotFoundException e) {
                }
                if(mCls != null) {
                    for (String id : menuAnnot.id()) {
                        try {
                            Field f = mCls.getDeclaredField(id);
                            ids.add(f.getInt(null));
                        }catch (Throwable e){}
                    }
                }
            }
        }
        int[] tids = new int[ids.size()];
        for(int n=0;n<ids.size();n++){
            tids[n] = ids.get(n);
        }
        return tids;
    }

    public static boolean onOptionsItemSelected(Object holder,MenuItem item){
        Map<Integer,Method> map = getMenuItemMaps(holder);
        Method m = map.get(item.getItemId());
        if(m == null) {
            return false;
        }
        Object r = null;
        m.setAccessible(true);
        if(m.getParameterTypes() != null && m.getParameterTypes().length == 1){
            try {
                r = m.invoke(holder,item);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                r = m.invoke(holder);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        if(r == null){
            return true;
        }
        return Boolean.TRUE.equals(r);
    }
}

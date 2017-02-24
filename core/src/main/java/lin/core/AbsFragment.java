package lin.core;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lin.core.annotation.OptionsMenu;
import lin.core.annotation.ResCls;

/**
 * Created by lin on 23/02/2017.
 */

public abstract class AbsFragment extends android.support.v4.app.Fragment {

//    int[] value() default 0;
//    String[] id() default "";
    public AbsFragment(){
        this.setHasOptionsMenu(this.defaultOptionsMenu());
    }

    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return LayoutInflaterFactory.setFactory2(super.getLayoutInflater(savedInstanceState));
    }

    protected boolean defaultOptionsMenu(){
        OptionsMenu optionsMenu = this.getClass().getAnnotation(OptionsMenu.class);
        if(optionsMenu != null){
            return optionsMenu.value();
        }
        return this.hasOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        lin.core.annotation.MenuId menuAnnot = this.getClass().getAnnotation(lin.core.annotation.MenuId.class);
        if(menuAnnot == null) {
            return;
        }

        processClsMenuAnnot(menuAnnot,menu,inflater);

        Method[] ms = this.getClass().getDeclaredMethods();
        for(Method m : ms){
            menuAnnot = m.getAnnotation(lin.core.annotation.MenuId.class);
            if(menuAnnot == null){
                continue;
            }
            processMethodMenuAnnot(m,menuAnnot,menu,inflater);
        }
    }

    private Map<Integer,Method> menuItemMaps = new HashMap<>();
    private void processMethodMenuAnnot(Method method,lin.core.annotation.MenuId menuAnnot,Menu menu, MenuInflater inflater){
        for(int id : menuAnnotIds(menuAnnot,"$id")){
            menuItemMaps.put(id,method);
        }
    }

    private void processClsMenuAnnot(lin.core.annotation.MenuId menuAnnot,Menu menu, MenuInflater inflater){
        for(int id : menuAnnotIds(menuAnnot,"$menu")){
            inflater.inflate(id,menu);
        }
    }

    private int[] menuAnnotIds(lin.core.annotation.MenuId menuAnnot,String clsSuff){
        if(menuAnnot.value() != null && menuAnnot.value().length > 0){
            return menuAnnot.value();
        }
        List<Integer> ids = new ArrayList<>();
        if(menuAnnot.id() != null && menuAnnot.value().length > 0){
            ResCls resCls = this.getClass().getAnnotation(ResCls.class);
            Class<?> rCls = null;
            if(resCls == null){
                try {
                    rCls = Class.forName(this.getContext().getPackageName()+".R");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Method m = menuItemMaps.get(item.getItemId());
        if(m != null){
            Object r = null;
            m.setAccessible(true);
            if(m.getParameterTypes() != null && m.getParameterTypes().length == 1){
                try {
                    r = m.invoke(this,item);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }else{
                try {
                    r = m.invoke(this);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            if(r == null){
                return true;
            }
            if(Boolean.TRUE.equals(r)){
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract View getView();

    protected void onCreateView(){}
}

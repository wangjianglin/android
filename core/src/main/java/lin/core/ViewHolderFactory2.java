package lin.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

/**
 * Created by lin on 01/01/2017.
 */

class ViewHolderFactory2 implements LayoutInflater.Factory2 {
    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        Class<?> cls = null;
        try{
            cls = Class.forName(name);
        }catch (Throwable e){}
        if(cls == null){
            return null;
        }
        if(!ViewHolder.class.isAssignableFrom(cls)) {
            return null;
        }
        try {
            Constructor<?> constructor = cls.getConstructor(Context.class, AttributeSet.class);
            ViewHolder viewHolder = null;
            if(constructor != null){
                viewHolder = (ViewHolder) constructor.newInstance(context,attrs);
            }else{
                constructor = cls.getConstructor(Context.class);
                viewHolder = (ViewHolder) constructor.newInstance(context);
            }
            if(parent instanceof ViewGroup){
                return viewHolder.getView((ViewGroup) parent);
            }else {
                return viewHolder.getView(null);
            }
        }catch (Throwable e){}
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }
}

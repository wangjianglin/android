package lin.core.tabbar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;

import lin.core.ContentView;
import lin.core.LayoutInflaterFactory;
import lin.core.TabBar;
import lin.core.ViewHolder;


/**
 * Created by lin on 30/12/2016.
 */

public class TabbarItemFactory2 extends LayoutInflaterFactory.AbsFactory2 {

    @Override
    protected String tag() {
        return "tabbarItem";
    }

    @Override
    protected View createView(View parent, Context context, AttributeSet attributeSet) {
        String className = attributeSet.getAttributeValue(null, "class");
        View view = null;
        if (className == null || "".equals(className)) {
            view = new TabBar.ClickItem(context,attributeSet);
            return view;
        }
        try {
            Class<?> cls = Class.forName(className);
            if (Fragment.class.isAssignableFrom(cls)) {
                Fragment fragment = (Fragment) cls.newInstance();
                view = new ViewImpl(context, attributeSet);
                view.setTag(fragment);
            } else if (View.class.isAssignableFrom(cls)) {
                Constructor<?> constructor = getConstructor(cls, Context.class, AttributeSet.class);
                if (constructor == null) {
                    constructor = getConstructor(cls, Context.class);
                    view = (View) constructor.newInstance(context);
                } else {
                    view = (View) constructor.newInstance(context, attributeSet);
                }
            } else if (ViewHolder.class.isAssignableFrom(cls)) {
                ViewHolder viewHolder = null;
                Constructor<?> constructor = getConstructor(cls, Context.class, AttributeSet.class);
                if (constructor == null) {
                    constructor = getConstructor(cls, Context.class);
                    viewHolder = (ViewHolder) constructor.newInstance(context);
                } else {
                    viewHolder = (ViewHolder) constructor.newInstance(context, attributeSet);
                }

                if (parent instanceof ViewGroup) {
                    view = viewHolder.getView((ViewGroup) parent);
                } else {
                    view = viewHolder.getView(null);
                }
            }
            return view;
        } catch (Throwable e) {

        }
//        TabBar tabBar = new TabBar(context,attributeSet);
//        return tabBar;
        return null;
    }

    private class ViewImpl extends ContentView {
        public ViewImpl(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }


    private Constructor<?> getConstructor(Class<?> cls, Class<?>... parameterTypes) {
        try {
            return cls.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
        }
        return null;
    }
}

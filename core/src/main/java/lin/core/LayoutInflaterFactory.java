package lin.core;

import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lin.core.form.FormFactory2;
import lin.core.form.RowFactory2;
import lin.core.form.SectionFactory2;
import lin.core.form.SegueFactory2;
import lin.core.tabbar.TabbarFactory2;
import lin.core.tabbar.TabbarItemFactory2;

/**
 * Created by lin on 30/12/2016.
 */

public class LayoutInflaterFactory {

    private volatile static LayoutInflater.Factory2 factory2 = null;
    private static Lock lock = new ReentrantLock();
//    private static Map<SoftReference<Context>,LayoutInflater> inflaters = new HashMap<>();
    private static List<SoftReference<LayoutInflater>> inflaters = new ArrayList<>();

    public static LayoutInflater from(Context context){

        LayoutInflater result = null;
        lock.lock();
        List<SoftReference<LayoutInflater>> removes = new ArrayList<>();
        for(SoftReference<LayoutInflater> item : inflaters){
            LayoutInflater inflater = item.get();
            if(inflater == null){
                removes.add(item);
                continue;
            }else if(inflater.getContext() == context){
                result = inflater;
                break;
            }
        }
        inflaters.removeAll(removes);

        if(result == null){
            result = setFactory2(context);
        }
        lock.unlock();
        return result;
    }

    public static LayoutInflater setFactory2(LayoutInflater inflater){
        if(inflater == null){
            return inflater;
        }
        LayoutInflater.Factory2 factory2 = inflater.getFactory2();
        if(factory2 instanceof LayoutInflaterFactory2Impl){
            return inflater;
        }

        lock.lock();

        if(factory2 instanceof LayoutInflaterFactory2Impl){
            return inflater;
        }

        if(factory2 == null){
            inflater.setFactory2(new LayoutInflaterFactory2Impl(null));
        }else{
            inflater = new LayoutInflaterImpl(inflater);
            inflaters.add(new SoftReference<LayoutInflater>(inflater));
        }

        lock.unlock();
        return inflater;
    }

    private static LayoutInflater setFactory2(Context context) {
        return setFactory2(LayoutInflater.from(context));
    }

    private volatile static List<LayoutInflater.Factory2> factory2s = new ArrayList<LayoutInflater.Factory2>();

    public static void addFactory2(LayoutInflater.Factory2 factory2){
        if(factory2 == null){
            return;
        }
        factory2s.add(factory2);
    }

    public static void removeFactory2(LayoutInflater.Factory2 factory2){
        if(factory2 == null){
            return;
        }
        factory2s.remove(factory2);
    }

    private static class LayoutInflaterFactory2Impl implements LayoutInflater.Factory2 {
        private LayoutInflater.Factory2 factory2 = null;
        LayoutInflaterFactory2Impl(LayoutInflater.Factory2 factory2){
            this.factory2 = factory2;
        }
        @Override
        public View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
            View r = null;
            for (LayoutInflater.Factory2 factory2: LayoutInflaterFactory.factory2s) {
                r = factory2.onCreateView(view,s,context,attributeSet);
                if(r != null){
                    return r;
                }
            }
            if(factory2 != null){
                return factory2.onCreateView(view,s,context,attributeSet);
            }
            return null;
        }

        @Override
        public View onCreateView(String s, Context context, AttributeSet attributeSet) {
            View r = null;
            for (LayoutInflater.Factory2 factory2: LayoutInflaterFactory.factory2s) {
                r = factory2.onCreateView(s,context,attributeSet);
                if(r != null){
                    return r;
                }
            }
            if(factory2 != null){
                return factory2.onCreateView(s,context,attributeSet);
            }
            return null;
        }
    }

    private static class LayoutInflaterImpl extends LayoutInflater {

        private static final String[] sClassPrefixList = {
            "android.widget.",
            "android.webkit.",
            "android.app."
        };

        private LayoutInflater inflater;

        protected LayoutInflaterImpl(LayoutInflater inflater) {
            super(inflater.getContext());
            this.inflater = inflater;

            LayoutInflaterFactory2Impl factory = new LayoutInflaterFactory2Impl(inflater.getFactory2());

            this.setFactory2(factory);
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new LayoutInflaterImpl(inflater);
        }

        @Override
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    // In this case we want to let the base class take a crack
                    // at it.
                }
            }

            return super.onCreateView(name, attrs);
        }
    }

    public static abstract class AbsFactory2 implements LayoutInflater.Factory2{

        protected abstract String tag();

        private boolean isSupport(String tag){
            if(tag == null){
                return false;
            }
            return tag.endsWith(tag());
        }

        @Override
        public final View onCreateView(View view, String s, Context context, AttributeSet attributeSet) {
            if(isSupport(s)){
                return this.createView(view,context,attributeSet);
            }
            return null;
        }

        @Override
        public final View onCreateView(String s, Context context, AttributeSet attributeSet) {
            if(isSupport(s)){
                return this.createView(null,context,attributeSet);
            }
            return null;
        }

        protected abstract View createView(View parent,Context context,AttributeSet attributeSet);
    }

    static {
        addFactory2(new TabbarFactory2());
        addFactory2(new TabbarItemFactory2());
        addFactory2(new ViewHolderFactory2());
        addFactory2(new FormFactory2());
        addFactory2(new SegueFactory2());
        addFactory2(new SectionFactory2());
        addFactory2(new RowFactory2());
    }
}



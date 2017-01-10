package lin.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by lin on 30/12/2016.
 */

//处理注解
public class Fragment extends android.support.v4.app.Fragment {


    private int resId = 0;
    public Fragment(){

    }

    protected Fragment(int resId){
        this.resId = resId;
    }
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return LayoutInflaterFactory.setFactory2(super.getLayoutInflater(savedInstanceState));
    }

    private View mView;
    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(resId != 0){
            mView = inflater.inflate(resId,container,false);
        }else {
            mView = inflater.inflate(Views.layoutId(this), container, false);
        }
        Views.process(this);
        this.onCreateView();
        return mView;
    }

    @Nullable
    @Override
    public View getView() {
        return mView;
    }

    protected void onCreateView(){}

//    private class LayoutInflaterImpl extends LayoutInflater{
//
//        private static final String[] sClassPrefixList = {
//                "android.widget.",
//                "android.webkit.",
//                "android.app."
//        };
//
//        private LayoutInflater inflater;
//        protected LayoutInflaterImpl(LayoutInflater inflater) {
//            super(inflater.getContext());
//            this.inflater = inflater;
//
//            LayoutInflaterFactory factory = new LayoutInflaterFactory();
//        }
//
//        @Override
//        public LayoutInflater cloneInContext(Context newContext) {
//            return new LayoutInflaterImpl(inflater);
//        }
//
//        @Override
//        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
//            for (String prefix : sClassPrefixList) {
//                try {
//                    View view = createView(name, prefix, attrs);
//                    if (view != null) {
//                        return view;
//                    }
//                } catch (ClassNotFoundException e) {
//                    // In this case we want to let the base class take a crack
//                    // at it.
//                }
//            }
//
//            return super.onCreateView(name, attrs);
//        }
////        protected LayoutInflaterImpl(Context context) {
////            super(context);
////        }
////
////        @Override
////        public LayoutInflater cloneInContext(Context newContext) {
////            return null;
////        }
////        private LayoutInflater inflater;
////        protected LayoutInflaterImpl(LayoutInflater inflater) {
////            super(inflater.getContext());
////            this.inflater = inflater;
////        }
//////        protected LayoutInflaterImpl(Context context) {
//////            super(context);
//////        }
//////
//////        protected LayoutInflaterImpl(LayoutInflater original, Context newContext) {
//////            super(original, newContext);
//////        }
////
////        @Override
////        public Context getContext() {
////            return inflater.getContext();
////        }
////
////        @Override
////        public void setFactory(Factory factory) {
////            inflater.setFactory(factory);
////        }
////
////        @Override
////        public void setFactory2(Factory2 factory) {
////            inflater.setFactory2(factory);
////        }
////
////        @Override
////        public Filter getFilter() {
////            return super.getFilter();
////        }
////
////        @Override
////        public void setFilter(Filter filter) {
////            inflater.setFilter(filter);
////        }
////
////        @Override
////        public View inflate(int resource, ViewGroup root) {
////            return inflater.inflate(resource, root);
////        }
////
////        @Override
////        public View inflate(XmlPullParser parser, ViewGroup root) {
////            return inflater.inflate(parser, root);
////        }
////
////        @Override
////        public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
////            return inflater.inflate(resource, root, attachToRoot);
////        }
////
////        @Override
////        public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
////            return inflater.inflate(parser, root, attachToRoot);
////        }
////
////        @Override
////        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
////            return inflater.onCreateView(name, attrs);
////        }
////
////        @Override
////        protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
////            return inflater.onCreateView(parent, name, attrs);
////        }
////
////        @Override
////        public LayoutInflater cloneInContext(Context newContext) {
////            return inflater.cloneInContext(newContext);
////        }
//    }
}

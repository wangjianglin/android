package lin.core.mvvm;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import java.lang.reflect.Constructor;

/**
 * Created by lin on 09/02/2017.
 */

public class Utils {

    public static Object loadView(Activity activity){
        try {
            ViewCls clsAnnot = activity.getClass().getAnnotation(ViewCls.class);

            if (clsAnnot == null) {
                return null;
            }

            Class<?> cls = clsAnnot.value();

            if (android.view.View.class.isAssignableFrom(cls)) {
                Constructor<?> constructor = getConstructor(cls,Context.class);
                if(constructor != null) {
                    return (android.view.View) constructor.newInstance(activity);
                }
                constructor = getConstructor(cls,Context.class, AttributeSet.class);
                if(constructor != null){
                    return (android.view.View) constructor.newInstance(activity,null);
                }

                constructor = getConstructor(cls,Context.class,AttributeSet.class,int.class);
                return (android.view.View) constructor.newInstance(activity,null,0);
            }else if(android.support.v4.app.Fragment.class.isAssignableFrom(cls)){
                return cls.getConstructor().newInstance();
            }
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
        return null;
    }
    private static Constructor<?> getConstructor(Class<?> cls,Class<?> ... types){
        try{
            return cls.getConstructor(types);
        }catch (NoSuchMethodException e) {
        }
        return null;
    }
    public static void processViewModel(Object obj){
        try {

            Class<?> cls = obj.getClass();
            View viewAnnot = cls.getAnnotation(View.class);
            if (viewAnnot == null && !(obj instanceof BaseView)) {
                return;
            }
            BaseView mView = (BaseView) obj;
            BasePresenter presenter = null;
            Presenter presenterAnnot = cls.getAnnotation(Presenter.class);
            if (presenterAnnot != null) {
                presenter = presenterAnnot.value().newInstance();
                presenter.setView(mView);
                mView.setPresenter(presenter);
            }

            Handler handler = cls.getAnnotation(Handler.class);
            if (handler != null) {
                ActionHandler actionHandler = (ActionHandler) obj;
                actionHandler.setPresenter(presenter);
            }

            ViewModel viewModelAnnot = cls.getAnnotation(ViewModel.class);
            if (viewModelAnnot != null) {
                BaseViewModel viewModel = viewModelAnnot.value().newInstance();
                if (presenter instanceof BaseViewModelPresenter) {
                    ((BaseViewModelPresenter) presenter).setViewModel(viewModel);
                }
                if (mView instanceof BaseViewModelView) {
                    ((BaseViewModelView) mView).setViewModel(viewModel);
                }
            }
        }catch (Throwable e){
            throw new RuntimeException(e);
        }
    }
}

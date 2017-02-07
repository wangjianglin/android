package lin.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.core.annotation.OptionsMenu;


/**
 * Created by lin on 30/12/2016.
 */

//处理注解
public class ResFragment extends android.support.v4.app.Fragment {


    private int mResId = 0;
    private boolean mHasOptionsMenu = false;
    public ResFragment(){
        this.setHasOptionsMenu(defaultOptionsMenu());
    }


    protected boolean defaultOptionsMenu(){
        OptionsMenu optionsMenu = this.getClass().getAnnotation(OptionsMenu.class);
        if(optionsMenu != null){
            return optionsMenu.value();
        }
        return this.hasOptionsMenu();
    }

    protected ResFragment(int resId){
        this.setHasOptionsMenu(defaultOptionsMenu());
        this.mResId = resId;
    }
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return LayoutInflaterFactory.setFactory2(super.getLayoutInflater(savedInstanceState));
    }

    private View mView;
    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mResId != 0){
            mView = inflater.inflate(mResId,container,false);
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
}

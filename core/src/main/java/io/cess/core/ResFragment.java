package io.cess.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.cess.core.annotation.OptionsMenu;


/**
 * Created by lin on 30/12/2016.
 */

//处理注解
public class ResFragment extends AbsFragment {


    private int mResId = 0;

    public ResFragment(){
    }

    protected ResFragment(int resId){
        this.mResId = resId;
    }
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        return LayoutInflaterFactory.setFactory2(super.getLayoutInflater(savedInstanceState));
    }

    @Nullable
    @Override
    final public View onCreateViewInternal(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView;
        if(mResId != 0){
            mView = inflater.inflate(mResId,container,false);
        }else {
            mView = inflater.inflate(Views.layoutId(this), container, false);
        }
        return mView;
    }

}

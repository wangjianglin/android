package lin.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import lin.core.annotation.OptionsMenu;
import lin.core.annotation.ToolsResId;

/**
 * Created by lin on 23/02/2017.
 */

public abstract class AbsFragment extends android.support.v4.app.Fragment {

//    int[] value() default 0;
//    String[] id() default "";
    private View mView;
    public AbsFragment(){
        this.setHasOptionsMenu(this.defaultOptionsMenu());
    }

    private LayoutInflater mLayoutInflater = null;
    public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
        mLayoutInflater = LayoutInflaterFactory.setFactory2(super.getLayoutInflater(savedInstanceState));
        return mLayoutInflater;
    }

    protected boolean defaultOptionsMenu(){
        OptionsMenu optionsMenu = this.getClass().getAnnotation(OptionsMenu.class);
        if(optionsMenu != null){
            return optionsMenu.value();
        }
        return this.hasOptionsMenu();
    }

    protected View mToolsView = null;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ToolsResId toolsRes = this.getClass().getAnnotation(ToolsResId.class);

        if(!(context instanceof ViewActivity)){
            return;
        }
        ViewActivity act = (ViewActivity) context;
        if(act.toolsLayout == null){
            return;
        }
        mToolsView = onCreateToolsViewInternal(mLayoutInflater,act.toolsLayout);
        if(mToolsView != null){
            Views.process(this,mToolsView);
        }
    }

    protected View onCreateToolsViewInternal(LayoutInflater inflater, @Nullable ViewGroup container){

        return Views.loadToolsView(this,this.getContext(),container,false);

    }


    //    private View mView;
    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if(mView != null){
//            this.onCreateView();
//            return mView;
//        }

        mView = this.onCreateViewInternal(inflater,container,savedInstanceState);

        Views.process(this,mView);
        lin.core.mvvm.Utils.processViewModel(this);

        this.onCreateView();
        return mView;
    }

    protected abstract View onCreateViewInternal(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        Menus.onCreateOptionsMenu(this,menu,inflater,getMenuId());
    }

    protected int getMenuId(){
        return 0;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(Menus.onOptionsItemSelected(this,item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View getView(){
        return mView;
    }

    protected void onCreateView(){}
}

package lin.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import lin.core.annotation.OptionsMenu;

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

//    private View mView;
    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if(mView != null){
//            this.onCreateView();
//            return mView;
//        }
        View mView = this.onCreateViewInternal(inflater,container,savedInstanceState);
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

    public abstract View getView();

    protected void onCreateView(){}
}

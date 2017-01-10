package lin.demo.tabbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import lin.core.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.core.annotation.ResId;
import lin.demo.R;

/**
 * Created by lin on 30/12/2016.
 */

@ResId(R.layout.activity_tabbar_tabbar_item)
public class Tab1 extends Fragment {

    public Tab1(){
        this.setHasOptionsMenu(true);
    }
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //return super.onCreateView(inflater, container, savedInstanceState);
//        setHasOptionsMenu(true);
//        return inflater.inflate(R.layout.activity_tabbar_tabbar_item, container, false);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

    @Override
    public void onPause() {
        System.out.println("pause...");
        super.onPause();
    }

    @Override
    public void onResume() {
        System.out.println("resume...");
        super.onResume();
    }

//    @Override
//    public void onAttachFragment(Fragment childFragment) {
//        super.onAttachFragment(childFragment);
//        System.out.println("on attach fragment");
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        System.out.println("on detach");
    }
}

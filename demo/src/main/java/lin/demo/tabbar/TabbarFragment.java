package lin.demo.tabbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import lin.core.Fragment;
import lin.core.annotation.ResId;
import lin.demo.R;

/**
 * Created by lin on 28/12/2016.
 */

@ResId(R.layout.activity_tabbar_frag)
public class TabbarFragment extends Fragment {// implements MainContract.View {

//    private MainContract.Presenter mPresenter;
//    private MainFragment.SimpleRecyclerAdapter mAdapter;

    public TabbarFragment() {
        // Requires empty public constructor
        setHasOptionsMenu(true);
    }

    public static TabbarFragment newInstance() {
        return new TabbarFragment();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View root = inflater.inflate(R.layout.activity_tabbar_frag, container, false);
//
//
//
//        setHasOptionsMenu(true);
//
//        return root;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.menu_clear:
//////                mPresenter.clearCompletedTasks();
////                break;
//            case R.id.menu_filter:
//                showFilteringPopUpMenu();
//                break;
//            case R.id.menu_refresh:
//////                mPresenter.loadTasks(true);
//                mPresenter.loadData();
//                break;
//        }
//        return true;
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }

}
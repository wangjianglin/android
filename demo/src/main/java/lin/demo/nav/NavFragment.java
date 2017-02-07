package lin.demo.nav;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import lin.core.annotation.NavTitle;
import lin.core.annotation.ResId;
import lin.demo.R;

/**
 * Created by lin on 05/01/2017.
 */

@ResId(R.layout.activity_nav_frag)
@NavTitle("nav frag")
public class NavFragment  extends lin.core.ResFragment{

    public NavFragment() {
        // Requires empty public constructor
        setHasOptionsMenu(true);
    }

    public static NavFragment newInstance() {
        return new NavFragment();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View root = inflater.inflate(R.layout.activity_nav_frag, container, false);
//
//
//
//
//
//        return root;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_clear:
////                mPresenter.clearCompletedTasks();
//                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
////                mPresenter.loadTasks(true);
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
    }


    private void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.active:
//                        mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
//                        mPresenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                        break;
                    default:
//                        mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                }
//                mPresenter.loadTasks(false);
                return true;
            }
        });

        popup.show();
    }


    private void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

}
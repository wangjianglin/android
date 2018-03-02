package io.cess.demo.tabbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import io.cess.core.ResFragment;
import io.cess.core.TabBarItem;
import io.cess.core.annotation.ResId;
import io.cess.demo.R;
import io.cess.demo.plugin.LinWeixinPlugin;

/**
 * Created by lin on 28/12/2016.
 */

@ResId(R.layout.activity_tabbar_frag)
public class TabbarFragment extends ResFragment {// implements MainContract.View {

//    private MainContract.Presenter mPresenter;
//    private MainFragment.SimpleRecyclerAdapter mAdapter;

    public TabbarFragment() {
        // Requires empty public constructor
        setHasOptionsMenu(true);
    }

    public static TabbarFragment newInstance() {
        return new TabbarFragment();
    }
    @Override
    protected void onCreateView() {
        TabBarItem item = (TabBarItem) this.getView().findViewById(R.id.tab_shouye_two);
        if (item != null) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareWxStation();
                }
            });
        }
    }
    private void shareWxStation() {
        LinWeixinPlugin plugin = new LinWeixinPlugin(this.getContext());
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("link", "http://ws.feicuibaba.com/?priceRate=undefined&userLevel=99&from=singlemessage");
        args.put("desc", "保证A货，假一罚十！工厂货源，比市场价至少便宜50%，全场包邮。");
        args.put("scene", "friend");
        args.put("title", "翡翠货源平台");
//        args.put("thumb", text);
        plugin.shareLink(args);
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
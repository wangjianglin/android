package lin.demo.main;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import lin.core.mvvm.AbsBasePresenter;
import lin.demo.WebViewActivity;
import lin.demo.binding.BindViewActivity;
import lin.demo.binding.BindViewHolderActivity;
import lin.demo.controls.ReViewActivity;
import lin.demo.controls.SwipeActivity;
import lin.demo.controls.SwitchButtonActivity;
import lin.demo.controls.ViewPagerActivity;
import lin.demo.core.ViewHolderActivity;
import lin.demo.core.ViewInheritActivity;
import lin.demo.form.FormActivity;
import lin.demo.nav.NavActivity;
import lin.demo.ptr.PtrDefaultActivity;
import lin.demo.ptr.PtrListActivity;
import lin.demo.ptr.PtrRecyclerActivity;
import lin.demo.ptr.PtrScrollActivity;
import lin.demo.tabbar.TabbarActivity;

/**
 * Created by lin on 23/11/2016.
 */

public class MainPresenter extends AbsBasePresenter<MainContract.View> implements MainContract.Presenter{

    private Object[][] items = null;

    public MainPresenter(){
        System.out.println();
    }
    @Override
    public void start() {
        this.loadData();
    }


    private List<Data> mDatas = new ArrayList<>();


    @Override
    public void loadData() {

        mDatas.clear();

        items = new Object[][]{
                new Object[]{"core","view inherit",new Intent(mView.getContext(),ViewInheritActivity.class)},
                new Object[]{"core","view holder",new Intent(mView.getContext(),ViewHolderActivity.class)},

                new Object[]{"controls","tabbar",new Intent(mView.getContext(),TabbarActivity.class)},
                new Object[]{"controls","nav",new Intent(mView.getContext(),NavActivity.class)},
                new Object[]{"controls","swipe",new Intent(mView.getContext(),SwipeActivity.class)},
                new Object[]{"controls","review",new Intent(mView.getContext(),ReViewActivity.class)},
                new Object[]{"controls","view pager",new Intent(mView.getContext(),ViewPagerActivity.class)},
                new Object[]{"controls","switch button",new Intent(mView.getContext(),SwitchButtonActivity.class)},

                //SwipeRefreshLayout 官方

                new Object[]{"binding","bind view",new Intent(mView.getContext(),BindViewActivity.class)},
                new Object[]{"binding","bind view holde",new Intent(mView.getContext(),BindViewHolderActivity.class)},


                new Object[]{"pull to refresh","ptr default",new Intent(mView.getContext(),PtrDefaultActivity.class)},
                new Object[]{"pull to refresh","ptr list",new Intent(mView.getContext(),PtrListActivity.class)},
                new Object[]{"pull to refresh","ptr recycler",new Intent(mView.getContext(),PtrRecyclerActivity.class)},
                new Object[]{"pull to refresh","ptr scroll",new Intent(mView.getContext(),PtrScrollActivity.class)},


                new Object[]{"form","classic",new Intent(mView.getContext(),FormActivity.class)},
                new Object[]{"event bus","classic",new Intent(mView.getContext(),FormActivity.class)},
                new Object[]{"web view","crosss walk",new Intent(mView.getContext(),WebViewActivity.class)}
        };

        Data data = null;
        for(Object[] item : items){
            data = new Data();
            data.setGroup((String) item[0]);
            data.setName((String) item[1]);
            data.setIntent((Intent) item[2]);
            mDatas.add(data);
        }

        mView.show(mDatas);
    }
}

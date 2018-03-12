package io.cess.demo.main;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import io.cess.core.Nav;
import io.cess.core.mvvm.AbsBasePresenter;
import io.cess.demo.ControlsActivity;
import io.cess.demo.ImagesActivity;
import io.cess.demo.WebViewActivity;
import io.cess.demo.binding.BindViewActivity;
import io.cess.demo.binding.BindViewHolderActivity;
import io.cess.demo.controls.ReViewActivity;
import io.cess.demo.controls.SwipeActivity;
import io.cess.demo.controls.SwitchButtonActivity;
import io.cess.demo.controls.ViewPagerActivity;
import io.cess.demo.core.ViewHolderActivity;
import io.cess.demo.core.ViewInheritActivity;
import io.cess.demo.form.FormActivity;
import io.cess.demo.gallery.GalleryActivity;
import io.cess.demo.http.HttpActivity;
import io.cess.demo.nav.NavActivity;
import io.cess.demo.ptr.PtrDefaultActivity;
import io.cess.demo.ptr.PtrListActivity;
import io.cess.demo.ptr.PtrRecyclerActivity;
import io.cess.demo.ptr.PtrScrollActivity;
import io.cess.demo.tabbar.TabbarActivity;

/**
 * @author lin
 * @date 23/11/2016.
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
                new Object[]{"controls","images",ImagesActivity.class},
                new Object[]{"controls","controls",ControlsActivity.class},
                new Object[]{"controls","gallery", GalleryActivity.class},
                new Object[]{"controls","images", io.cess.demo.controls.ImagesActivity.class},

                //SwipeRefreshLayout 官方

                new Object[]{"binding","bind view",new Intent(mView.getContext(),BindViewActivity.class)},
                new Object[]{"binding","bind view holde",new Intent(mView.getContext(),BindViewHolderActivity.class)},


                new Object[]{"pull to refresh","ptr default",new Intent(mView.getContext(),PtrDefaultActivity.class)},
                new Object[]{"pull to refresh","ptr list",new Intent(mView.getContext(),PtrListActivity.class)},
                new Object[]{"pull to refresh","ptr recycler",new Intent(mView.getContext(),PtrRecyclerActivity.class)},
                new Object[]{"pull to refresh","ptr scroll",new Intent(mView.getContext(),PtrScrollActivity.class)},


                new Object[]{"form","classic",FormActivity.class},
                new Object[]{"event bus","classic",FormActivity.class},
                new Object[]{"web view","crosss walk",new Intent(mView.getContext(),WebViewActivity.class)},


                new Object[]{"http","http",new Intent(mView.getContext(),HttpActivity.class)}
        };

        Data data = null;
        for(Object[] item : items){
            data = new Data();
            data.setGroup((String) item[0]);
            data.setName((String) item[1]);
            data.setIntent(item[2]);
            mDatas.add(data);
        }

        mView.show(mDatas);
    }
}

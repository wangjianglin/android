package lin.demo.main;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import lin.demo.nav.NavActivity;
import lin.demo.swiperefresh.SwiperefreshActivity;
import lin.demo.tabbar.TabbarActivity;

/**
 * Created by lin on 23/11/2016.
 */

public class MainPresenter implements MainContract.Presenter{


    private MainContract.View mView;

    private Object[][] items = null;

    public MainPresenter(MainContract.View view){
        mView = view;

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
                new Object[]{"controls","tabbar",new Intent(mView.getContext(),TabbarActivity.class)},
                new Object[]{"controls","nav",new Intent(mView.getContext(),NavActivity.class)},
                new Object[]{"controls","swipe refresh",new Intent(mView.getContext(),SwiperefreshActivity.class)}
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

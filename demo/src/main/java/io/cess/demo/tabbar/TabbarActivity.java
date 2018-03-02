package io.cess.demo.tabbar;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import io.cess.demo.ActivityUtils;
import io.cess.demo.R;

public class TabbarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        TabbarFragment tabbarFragment = (TabbarFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (tabbarFragment == null){
            tabbarFragment = TabbarFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tabbarFragment, R.id.contentFrame);
        }

        //listFragment.setPresenter(new MainPresenter(listFragment));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

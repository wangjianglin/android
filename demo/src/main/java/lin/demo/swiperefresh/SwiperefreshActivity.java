package lin.demo.swiperefresh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import lin.demo.ActivityUtils;
import lin.demo.R;
import lin.demo.tabbar.TabbarFragment;

public class SwiperefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiperefresh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        SwiperefreshFragment swiperefreshFragment = (SwiperefreshFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (swiperefreshFragment == null){
            swiperefreshFragment = SwiperefreshFragment.newInstance();
            swiperefreshFragment.setPresenter(new SwiperefreshPresenter(swiperefreshFragment));
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), swiperefreshFragment, R.id.contentFrame);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

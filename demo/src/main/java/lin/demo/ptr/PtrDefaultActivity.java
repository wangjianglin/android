package lin.demo.ptr;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import lin.core.ptr.PtrView;
import lin.demo.R;

public class PtrDefaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_default);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        PtrView ptr = (PtrView) this.findViewById(R.id.ptr_default_view);

        ptr.setOnLoadMoreListener(new PtrView.OnLoadMoreListener() {
            @Override
            public void onLoadMore(final PtrView ptr) {
                ptr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptr.complete();
                    }
                }, 2500);
            }
        });

        ptr.setOnRefreshListener(new PtrView.OnRefreshListener() {
            @Override
            public void onRefresh(final PtrView ptr) {
                ptr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptr.complete();
                    }
                }, 2500);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

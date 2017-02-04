package lin.demo.ptr;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import lin.core.ptr.PtrRecyclerView;
import lin.core.ptr.PtrScrollView;
import lin.core.ptr.PtrView;
import lin.demo.R;
import lin.demo.Utils;

public class PtrScrollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptr_scrollview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        final PtrScrollView ptr = (PtrScrollView) this.findViewById(R.id.ptr_default_view);



        ptr.setOnLoadMoreListener(new PtrView.OnLoadMoreListener() {
            @Override
            public void onLoadMore(final PtrView ptr) {
                ptr.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptr.loadMoreComplete();
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
                        ptr.refreshComplete();
                    }
                }, 2500);
            }
        });

        ptr.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptr.autoRefresh();
            }
        },1000);

        LinearLayout layout = (LinearLayout) this.findViewById(R.id.ptr_scrollview_layout);


        for(int n=0;n<20;n++){
            TextView item = new TextView(this.getApplicationContext());
            item.setText(Utils.getDigit(n));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    this.getResources().getDimensionPixelSize(R.dimen.ptr_scrollview_textview_height));
            item.setLayoutParams(lp);
            layout.addView(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

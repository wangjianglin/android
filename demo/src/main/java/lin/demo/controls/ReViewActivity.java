package lin.demo.controls;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import lin.core.ReView;
import lin.core.ptr.PtrScrollView;
import lin.core.ptr.PtrView;
import lin.demo.R;
import lin.demo.Utils;

public class ReViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


        final ReView reView = (ReView) this.findViewById(R.id.ptr_default_view);


        //LinearLayout layout = (LinearLayout) this.findViewById(R.id.ptr_scrollview_layout);


        for(int n=0;n<5;n++){
            TextView item = new TextView(this.getApplicationContext());
            item.setText(Utils.getDigit(n));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    this.getResources().getDimensionPixelSize(R.dimen.ptr_scrollview_textview_height));
            item.setLayoutParams(lp);
            reView.addView(item);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

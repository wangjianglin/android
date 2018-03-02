package io.cess.demo.controls;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import io.cess.core.ViewActivity;
import io.cess.demo.R;

public class ImagesActivity extends ViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_view);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);


//        SwipeView swipeView = (SwipeView) this.findViewById(R.id.act_default_view);
//
//        for(int n=0;n<10;n++) {
//            TextView textView = new TextView(getApplicationContext());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                    , ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(10, 10, 10, 10);
//
//            textView.setLayoutParams(lp);
//            textView.setText(Utils.getDigit(n));
//
//
//            Random random = new Random();
//            int r = random.nextInt(256);
//            int b = random.nextInt(256);
//            int g = random.nextInt(256);
//
//            textView.setBackgroundColor((0xff << 24) + (r << 16) + (g << 8) + b);
//
//            swipeView.addView(textView);
//        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

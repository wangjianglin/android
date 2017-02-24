package lin.demo.controls;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

import lin.core.ViewActivity;
import lin.core.ViewPager;
import lin.demo.R;
import lin.demo.Utils;

public class SwitchButtonActivity extends ViewActivity {

    private int listCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ptr_default);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls_switch_button);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);


//        final ViewPager viewPager = (ViewPager) this.findViewById(R.id.ptr_default_view);
//
//        viewPager.setAdapter(new SimplePagerAdapter());


//        for(int n=0;n<5;n++) {
//
//            TextView textView = new TextView(this.getApplicationContext());
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                    , ViewGroup.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(10, 10, 10, 10);
//
//            textView.setLayoutParams(lp);
//            textView.setText(getDigit(n));
//
//            viewPager.addView(textView);
//        }
    }

    private class SimplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
        public Object instantiateItem(View container, int position) {
            TextView textView = new TextView(getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);

            textView.setLayoutParams(lp);
            textView.setText(Utils.getDigit(position));

            ((ViewPager)container).addView(textView);

            Random random = new Random();
            int r = random.nextInt(256);
            int b = random.nextInt(256);
            int g = random.nextInt(256);

            textView.setBackgroundColor((0xff << 24) + (r << 16) + (g << 8) + b);

            return textView;

        }

//        @Override
//        public Object getItem(int position) {
//            return getDigit(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(!(convertView instanceof TextView)){
//                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_ptr_listview_item,parent,false);
//            }
//            ((TextView) convertView).setText(getDigit(position+1));
//            return convertView;
//        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

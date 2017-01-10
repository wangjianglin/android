package lin.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


import lin.core.ImagePicker;
import lin.core.fitChart.FitChart;


public class FitChartActivity extends Activity {

    private Handler mHandler = new Handler();

    private FitChart fitChart;
    private float v = 0;
    private Runnable taks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit_chart);

//        fitChart = (FitChart)this.findViewById(R.id.fitChart);
//
//        taks = new Runnable() {
//            @Override
//            public void run() {
//
//                //fitChart.
//                fitChart.setValue(v);
//                if(v < 100) {
//                    mHandler.postDelayed(taks, 1000);
//                }
//                v += 10;
//            }
//        };
//        mHandler.postDelayed(taks,100);
        ImagePicker imagePicker = (ImagePicker) this.findViewById(R.id.imagePickerId);
        imagePicker.setVedioImage("http://i.feicuibaba.com/upload/store/98/2D57851E-F0EC-466E-8402-16DAB5F1F691.jpg");
        imagePicker.setVedioUrl("http://i.feicuibaba.com/upload/store/98/40731FB6-9801-4109-A678-B53EDE8BA2F4.mp4");
        imagePicker.setVedio(true);
    }


}

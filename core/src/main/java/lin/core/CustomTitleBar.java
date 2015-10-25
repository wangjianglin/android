package lin.core;

import android.app.Activity;

public class CustomTitleBar {
//	private static Activity mActivity;  
	  
    /** 
     * @see [自定义标题栏] 
     * @param activity 
     * @param title 
     */  
    public static void getTitleBar(Activity activity,String title) {  
//        mActivity = activity;  
//        activity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
//        activity.setContentView(R.layout.custom_title);  
//        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,  
//                R.layout.custom_title);  
//        TextView textView = (TextView) activity.findViewById(R.id.head_center_text);  
//        textView.setText(title);  
//        Button titleBackBtn = (Button) activity.findViewById(R.id.head_TitleBackBtn);  
//        titleBackBtn.setOnClickListener(new OnClickListener() {  
//            public void onClick(View v) {  
//                KeyEvent newEvent = new KeyEvent(KeyEvent.ACTION_DOWN,  
//                        KeyEvent.KEYCODE_BACK);  
//                mActivity.onKeyDown(KeyEvent.KEYCODE_BACK, newEvent);  
//            }  
//        });  
    }  
}


//public class MainActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////		TitleSet.getTitleBar(this,"我的自定义标题栏");  
//		CustomTitleBar.getTitleBar(this,"我的自定义标题栏");
//		setContentView(R.layout.activity_main);
//	}
//}

package lin.core;

import android.content.res.Configuration;
import android.os.Bundle;

/**
 * 
 * @author lin
 * @date Jul 17, 2015 10:06:36 AM
 *
 */
public interface ActivieyLifeCycle {

	void onCreate(Bundle savedInstanceState);
	
	void onStart();
	
	void onResume();
	
	void onPause();
	
	void onStop();
	
	void onDestroy();

	void onConfigurationChanged(Configuration newConfig);
	
	void onSaveInstanceState(Bundle outState);
}

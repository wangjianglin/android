package lin.core;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author lin
 * @date Mar 12, 2015 5:08:47 PM
 *
 */
public class ResourceFragment extends Fragment {
	
	private int resourceId;
	public ResourceFragment(int resourceId){
		this.resourceId = resourceId;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(this.resourceId,container,false);
	}
}

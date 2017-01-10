package lin.core;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author lin
 * @date Mar 12, 2015 5:08:34 PM
 *
 */
public class ViewFragment extends android.support.v4.app.Fragment {
	
	private View view;
	public ViewFragment(){

	}
	public ViewFragment(View view){
		this.view = view;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return view;
	}

	@Nullable
	@Override
	public View getView() {
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
}

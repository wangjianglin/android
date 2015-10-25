package lin.core;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author lin
 * @date May 7, 2015 12:55:33 PM
 *
 */
public class ClassFragment extends Fragment {
	
	public ClassFragment(){}
	private Class<? extends View> cls;
	public ClassFragment(Class<? extends View> cls){
		this.cls = cls;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		try {
			return cls.getConstructor(Context.class).newInstance(this.getActivity());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}

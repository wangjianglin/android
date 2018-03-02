package io.cess.web.plugin;

import android.app.Activity;
import android.content.Context;

public abstract class LinWebPlugin {

	private Context context;
	public LinWebPlugin(Context context) {
		this.context = context;
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public Activity getActivity(){
		return (Activity)context;
	}
}

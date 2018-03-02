package io.cess.web.plugin;

import java.util.Map;

import android.content.Context;
import android.content.Intent;

public class VideoPlugin extends LinWebPlugin{

	public VideoPlugin(Context context) {
		super(context);
	}

	public void play(Map<String,Object> args){
		Intent intent = new Intent(this.getContext(),VideoPluginActivity.class);
		intent.setAction((String) args.get("url"));
		this.getActivity().startActivity(intent);
	}
}

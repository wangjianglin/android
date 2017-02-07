package lin.web.plugin;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import lin.web.R;

/**
 * 
 * @author lin
 * @date Aug 18, 2015 6:34:24 PM
 *
 */
public class VideoPluginActivity extends Activity{

	private VideoView videoView;
	private Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.lin_web_video_plugin_view);
		videoView = (VideoView) this.findViewById(R.id.lin_web_video_plugin_vedio_id);
		videoView.setMediaController(new MediaController(this));

		handler.postDelayed(new Runnable(){

			@Override
			public void run() { 
				String url = getIntent().getAction(); 
				Uri uri = Uri.parse(url);
				videoView.setVideoURI(uri); 
				videoView.start(); 
				videoView.requestFocus();
			}},200);
		
	}

}

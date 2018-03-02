package io.cess.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import io.cess.core.ImagesSelectView.PhotoAibum;
import io.cess.core.ImagesSelectView.PhotoItem;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Aug 18, 2015 12:26:14 AM
 *
 */
@ResCls(R.class)
@ResId(id="io_cess_core_images_select_view_detail_activity")
public class ImagesSelectPhotoAlbumView extends ResView {

	public ImagesSelectPhotoAlbumView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ImagesSelectPhotoAlbumView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImagesSelectPhotoAlbumView(Context context) {
		super(context);
	}

	private GridView gridView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
	// private String cameraDir = "/DCIM/";
	@ViewById(id="progressbar")
	private ProgressBar progressBar;
	private AlbumGridViewAdapter gridImageAdapter;
	@ViewById(id="selected_image_layout")
	private LinearLayout selectedImageLayout;
	@ViewById(id="ok_button")
	private Button okButton;
	@ViewById(id="scrollview")
	private HorizontalScrollView scrollview;
	
	private PhotoAibum aibum;
	
	@Override
	protected void onInited() {
		aibum = (PhotoAibum) Nav.getNav(this).getArgs()[0];
		
		progressBar.setVisibility(View.GONE);
		gridView = (GridView) findViewById(R.id.myGrid);
//		gridImageAdapter = new AlbumGridViewAdapter(this.getContext(), dataList, ImagesSelectView.selectedDataList);
		gridImageAdapter = new AlbumGridViewAdapter(this.getContext(), dataList, null);
		gridView.setAdapter(gridImageAdapter);
		refreshData();
//		selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
//		okButton = (Button) findViewById(R.id.ok_button);
//		scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);
	}
	
	
	private void refreshData() {

		new AsyncTask<Void, Void, ArrayList<String>>() {

			@Override
			protected void onPreExecute() {
				progressBar.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				// ArrayList<String> tmpList = new ArrayList<String>();
				// ArrayList<String> listDirlocal = listAlldir( new
				// File(cameraDir));
				// ArrayList<String> listDiranjuke = new ArrayList<String>();
				// listDiranjuke.addAll(listDirlocal);

				// for (int i = 0; i < listDiranjuke.size(); i++){
				// listAllfile( new File( listDiranjuke.get(i) ),tmpList);
				// }
				ArrayList<String> tmpList = getList(aibum);
				return tmpList;
			}

			private ArrayList<String> getList(PhotoAibum aibum) {
				ArrayList<String> photospath = new ArrayList<String>();
				List<PhotoItem> photoItems = aibum.getBitList();
				int size = photoItems.size();
				for (int i = 0; i < size; i++) {
					photospath.add(photoItems.get(i).getPath());
				}
				return photospath;
			}

			protected void onPostExecute(ArrayList<String> tmpList) {

//				if (AlbumActivity.this == null || AlbumActivity.this.isFinishing()) {
//					return;
//				}
				progressBar.setVisibility(View.GONE);
				dataList.clear();
				dataList.addAll(tmpList);
				gridImageAdapter.notifyDataSetChanged();
				return;

			};

		}.execute();

	}
	
}

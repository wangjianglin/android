package io.cess.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;

/**
 * 
 * @author lin
 * @date Aug 17, 2015 12:26:45 AM
 *
 */
@ResCls(R.class)
@ResId(id="io_cess_core_images_select_view_main")
public class ImagesSelectView extends ResView {

	public ImagesSelectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ImagesSelectView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImagesSelectView(Context context) {
		super(context);
	}

//	@ViewById(id="album_gridview")
//	private ListView aibumGV;
	private List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();
	private List<PhotoItem> photos = new ArrayList<PhotoItem>();

//	public static ArrayList<String> selectedDataList = new ArrayList<String>();
//	@ViewById(id="selected_image_layout")
//	private LinearLayout selectedImageLayout;
	
//	@ViewById(id="ok_button")
//	private Button okButton;
//	@ViewById(id="scrollview")
//	private HorizontalScrollView scrollview;
//	private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
	
	@ViewById(id="images_select_view_grid_view")
	private GridView gridView;
	
	private Nav nav;
	
	// 设置获取图片的字段信息
	private static final String[] STORE_IMAGES = { MediaStore.Images.Media.DISPLAY_NAME, // 显示的名称
			MediaStore.Images.Media.DATA, MediaStore.Images.Media.LONGITUDE, // 经度
			MediaStore.Images.Media._ID, // id
			MediaStore.Images.Media.BUCKET_ID, // dir id 目录
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME // dir name 目录名字
	};
			
		
	@Override
	protected void onInited() {
		aibumList = getPhotoAlbum();
//		aibumGV.setAdapter(new PhotoAibumAdapter(aibumList, this.getContext()));
//		aibumGV.setOnItemClickListener(aibumClickListener);
		gridView.setAdapter(new GridViewAdapter());
		
		
		nav = Nav.getNav(this);
		//nav.setTitle("图片选择");
		
//		NavButton cancelButton = new NavButton();
//		cancelButton.setTitle("取消");
//		cancelButton.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				nav.pop();
//			}});
//
////		nav.setLeftButton(cancelButton);
//
//		NavButton doneButton = new NavButton();
//		doneButton.setTitle("完成");
//		doneButton.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View v) {
//				nav.pop();
//			}});
		
//		nav.setRightButton(doneButton);

	}
	
	private class GridViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public Object getItem(int position) {
			return photos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return photos.get(position).getPhotoID();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = new TextView(getContext());
			view.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 200));
			view.setText("-----");
			return view;
		}}
	/**
	 * 相册点击事件
	 */
	OnItemClickListener aibumClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Nav.getNav(ImagesSelectView.this).push(ImagesSelectPhotoAlbumView.class, null,aibumList.get(position));
//			Intent intent = new Intent(PhotoAlbumActivity.this, AlbumActivity.class);
//			intent.putExtra("aibum", aibumList.get(position));
//			Bundle bundle = new Bundle();
//			// intent.putArrayListExtra("dataList", dataList);
//			bundle.putStringArrayList("dataList", getIntentArrayList(selectedDataList));
//			intent.putExtras(bundle);
//			startActivityForResult(intent, 0);
			// startActivity(intent);
		}
	};
	
	/**
	 * 方法描述：按相册获取图片信息
	 * 
	 * @author: why
	 * @time: 2013-10-18 下午1:35:24
	 */
	private List<PhotoAibum> getPhotoAlbum() {
		List<PhotoAibum> aibumList = new ArrayList<PhotoAibum>();
		Cursor cursor = MediaStore.Images.Media.query(this.getActivity().getContentResolver(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
		Map<String, PhotoAibum> countMap = new HashMap<String, PhotoAibum>();
		PhotoAibum pa = null;
		PhotoItem pi = null;
		while (cursor.moveToNext()) {
			String path = cursor.getString(1);
			String id = cursor.getString(3);
			String dir_id = cursor.getString(4);
			String dir = cursor.getString(5);
			Log.e("info", "id===" + id + "==dir_id==" + dir_id + "==dir==" + dir + "==path=" + path);
			if (!countMap.containsKey(dir_id)) {
				pa = new PhotoAibum();
				pa.setName(dir);
				pa.setBitmap(Integer.parseInt(id));
				pa.setCount("1");
				
				pi = new PhotoItem(Integer.valueOf(id),path);
				countMap.put(dir_id, pa);
			} else {
				pa = countMap.get(dir_id);
				pa.setCount(String.valueOf(Integer.parseInt(pa.getCount()) + 1));
//				pa.getBitList().add();
				pi = new PhotoItem(Integer.valueOf(id), path);
			}

			pa.getBitList().add(pi);
			photos.add(pi);
		}
		cursor.close();
		Iterable<String> it = countMap.keySet();
		for (String key : it) {
			aibumList.add(countMap.get(key));
		}
		return aibumList;
	}
			
	public class PhotoItem implements Serializable {
		private static final long serialVersionUID = 8682674788506891598L;
		private int  photoID;
		private boolean select;
		private String path;
		public PhotoItem(int id,String path) {
			photoID = id;
			select = false;
			this.path=path;
		}
		
		public PhotoItem(int id,boolean flag) {
			photoID = id;
			select = flag;
		}
		
		
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getPhotoID() {
			return photoID;
		}
		public void setPhotoID(int photoID) {
			this.photoID = photoID;
		}
		public boolean isSelect() {
			return select;
		}
		public void setSelect(boolean select) {
			this.select = select;
		}
		@Override
		public String toString() {
			return "PhotoItem [photoID=" + photoID + ", select=" + select + "]";
		}
	}
	
	public class PhotoAibum implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;   //相册名字
		private String count; //数量
		private int  bitmap;  // 相册第一张图片
		
		private List<PhotoItem> bitList = new ArrayList<PhotoItem>();
		
		public PhotoAibum() {
		}
		
		
		public PhotoAibum(String name, String count, int bitmap) {
			super();
			this.name = name;
			this.count = count;
			this.bitmap = bitmap;
		}


		public List<PhotoItem> getBitList() {
			return bitList;
		}


		public void setBitList(List<PhotoItem> bitList) {
			this.bitList = bitList;
		}


		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCount() {
			return count;
		}
		public void setCount(String count) {
			this.count = count;
		}
		public int getBitmap() {
			return bitmap;
		}
		public void setBitmap(int bitmap) {
			this.bitmap = bitmap;
		}
		@Override
		public String toString() {
			return "PhotoAibum [name=" + name + ", count=" + count + ", bitmap="
					+ bitmap + ", bitList=" + bitList + "]";
		}
	}
}

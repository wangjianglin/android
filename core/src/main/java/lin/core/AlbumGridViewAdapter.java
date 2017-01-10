package lin.core;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class AlbumGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> dataList;
	private ArrayList<String> selectedDataList;
	private DisplayMetrics dm;
	private int screenWidth;

	public AlbumGridViewAdapter(Context c, ArrayList<String> dataList,
			ArrayList<String> selectedDataList) {

		mContext = c;
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = getScreenWidth(mContext);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public ToggleButton toggleButton;
		public ImageView photo_select;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
//			convertView = LayoutInflater.from(mContext).inflate(R.layout.photoselect_album_detail_item, parent,
//					false);
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lin_core_images_select_view_detail_item, parent,
					false);
			viewHolder.photo_select = (ImageView) convertView.findViewById(R.id.photo_select);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
			ViewGroup.LayoutParams params = viewHolder.imageView.getLayoutParams();
			params.height = screenWidth / 3;
			viewHolder.imageView.setLayoutParams(params);
			viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
			params = viewHolder.toggleButton.getLayoutParams();
			params.height = screenWidth / 3;
			viewHolder.toggleButton.setLayoutParams(params);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position);
		else
			path = "camera_default";
		if (path.contains("default")) {
//			viewHolder.imageView.setImageResource(R.drawable.photoselect_bg);
		} else {
			// viewHolder.imageView.setImageResource(R.drawable.pic_thumb_bg);
			// viewHolder.imageView.setTag(path);
			// loadImage(viewHolder.imageView, path);
//			ImageManager2.from(mContext).displayImage(viewHolder.imageView, path,
//					R.drawable.photoselect_bg, 400, 400);
		}

		System.out.println("图片path===" + path);
		viewHolder.toggleButton.setTag(position);
		viewHolder.toggleButton.setOnClickListener(new MyOnClick(viewHolder.photo_select));
		if (isInSelectedDataList(path)) {
			viewHolder.toggleButton.setChecked(true);
//			viewHolder.photo_select.setImageResource(R.drawable.photoselect_pic_sel);
		} else {
			viewHolder.toggleButton.setChecked(false);
//			viewHolder.photo_select.setImageResource(R.drawable.photoselect_pic_unsel);
		}

		return convertView;
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	private boolean isInSelectedDataList(String selectedString) {
		for (int i = 0; i < selectedDataList.size(); i++) {
			if (selectedDataList.get(i).equals(selectedString)) {
				return true;
			}
		}
		return false;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	class MyOnClick implements OnClickListener {
		private ImageView imageView;

		public MyOnClick(ImageView imageView) {
			this.imageView = imageView;
		}

		@Override
		public void onClick(View view) {
			if (view instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) view;
				int position = (Integer) toggleButton.getTag();
				if (toggleButton.isChecked()) {
//					imageView.setImageResource(R.drawable.photoselect_pic_sel);
				}else {
//					imageView.setImageResource(R.drawable.photoselect_pic_unsel);
				}
				if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
					mOnItemClickListener.onItemClick(imageView, toggleButton, position,
							dataList.get(position), toggleButton.isChecked());
				}
			}

		}

	}

	// @Override
	// public void onClick(View view) {
	// if (view instanceof ToggleButton) {
	// ToggleButton toggleButton = (ToggleButton) view;
	// int position = (Int) toggleButton.getTag();
	// if (dataList != null && mOnItemClickListener != null && position <
	// dataList.size()) {
	// mOnItemClickListener.onItemClick(toggleButton, position,
	// dataList.get(position),
	// toggleButton.isChecked());
	// }
	// }
	// }

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ImageView photo_select, ToggleButton view, int position,
								String path, boolean isChecked);
	}

}

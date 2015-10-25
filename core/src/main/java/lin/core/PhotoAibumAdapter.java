package lin.core;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import lin.core.ImagesSelectView.PhotoAibum;


public class PhotoAibumAdapter extends BaseAdapter {
	private List<PhotoAibum> aibumList;
	private Context context;
	private ViewHolder holder;
	public PhotoAibumAdapter(List<PhotoAibum> list, Context context ) {
		this.aibumList = list;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return aibumList.size();
	}

	@Override
	public Object getItem(int position) {
		return aibumList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;   
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
//			convertView = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.photoselect_album_item, null);
			convertView = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.lin_core_images_select_view_item, parent,false);
			holder = new ViewHolder();
			holder.iv = (ImageView)convertView.findViewById(R.id.photoalbum_item_image);
			holder.tv = (TextView)convertView.findViewById(R.id.photoalbum_item_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		/** 通过ID 获取缩略图*/
		Bitmap bitmap = Thumbnails.getThumbnail(context.getContentResolver(), aibumList.get(position).getBitmap(), Thumbnails.MICRO_KIND, null);
		holder.iv.setImageBitmap(bitmap);
		holder.tv.setText(aibumList.get(position).getName()+" ( "+ aibumList.get(position).getCount()+"张 )");
		return convertView;
	}
	
	static class ViewHolder{
		ImageView iv;
		TextView tv;
	}

}

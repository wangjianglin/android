package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class ListItemClickProcessor extends AbstractMethodProcessor{

	@Override
	protected int[] getIds(Annotation annotation) {
		return ((ListItemClick)annotation).value();
	}

	@Override
	protected String[] getStringIds(Annotation annotation) {
		return ((ListItemClick)annotation).id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processFieldItem(View view, Method method, View itemView){

//		ListItemClick item = (ListItemClick)annotation;
//
//		int viewId = 0;
//		if(item.value() != 0){
//			viewId = item.value();
//		}else if(!"".equals(item.id())){
//			try{
//				Field f = idClass.getDeclaredField(item.id());
//				viewId = f.getInt(null);
//			}catch(Throwable e){}
//		}else{
//			try{
//				Field f = idClass.getDeclaredField(method.getName());
//				viewId = f.getInt(null);
//			}catch(Throwable e){}
//		}

		Class<?> viewClass = null;
		Class<?>[] clcikMethodParams = method.getParameterTypes();
		if(clcikMethodParams != null){
			if(clcikMethodParams.length > 4){
				return;
			}
			boolean isAdapterView = false;
			boolean isView = false;
			boolean isPosition = false;
			boolean isLong = false;
			for(Class<?> clcikMethodParam : clcikMethodParams){
				if(clcikMethodParam == null){
					return;
				}
				if(AdapterView.class.isAssignableFrom(clcikMethodParam)){
					if(isAdapterView){return;}
					isAdapterView = true;
					viewClass = clcikMethodParam;
				}else if(View.class.isAssignableFrom(clcikMethodParam)){
					if(isView){return;}
					isView = true;
				}else if(int.class.isAssignableFrom(clcikMethodParam)){
					if(isPosition){return;}
					isPosition = true;
				}else if(long.class.isAssignableFrom(clcikMethodParam)){
					if(isLong){return;}
					isLong = true;
				}else{
					return;
				}
			}
		}
//		View itemView = view;
//		if(viewId != 0){
//			itemView = view.findViewById(viewId);
//		}
//		if(!(itemView instanceof ListView)){
//			return;
//		}
		//onItemClick(AdapterView<?> parent, View view,
//			int position, long id)
		
		
//			CompoundButton button = null; 
//			if(itemView instanceof CompoundButton){
//				button = (CompoundButton)itemView;
//			}
//			if(button == null){
//				return;
//			}

		AdapterView<?> listView = (AdapterView<?>) itemView;
		if(!(viewClass == null || viewClass.isAssignableFrom(listView.getClass()))){
			return;
		}
//			itemView.setOnClickListener(new ViewOnClickListener(itemView,method,view));
//			itemView.setOnKeyListener(new ViewOnKeyListener(view,method,clcikMethodParams));
//			CheckBox b;
		//button.setOnCheckedChangeListener(new ViewOnCheckedChangeListener(view,method,clcikMethodParams));
		listView.setOnItemClickListener(new ViewOnItemClickListener(view,method,clcikMethodParams));
	}

	private class ViewOnItemClickListener implements OnItemClickListener{

		private View view;
		private Class<?>[] clcikMethodParams;
		private Method method;
		ViewOnItemClickListener(View view,Method method,Class<?>[] clcikMethodParams){
			this.view = view;
			this.clcikMethodParams = clcikMethodParams;
			this.method = method;
		}
//		@Override
//		public void onClick(View v) {
//			try{
//				method.setAccessible(true);
//				if(this.method.getParameterTypes() == null || this.method.getParameterTypes().length == 0){
//					method.invoke(this.view);
//				}else{
//					method.invoke(this.view,this.item);
//				}
//			}catch(Throwable e){e.printStackTrace();}
//		}
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)  {
//			try{
//				method.setAccessible(true);
//				if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
//					method.invoke(this.view);
//				}else{
//					Class<?> clcikMethodParam = null;
//					List<Object> args = new ArrayList<Object>();
//					for(int n=0;n<clcikMethodParams.length;n++){
//						clcikMethodParam = clcikMethodParams[n];
//						if(clcikMethodParam == null){
//							args.add(null);
//							continue;
//						}
//						if(CompoundButton.class.isAssignableFrom(clcikMethodParam)){
//							args.add(buttonView);
//						}
//						else if(boolean.class.isAssignableFrom(clcikMethodParam)){
//							args.add(isChecked);
//						}
//					}
//					method.invoke(this.view,args.toArray());
//				}
//			}catch(Throwable e){e.printStackTrace();}
//		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try{
				method.setAccessible(true);
				if(this.clcikMethodParams == null || this.clcikMethodParams.length == 0){
					method.invoke(this.view);
				}else{
					Class<?> clcikMethodParam = null;
					List<Object> args = new ArrayList<Object>();
					for(int n=0;n<clcikMethodParams.length;n++){
						clcikMethodParam = clcikMethodParams[n];
						if(clcikMethodParam == null){
							args.add(null);
							continue;
						}
						if(AdapterView.class.isAssignableFrom(clcikMethodParam)){
							args.add(parent);
						}
						else if(View.class.isAssignableFrom(clcikMethodParam)){
							args.add(view);
						}
						else if(int.class.isAssignableFrom(clcikMethodParam)){
							args.add(position);
						}
						else if(long.class.isAssignableFrom(clcikMethodParam)){
							args.add(id);
						}
					}
					method.invoke(this.view,args.toArray());
				}
			}catch(Throwable e){e.printStackTrace();}
		}
		
	}
}

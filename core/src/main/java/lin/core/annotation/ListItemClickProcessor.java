package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
public class ListItemClickProcessor extends AbstractMethodProcessor<ListItemClick,AdapterView<?>>{

	@Override
	protected int[] getIds(ListItemClick annot) {
		return annot.value();
	}

	@Override
	protected String[] getStringIds(ListItemClick annot) {
		return annot.id();
	}

	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
	protected void processMethod(Object target, Method method, AdapterView<?> itemView,ListItemClick annot){


		Class<?> viewClass = null;
		Class<?>[] methodParams = method.getParameterTypes();
		if(!Utils.validate(methodParams,itemView.getClass(),View.class,int.class,long.class)){
			return;
		}
//		if(clcikMethodParams != null){
//			if(clcikMethodParams.length > 4){
//				return;
//			}
//			boolean isAdapterView = false;
//			boolean isView = false;
//			boolean isPosition = false;
//			boolean isLong = false;
//			for(Class<?> clcikMethodParam : clcikMethodParams){
//				if(clcikMethodParam == null){
//					return;
//				}
//				if(AdapterView.class.isAssignableFrom(clcikMethodParam)){
//					if(isAdapterView){return;}
//					isAdapterView = true;
//					viewClass = clcikMethodParam;
//				}else if(View.class.isAssignableFrom(clcikMethodParam)){
//					if(isView){return;}
//					isView = true;
//				}else if(int.class.isAssignableFrom(clcikMethodParam)){
//					if(isPosition){return;}
//					isPosition = true;
//				}else if(long.class.isAssignableFrom(clcikMethodParam)){
//					if(isLong){return;}
//					isLong = true;
//				}else{
//					return;
//				}
//			}
//		}
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

//		AdapterView<?> listView = null;
//		if(itemView instanceof AdapterView<?>){
//			listView = (AdapterView<?>) itemView;
//		}else{
//			return;
//		}
//
//		if(!(viewClass == null || viewClass.isAssignableFrom(listView.getClass()))){
//			return;
//		}
//			itemView.setOnClickListener(new ViewOnClickListener(itemView,method,view));
//			itemView.setOnKeyListener(new ViewOnKeyListener(view,method,clcikMethodParams));
//			CheckBox b;
		//button.setOnCheckedChangeListener(new ViewOnCheckedChangeListener(view,method,clcikMethodParams));
		itemView.setOnItemClickListener(new ViewOnItemClickListener(target,method,methodParams));
	}

	private class ViewOnItemClickListener implements OnItemClickListener{

		private Object view;
		private Class<?>[] methodParams;
		private Method method;
		ViewOnItemClickListener(Object view,Method method,Class<?>[] methodParams){
			this.view = view;
			this.methodParams = methodParams;
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
				method.invoke(this.view,Utils.args(methodParams,parent,view,position,id));
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
//						if(AdapterView.class.isAssignableFrom(clcikMethodParam)){
//							args.add(parent);
//						}
//						else if(View.class.isAssignableFrom(clcikMethodParam)){
//							args.add(view);
//						}
//						else if(int.class.isAssignableFrom(clcikMethodParam)){
//							args.add(position);
//						}
//						else if(long.class.isAssignableFrom(clcikMethodParam)){
//							args.add(id);
//						}
//					}
//					method.invoke(this.view,args.toArray());
//				}
			}catch(Throwable e){e.printStackTrace();}
		}
		
	}
}

package lin.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 
 * @author lin
 * @date Jun 14, 2015 5:24:20 PM
 *
 */
//public class CheckedChangeProcessor implements MethodProcessor{
public class CheckedProcessor extends AbstractMethodProcessor{

//	@Override
//	public void process(View view,Method method, Annotation annotation,
//			Class<?> idClass) {
//		CheckedChange item = (CheckedChange) annotation;
//		String[] idStrings = item.id();
//		int[] ids = item.value();
//		if(idStrings == null || ids == null
//				|| idStrings.length == 0 || ids.length == 0
//				||(idStrings.length == 1 && "".equals(idStrings[0]))
//			|| (ids.length == 1 && ids[0] == 0)){
//			processItem(view,method,0,idClass);
//		}
//		if(ids.length != 1 || ids[0] != 0) {
//			for (int id : ids) {
//				processItem(view, method, id, idClass);
//			}
//		}else{
//			for (String id : idStrings) {
//				processStringItem(view, method, id, idClass);
//			}
//		}
//	}



	@Override
	protected int[] getIds(Annotation annotation) {
		return ((Checked)annotation).value();
	}

	@Override
	protected String[] getStringIds(Annotation annotation) {
		return ((Checked)annotation).id();
	}
	@Override
	protected void processFieldItem(Object target, Method method, View itemView) {
//	private void processStringItem(View view,Method method, String stringId,
//							 Class<?> idClass){

//		int viewId = 0;
//		if("".equals(stringId) || stringId == null){
//		try{
//				Field f = idClass.getDeclaredField(stringId);
//				viewId = f.getInt(null);
//			}catch(Throwable e){}
//		}else {
//			try {
//				Field f = idClass.getDeclaredField(method.getName());
//				viewId = f.getInt(null);
//			} catch (Throwable e) {
//			}
//		}
//		processItem(view, method, viewId, idClass);
//	}
//
//	private void processItem(View view,Method method, int viewId,
//								Class<?> idClass){
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
			if(clcikMethodParams.length > 3){
				return;
			}
			boolean vc = false;
			boolean ib = false;
			for(Class<?> clcikMethodParam : clcikMethodParams){
				if(clcikMethodParam == null){
					return;
				}
				if(CompoundButton.class.isAssignableFrom(clcikMethodParam)){
					if(vc){return;}
					viewClass = clcikMethodParam;
					vc = true;
				}
				else if(boolean.class.isAssignableFrom(clcikMethodParam)){
					if(ib){return;}
					ib = true;
				}else{
					return;
				}
			}
		}
//		View itemView = view;
//		if(viewId != 0){
//			itemView = view.findViewById(viewId);
//		}
		CompoundButton button = null; 
		if(itemView instanceof CompoundButton){
			button = (CompoundButton)itemView;
		}
		if(button == null){
			return;
		}
		if(!(viewClass == null || viewClass.isAssignableFrom(button.getClass()))){
			return;
		}
//			itemView.setOnClickListener(new ViewOnClickListener(itemView,method,view));
//			itemView.setOnKeyListener(new ViewOnKeyListener(view,method,clcikMethodParams));
//			CheckBox b;
		button.setOnCheckedChangeListener(new ViewOnCheckedChangeListener(target,method,clcikMethodParams));
	}

	private class ViewOnCheckedChangeListener implements OnCheckedChangeListener{

		private Object view;
		private Class<?>[] clcikMethodParams;
		private Method method;
		ViewOnCheckedChangeListener(Object view,Method method,Class<?>[] clcikMethodParams){
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
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)  {
			try{
				if(!isChecked){
					return;
				}
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
						if(CompoundButton.class.isAssignableFrom(clcikMethodParam)){
							args.add(buttonView);
						}
						else if(boolean.class.isAssignableFrom(clcikMethodParam)){
							args.add(isChecked);
						}
					}
					method.invoke(this.view,args.toArray());
				}
			}catch(Throwable e){e.printStackTrace();}
		}
		
	}
}

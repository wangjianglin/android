package lin.util.beans;

import android.annotation.SuppressLint;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class BeanInfo {

	public PropertyDescriptor[] getPropertyDescriptors() {
		return descroptors;
	}

	private PropertyDescriptor[] descroptors;
	@SuppressLint("DefaultLocale")
	private void init(){
		if(type == null){
			return;
		}
		if(this.superclass == null){
			this.superclass = Object.class;
		}
		Method[] methods = type.getDeclaredMethods();
		Map<String,PropertyDescriptor> descMap = new HashMap<String,PropertyDescriptor>();
		String methodName = null;
		String propetyName = null;
		PropertyDescriptor desc = null;
		for(Method method : methods){
			if(method == null){
				continue;
			}
			if(method.getClass() == this.superclass){
				continue;
			}
			methodName = method.getName();
//			if(methodName.length() < 4){
//				continue;
//			}
			if(methodName.startsWith("get") && methodName.length() > 3){
				propetyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				desc = descMap.get(propetyName);
				if(desc == null){
					desc = new PropertyDescriptor();
					descMap.put(propetyName, desc);
					desc.name = propetyName;
					desc.readMethod = method;
				}else if(desc.readMethod != null){
					desc.readMethod = method;
				}
				continue;
			}
			if(methodName.startsWith("is") && methodName.length() > 2){
				propetyName = methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
				desc = descMap.get(propetyName);
				if(desc == null){
					desc = new PropertyDescriptor();
					descMap.put(propetyName, desc);
					desc.name = propetyName;
					desc.readMethod = method;
				}
				desc.readMethod = method;
				continue;
			}
			if(methodName.startsWith("set") && methodName.length() > 3){
				propetyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
				desc = descMap.get(propetyName);
				if(desc == null){
					desc = new PropertyDescriptor();
					descMap.put(propetyName, desc);
					desc.name = propetyName;
					desc.writeMethod = method;
				}
				desc.writeMethod = method;
				
				continue;
			}
		}
		this.descroptors = descMap.values().toArray(new PropertyDescriptor[]{});
	}
	private Class<?> type;
	private Class<?> superclass;
	BeanInfo(Class<?> type){
		this.type = type;
		this.init();
	}

	public BeanInfo(Class<?> type, Class<?> superclass) {
		this.type = type;
		this.superclass = superclass;
		this.init();
	}
}

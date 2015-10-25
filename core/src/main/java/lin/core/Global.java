package lin.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author lin
 * @date Mar 11, 2015 9:49:43 PM
 *
 */
public class Global {

	private static Map<String,Object> map = new HashMap<String,Object>();
	
	public static <T> T get(String name){
		@SuppressWarnings("unchecked")
		T obj = (T) map.get(name);
		return obj;
	}
	
	public static void set(String name,Object value){
		map.put(name, value);
	}
	
	public static void remove(String name){
		map.remove(name);
	}
}

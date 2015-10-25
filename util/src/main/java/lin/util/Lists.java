package lin.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author lin
 * @date Jun 29, 2015 3:15:45 PM
 *
 */
public class Lists {

	public interface Equals<T>{
		boolean isEquals(T a,T b);
	}
	@SuppressWarnings("rawtypes")
	private static Equals DEFAULT_EQUALS = new Equals(){

		@Override
		public boolean isEquals(Object a, Object b) {
			if(a == null || b == null){
				return false;
			}
			return a.equals(b);
		}
	};
	@SuppressWarnings("unchecked")
	public static <T> void add(List<T> dest,List<T> source){
		add(dest,source,DEFAULT_EQUALS);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void add(List<T> dest,List<T> source,Equals<T> equals){
		if(dest == null || source == null){
			return;
		}
		if(equals == null){
			equals = DEFAULT_EQUALS;
		}
		List<T> tmp = new ArrayList<T>();
		out:for(T item : source){
			for(T ditem : dest){
				if(equals.isEquals(item, ditem)){
					break out;
				}
			}
			tmp.add(item);
		}
		dest.addAll(tmp);
	}
}

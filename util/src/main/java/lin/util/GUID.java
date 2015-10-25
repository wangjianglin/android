package lin.util;

/**
 * 
 * @author lin
 * @date 2011-2-27
 *
 */
public class GUID {

	public static String next(){
		return java.util.UUID.randomUUID().toString();
	}
}

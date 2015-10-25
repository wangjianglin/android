package lin.util;

import java.io.UnsupportedEncodingException;

public class Base64 {

	public static String encode(String str){
//		return new sun.misc.BASE64Encoder().encodeBuffer(str.getBytes());
		return android.util.Base64.encodeToString(str.getBytes(), android.util.Base64.DEFAULT);
	}
	
	public static String decode(String str,String charsetName) throws UnsupportedEncodingException{
//		try {
			return new String(android.util.Base64.decode(str, android.util.Base64.DEFAULT),charsetName);
//		} catch (UnsupportedEncodingException e) {
//		}
//		return null;
	}
}

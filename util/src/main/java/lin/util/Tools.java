package lin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools 
{
	public static boolean isStringNotNull(String s )
	{
		if(null!=s&&!"".equals(s))
		{
			return true;
		}
		return false;
		
			
		
	}
	
	/**
	 * 判断是否为手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles)
	{

//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern.compile("^(1[3|5|8])\\d{9}$");
		Matcher m = p.matcher(mobiles);  
        return m.matches();
	
	}  

}

package io.cess.comm.http;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:38
 *
 */
public class HttpUtils {


	public static void fireResult(ResultListener listener, Object obj, List<Error> warning){
		if(listener != null){
			listener.result(obj, warning);
		}
	}
//	static void fireProgress(ProgressListener listener,long count,long total){
//		if(listener != null){
//			listener.progress(count, total);
//		}
//	}
	public static void fireFault(ResultListener listener,Error error){
		if(listener != null){
			listener.fault(error);
		}
	}

	private static long _Sequeue = 0;
    /// <summary>
    /// 序列号
    /// </summary>
    public synchronized static long getSequeue() { 
    	_Sequeue++; 
    	return (1L + _Sequeue + Long.MAX_VALUE) % (Long.MAX_VALUE + 1L);
    }
    //private static final long offset = new DateTime(1970, 1, 1, 0, 0, 0, DateTimeKind.Utc).Ticks;
    /// <summary>
    /// 时间戳，以伦敦时间1970-01-01 00:00:00.000为基准的毫秒数
    /// </summary>
    public static long getTimestamp() { return new Date().getTime(); }
    
	@SuppressLint("DefaultLocale")
	public static String uri(HttpCommunicateImpl impl,io.cess.comm.http.HttpPackage pack) {
		if (pack.getUrl() == null) {
			return null;
		}

		String commUriString = null;
		if (pack.getUrl().toLowerCase().startsWith("http://")
				|| pack.getUrl().toLowerCase().startsWith("https://")) {
			commUriString = pack.getUrl();
		}else {
			URL commUrl = impl.getCommUrl();

			commUriString = commUrl.toString();

			if(pack.getUrl().startsWith("/")){
				commUriString += pack.getUrl();
			}else{
				commUriString += "/" + pack.getUrl();
			}
		}

		String uri = commUriString;

		if (!pack.isEnableCache()) {
			if (commUriString.contains("?")) {
				uri = commUriString + "&_time_stamp_" + (new Date()).getTime() + "=1";
			} else {
				uri = commUriString + "?_time_stamp_" + (new Date()).getTime() + "=1";
			}
		}
		return uri;
	}

	public static String parseCharset(String contentType, String defaultCharset) {
		if(contentType != null) {
			String[] params = contentType.split(";");

			for(int i = 1; i < params.length; ++i) {
				String[] pair = params[i].trim().split("=");
				if(pair.length == 2 && pair[0].equals("charset")) {
					return pair[1];
				}
			}
		}

		return defaultCharset;
	}

	public static String urlAddQueryString(String urlString, String queryString) {
		if(queryString == null || "".equals(queryString)){
			return urlString;
		}
		if (urlString.indexOf('?') == -1) {
			urlString += "?" + queryString;
		} else {
			urlString += "&" + queryString;
		}
		return urlString;
	}

	public static Map<String,String> queryMap(Map<String, Object> params) throws UnsupportedEncodingException {
    	Map<String,String> map = new HashMap<>();

    	if(params == null || params.isEmpty()){
    		return map;
		}
		for (Map.Entry<String, Object> item : params.entrySet()) {
    		map.put(item.getKey(),encode(item.getValue().toString()));
		}
    	return map;
	}

	public static String generQueryString(Map<String, Object> params) throws UnsupportedEncodingException {

		if (params == null) {
			return "";
		}
		StringBuffer sBuffer = new StringBuffer();
		for (Map.Entry<String, Object> item : params.entrySet()) {
			sBuffer.append(item.getKey());
			sBuffer.append("=");
			if (item.getValue() != null) {
				sBuffer.append(encode(item.getValue().toString()));
//                sBuffer.append(item.getValue());

			}
			sBuffer.append("&");
		}
		if (sBuffer.length() > 0) {
			sBuffer.deleteCharAt(sBuffer.length() - 1);
		}
		return sBuffer.toString();
	}

	public static String encode(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "utf-8");
	}
}

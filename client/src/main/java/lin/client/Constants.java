package lin.client;

/**
 * 
 * @author 王江林
 * @date 2012-7-3 上午10:50:03
 * 一些常用常量
 */
public class Constants {

//	/**
//	 * 
//	 */
//	public static final String CloudResult = "__result__";
//	
//	/**
//	 * 
//	 */
//	public static final String  ErrorCode = "exceptionCode";
	
	
	/**
	 * json数据的参数名
	 */
	public static final String HTTP_JSON_PARAM = "__json_param__";
	/**
	 * 响应数据的类型，默认为html，支持html、json、xml
	 */
	//private static final String RESULT = "__result__";
	
	/**
	 * 数据通信协议版本的参数，无此参数，则表示不采用此种通信协议进行通信
	 */
	//private static final String VERSION = "__version__";
	/**
	 * 客户端请求数据的编码参数方式
	 */
	public static final String HTTP_REQUEST_CODING = "__request_coding__";
	
	/**
	 * 客户端要求响应数据的编码方式，默认为utf-8
	 */
	//private static final String RESPONSE_CODING = "__response_coding__";

	public static final String HTTP_COMM_PROTOCOL = "__http_comm_protocol__";
	public static final String HTTP_COMM_PROTOCOL_DEBUG = "__http_comm_protocol_debug__";
	public static final String HTTP_COMM_PROTOCOL_VERSION = "/__http_comm_protocol__/__version__";
	public static final String HTTP_VERSION = "0.1";
	
//	public static final String DEFAULT_LOCATION = "/WEB-INF/content/";
//	public static final String LOCATION_PARAM = "location";
}

package io.cess.comm;

/**
 * 
 * @author 王江林
 * @date 2012-7-3 上午10:50:03
 * 一些常用常量
 */
public class Constants {

	/**
	 * json数据的参数名
	 */
	public static final String HTTP_JSON_PARAM = "__json_param__";

	/**
	 * 客户端请求数据的编码参数方式，默认为utf-8
	 */
	public static final String HTTP_REQUEST_CODING = "__request_coding__";

	/**
	 * http请求协议头，值为协议版本，默认为 0.1
	 */
	public static final String HTTP_COMM_PROTOCOL = "__http_comm_protocol__";
	/**
	 * http返回是否带有警告信息
	 */
	public static final String HTTP_COMM_WITH_WARNING = "__http_comm_with_warning__";
	/**
	 * http返回是否为错误信息
	 */
	public static final String HTTP_COMM_WITH_ERROR = "__http_comm_with_error__";
	/**
	 * 表示当请求产生错误时，是否返回用于跟踪错误的调试信息
	 */
	public static final String HTTP_COMM_PROTOCOL_DEBUG = "__http_comm_protocol_debug__";
	/**
	 * 当前协议版本 0.2
	 */
	public static final String HTTP_VERSION = "0.2";

}

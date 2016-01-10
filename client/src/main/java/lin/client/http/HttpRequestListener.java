package lin.client.http;

import java.util.List;


/**
 * 监听所有http通信
 * @author 王江林
 * @date 2013-7-16 上午11:33:44
 *
 */
public interface HttpRequestListener {

	 /// <summary>
    /// 开始请求
    /// </summary>
    //public static event System.Action<HttpCommunicateType, object> HttpRequest;
	 void request(HttpCommunicateImpl impl, HttpPackage pack);
    /// <summary>
    /// 请求完成
    /// </summary>
    //public static event System.Action<HttpCommunicateType, object, object, IList<Error>> HttpRequestComplete;
	void requestComplete(HttpCommunicateImpl impl, HttpPackage pack, Object obj, List<Error> warning);

    /// <summary>
    /// 请求错误
    /// </summary>
    //public static event System.Action<HttpCommunicateType, object, Error> HttpRequestFault;
	void requestFault(HttpCommunicateImpl impl, HttpPackage pack, Error error);
}

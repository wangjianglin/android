//package lin.client.http;
//
//import java.util.Map;
//
//import org.apache.http.message.AbstractHttpMessage;
//
///**
// *
// * @author 王江林
// * @date 2013年12月27日 上午10:19:00
// *
// */
//public class FileDownloadPackage extends HttpPackage{
//	private static class FileDownloadHttpRequestListener implements HttpRequestHandle{
//
//		@Override
//		public Map<String, Object> getParams(AbstractHttpMessage httpMessage,HttpPackage pack) {
//			return null;
//		}
//
//		@Override
//		public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
//
//		}
//
//	}
//	private static HttpRequestHandle HttpRequestHandle = new FileDownloadHttpRequestListener();
//
//	private String key;
//
//	public FileDownloadPackage(){
//		this(null);
//	}
//
//	public FileDownloadPackage(String key){
//		super("");
//		this.key = key;
//		this.setRequestHandle(HttpRequestHandle);
//	}
//
//	public String getKey() {
//		return key;
//	}
//
//	public void setKey(String key) {
//		this.key = key;
//	}
//}

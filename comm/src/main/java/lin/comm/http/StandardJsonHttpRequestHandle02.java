package lin.comm.http;

import java.util.List;

import lin.comm.Constants;
import lin.util.JsonUtil;
import lin.util.Utils;
import lin.util.reflect.PropertyOperator;


/**
 * 
 * @author lin
 * @date Mar 8, 2015 3:01:41 PM
 *
 *
 */
public class StandardJsonHttpRequestHandle02 extends AbstractHttpRequestHandle{

	public static class ResultData<T>{
		private long code;
		private long sequeueid;// { get; set; }
	        //public object result { get; set; }
		private String message;// { get; set; }
		private List<Error> warning;// { get; set; }

		private String cause;// { get; set; }

		private String stackTrace;// { get; set; }

		private int dataType ;//{ get; set; }
		
		private T result;
	        
		public long getCode() {
			return code;
		}
		public void setCode(long code) {
			this.code = code;
		}
		public long getSequeueid() {
			return sequeueid;
		}
		public void setSequeueid(long sequeueid) {
			this.sequeueid = sequeueid;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public List<Error> getWarning() {
			return warning;
		}
		public void setWarning(List<Error> warning) {
			this.warning = warning;
		}
		public String getCause() {
			return cause;
		}
		public void setCause(String cause) {
			this.cause = cause;
		}
		public String getStackTrace() {
			return stackTrace;
		}
		public void setStackTrace(String stackTrace) {
			this.stackTrace = stackTrace;
		}
		public int getDataType() {
			return dataType;
		}
		public void setDataType(int dataType) {
			this.dataType = dataType;
		}
		public T getResult() {
			return result;
		}
		public void setResult(T result) {
			this.result = result;
		}
	}

	public void preprocess(HttpPackage pack,HttpCommunicate.Params params){
		params.addHeader(Constants.HTTP_COMM_PROTOCOL,"0.2");

		if(params.isDebug()){
			params.addHeader(Constants.HTTP_COMM_PROTOCOL_DEBUG,"");
		}
	}

	public void error(HttpPackage pack, byte[] bytes, ResultListener listener){
		Error error = null;
		try {
			String resp = new String(bytes, "utf-8");
			ResultData resultData = JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, pack.getRespType()));

			error = new Error(resultData.code,
					resultData.getMessage(),
					resultData.getCause(),
					resultData.getStackTrace());
			error.setWarning(resultData.warning);
		}catch (Throwable e){
			error = new Error(-5,"",e.getMessage(), Utils.printStackTrace(e));
		}
		listener.fault(error);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void response(HttpPackage pack,HttpClientResponse response, byte[] bytes, ResultListener listener) {

		if (response.getStatusCode() == 200) {
			if(response.getHeader(Constants.HTTP_COMM_WITH_WARNING) != null) {
				resultWithWarning(pack, bytes, listener);
			}else{
				result(pack, bytes, listener);
			}
		}else if(response.getStatusCode() == 600) {
			error(pack, bytes, listener);
		}else{
			listener.fault(new Error(-4,"未知错误","",""));
		}
	}

	public void resultWithWarning(HttpPackage pack, byte[] bytes, ResultListener listener) {

		Object obj = null;
		List<Error> warning = null;
		Error error = null;
		try{
			String resp = new String(bytes,"utf-8");

			ResultData resultData = JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, pack.getRespType()));

			obj = resultData.getResult();
			warning = resultData.getWarning();

		}catch(Throwable e){
			error = new Error(-1,null,null,lin.util.Utils.printStackTrace(e));
		}
		if(error != null){
			HttpUtils.fireFault(listener, error);
		}else{
			HttpUtils.fireResult(listener,obj,warning);
		}
	}

	public void result(HttpPackage pack, byte[] bytes, ResultListener listener){

		Object obj = null;
		List<Error> warning = null;
		Error error = null;
		try{
			String resp = new String(bytes,"utf-8");
			obj = JsonUtil.deserialize(resp, pack.getRespType());
		}catch(Throwable e){
			error = new Error(-1,null,null,lin.util.Utils.printStackTrace(e));
		}
		if(error != null){
			HttpUtils.fireFault(listener, error);
		}else{
			HttpUtils.fireResult(listener,obj,warning);
		}
	}

}

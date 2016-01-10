package lin.client.http;

import java.util.List;

import lin.util.JsonUtil;
import lin.util.reflect.PropertyOperator;


/**
 * 
 * @author lin
 * @date Mar 8, 2015 3:01:41 PM
 *
 *
 */
public class StandardJsonHttpRequestHandle extends AbstractHttpRequestHandle{

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


	@SuppressWarnings("unchecked")
	@Override
	public void response(HttpPackage pack, byte[] bytes, ResultListener listener) {
		
		Object obj = null;
		List<Error> warning = null;
		Error error = null;
		try{
			String resp = new String(bytes,"utf-8");
//			obj = lin.util.JsonUtil.deserialize(resp, pack.getRespType());
//			obj  = lin.util.json.JSONUtil.deserialize(resp);
//			ResultData resultData = (ResultData) lin.util.json.JSONUtil.deserialize(obj,ResultData.class);
			@SuppressWarnings("rawtypes")
			ResultData resultData = JsonUtil.deserialize(resp, new JsonUtil.GeneralType(ResultData.class, pack.getRespType()));
			///ResultData resltData = (ResultData) ad.util.json.JSONUtil.deserialize(resp, ResultData.class);
			if(resultData.code <0){
//				error = new Error();
//				error.setCode(resultData.code);
//				error.setCause(resultData.cause);
//				error.setMessage(resultData.message);
//				error.setStackTrace(resultData.stackTrace);
				error = new Error(resultData.code,
						resultData.getMessage(),
						resultData.getCause(),
						resultData.getStackTrace());
				PropertyOperator.copy(resultData, error);
			}else{
//				Map<String,Object> map = (Map<String, Object>) obj;
				//obj = lin.util.json.JSONUtil.deserialize(map.get("result"), pack.getRespType());
//				Object obj2 = resultData.getResult();
				obj = resultData.getResult();
				warning = resultData.getWarning();
			}
		}catch(Throwable e){
			e.printStackTrace();
			error = new Error(-1,null,null,null);
//			error.setCode(-1);
			//return;
		}
		if(error != null){
			HttpUtils.fireFault(listener, error);
		}else{
			HttpUtils.fireResult(listener,obj,warning);
		}
	}

}

package lin.comm.tcp;

import lin.util.JsonUtil;

import java.io.UnsupportedEncodingException;

/// <summary>
/// 请求错误响应包
/// </summary>
public class ErrorTcpPackage extends ResponseTcpPackage {
	@Override
	public final byte getType() {
		return 0;
	}

	private Data data;

	// private byte _type;
	@Override
	public final byte[] write() {

		try {
			return JsonUtil.serialize(data).getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ErrorTcpPackage(long code, String message){

		data = new Data();
		data.setCode(code);
		data.setMessage(message);

	}

	public ErrorTcpPackage(long code, String message, String cause){

		data = new Data();
		data.setCode(code);
		data.setMessage(message);
		data.setCause(cause);

	}

	public ErrorTcpPackage(long code, String message, String cause, String detial){

		data = new Data();
		data.setCode(code);
		data.setMessage(message);
		data.setCause(cause);
		data.setDetial(detial);
	}

	ErrorTcpPackage(Data data){
		this.data = data;
	}

	public long getCode() {
		return data.getCode();
	}

	public String getMessage() {
		return data.getMessage();
	}

	public String getCause() {
		return data.getCause();
	}

	public String getDetial() {
		return data.getDetial();
	}

	class Data{
		private long code;
		private String message;
		private String cause;
		private String detial;

		public long getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public String getCause() {
			return cause;
		}

		public String getDetial() {
			return detial;
		}

		public void setCode(long code) {
			this.code = code;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setCause(String cause) {
			this.cause = cause;
		}

		public void setDetial(String detial) {
			this.detial = detial;
		}
	}
}

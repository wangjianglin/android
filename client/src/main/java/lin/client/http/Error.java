package lin.client.http;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 下午12:02:43
 *
 */
public class Error {

	/**
	 * 产生错误的具体原因，程序层面的，如：NullPointException
	 */
	private String cause;

	/**
	 * 错误的唯一标识码，用于精确定位错误源
	 */
	private long code;
	/**
	 * 产生的错误消息，用于展示给用户的，如：密码错误
	 */
	private String message;

	/**
	 * 用跟踪错误的堆栈信息
	 */
	private String stackTrace;

	/**
	 * 错误发生时，伴随的与错误相关的数据
	 */
	private Object data;

	/**
	 * 错误类型,0、由后台业务逻辑错误产生，1、后台请求前置验证不通过产生的错误，2、HTTP请求由于网络方面的原因产生的错误
	 */
	private int type = 3;

	public Error(long code,String message,String cause,String stackTrace){
		this.code = code;
		this.message = message;
		this.cause = cause;
		this.stackTrace = stackTrace;
	}

	public Error(long code,String message,String cause,String stackTrace,int type,Object data){
		this.code = code;
		this.message = message;
		this.cause = cause;
		this.stackTrace = stackTrace;
		this.type = type;
		this.data = data;
	}
    
	public long getCode() {
		return code;
	}

	void setCode(long code) {
		this.code = code;
	}

	public String getCause() {
		return cause;
	}

	void setCause(String cause) {
		this.cause = cause;
	}

	public String getMessage() {
		return message;
	}

	void setMessage(String message) {
		this.message = message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public Object getData() {
		return data;
	}

	void setData(Object data) {
		this.data = data;
	}

	public int getType() {
		return type;
	}

	void setType(int type) {
		this.type = type;
	}
	
}

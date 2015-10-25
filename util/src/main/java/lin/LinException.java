package lin;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author lin
 * @date 2012-3-20 下午3:43:16
 *
 *
 */
public class LinException extends java.lang.RuntimeException{

	/**
	 * 记录警告信息
	 */
	private static ThreadLocal<List<LinException>> warnings = new ThreadLocal<List<LinException>>(){
		
		/**
		 * 设置默认值
		 */
		@Override
		public List<LinException> initialValue(){
			List<LinException> result = super.initialValue();
			if(result == null){
				result  = new ArrayList<LinException>();
			}
			return result;
		}
	};
	
	/**
	 * 获取当前的警告记录列表
	 * 
	 * @return
	 */
	public static List<LinException> get(){
		return warnings.get();
	}
	
	/**
	 * 添加一个警告信息
	 * 
	 * @param warning
	 */
	public static void add(LinException warning){
		warnings.get().add(warning);
	}
	
	/**
	 * 设置警告信息列表
	 * 
	 * @param list
	 */
	public static void set(List<LinException> list){
		warnings.set(list);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


private static Map<Long,String> messages = new HashMap<Long,String>();
	
	static{
		try {
			Enumeration<URL> urls = LinException.class.getClassLoader().getResources("META-INF/ad.exception.code.properties");
			URL tmpUrl = null;
			java.util.Properties prop = null;
			while(urls.hasMoreElements()){
				tmpUrl = urls.nextElement();
				prop = new java.util.Properties();
				//prop.load(tmpUrl.openStream());
				prop.load(new java.io.DataInputStream(tmpUrl.openStream()));
				for(Map.Entry<Object,Object> entity : prop.entrySet()){
					try{
						messages.put(Long.parseLong(entity.getKey().toString().replace("_", "").replace("0x", "").replace("0X", ""),16), entity.getValue().toString());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static String getMessage(long code){
		return messages.get(code);
	}
	private long code;
	
	public LinException(long code) {
		super();
		this.code = code;
	}

	public LinException(long code,String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public LinException(long code,String message) {
		super(message);
		this.code = code;
	}

	public LinException(long code,Throwable cause) {
		super(cause);
		this.code = code;
	}

	public long getCode() {
		return code;
	}

	public static void claer() {
		if(warnings.get()!=null){
			warnings.get().clear();
		}
	}

//	public void setCode(long code) {
//		this.code = code;
//	}
}

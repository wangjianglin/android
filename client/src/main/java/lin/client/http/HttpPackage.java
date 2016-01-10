package lin.client.http;

import org.apache.http.entity.ContentType;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import lin.client.http.annotation.HttpFileInfo;
import lin.client.http.annotation.HttpPackageMethod;
import lin.client.http.annotation.HttpPackageReturnType;
import lin.client.http.annotation.HttpPackageUrl;
import lin.client.http.annotation.HttpParamName;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 上午11:58:23
 *
 */
public abstract class HttpPackage {
	public static final HttpRequestHandle JSON = new EncryptJsonHttpRequestHandle();
	public static final HttpRequestHandle STANDARD_JSON = new StandardJsonHttpRequestHandle();
    public static final HttpRequestHandle NONE = new NoneHttpRequestHandle();
//    private UrlType urlType = UrlType.RELATIVE;// { get; protected set; }
    
    //private static final Class<?> _DefautlRespType = String.class;

    static
    {
        
        //如果没有配置主版，则默认为无版本信息

    }

    /// <summary>
    /// 是否启用缓存，默认不启用
    /// </summary>
    //[DefaultValue(false)]
    private boolean enableCache;// { get; protected set; }
	private HttpRequestHandle requestHandle = STANDARD_JSON;
	private boolean multipart = false;

	private HttpMethod method = HttpMethod.POST;
	private Type respType  = String.class;//{ get;protected set; }
    
    public HttpPackage(){
    	this.init();
    }
    
    private void init(){
		Class<?> cls = this.getClass();
    	HttpPackageUrl urla = cls.getAnnotation(HttpPackageUrl.class);
    	//this(urla.value());

    	if(urla != null){
    		this.url = urla.value();
    	}
    	HttpPackageMethod methoda = cls.getAnnotation(HttpPackageMethod.class);
    	if(methoda != null){
    		this.method = methoda.value();
    	}else{
//			this.method = HttpMethod.POST;
		}
    	
    	final HttpPackageReturnType methodt = cls.getAnnotation(HttpPackageReturnType.class);
    	if(methodt != null){
    		final Class<?>[] ptypes = methodt.parameterizedType();
    		if(ptypes == null || ptypes.length == 0){
    			this.respType = methodt.value();
    		}else{
    			this.respType = new ParameterizedType(){

    				@Override
    				public Type[] getActualTypeArguments() {
    					return ptypes;
    				}

    				@Override
    				public Type getOwnerType() {
    					return null;
    				}

    				@Override
    				public Type getRawType() {
    					return methodt.value();
    				}
    			};
    		}
    	}
    	
    }



    public HttpPackage(String url){
    	this(url,HttpMethod.POST);
    }

    public HttpPackage(String url,HttpMethod method)
    {
    	this.init();
    	this.url = url;
    	this.method = method;
        //EnableCache = false;
//        UrlType = UrlType.RELATIVE;
//        this.RequestHandle = JSON;
//        EnableCache = false;
//        //HasParams = true;
//        this.RespType = _DefautlRespType;
//        this.Version = new Version();
//        this.Version.Major = 0;
//        this.Version.Minor = 0;
    }
    private String url;//{ get; set; }

    /// <summary>
    /// 数据包的版本号
    /// </summary>
//    private Version version = new Version(0,0);// { get; protected set; }


    /// <summary>
    /// 表示是否需要进行参数设置
    /// </summary>
    //public virtual bool HasParams { get; protected set; }

    public HttpRequestHandle getRequestHandle() { 
    	return requestHandle;
}
    protected void setRequestHandle(HttpRequestHandle handle) {
		this.requestHandle = handle;
	}

    public Map<String, Object> getParams()
    {
    	Class<?> cls = this.getClass();
    	Field[] fs = cls.getDeclaredFields();
		HttpParamName item = null;
		Object paramValue;
		String paramName = null;
		FileParamInfo contentBody = null;
		String fileName = null;
//		ContentType mimeType = ContentType.DEFAULT_BINARY;
		String mineType = "application/octet-stream";
		HttpFileInfo fileInfo;
		Map<String,Object> params = new HashMap<String,Object>();
    	for(Field f : fs){
    		item = f.getAnnotation(HttpParamName.class);
    		if(item == null){
    			continue;
    		}
    		paramName = item.value();
    		if(paramName == null || "".equals(paramName)){
    			paramName = f.getName();
    		}
    		f.setAccessible(true);
    		try {
//    			ContentBody f2;
//    			ByteArrayBody b = new ByteArrayBody(data, filename);
//    			FileBody fb = new FileBody(file);
//    			InputStreamBody ib = new InputStreamBody();
//    			StringBody sb = new StringBody();
    			
    			paramValue = f.get(this);
				if(paramValue == null){
					params.put(paramName,"");
					continue;
				}
    			if(paramValue instanceof String || paramValue.getClass().isPrimitive()){// || paramValue instanceof ContentBody){
    				params.put(paramName, paramValue);
    				continue;
    			}
    			fileInfo = f.getAnnotation(HttpFileInfo.class);
    			if(fileInfo != null){
    				fileName = fileInfo.name();
//    				mimeType = new ContentType(fileInfo.mimeType());
    			}else{
    				fileName = paramName;
//    				mimeType = null;
    			}
    			if(paramValue instanceof byte[]){
    				//contentBody = new ByteArrayBody((byte[]) paramValue,mimeType,fileName);
					contentBody = new FileParamInfo();
					contentBody.setFile((byte[])paramValue);
					contentBody.setMimeType(mineType);
					contentBody.setFileName(fileName);
    				multipart = true;
    			}else if(paramValue instanceof java.io.File){
    				//contentBody = new FileBody((File) paramValue,mimeType,((File) paramValue).getName());
//					contentBody = new FileBody((File) paramValue);
					contentBody = new FileParamInfo();
					contentBody.setFile((File)paramValue);
					contentBody.setMimeType(mineType);
					contentBody.setFileName(fileName);
    				multipart = true;
    			}else if(paramValue instanceof java.io.InputStream){
//    				contentBody = new InputStreamBody((InputStream) paramValue,mimeType,fileName);
					contentBody = new FileParamInfo();
					contentBody.setFile((InputStream)paramValue);
					contentBody.setMimeType(mineType);
					contentBody.setFileName(fileName);
    				multipart = true;
    			}else{
    				params.put(paramName, paramValue.toString());
    				continue;
    			}
				params.put(paramName, contentBody);
			} catch (Throwable e) {
				e.printStackTrace();
			}
    	}
        return params;
    }
    public HttpMethod getMethod(){
    	return method;
    }
//	public UrlType getUrlType() {
//		return urlType;
//	}
//	public void setUrlType(UrlType urlType) {
//		this.urlType = urlType;
//	}
	public boolean isEnableCache() {
		return enableCache;
	}
	protected void setEnableCache(boolean enableCache) {
		this.enableCache = enableCache;
	}
	public String getUrl() {
		return url;
	}
	
//	private void setUri(String uri) {
//		this.uri = uri;
//	}
	public Type getRespType() {
		return respType;
	}
	protected void setRespType(Type respType) {
		this.respType = respType;
	}
//	public Version getVersion() {
//		return version;
//	}
//	protected void setVersion(Version version) {
//		this.version = version;
//	}

	public boolean isMultipart(){
		return multipart;
	}
}

//
//	public Map<String, Object> getParams()
//	{
//		Class<?> cls = this.getClass();
//		Field[] fs = cls.getDeclaredFields();
//		HttpParamName item = null;
//		Object paramValue;
//		String paramName = null;
//		ContentBody contentBody = null;
//		String fileName = null;
//		ContentType mimeType = ContentType.DEFAULT_BINARY;
//		HttpFileInfo fileInfo;
//		Map<String,Object> params = new HashMap<String,Object>();
//		for(Field f : fs){
//			item = f.getAnnotation(HttpParamName.class);
//			if(item == null){
//				continue;
//			}
//			paramName = item.value();
//			if(paramName == null || "".equals(paramName)){
//				paramName = f.getName();
//			}
//			f.setAccessible(true);
//			try {
////    			ContentBody f2;
////    			ByteArrayBody b = new ByteArrayBody(data, filename);
////    			FileBody fb = new FileBody(file);
////    			InputStreamBody ib = new InputStreamBody();
////    			StringBody sb = new StringBody();
//
//				paramValue = f.get(this);
//				if(paramValue == null || paramValue instanceof String || paramValue.getClass().isPrimitive() || paramValue instanceof ContentBody){
//					params.put(paramName, paramValue);
//					continue;
//				}
//				fileInfo = f.getAnnotation(HttpFileInfo.class);
//				if(fileInfo != null){
//					fileName = fileInfo.name();
////    				mimeType = new ContentType(fileInfo.mimeType());
//				}else{
//					fileName = paramName;
////    				mimeType = null;
//				}
//				if(paramValue instanceof byte[]){
//					contentBody = new ByteArrayBody((byte[]) paramValue,mimeType,fileName);
//					multipart = true;
//				}else if(paramValue instanceof java.io.File){
//					//contentBody = new FileBody((File) paramValue,mimeType,((File) paramValue).getName());
//					contentBody = new FileBody((File) paramValue);
//					multipart = true;
//				}else if(paramValue instanceof java.io.InputStream){
//					contentBody = new InputStreamBody((InputStream) paramValue,mimeType,fileName);
//					multipart = true;
//				}else{
//					params.put(paramName, paramValue);
//					continue;
//				}
//				params.put(paramName, contentBody);
//			} catch (Throwable e) {
//				e.printStackTrace();
//			}
//		}
//		return params;
//	}

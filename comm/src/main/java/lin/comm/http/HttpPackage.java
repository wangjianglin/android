package lin.comm.http;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import lin.comm.http.annotation.HttpFileInfo;
import lin.comm.http.annotation.HttpHeaderName;
import lin.comm.http.annotation.HttpPackageMethod;
import lin.comm.http.annotation.HttpPackageReturnType;
import lin.comm.http.annotation.HttpPackageUrl;
import lin.comm.http.annotation.HttpParamName;
import lin.comm.http.annotation.HttpPath;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 上午11:58:23
 *
 */
public abstract class HttpPackage<T> {
	public static final HttpRequestHandle JSON = new EncryptJsonHttpRequestHandle();
	public static final HttpRequestHandle STANDARD_JSON = new StandardJsonHttpRequestHandle02();
	public static final HttpRequestHandle NONE = new NoneHttpRequestHandle();
	public static final HttpRequestHandle NORMAL = new NormalHttpRequestHandle();


    static
    {

    }

    /// <summary>
    /// 是否启用缓存，默认不启用
    /// </summary>
    //[DefaultValue(false)]
    private boolean mEnableCache;// { get; protected set; }
	private HttpRequestHandle mRequestHandle = STANDARD_JSON;
	private boolean mMultipart = false;

	private HttpMethod mMethod = HttpMethod.POST;
	private Type mRespType  = String.class;//{ get;protected set; }

	private Map<String,String> mHeaders = new HashMap<String, String>();

	private HttpCommParams mCommParams = new HttpCommParams();
    public HttpPackage(){
    	this.init();
    }
    
    private void init(){
		Class<?> cls = this.getClass();
    	HttpPackageUrl urla = cls.getAnnotation(HttpPackageUrl.class);

    	if(urla != null){
    		this.mOriginUrl = urla.value();
    		this.mUrl = urla.value();
    	}
    	HttpPackageMethod methoda = cls.getAnnotation(HttpPackageMethod.class);
    	if(methoda != null){
    		this.mMethod = methoda.value();
    	}
    	
    	final HttpPackageReturnType methodt = cls.getAnnotation(HttpPackageReturnType.class);
    	if(methodt != null){
    		final Class<?>[] ptypes = methodt.parameterizedType();
    		if(ptypes == null || ptypes.length == 0){
    			this.mRespType = methodt.value();
    		}else{
    			this.mRespType = new ParameterizedType(){

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
    	this.mOriginUrl = url;
    	this.mUrl = url;
    	this.mMethod = method;
    }

    private String mOriginUrl;
    private String mUrl;

    public HttpRequestHandle getRequestHandle() { 
    	return mRequestHandle;
}
    protected void setRequestHandle(HttpRequestHandle handle) {
		this.mRequestHandle = handle;
	}


	private void processHttpPath(Field f){
		HttpPath path = f.getAnnotation(HttpPath.class);
		if(path == null){
			return;
		}
		String pathName = path.value();
		if(path == null || "".equals(pathName)){
			pathName=f.getName();
		}

		f.setAccessible(true);
		try {
			Object pathValue = f.get(this);
			this.mUrl = this.mUrl.replaceAll(":"+pathName,pathValue != null?pathValue.toString():"");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	private void processHttpParams(Map<String,Object> params,Field f){
		HttpParamName item = f.getAnnotation(HttpParamName.class);
		if(item == null){
			return;
		}
		String paramName = item.value();
		if(paramName == null || "".equals(paramName)){
			paramName = f.getName();
		}
		f.setAccessible(true);
		try {
			Object paramValue = f.get(this);
			if(paramValue == null){
				return;
			}
			if(paramValue instanceof String
					|| paramValue.getClass().isEnum()
					|| paramValue.getClass().isPrimitive()
					|| Number.class.isAssignableFrom(paramValue.getClass())){// || paramValue instanceof ContentBody){
				params.put(paramName, paramValue);
				return;
			}

			HttpFileInfo fileInfo = f.getAnnotation(HttpFileInfo.class);
			String fileName;
			if(fileInfo != null){
				fileName = fileInfo.name();
			}else{
				fileName = paramName;
			}
			FileParamInfo contentBody = null;
			String mineType = "application/octet-stream";
			if(paramValue instanceof byte[]){
				contentBody = new FileParamInfo();
				contentBody.setFile((byte[])paramValue);
				contentBody.setMimeType(mineType);
				contentBody.setFileName(fileName);
				mMultipart = true;
			}else if(paramValue instanceof java.io.File){
				contentBody = new FileParamInfo();
				contentBody.setFile((File)paramValue);
				contentBody.setMimeType(mineType);
				contentBody.setFileName(fileName);
				mMultipart = true;
			}else if(paramValue instanceof java.io.InputStream){
				contentBody = new FileParamInfo();
				contentBody.setFile((InputStream)paramValue);
				contentBody.setMimeType(mineType);
				contentBody.setFileName(fileName);
				mMultipart = true;
			}else{
				params.put(paramName, paramValue);
				return;
			}
			params.put(paramName, contentBody);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
    public Map<String, Object> getParams()
    {
    	Class<?> cls = this.getClass();
    	Field[] fs = cls.getDeclaredFields();

		HttpFileInfo fileInfo;
		Map<String,Object> params = new HashMap<String,Object>();
		this.mUrl = this.mOriginUrl;
    	for(Field f : fs){
			processHttpParams(params,f);
			processHttpPath(f);
    	}
        return params;
    }
    public HttpMethod getMethod(){
    	return mMethod;
    }
	public boolean isEnableCache() {
		return mEnableCache;
	}
	protected void setEnableCache(boolean enableCache) {
		this.mEnableCache = enableCache;
	}
	public String getUrl() {
		return mUrl;
	}

	public Type getRespType() {
		return mRespType;
	}
	protected void setRespType(Type respType) {
		this.mRespType = respType;
	}

	public HttpCommParams getCommParams() {
		return mCommParams;
	}

	public boolean isMultipart(){
		return mMultipart;
	}

	private Map<String,String> getAnonHeaders(){
		Class<?> cls = this.getClass();
		Field[] fs = cls.getDeclaredFields();
		HttpHeaderName item;
		String paramName;

		Map<String,String> headers = new HashMap<String,String>();
		for(Field f : fs) {
			item = f.getAnnotation(HttpHeaderName.class);
			if (item == null) {
				continue;
			}
			paramName = item.value();
			if(paramName == null || "".equals(paramName.trim())){
				paramName = f.getName();
			}
			try {
				headers.put(paramName, (String) f.get(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return headers;
	}
	public void addHeader(String name,String value){
		mHeaders.put(name,value);
	}
	public Map<String,String> getHeaders(){
		Map<String,String> r = new HashMap<String,String>();
		Map<String,String> h = getAnonHeaders();
		r.putAll(mHeaders);
		r.putAll(h);
		return r;
	}
}


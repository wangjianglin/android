package io.cess.util;

import android.annotation.SuppressLint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cess.util.beans.BeanInfo;
import io.cess.util.beans.Introspector;
import io.cess.util.beans.PropertyDescriptor;


/**
 * 
 * @author lin
 * @date 2012-3-13 下午8:56:19
 *
 * 用于序列化与反序列化对象，能够把Java对象序列化成JSON格式的字符串，也可以把JSON格式的字符串转换成Java对象
 *
 */
public class JsonUtil {
	
	public static class GeneralType implements ParameterizedType{

		private Type rawType;
		private Type[] actualTypes;
		
		public GeneralType(Type rawType,Type ... actualTypes){
			this.rawType = rawType;
			this.actualTypes = actualTypes;
		}
		@Override
		public Type[] getActualTypeArguments() {
			return actualTypes;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

		@Override
		public Type getRawType() {
			return rawType;
		}
	}
	
	//日期类型数据序列化格式
	public final static String RFC3339_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 把一个Java对象序列化成JSON格式的字符串
     * 
     * @param object
     *            to be serialized
     * @return JSON string
     * @throws JSONException
     */
    public static String serialize(Object object) {
        return com.alibaba.fastjson.JSON.toJSONString(object);
    }

    public static Object deserialize(String json) {
    	return com.alibaba.fastjson.JSON.parse(json);
    }
    
    public static <T> T deserialize(InputStream inputStream,Type type){
    	ByteArrayOutputStream _out = new ByteArrayOutputStream();
    	byte[] bs = new byte[1024];
    	int count = 0;
    	try {
			while((count = inputStream.read(bs)) != -1){
				_out.write(bs, 0, count);
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
    	
    	return deserialize(_out.toString(),type);
    }
    
@SuppressWarnings("unchecked")
public static <T> T deserialize(String json,Type type){
    	
	return (T)com.alibaba.fastjson.JSON.parseObject(json, type);
    }


    //--------------------------------------------------------------------
	public static Map<String,String> toParameters(String json){
		Map<String,String> result = new HashMap<String, String>();
//		try {
			Object obj = deserialize(json);
			if(obj == null){
				//throw new LinException(0xA00103,"客户端向服务器传输的数据为空！");
			}
			if(obj instanceof Map){
				@SuppressWarnings("unchecked")
				Map<String,String> tmp = (Map<String,String>)obj;
//				validateData(tmp);
				//processesParameters(tmp.get("data"),result,null);
				processesParameters(tmp,result,null);
				//ActionContext.getContext().put(sequeueid, tmp.get(sequeueid));
			}
//		} catch (JSONException e) {
//			System.out.println("json:"+json);
//			e.printStackTrace();
//			//throw new LinException(0xA00101,"数据格式异常！");
//		}
		return result;
	}
	
	public static Map<String,String> toParameters(Object obj){
		Map<String,String> map = new HashMap<String,String>();
		processesParameters(obj,map,null);
		return map;
	}
	@SuppressLint("SimpleDateFormat")
	private static Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	private static void processesParameters(Object obj,Map<String,String> map,String pre){
		if(obj == null){
			return;
		}
		Class<?> type = obj.getClass();

		if(Date.class.isAssignableFrom(type)){
			
			map.put(pre,format.format(obj));
			return;
		}

		if(type.isPrimitive()
				|| type.isEnum()
				|| Number.class.isAssignableFrom(type)
				|| String.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type)){
			map.put(pre, obj+"");
			return;
		}

		//如果是一个对象
		if(obj instanceof Map){
			@SuppressWarnings("unchecked")
			Map<String,Object> tmpMap = (Map<String, Object>) obj;
			if(pre == null){
				for(String key : tmpMap.keySet()){
					processesParameters(tmpMap.get(key),map,key);
				}
			}else{
				for(String key : tmpMap.keySet()){
					processesParameters(tmpMap.get(key),map,pre == null ? key : pre+"."+key);
				}
			}
		}
		//如果是一个数组
		else if(obj instanceof List){
			if(pre == null){
				//抛出异常
//				for(Object key : (List<Object>)obj){
//					processesParameters(obj,map,key);
//				}
			}else{
				@SuppressWarnings("rawtypes")
				List tmpList = (List) obj;
				int n = 0;
				for(Object tmp : tmpList){
					processesParameters(tmp,map,pre == null ? "["+n+"]" : pre + "["+n+"]");
					n++;
				}
			}
		}else{
			
			BeanInfo infos = Introspector.getBeanInfo(type);
			
			for(PropertyDescriptor pd : infos.getPropertyDescriptors()){
					//pd.setValue(pd.getName(), deserializeImpl(map.get(pd.getName()),pd.getPropertyType()));
				if(pd.getReadMethod() == null){
					continue;
				}
				try {
//					map.putAll(pre+"."+pd.getName(), pd.getReadMethod().invoke(obj));
					processesParameters(pd.getReadMethod().invoke(obj),map,pre == null ? pd.getName() : pre+"."+pd.getName());
				
//					if(pd.getWriteMethod() != null){

				} catch (Throwable e) {
//						e.printStackTrace();
				}
			}
		}
	}
	//---------------------------------------------------------------------
}

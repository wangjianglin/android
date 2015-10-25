package lin.util.json;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lin.util.beans.BeanInfo;
import lin.util.beans.Introspector;
import lin.util.beans.PropertyDescriptor;


/**
 * 
 * @author lin
 * @date 2012-3-13 下午8:56:19
 *
 * 用于序列化与反序列化对象，能够把Java对象序列化成JSON格式的字符串，也可以把JSON格式的字符串转换成Java对象
 *
 */
@Deprecated
public class JSONUtil {
	//日期类型数据序列化格式
	final static String RFC3339_FORMAT = "yyyy-MM-dd HH:mm:ss";
//    private static final Logger LOG = LoggerFactory.getLogger(JSONUtil.class);

    /**
     * 把一个Java对象序列化成JSON格式的字符串
     * 
     * @param object
     *            to be serialized
     * @return JSON string
     * @throws JSONException
     */
    public static String serialize(Object object) throws JSONException {
        JSONWriter writer = new JSONWriter();

        return writer.write(object);
    }

    /**
     * Serializes an object into JSON, excluding any properties matching any of
     * the regular expressions in the given collection.
     * 
     * @param object
     *            to be serialized
     * @param excludeProperties
     *            Patterns matching properties to exclude
     * @param ignoreHierarchy
     *            whether to ignore properties defined on base classes of the
     *            root object
     * @return JSON string
     * @throws JSONException
     */
    public static String serialize(Object object, Collection<Pattern> excludeProperties,
            Collection<Pattern> includeProperties, boolean ignoreHierarchy, boolean excludeNullProperties)
            throws JSONException {
        JSONWriter writer = new JSONWriter();
        writer.setIgnoreHierarchy(ignoreHierarchy);
        return writer.write(object, excludeProperties, includeProperties, excludeNullProperties);
    }

    /**
     * Serializes an object into JSON, excluding any properties matching any of
     * the regular expressions in the given collection.
     * 
     * @param object
     *            to be serialized
     * @param excludeProperties
     *            Patterns matching properties to exclude
     * @param ignoreHierarchy
     *            whether to ignore properties defined on base classes of the
     *            root object
     * @param enumAsBean
     *            whether to serialized enums a Bean or name=value pair
     * @return JSON string
     * @throws JSONException
     */
    public static String serialize(Object object, Collection<Pattern> excludeProperties,
            Collection<Pattern> includeProperties,String dateFormat, boolean ignoreHierarchy, boolean enumAsBean,
            boolean excludeNullProperties) throws JSONException {
        JSONWriter writer = new JSONWriter();
        writer.setIgnoreHierarchy(ignoreHierarchy);
        writer.setEnumAsBean(enumAsBean);
        writer.setDateFormatString(dateFormat);
        return writer.write(object, excludeProperties, includeProperties, excludeNullProperties);
    }

    /**
     * Serializes an object into JSON to the given writer.
     * 
     * @param writer
     *            Writer to serialize the object to
     * @param object
     *            object to be serialized
     * @throws IOException
     * @throws JSONException
     */
    public static void serialize(Writer writer, Object object) throws IOException, JSONException {
        writer.write(serialize(object));
    }

    /**
     * Serializes an object into JSON to the given writer, excluding any
     * properties matching any of the regular expressions in the given
     * collection.
     * 
     * @param writer
     *            Writer to serialize the object to
     * @param object
     *            object to be serialized
     * @param excludeProperties
     *            Patterns matching properties to ignore
     * @throws IOException
     * @throws JSONException
     */
    public static void serialize(Writer writer, Object object, Collection<Pattern> excludeProperties,
            Collection<Pattern> includeProperties, boolean excludeNullProperties) throws IOException,
            JSONException {
        writer.write(serialize(object, excludeProperties, includeProperties, true, excludeNullProperties));
    }

    /**
     * Deserializes a object from JSON
     * 
     * @param json
     *            string in JSON
     * @return desrialized object
     * @throws JSONException
     */
    public static Object deserialize(String json) throws JSONException {
        JSONReader reader = new JSONReader();
        return reader.read(json);
    }
    
    public static Object deserialize(InputStream inputStream,Type type) throws JSONException{
    	
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
    
public static Object deserialize(String json,Type type) throws JSONException{
    	
    	return deserializeImpl(deserialize(json),type);
    }

public static Object deserialize(Object obj,Type type) throws JSONException{
	
	return deserializeImpl(obj,type);
}
	private static Object deserializeImpl(Object jsonObj,Type type) throws JSONException{
    	if(type instanceof Class<?>){
    		return deserializeClassImpl(jsonObj,(Class<?>)type,null,null);
    	}else if(type instanceof ParameterizedType){
    		return deserializeParameterizedTypeImpl(jsonObj,(ParameterizedType)type);
    	}
    	return null;
    }
	@SuppressWarnings("unchecked")
	private static Object deserializeParameterizedTypeImpl(Object jsonObj,ParameterizedType type) throws JSONException{
		//如果是List对象
		if(jsonObj == null){
			return null;
		}
		Class<?> dataType = jsonObj.getClass();
		Class<?> rawType = (Class<?>) type.getRawType();
		if(List.class.isAssignableFrom(rawType)
				&& List.class.isAssignableFrom(dataType)){
			@SuppressWarnings("rawtypes")
			List list = (List)jsonObj;
			@SuppressWarnings("rawtypes")
			List listlValue = new ArrayList();
			//@SuppressWarnings("rawtypes")
			//List listlValue = new ArrayList();
			//Class<?> t = Class.
			//listVaue.
			for(int n=0;n<list.size();n++){
				listlValue.add(deserializeImpl(list.get(n), type.getActualTypeArguments()[0]));
			}
			return listlValue;
		}
		if(Map.class.isAssignableFrom(rawType)
				&& Map.class.isAssignableFrom(dataType)){
			@SuppressWarnings("rawtypes")
			Map<String,Object> map = (Map)jsonObj;
			@SuppressWarnings("rawtypes")
			Map mapValue = new HashMap();
			//Class<?> t = Class.
			//listVaue.
			for(Map.Entry<String, Object> entry : map.entrySet()){
				mapValue.put(entry.getKey(), deserializeImpl(entry.getValue(), type.getActualTypeArguments()[1]));
			}
			return mapValue;
		}
		return deserializeClassImpl(jsonObj,rawType,type,type.getActualTypeArguments());
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private static java.text.Format dateFormat = new java.text.SimpleDateFormat(RFC3339_FORMAT);
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object deserializeClassImpl(Object jsonObj,Class<?> type,ParameterizedType ptype,Type[] actualTypeArguments) throws JSONException{
    	
    	if(jsonObj == null){
    		return null;
    	}
    	if(type == Date.class){
    		
    		try {
				return dateFormat.parseObject(jsonObj.toString());
			} catch (ParseException e) {
			}
    		return null;
    	}
    	if(type == String.class){
    		if(String.class.isAssignableFrom(type)){
    			return jsonObj;
    		}else{
    			return null;
    		}
    	}
    	if(type == char.class){
    		if(char.class.isAssignableFrom(type)){
    			return jsonObj;
    		}else if(String.class.isAssignableFrom(type)){
    			String tmps = (String)jsonObj;
    			if(tmps.length() == 1){
    				return tmps.charAt(0);
    			}else{
    				return null;
    			}
    		}else{
    			return null;
    		}
    	}
    	if(Enum.class.isAssignableFrom(type)){
    		return Enum.valueOf((Class)type, jsonObj.toString());
    	}
    	//josn对象的类型
    	Class<?> dataType = jsonObj.getClass();
    	if(type.isPrimitive() || java.lang.Number.class.isAssignableFrom(type)){//如果是基本类型
    		java.lang.Number number = (java.lang.Number)jsonObj;
    		//number.
    		if(type == int.class || type == Integer.class){
    			return number.intValue();
    		}else if(type == long.class || type == Long.class){
    			return number.longValue();
    		}else if(type == float.class || type == Float.class){
    			return number.floatValue();
    		}else if(type == double.class || type == Double.class){
    			return number.doubleValue();
    		}else if(type == short.class || type == Short.class){
    			return number.shortValue();
    		}else if(type == byte.class || type == Byte.class){
    			return number.byteValue();
    		}
    	}//if()
    	
    	//如果是一个数组
    	if(type.isArray()){
    		if(List.class.isAssignableFrom(dataType)){
    			List list = (List)jsonObj;
    			Object arrayValue = Array.newInstance(type.getComponentType(), list.size());
    			for(int n=0;n<list.size();n++){
    				Array.set(arrayValue, n, deserializeImpl(list.get(n), type.getComponentType()));
    			}
    			return arrayValue;
    		}
    		return null;
    	}
    	
    	//如果是一个列表
    	if(List.class.isAssignableFrom(type)){
    		if(List.class.isAssignableFrom(dataType)){
    			List list = (List)jsonObj;
    			//@SuppressWarnings("rawtypes")
				//List listlValue = new ArrayList();
    			//Class<?> t = Class.
    			//listVaue.
    			//for(int n=0;n<list.size();n++){
    			//	listlValue.set(n, deserializeImpl(list.get(n), (Class<?>)type.getGenericInterfaces()[0]));
    			//}
    			//return listlValue;
    			return list;
    		}
    		return null;
    	}
    	
    	//如果是一个Map对象
    	
    	if(Map.class.isAssignableFrom(type)){
    		if(Map.class.isAssignableFrom(dataType)){
//    			@SuppressWarnings("rawtypes")
				Map map = (Map)jsonObj;
    			//@SuppressWarnings("rawtypes")
    			//Map mapValue = new HashMap();
    			//Class<?> t = Class.
    			//listVaue.
    			//for(Map.Entry<String, Object> entry : map.entrySet()){
    			//	mapValue.put(entry.getKey(), deserializeImpl(entry.getValue(), (Class<?>)type.getGenericInterfaces()[1]));
    			//}
    			//return mapValue;
    			return map;
    		}
    		return null;
    	}
    	
    	if(Map.class.isAssignableFrom(dataType)){//是一个对象
    		Object value = null;
			try {
				value = type.newInstance();
			} catch (Throwable e) {
				//e.printStackTrace();
				throw new JSONException();
			}
			
			Type valueType = null;
//			@SuppressWarnings("unchecked")
			Map<String,Object> map = (Map<String,Object>)jsonObj;
    		//for(Map.Entry<String, Object> entry : map.entrySet()){
    			
				BeanInfo infos = Introspector.getBeanInfo(type);
				PropertyDescriptor[] pds = infos.getPropertyDescriptors();
				for(PropertyDescriptor pd : pds){
						//pd.setValue(pd.getName(), deserializeImpl(map.get(pd.getName()),pd.getPropertyType()));
					try {
						if(pd.getWriteMethod() != null){

							valueType = pd.getWriteMethod().getGenericParameterTypes()[0];
							if(valueType instanceof ParameterizedType){
								TypeVariable<?>[] typeVars = type.getTypeParameters();
								Type[] valueTypes = ((ParameterizedType) valueType).getActualTypeArguments();
								Type[] valueActualTypes = null;
								if(valueTypes != null && valueTypes.length > 0){
									valueActualTypes = new Type[valueTypes.length];
									for(int n=0;n<valueTypes.length;n++){
										Type var = valueTypes[n];
										if(var instanceof TypeVariable){
											TypeVariable tvar = (TypeVariable) var;
											for(int m=0; m<typeVars.length;m++){
												if(tvar.getName().equals(typeVars[m].getName())){
													valueActualTypes[n] = actualTypeArguments[m];
													break;
												}
											}
										}else{
											valueActualTypes[n] = var;
										}
									}
								}

								ParameterizedType valueParameterizedType = new ValueParameterizedType(((ParameterizedType) valueType).getRawType(),((ParameterizedType) valueType).getOwnerType()
										,valueActualTypes);
				    			pd.getWriteMethod().invoke(value, deserializeImpl(map.get(pd.getName()),valueParameterizedType));
							}else{
								pd.getWriteMethod().invoke(value, deserializeImpl(map.get(pd.getName()),valueType));
							}
						}
					} catch (Throwable e) {
//							e.printStackTrace();
					}
				}
				
    		//}
    			return value;
    	}
    	
    	return null;
    }
    	
    private static class ValueParameterizedType implements ParameterizedType{

    	private Type rawType;
    	private Type ownerType;
    	private Type[] actualType;
    	private ValueParameterizedType(Type rawType,Type ownerType,Type[] actualType){
    		this.rawType = rawType;
    		this.ownerType = ownerType;
    		this.actualType = actualType;
    	}
		@Override
		public Type[] getActualTypeArguments() {
			return this.actualType;
		}

		@Override
		public Type getOwnerType() {
			return ownerType;
		}

		@Override
		public Type getRawType() {
			return this.rawType;
		}
	};
    public static Object deserialize(Reader reader,Class<?> type) throws JSONException{
    
    	return deserializeImpl(deserialize(reader),type);
    }
    /**
     * Deserializes a object from JSON
     * 
     * @param reader
     *            Reader to read a JSON string from
     * @return deserialized object
     * @throws JSONException
     *             when IOException happens
     */
    public static Object deserialize(Reader reader) throws JSONException {
        // read content
        BufferedReader bufferReader = new BufferedReader(reader);
        String line = null;
        StringBuilder buffer = new StringBuilder();

        try {
            while ((line = bufferReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            throw new JSONException(e);
        }

        return deserialize(buffer.toString());
    }

//    public static void writeJSONToResponse(SerializationParams serializationParams) throws IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        if (StringUtils.isNotBlank(serializationParams.getSerializedJSON()))
//            stringBuilder.append(serializationParams.getSerializedJSON());
//
//        if (StringUtils.isNotBlank(serializationParams.getWrapPrefix()))
//            stringBuilder.insert(0, serializationParams.getWrapPrefix());
//        else if (serializationParams.isWrapWithComments()) {
//            stringBuilder.insert(0, "/* ");
//            stringBuilder.append(" */");
//        } else if (serializationParams.isPrefix())
//            stringBuilder.insert(0, "{}&& ");
//
//        if (StringUtils.isNotBlank(serializationParams.getWrapSuffix()))
//            stringBuilder.append(serializationParams.getWrapSuffix());
//
//        String json = stringBuilder.toString();
//
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("[JSON]" + json);
//        }
//
//        HttpServletResponse response = serializationParams.getResponse();
//
//        // status or error code
//        if (serializationParams.getStatusCode() > 0)
//            response.setStatus(serializationParams.getStatusCode());
//        else if (serializationParams.getErrorCode() > 0)
//            response.sendError(serializationParams.getErrorCode());
//
//        // content type
//        if (serializationParams.isSmd())
////            response.setContentType("application/json-rpc;charset=" + serializationParams.getEncoding());
//        	response.setContentType(serializationParams.getContentType() + ";charset="
//                    + serializationParams.getEncoding());
//        else
//            response.setContentType(serializationParams.getContentType() + ";charset="
//                    + serializationParams.getEncoding());
//
//        if (serializationParams.isNoCache()) {
//            response.setHeader("Cache-Control", "no-cache");
//            response.setHeader("Expires", "0");
//            response.setHeader("Pragma", "No-cache");
//        }
//
//        if (serializationParams.isGzip()) {
//            response.addHeader("Content-Encoding", "gzip");
//            GZIPOutputStream out = null;
//            InputStream in = null;
//            try {
//                out = new GZIPOutputStream(response.getOutputStream());
//                in = new ByteArrayInputStream(json.getBytes());
//                byte[] buf = new byte[1024];
//                int len;
//                while ((len = in.read(buf)) > 0) {
//                    out.write(buf, 0, len);
//                }
//            } finally {
//                if (in != null)
//                    in.close();
//                if (out != null) {
//                    out.finish();
//                    out.close();
//                }
//            }
//
//        } else {
//            response.setContentLength(json.getBytes(serializationParams.getEncoding()).length);
//            PrintWriter out = response.getWriter();
//            out.print(json);
//        }
//    }

    public static List<String> asList(String commaDelim) {
        if ((commaDelim == null) || (commaDelim.trim().length() == 0))
            return null;
        List<String> list = new ArrayList<String>();
        String[] split = commaDelim.split(",");
        for (int i = 0; i < split.length; i++) {
            String trimmed = split[i].trim();
            if (trimmed.length() > 0) {
                list.add(trimmed);
            }
        }
        return list;
    }

    /**
     * List visible methods carrying the
     * 
     * @SMDMethod annotation
     * 
     * @param ignoreInterfaces
     *            if true, only the methods of the class are examined. If false,
     *            annotations on every interfaces' methods are examined.
     */
//    public static Method[] listSMDMethods(Class<?> clazz, boolean ignoreInterfaces) {
//        final List<Method> methods = new LinkedList<Method>();
//        if (ignoreInterfaces) {
//            for (Method method : clazz.getMethods()) {
//                SMDMethod smdMethodAnnotation = method.getAnnotation(SMDMethod.class);
//                if (smdMethodAnnotation != null) {
//                    methods.add(method);
//                }
//            }
//        } else {
//            // recurse the entire superclass/interface hierarchy and add in
//            // order encountered
//            JSONUtil.visitInterfaces(clazz, new JSONUtil.ClassVisitor() {
//                public boolean visit(Class<?> aClass) {
//                    for (Method method : aClass.getMethods()) {
//                        SMDMethod smdMethodAnnotation = method.getAnnotation(SMDMethod.class);
//                        if ((smdMethodAnnotation != null) && !methods.contains(method)) {
//                            methods.add(method);
//                        }
//                    }
//                    return true;
//                }
//            });
//        }
//
//        Method[] methodResult = new Method[methods.size()];
//        return methods.toArray(methodResult);
//    }

    /**
     * Realizes the visit(Class) method called by vistInterfaces for all
     * encountered classes/interfaces
     */
    public static interface ClassVisitor {

        /**
         * Called when a new interface/class is encountered
         * 
         * @param aClass
         *            the encountered class/interface
         * @return true if the recursion should continue, false to stop
         *         recursion immediately
         */
        boolean visit(Class<?> aClass);
    }

    /**
     * Visit all the interfaces realized by the specified object, its
     * superclasses and its interfaces <p/> Visitation is performed in the
     * following order: aClass aClass' interfaces the interface's superclasses
     * (interfaces) aClass' superclass superclass' interfaces superclass'
     * interface's superclasses (interfaces) super-superclass and so on <p/> The
     * Object base class is base excluded. Classes/interfaces are only visited
     * once each
     * 
     * @param aClass
     *            the class to start recursing upwards from
     * @param visitor
     *            this vistor is called for each class/interface encountered
     * @return true if all classes/interfaces were visited, false if it was
     *         exited early as specified by a ClassVisitor result
     */
    public static boolean visitInterfaces(Class<?> aClass, ClassVisitor visitor) {
        List<Class<?>> classesVisited = new LinkedList<Class<?>>();
        return visitUniqueInterfaces(aClass, visitor, classesVisited);
    }

    /**
     * Recursive method to visit all the interfaces of a class (and its
     * superclasses and super-interfaces) if they haven't already been visited.
     * <p/> Always visits itself if it hasn't already been visited
     * 
     * @param thisClass
     *            the current class to visit (if not already done so)
     * @param classesVisited
     *            classes already visited
     * @param visitor
     *            this vistor is called for each class/interface encountered
     * @return true if recursion can continue, false if it should be aborted
     */
    private static boolean visitUniqueInterfaces(Class<?> thisClass, ClassVisitor visitor,
            List<Class<?>> classesVisited) {
        boolean okayToContinue = true;

        if (!classesVisited.contains(thisClass)) {
            classesVisited.add(thisClass);
            okayToContinue = visitor.visit(thisClass);

            if (okayToContinue) {
                Class<?>[] interfaces = thisClass.getInterfaces();
                int index = 0;
                while ((index < interfaces.length) && (okayToContinue)) {
                    okayToContinue = visitUniqueInterfaces(interfaces[index++], visitor, classesVisited);
                }

                if (okayToContinue) {
                    Class<?> superClass = thisClass.getSuperclass();
                    if ((superClass != null) && (!Object.class.equals(superClass))) {
                        okayToContinue = visitUniqueInterfaces(superClass, visitor, classesVisited);
                    }
                }
            }
        }
        return okayToContinue;
    }

    //--------------------------------------------------------------------
	public static Map<String,String> toParameters(String json){
		Map<String,String> result = new HashMap<String, String>();
		try {
			Object obj = JSONUtil.deserialize(json);
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
		} catch (JSONException e) {
			System.out.println("json:"+json);
			e.printStackTrace();
			//throw new LinException(0xA00101,"数据格式异常！");
		}
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

		if(type.isPrimitive() || Number.class.isAssignableFrom(type) || String.class.isAssignableFrom(type)){
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

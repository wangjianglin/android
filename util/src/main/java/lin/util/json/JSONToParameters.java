//package lin.util.json;
//
//import android.annotation.SuppressLint;
//import java.util.Date;
//import java.text.Format;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import lin.util.beans.BeanInfo;
//import lin.util.beans.Introspector;
//import lin.util.beans.PropertyDescriptor;
//
//
///**
// * 
// * @author lin
// * @date 2011-5-20
// *
// * 实现把Json字符串转换为param=value形式
// */
//public class JSONToParameters {
//
//	/**
//	 * 防止此类被外部实例化
//	 */
//	private JSONToParameters(){}
//	/**
//	 * 对传输到服务器端的数据进行验证，不能超时，不能重复提交
//	 * @param map
//	 */
////	private static void validateData(Map<String,String> map){
//////		long timestamp = (Long) map.get(JSONToParameters.timestamp);
//////		if(timestamp + timeSpace < (new Date()).getTime()){
//////			throw new LinException(0xA00110);
//////		}
////		//处理重复提交的问题，要存储大量数据，需要大量内存
////		//if(mapTimestamp.get(timestamp) != null){throw "重叠提交数据"}
////	}
//	public static Map<String,String> toParameters(String json){
//		Map<String,String> result = new HashMap<String, String>();
//		try {
//			Object obj = JSONUtil.deserialize(json);
//			if(obj == null){
//				//throw new LinException(0xA00103,"客户端向服务器传输的数据为空！");
//			}
//			if(obj instanceof Map){
//				@SuppressWarnings("unchecked")
//				Map<String,String> tmp = (Map<String,String>)obj;
////				validateData(tmp);
//				//processesParameters(tmp.get("data"),result,null);
//				processesParameters(tmp,result,null);
//				//ActionContext.getContext().put(sequeueid, tmp.get(sequeueid));
//			}
//		} catch (JSONException e) {
//			System.out.println("json:"+json);
//			e.printStackTrace();
//			//throw new LinException(0xA00101,"数据格式异常！");
//		}
//		return result;
//	}
//	
//	public static Map<String,String> toParameters(Object obj){
//		Map<String,String> map = new HashMap<String,String>();
//		processesParameters(obj,map,null);
//		return map;
//	}
//	@SuppressLint("SimpleDateFormat")
//	private static Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	
//	
//	private static void processesParameters(Object obj,Map<String,String> map,String pre){
//		if(obj == null){
//			return;
//		}
//		Class<?> type = obj.getClass();
//
//		if(Date.class.isAssignableFrom(type)){
//			
//			map.put(pre,format.format(obj));
//			return;
//		}
//
//		if(type.isPrimitive() || Number.class.isAssignableFrom(type) || String.class.isAssignableFrom(type)){
//			map.put(pre, obj+"");
//			return;
//		}
//		//如果是一个对象
//		if(obj instanceof Map){
//			@SuppressWarnings("unchecked")
//			Map<String,Object> tmpMap = (Map<String, Object>) obj;
//			if(pre == null){
//				for(String key : tmpMap.keySet()){
//					processesParameters(tmpMap.get(key),map,key);
//				}
//			}else{
//				for(String key : tmpMap.keySet()){
//					processesParameters(tmpMap.get(key),map,pre+"."+key);
//				}
//			}
//		}
//		//如果是一个数组
//		else if(obj instanceof List){
//			if(pre == null){
//				//抛出异常
////				for(Object key : (List<Object>)obj){
////					processesParameters(obj,map,key);
////				}
//			}else{
//				@SuppressWarnings("rawtypes")
//				List tmpList = (List) obj;
//				int n = 0;
//				for(Object tmp : tmpList){
//					processesParameters(tmp,map,pre+"["+n+++"]");
//				}
//			}
//		}else{
//			
//			BeanInfo infos = Introspector.getBeanInfo(type);
//			
//			for(PropertyDescriptor pd : infos.getPropertyDescriptors()){
//					//pd.setValue(pd.getName(), deserializeImpl(map.get(pd.getName()),pd.getPropertyType()));
//				if(pd.getReadMethod() == null){
//					continue;
//				}
//				try {
////					map.putAll(pre+"."+pd.getName(), pd.getReadMethod().invoke(obj));
//					processesParameters(pd.getReadMethod().invoke(obj),map,pre+"."+pd.getName());
//				
////					if(pd.getWriteMethod() != null){
//
//				} catch (Throwable e) {
////						e.printStackTrace();
//				}
//			}
//		}
//	}
//}
////public class DataEncrypt extends MethodFilterInterceptor {
////
//////	     private static final Logger LOG = LoggerFactory.getLogger(ParametersInterceptor.class);
////
////	     boolean ordered = false;
////	     Set<Pattern> excludeParams = Collections.emptySet();
////	     Set<Pattern> acceptParams = Collections.emptySet();
////	     static boolean devMode = false;
////
////	     // Allowed names of parameters
////	     private String acceptedParamNames = "[a-zA-Z0-9\\.\\]\\[\\(\\)_'\\s]+";
////	     private Pattern acceptedPattern = Pattern.compile(acceptedParamNames);
////
////	     private ValueStackFactory valueStackFactory;
////
////	     @Inject
////	     public void setValueStackFactory(ValueStackFactory valueStackFactory) {
////	         this.valueStackFactory = valueStackFactory;
////	     }
////
////	     @Inject("devMode")
////	     public static void setDevMode(String mode) {
////	         devMode = "true".equals(mode);
////	     }
////
////	     public void setAcceptParamNames(String commaDelim) {
////	         Collection<String> acceptPatterns = asCollection(commaDelim);
////	         if (acceptPatterns != null) {
////	             acceptParams = new HashSet<Pattern>();
////	             for (String pattern : acceptPatterns) {
////	                 acceptParams.add(Pattern.compile(pattern));
////	             }
////	         }
////	     }
////
////	     /**
////	      * Compares based on number of '.' characters (fewer is higher)
////	      */
////	     static final Comparator<String> rbCollator = new Comparator<String>() {
////	         public int compare(String s1, String s2) {
////	             int l1 = 0, l2 = 0;
////	             for (int i = s1.length() - 1; i >= 0; i--) {
////	                 if (s1.charAt(i) == '.') l1++;
////	             }
////	             for (int i = s2.length() - 1; i >= 0; i--) {
////	                 if (s2.charAt(i) == '.') l2++;
////	             }
////	             return l1 < l2 ? -1 : (l2 < l1 ? 1 : s1.compareTo(s2));
////	         }
////
////	     };
////
////	     @Override
////	     public String doIntercept(ActionInvocation invocation) throws Exception {
////	         Object action = invocation.getAction();
////	         if (!(action instanceof NoParameters)) {
////	             ActionContext ac = invocation.getInvocationContext();
////	             final Map<String, Object> parameters = retrieveParameters(ac);
////
//////	             if (LOG.isDebugEnabled()) {
//////	                 LOG.debug("Setting params " + getParameterLogMap(parameters));
//////	             }
////
////	             if (parameters != null) {
////	                 Map<String, Object> contextMap = ac.getContextMap();
////	                 try {
////	                     ReflectionContextState.setCreatingNullObjects(contextMap, true);
////	                     ReflectionContextState.setDenyMethodExecution(contextMap, true);
////	                     ReflectionContextState.setReportingConversionErrors(contextMap, true);
////
////	                     ValueStack stack = ac.getValueStack();
////	                     setParameters(action, stack, parameters);
////	                 } finally {
////	                     ReflectionContextState.setCreatingNullObjects(contextMap, false);
////	                     ReflectionContextState.setDenyMethodExecution(contextMap, false);
////	                     ReflectionContextState.setReportingConversionErrors(contextMap, false);
////	                 }
////	             }
////	         }
////	         return invocation.invoke();
////	     }
////
////	     /**
////	      * Gets the parameter map to apply from wherever appropriate
////	      *
////	      * @param ac The action context
////	      * @return The parameter map to apply
////	      */
////	     protected Map<String, Object> retrieveParameters(ActionContext ac) {
////	         return ac.getParameters();
////	     }
////
////
////	     /**
////	      * Adds the parameters into context's ParameterMap
////	      *
////	      * @param ac        The action context
////	      * @param newParams The parameter map to apply
////	      *                  <p/>
////	      *                  In this class this is a no-op, since the parameters were fetched from the same location.
////	      *                  In subclasses both retrieveParameters() and addParametersToContext() should be overridden.
////	      */
////	     protected void addParametersToContext(ActionContext ac, Map<String, Object> newParams) {
////	     }
////
////	     protected void setParameters(Object action, ValueStack stack, final Map<String, Object> parameters) {
////	         ParameterNameAware parameterNameAware = (action instanceof ParameterNameAware)
////	                 ? (ParameterNameAware) action : null;
////
////	         Map<String, Object> params;
////	         Map<String, Object> acceptableParameters;
////	         if (ordered) {
////	             params = new TreeMap<String, Object>(getOrderedComparator());
////	             acceptableParameters = new TreeMap<String, Object>(getOrderedComparator());
////	             params.putAll(parameters);
////	         } else {
////	             params = new TreeMap<String, Object>(parameters);
////	             acceptableParameters = new TreeMap<String, Object>();
////	         }
////
////	         for (Map.Entry<String, Object> entry : params.entrySet()) {
////	             String name = entry.getKey();
////
////	             boolean acceptableName = acceptableName(name)
////	                     && (parameterNameAware == null
////	                     || parameterNameAware.acceptableParameterName(name));
////
////	             if (acceptableName) {
////	                 acceptableParameters.put(name, entry.getValue());
////	             }
////	         }
////
////	         ValueStack newStack = valueStackFactory.createValueStack(stack);
////	         boolean clearableStack = newStack instanceof ClearableValueStack;
////	         if (clearableStack) {
////	             //if the stack's context can be cleared, do that to prevent OGNL
////	             //from having access to objects in the stack, see XW-641
////	             ((ClearableValueStack)newStack).clearContextValues();
////	             Map<String, Object> context = newStack.getContext();
////	             ReflectionContextState.setCreatingNullObjects(context, true);
////	             ReflectionContextState.setDenyMethodExecution(context, true);
////	             ReflectionContextState.setReportingConversionErrors(context, true);
////
////	             //keep locale from original context
////	             context.put(ActionContext.LOCALE, stack.getContext().get(ActionContext.LOCALE));
////	         }
////
////	         boolean memberAccessStack = newStack instanceof MemberAccessValueStack;
////	         if (memberAccessStack) {
////	             //block or allow access to properties
////	             //see WW-2761 for more details
////	             MemberAccessValueStack accessValueStack = (MemberAccessValueStack) newStack;
////	             accessValueStack.setAcceptProperties(acceptParams);
////	             accessValueStack.setExcludeProperties(excludeParams);
////	         }
////
////	         for (Map.Entry<String, Object> entry : acceptableParameters.entrySet()) {
////	             String name = entry.getKey();
////	             Object value = entry.getValue();
////	             try {
////	                 newStack.setValue(name, value);
////	             } catch (RuntimeException e) {
////	                 if (devMode) {
////	                     String developerNotification = LocalizedTextUtil.findText(ParametersInterceptor.class, "devmode.notification", ActionContext.getContext().getLocale(), "Developer Notification:\n{0}", new Object[]{
////	                              "Unexpected Exception caught setting '" + name + "' on '" + action.getClass() + ": " + e.getMessage()
////	                     });
//////	                     LOG.error(developerNotification);
////	                     if (action instanceof ValidationAware) {
////	                         ((ValidationAware) action).addActionMessage(developerNotification);
////	                     }
////	                 }
////	             }
////	         }
////
////	         if (clearableStack && (stack.getContext() != null) && (newStack.getContext() != null))
////	             stack.getContext().put(ActionContext.CONVERSION_ERRORS, newStack.getContext().get(ActionContext.CONVERSION_ERRORS));
////
////	         addParametersToContext(ActionContext.getContext(), acceptableParameters);
////	     }
////
////	     /**
////	      * Gets an instance of the comparator to use for the ordered sorting.  Override this
////	      * method to customize the ordering of the parameters as they are set to the
////	      * action.
////	      *
////	      * @return A comparator to sort the parameters
////	      */
////	     protected Comparator<String> getOrderedComparator() {
////	         return rbCollator;
////	     }
////
////	     private String getParameterLogMap(Map<String, Object> parameters) {
////	         if (parameters == null) {
////	             return "NONE";
////	         }
////
////	         StringBuilder logEntry = new StringBuilder();
////	         for (Map.Entry entry : parameters.entrySet()) {
////	             logEntry.append(String.valueOf(entry.getKey()));
////	             logEntry.append(" => ");
////	             if (entry.getValue() instanceof Object[]) {
////	                 Object[] valueArray = (Object[]) entry.getValue();
////	                 logEntry.append("[ ");
////	 		        if (valueArray.length > 0 ) {
////	                     for (int indexA = 0; indexA < (valueArray.length - 1); indexA++) {
////	                         Object valueAtIndex = valueArray[indexA];
////	                         logEntry.append(String.valueOf(valueAtIndex));
////	                         logEntry.append(", ");
////	                     }
////	                     logEntry.append(String.valueOf(valueArray[valueArray.length - 1]));
////	                 }
////	                 logEntry.append(" ] ");
////	             } else {
////	                 logEntry.append(String.valueOf(entry.getValue()));
////	             }
////	         }
////
////	         return logEntry.toString();
////	     }
////
////	     protected boolean acceptableName(String name) {
////	         if (isAccepted(name) && !isExcluded(name)) {
////	             return true;
////	         }
////	         return false;
////	     }
////
////	     protected boolean isAccepted(String paramName) {
////	         if (!this.acceptParams.isEmpty()) {
////	             for (Pattern pattern : acceptParams) {
////	                 Matcher matcher = pattern.matcher(paramName);
////	                 if (matcher.matches()) {
////	                     return true;
////	                 }
////	             }
////	             return false;
////	         } else
////	             return acceptedPattern.matcher(paramName).matches();
////	     }
////
////	     protected boolean isExcluded(String paramName) {
////	         if (!this.excludeParams.isEmpty()) {
////	             for (Pattern pattern : excludeParams) {
////	                 Matcher matcher = pattern.matcher(paramName);
////	                 if (matcher.matches()) {
////	                     return true;
////	                 }
////	             }
////	         }
////	         return false;
////	     }
////
////	     /**
////	      * Whether to order the parameters or not
////	      *
////	      * @return True to order
////	      */
////	     public boolean isOrdered() {
////	         return ordered;
////	     }
////
////	     /**
////	      * Set whether to order the parameters by object depth or not
////	      *
////	      * @param ordered True to order them
////	      */
////	     public void setOrdered(boolean ordered) {
////	         this.ordered = ordered;
////	     }
////
////	     /**
////	      * Gets a set of regular expressions of parameters to remove
////	      * from the parameter map
////	      *
////	      * @return A set of compiled regular expression patterns
////	      */
////	     protected Set getExcludeParamsSet() {
////	         return excludeParams;
////	     }
////
////	     /**
////	      * Sets a comma-delimited list of regular expressions to match
////	      * parameters that should be removed from the parameter map.
////	      *
////	      * @param commaDelim A comma-delimited list of regular expressions
////	      */
////	     public void setExcludeParams(String commaDelim) {
////	         Collection<String> excludePatterns = asCollection(commaDelim);
////	         if (excludePatterns != null) {
////	             excludeParams = new HashSet<Pattern>();
////	             for (String pattern : excludePatterns) {
////	                 excludeParams.add(Pattern.compile(pattern));
////	             }
////	         }
////	     }
////
////	     /**
////	      * Return a collection from the comma delimited String.
////	      *
////	      * @param commaDelim the comma delimited String.
////	      * @return A collection from the comma delimited String. Returns <tt>null</tt> if the string is empty.
////	      */
////	     private Collection<String> asCollection(String commaDelim) {
////	         if (commaDelim == null || commaDelim.trim().length() == 0) {
////	             return null;
////	         }
////	         return TextParseUtil.commaDelimitedStringToSet(commaDelim);
////	     }
////
////	 }
//

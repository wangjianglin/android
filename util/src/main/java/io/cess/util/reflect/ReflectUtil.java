package io.cess.util.reflect;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * @author lin
 * @date 2010-8-14
 */
public abstract class ReflectUtil {


	/**
	 * 
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
  public static Field findField(Class<?> clazz, String fieldName) {
    return findFieldImpl(clazz, fieldName);
  }

  private static Field findFieldImpl(Class<?> clazz, String fieldName) {
    Field field = null;

    try {
      field = clazz.getDeclaredField(fieldName);
    } catch (SecurityException e) {
    } catch (NoSuchFieldException e) {
      if (clazz.getSuperclass()!=null) {
        return findFieldImpl(clazz.getSuperclass(), fieldName);
      }else{
    	  throw new RuntimeException(e);
      }
    }
    
    return field;
  }

  public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
    return getMethodImpl(clazz, methodName, parameterTypes);
  }

  private static Method getMethodImpl(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
    Method method = null;
    
    try {
      method = clazz.getDeclaredMethod(methodName, parameterTypes);
    } catch (SecurityException e) {
    } catch (NoSuchMethodException e) {
      if (clazz.getSuperclass()!=null) {
        return getMethodImpl(clazz.getSuperclass(), methodName, parameterTypes);
      }else{
    	  throw new RuntimeException(e);
      }
    }
    
    return method;
  }

//  private static String getParameterTypesText(Class<?>[] parameterTypes) {
//    if (parameterTypes==null) return "";
//    StringBuilder parameterTypesText = new StringBuilder();
//    for (int i=0; i<parameterTypes.length; i++) {
//      Class<?> parameterType = parameterTypes[i];
//      parameterTypesText.append(parameterType.getName());
//      if (i!=parameterTypes.length-1) {
//        parameterTypesText.append(", ");
//      }
//    }
//    return parameterTypesText.toString();
//  }

  public static <T> T newInstance(Class<T> clazz) {
    return newInstance(clazz, null, null);
  }
  public static <T> T newInstance(Constructor<T> constructor) {
    return newInstance(null, constructor, null);
  }
  public static <T> T newInstance(Constructor<T> constructor, Object[] args) {
    return newInstance(null, constructor, args);
  }
  
  private static <T> T newInstance(Class<T> clazz, Constructor<T> constructor, Object[] args) {
    if ( (clazz==null)
         && (constructor==null)
       ) {
      throw new IllegalArgumentException("can't create new instance without clazz or constructor");
    }

    try {
      if (constructor==null) {
        constructor = clazz.getConstructor((Class[])null);
      }
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor.newInstance(args);

    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
  
  public static Object get(Field field, Object object) {
    try {
      Object value = field.get(object);
      return value;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void set(Field field, Object object, Object value) {
    try {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      field.set(object, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public static Object invoke(Method method, Object target, Object[] args) {
    try {
      if (!method.isAccessible()) {
        method.setAccessible(true);
      }
      return method.invoke(target, args);
    } catch (InvocationTargetException e) {
      Throwable targetException = e.getTargetException();
      throw new RuntimeException(targetException);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Method findMethod(Class<?> clazz, String methodName,Object[] args) {
	    Method[] candidates = clazz.getDeclaredMethods();
	    for (int i=0; i<candidates.length; i++) {
	      Method candidate = candidates[i];
	      if ( (candidate.getName().equals(methodName))
	           && (isArgumentMatch(candidate.getParameterTypes(), args))
	         ) {

	        return candidate;
	      }
	    }
	    if (clazz.getSuperclass()!=null) {
	      Method m = findMethod(clazz.getSuperclass(), methodName, args);
	      if(m != null){
	    	  return m;
	      }
	    }
	    Class<?>[] interfaces = clazz.getInterfaces();
	    for(Class<?> interfaceClass:interfaces){
	    	Method m = findMethod(interfaceClass, methodName, args);
	    	if(m != null){
	    		return m;
	    	}
	    }
	    return null;
	  }
  
  public static Method findMethod(Class<?> clazz, String methodName,Class<?>[] args) {
	    Method[] candidates = clazz.getDeclaredMethods();
	    for (int i=0; i<candidates.length; i++) {
	      Method candidate = candidates[i];
	      if ( (candidate.getName().equals(methodName))
	           && (isArgumentMatch(candidate.getParameterTypes(), args))
	         ) {

	        return candidate;
	      }
	    }
	    if (clazz.getSuperclass()!=null) {
	      Method m = findMethod(clazz.getSuperclass(), methodName, args);
	      if(m != null){
	    	  return m;
	      }
	    }
	    Class<?>[] interfaces = clazz.getInterfaces();
	    for(Class<?> interfaceClass:interfaces){
	    	Method m = findMethod(interfaceClass, methodName, args);
	    	if(m != null){
	    		return m;
	    	}
	    }
	    return null;
	  }

  public static Constructor<?> findConstructor(Class<?> clazz,Object[] args) {
	    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
	    for (int i=0; i<constructors.length; i++) {
	      if (isArgumentMatch(constructors[i].getParameterTypes(),args)) {
	        return constructors[i];
	      }
	    }
	    return null;
	  }
  
  public static Constructor<?> findConstructor(Class<?> clazz,Class<?>[] args) {
	    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
	    for (int i=0; i<constructors.length; i++) {
	      if (isArgumentMatch(constructors[i].getParameterTypes(),args)) {
	        return constructors[i];
	      }
	    }
	    return null;
	  }
  
  public static boolean isArgumentMatch(Class<?>[] types,Object[] args) {
	  int ts=0;
	  int as=0;
	  if(types != null){
		  ts=types.length;
	  }
	  if(args != null){
		  as=args.length;
	  }
	  if(ts != as){
		  return false;
	  }
	  if(ts==0){
		  return true;
	  }
	  for (int n = 0; n < types.length; n++) {
		  if(args[n] == null){
			  continue;
		  }
		  if(!types[n].isAssignableFrom(args[n].getClass())){
			  return false;
		  }
	  }
		  return true;
  } 
  public static boolean isArgumentMatch(Class<?>[] types,Class<?>[] args) {
	  int ts=0;
	  int as=0;
	  if(types != null){
		  ts=types.length;
	  }
	  if(args != null){
		  as=args.length;
	  }
	  if(ts != as){
		  return false;
	  }
	  if(ts==0){
		  return true;
	  }
	  for (int n = 0; n < types.length; n++) {
		  if(args[n] == null){
			  continue;
		  }
		  if(!types[n].isAssignableFrom(args[n])){
			  return false;
		  }
	  }
		  return true;
  }
  //public static boolean isArgumentMatch(Class<?>[] parameterTypes,Class<?>[] types, Object[] args) {
//		int nbrOfArgs = 0;
//		if (args != null)
//			nbrOfArgs = args.length;
//
//		int nbrOfParameterTypes = 0;
//		if (parameterTypes != null)
//			nbrOfParameterTypes = parameterTypes.length;
//
//		if ((nbrOfArgs == 0) && (nbrOfParameterTypes == 0)) {
//			return true;
//		}
//
//		if (nbrOfArgs != nbrOfParameterTypes) {
//			return false;
//		}
//
//		for (int i = 0; (i < parameterTypes.length); i++) {
//			Class<?> parameterType = parameterTypes[i];
//			String argTypeName = (argDescriptors != null ? argDescriptors
//					.get(i).getTypeName() : null);
//			if (argTypeName != null) {
//				if (!argTypeName.equals(parameterType.getName())) {
//					return false;
//				}
//			} else if ((args[i] != null)
//					&& (!parameterType.isAssignableFrom(args[i].getClass()))) {
//				return false;
//			}
//		}
//		return true;
//	}
	  
//  public static Constructor<?> findConstructor(Class<?> clazz, List<ArgDescriptor> argDescriptors, Object[] args) {
//    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
//    for (int i=0; i<constructors.length; i++) {
//      if (isArgumentMatch(constructors[i].getParameterTypes(), argDescriptors, args)) {
//        return constructors[i];
//      }
//    }
//    return null;
//  }
//
//	public static boolean isArgumentMatch(Class<?>[] parameterTypes,Class<?>[] types, Object[] args) {
//		int nbrOfArgs = 0;
//		if (args != null)
//			nbrOfArgs = args.length;
//
//		int nbrOfParameterTypes = 0;
//		if (parameterTypes != null)
//			nbrOfParameterTypes = parameterTypes.length;
//
//		if ((nbrOfArgs == 0) && (nbrOfParameterTypes == 0)) {
//			return true;
//		}
//
//		if (nbrOfArgs != nbrOfParameterTypes) {
//			return false;
//		}
//
//		for (int i = 0; (i < parameterTypes.length); i++) {
//			Class<?> parameterType = parameterTypes[i];
//			String argTypeName = (argDescriptors != null ? argDescriptors
//					.get(i).getTypeName() : null);
//			if (argTypeName != null) {
//				if (!argTypeName.equals(parameterType.getName())) {
//					return false;
//				}
//			} else if ((args[i] != null)
//					&& (!parameterType.isAssignableFrom(args[i].getClass()))) {
//				return false;
//			}
//		}
//		return true;
//	}

  public static String getSignature(Method method) {
	  return method.toGenericString();
  }
  
  public static String getUnqualifiedClassName(Class<?> clazz) {
    if (clazz==null) {
      return null;
    }
    return getUnqualifiedClassName(clazz.getSimpleName());
  }

  public static String getUnqualifiedClassName(String className) {
    if (className==null) {
      return null;
    }
    int dotIndex = className.lastIndexOf('.');
    if (dotIndex!=-1) {
      className = className.substring(dotIndex+1);
    }
    return className;
  }

//  public static ClassLoader installDeploymentClassLoader(ProcessDefinitionImpl processDefinition) {
//    String deploymentId = processDefinition.getDeploymentId();
//    if (deploymentId==null) {
//      return null;
//    }
//
//    Thread currentThread = Thread.currentThread();
//    ClassLoader original = currentThread.getContextClassLoader();
//
//    RepositoryCache repositoryCache = EnvironmentImpl.getFromCurrent(RepositoryCache.class); 
//    DeploymentClassLoader deploymentClassLoader = repositoryCache.getDeploymentClassLoader(deploymentId, original);
//    if (deploymentClassLoader==null) {
//      deploymentClassLoader = new DeploymentClassLoader(original, deploymentId);
//      repositoryCache.setDeploymentClassLoader(deploymentId, original, deploymentClassLoader);
//    }
//    
//    currentThread.setContextClassLoader(deploymentClassLoader);
//    
//    return original;
//  }
//
//  public static void uninstallDeploymentClassLoader(ClassLoader original) {
//    if (original!=null) {
//      Thread.currentThread().setContextClassLoader(original);
//    }
//  }
//  
//  public static Object instantiateUserCode(Descriptor descriptor, ProcessDefinitionImpl processDefinition) {
//    ClassLoader classLoader = ReflectUtil.installDeploymentClassLoader(processDefinition);
//    try {
//      return WireContext.create(descriptor);
//    } finally {
//      ReflectUtil.uninstallDeploymentClassLoader(classLoader);
//    }
//  }
}

package io.cess.util.reflect;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 实现对类处理的一些基本功能，处理单位是一个Jar文件或者是一个目录
 * 
 * @author lin
 * @date 2011-2-27
 *
 */
final public class ClassUtils {

	/**
	 * 加载Jar文件中所有的类
	 * 
	 * @param file
	 * @return
	 */
	public static List<Class<?>> load(JarFile file){
		return loadImpl(file,Object.class,null);
	}
	
	/**
	 * 加载Jar文件中，是base类或是其子类的所有类
	 * 
	 * @param file
	 * @param base
	 * @return
	 */
	public static List<Class<?>> load(JarFile file,Class<?> base){
		return loadImpl(file,base,null);
	}
	
	/**
	 * 加载Jar文件中，是base类、其子类或者有annotation类注释的所有类
	 * 
	 * @param file
	 * @param base
	 * @param annotation
	 * @return
	 */
	public static List<Class<?>> load(JarFile file,Class<?> base,Class<? extends Annotation> annotation){
		return loadImpl(file,base,annotation);
	}


	/**
	 * 
	 * @param file
	 * @param base
	 * @param annotation
	 * @return
	 */
	private static List<Class<?>> loadImpl(JarFile file,Class<?> base,Class<? extends Annotation> annotation){
		List<Class<?>> result = new ArrayList<Class<?>>();
		try{
			Enumeration<JarEntry> entrys = file.entries();
			String className = null;
			Class<?> tmpClass=null;
			while(entrys.hasMoreElements()){
				try{
					JarEntry entry = entrys.nextElement();
					if(entry.isDirectory()){
						continue;
					}
					className = getClassName(entry.getName());
					tmpClass = Class.forName(className,false,ClassUtils.class.getClassLoader());
					if(annotation != null){
						if(tmpClass.getAnnotation(annotation)!=null){
							result.add(tmpClass);
							continue;
						}
					}
					if(base != null){
						if(base.isAssignableFrom(tmpClass)){
							result.add(tmpClass);
							continue;
						}
					}
				}catch(Exception e){}
			}
		}catch(Exception e){
			
		}
		return result;
	}
	
	/**
	 * 加载指定目录下的所有的类
	 * 
	 * @param file
	 * @return
	 */
	public static List<Class<?>> load(File file){
		return loadImpl(null,null,file,Object.class,null);
	}
	
	/**
	 * 加载指定目录下的，是base类或是其子类的所有类
	 * 
	 * @param file
	 * @param base
	 * @return
	 */
	public static List<Class<?>> load(File file,Class<?> base){
		return loadImpl(null,null,file,base,null);
	}
	
	/**
	 * 加载指定目录下的，是base类、其子类或者有annotation类注释的所有类
	 * 
	 * @param file
	 * @param base
	 * @param annotation
	 * @return
	 */
	public static List<Class<?>> load(File file,Class<?> base,Class<? extends Annotation> annotation){
		return loadImpl(null,null,file,base,annotation);
	}
	
	/**
	 * 
	 * @param result
	 * @param pre
	 * @param file
	 * @param base
	 * @param annotation
	 * @return
	 */
	private static List<Class<?>> loadImpl(List<Class<?>> result,String pre,File file,Class<?> base,Class<? extends Annotation> annotation){
		//List<Class<?>> result = new ArrayList<Class<?>>();
		if(result == null){
			result = new ArrayList<Class<?>>();
		}
		try{
			if(file.isFile()){
				if("".equals(pre)){
					loadClassImpl(result,getClassName(file.getName()),base,annotation);
					pre = file.getName();
				}else{
					loadClassImpl(result,getClassName(pre+"."+file.getName()),base,annotation);
				}
				loadClassImpl(result,getClassName(pre+"."+file.getName()),base,annotation);
			}else{
				File[] files = file.listFiles();
				if(pre == null){
					pre = "";
				}else if("".equals(pre)){
					pre = file.getName();
				}else{
					pre = pre +"."+file.getName();
				}
				for(File tmpFile:files){
					loadImpl(result,pre,tmpFile,base,annotation);
				}
			}
		}catch(Exception e){
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param result
	 * @param className
	 * @param base
	 * @param annotation
	 */
	private static void loadClassImpl(List<Class<?>> result,String className,Class<?> base,Class<? extends Annotation> annotation){
		try{
			Class<?> tmpClass = Class.forName(className,false,ClassUtils.class.getClassLoader());
			if(annotation != null){
				if(tmpClass.getAnnotation(annotation)!=null){
					result.add(tmpClass);
					return;
				}
			}
			if(base != null){
				if(base.isAssignableFrom(tmpClass)){
					result.add(tmpClass);
					return;
				}
			}
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 根据JarEntry名或class文件路径名解析得到类名
	 * 
	 * @param className
	 * @return
	 */
	private static String getClassName(String className)
	{
		return className.replace('/', '.').replace('\\', '.')
				.replaceAll(".class", "");
	}
}

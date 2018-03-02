package io.cess.util;

import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * 
 * @author lin
 * @date 2014年3月5日 上午11:49:55
 *
 */
public class Jar {
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	public static java.util.jar.JarFile jarFileFromClass(Class<?> c){
		try{
			String resourceName = c.getName();
			Enumeration<URL> urls = Jar.class.getClassLoader().getResources(resourceName.replace('.', '/') + ".class");
			while(urls.hasMoreElements()){
				URL url = urls.nextElement();
				String file = url.getPath();
				file = URLDecoder.decode(file,System.getProperty("file.encoding"));
				file = file.substring(6, file.length()-resourceName.length() - 8);
				return new java.util.jar.JarFile(file);
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
		return null;
	}
}

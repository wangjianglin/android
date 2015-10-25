package lin.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * @author lin
 * @date 2014年3月5日 上午11:48:01
 *
 */
public class Packages {

	/**
	 * description:加载所有指定路径的jar包中的类
	 * 
	 * @param file  *.jar，绝对路径
	 * @return 返回jar包中所有类
	 */
	public static Set<String> getClasses(String file) {
		JarFile jar = null;
		Set<String> set = new HashSet<String>();
		try {
			jar = new JarFile(file);
		} catch (IOException e) {
			return null;
		}
		Enumeration<JarEntry> entries = jar.entries();

		URLClassLoader loader = getLoad(file);
		String name;
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().endsWith(".class")) {
				name = entry.getName();
				name = name.substring(0, name.length() - 6);
				name = name.replaceAll("/", ".");
				// Class<?> cc = null;
				try {
					loader.loadClass(name);
				} catch (ClassNotFoundException e) {
					continue;
				}
				// if(isDriver(cc))
				set.add(name);
			}
		}
		return set;
	}
	
	/**
	 * description:给定一个类名，反射出这个类的一个实例。
	 * 
	 * @param String className
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getInstance(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		//try {
			return (T)Class.forName(className).newInstance();
		//} catch (Exception e) {
			//e.printStackTrace();
			//return null;
		//}
	}
	
	/**
	 * description:根据一个文件(jar)，返回一个ClassLoader
	 * 
	 * @param file
	 *            *.jar，绝对路径
	 * @return 返回一个ClassLoader
	 */
	public static URLClassLoader getLoad(String file) {
		URL[] url = null;
		try {
			url = new URL[] { new URL("file:" + file) };
		} catch (MalformedURLException e) {
			return null;
		}
		URLClassLoader loader = new URLClassLoader(url);
		return loader;
	}

	/**
	 * description:////未用
	 * 
	 * @param file
	 *            一个jar文件(绝对路径)
	 * @param clazz
	 *            需要加载的class路径com.mysql.jdbc.MysqlDriver
	 * @return
	 */
	public static Class<?> getDynamic(String file, String clazz) {
		URLClassLoader loader = getLoad(file);
		try {
			return loader.loadClass(clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * description:判断是否是数据驱动/////未使用
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isDriver(Class<?> clazz) {
		Class<?>[] ccc = clazz.getInterfaces();
		boolean flag = false;
		for (Class<?> aa : ccc) {
			if (aa.getName().equals("java.sql.Driver")) {
				flag = true;
			}
		}
		if (!flag) {
			for (; clazz != null; clazz = clazz.getSuperclass()) {
				Class<?>[] interfaces = clazz.getInterfaces();
				for (Class<?> aa : interfaces) {
					if (aa.getName().equals("java.sql.Driver")) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}


}

package io.cess.util;

import java.lang.reflect.Method;

/**
 * 
 * @author lin
 * @date 2014年3月5日 上午11:48:56
 *
 */
public class PropertyOperator {

	/**
	 * 属性复制
	 * @param src
	 * @param dest
	 */
	public static void copy(Object src, Object dest) {
		if (src == null || dest == null) {
			return;
		}
		Class<?> c = src.getClass();
		Method[] methods = c.getMethods();
		if (methods == null) {
			return;
		}
		Method tmp;
		for (Method method : dest.getClass().getMethods()) {
			try {
				if (method.getName().startsWith("set")) {
					tmp = null;
					try {
						tmp = c.getMethod("get" + method.getName().substring(3));
					} catch (Exception e) {
					}
					if (tmp == null) {
						tmp = c.getMethod("is" + method.getName().substring(3));
					}
					if (tmp == null) {
						continue;
					}
					method.invoke(dest, tmp.invoke(src));
				}
			} catch (Exception e) {
			}
		}
	}
}

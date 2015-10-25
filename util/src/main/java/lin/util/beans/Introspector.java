package lin.util.beans;

public class Introspector {

	public static BeanInfo getBeanInfo(Class<?> type) {
		return new BeanInfo(type);
	}

	public static BeanInfo getBeanInfo(Class<?> clazz, Class<?> superclass) {
		return new BeanInfo(clazz,superclass);
	}

}

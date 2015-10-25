package lin.util.beans;

import java.lang.reflect.Method;

public class PropertyDescriptor {

	String name;
	Method writeMethod;
	Method readMethod;
	public Method getWriteMethod() {
		return writeMethod;
	}

	public String getName() {
		return name;
	}

	public Method getReadMethod() {
		return readMethod;
	}

}

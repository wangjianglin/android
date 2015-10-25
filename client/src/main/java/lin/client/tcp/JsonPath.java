package lin.client.tcp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface JsonPath {
	public String path();
}

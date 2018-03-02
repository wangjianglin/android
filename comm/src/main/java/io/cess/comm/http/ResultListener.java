package io.cess.comm.http;

import java.util.List;

/**
 * 
 * @author 王江林
 * @date 2013-7-16 上午11:28:01
 *
 */
public interface ResultListener<T>  {
	void result(T obj, List<Error> warning);
	void fault(Error error);
}



package lin.util;

import java.util.Date;

/**
 * 
 * @author lin
 * @date 2010-11-10
 * 
 * 以GMT0区的2012-01-01 00:00:00.000 开始
 */
public class IDGener {

	// private static final long offset = -1288190194;
//	private static final long offset = -1314125018;
	private static final long offset = -1325404800;
	//1384128720112    7^12
	private static long pre = 0;
	private static long preRand = -1;
	private static final long inveral = 100000;
	private static final int space = 8;

	// private static final Map<Long,Boolean> map = new HashMap<Long,
	// Boolean>();
	/**
	 * �õ�һ�����ID
	 */
	public synchronized static long next() {
		return next(0);
	}

	private static long next(int dept) {
		long result = (new Date()).getTime() / 1000 + offset;
		long rand = (long) (Math.random() * space) + 1;
		if (result != pre && preRand > inveral - 1) {
			preRand = 0;
			pre = result;
		}
		rand += preRand;
		preRand = rand;
		if (rand > inveral - 1) {
			try {
				if (dept > 1) {
					// System.out.println(result+"-------------------------");
					Thread.sleep(dept * 55);
				}
			} catch (Exception e) {
			}
			return next(++dept);
		}
		// map.put(rand, true);
		result = result * inveral + rand;
		return result;
	}
}

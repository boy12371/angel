package test.com.feinno.ha.logging.newtest;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.logging.common.LogCommon;

public class TestLog2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestLog2.class);

	private static final AtomicInteger counter = new AtomicInteger();

	public static void writeLog() {
		LOGGER.trace("{}.This is {}.trace.", counter.incrementAndGet(), TestLog2.class.getSimpleName());
		LOGGER.debug("{}.This is {}.debug.", counter.incrementAndGet(), TestLog2.class.getSimpleName());
		LOGGER.info("{}.This is {}.info.", counter.incrementAndGet(), TestLog2.class.getSimpleName());
		LOGGER.warn("{}.This is {}.warn.", counter.incrementAndGet(), TestLog2.class.getSimpleName());
		LOGGER.error("{}.This is {}.error.", counter.incrementAndGet(), TestLog2.class.getSimpleName());
	}

	@SuppressWarnings("null")
	public static void main(String args[]) {
		// String abody = "hello";
		// String rbody = "feinno";
		// Exception error = new Exception("");
		// LOGGER.error(String.format("invoke failed :\n %s \n %s \n", abody,
		// rbody),error);

		// int count = 10000 * 10;
		// String errorStr = LogCommon.formaError(new NullPointerException());
		// int baseLength = errorStr.length();
		// long startTime = System.nanoTime();
		// for (int i = 0; i < count; i++) {
		// String s = LogCommon.formaError(new NullPointerException());
		// if (s.length() != baseLength) {
		// System.out.println(s);
		// }
		// }
		// long endTime = System.nanoTime();
		// System.out.println("耗时" + TimeUnit.MILLISECONDS.convert(endTime -
		// startTime, TimeUnit.NANOSECONDS) + "ms");

		int count = 0;
		int max = 10000 * 10;
		while (count++ < max) {
			try {
				String str = null;
				str.toString();
			} catch (Exception e) {
				if (e.getStackTrace().length == 0) {
					System.out.println("count is " + count);
					System.out.println(LogCommon.formaError(e));
					return;
				}
			}
		}
		System.out.println("Nothing.");
	}
}

package test.com.feinno.ha.logging.newtest;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog4 {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestLog4.class);

	private static final AtomicInteger counter = new AtomicInteger();

	public static void writeLog() {
		LOGGER.trace("{}.This is {}.trace.", counter.incrementAndGet(), TestLog4.class.getSimpleName());
		LOGGER.debug("{}.This is {}.debug.", counter.incrementAndGet(), TestLog4.class.getSimpleName());
		LOGGER.info("{}.This is {}.info.", counter.incrementAndGet(), TestLog4.class.getSimpleName());
		LOGGER.warn("{}.This is {}.warn.", counter.incrementAndGet(), TestLog4.class.getSimpleName());
		LOGGER.error("exception test : {}", new RuntimeException(Integer.toString(counter.get())));
	}

}

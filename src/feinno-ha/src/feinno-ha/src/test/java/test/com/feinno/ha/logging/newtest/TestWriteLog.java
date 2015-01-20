package test.com.feinno.ha.logging.newtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWriteLog {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestWriteLog.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 1; i < 10001; i++) {
			LOGGER.info(String.format("Test index %s", i));
		}
		System.out.println("over");
	}

}

package test.com.feinno.ha.logging.newtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestManyException {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestManyException.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new TestManyException().test1();
		Thread.sleep(1000);
	}

	public void test1() {
		try {
			testRuntimeException();
		} catch (Exception e) {
			LOGGER.error("Found error.", e);
		}
	}

	public void testRuntimeException() {
		try {
			testNullPointException();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("null")
	public void testNullPointException() {
		String str = null;
		str.toString();
	}

}

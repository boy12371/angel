package test.com.feinno.ha.logging.newtest;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.logging.LogEvent;
import com.feinno.logging.LogLevel;
import com.feinno.serialization.Serializer;

public class TestLogEventSerial {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestLogEventSerial.class);

	@org.junit.Test
	public void test1() throws Exception {
		LogLevel level = LogLevel.ERROR;
		String message = "Test serialization LogEvent,is {}";
		Object[] params = new Object[] { "first" };
		Throwable throwable = new RuntimeException("Test serialization Exception.");
		LogEvent originalLogEvent = new LogEvent((com.feinno.logging.Logger) LOGGER, level, message, params, throwable);
		byte[] buffer = Serializer.encode(originalLogEvent);
		LogEvent newLogEvent = Serializer.decode(LogEvent.class, buffer);
		System.out.println("Original logEventTemp:");
		System.out.println(originalLogEvent);
		System.out.println("New logEventTemp:");
		System.out.println(newLogEvent);
		Assert.assertEquals(originalLogEvent.toString(), newLogEvent.toString());
	}
}

package test.com.feinno.ha.logging.newtest.Marker;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import com.feinno.logging.filter.FilterManager;

public class TestMarkerLog {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestMarkerLog.class);

	public static void main(String[] args) {
		TestMarkerLog testMarkerLog = new TestMarkerLog();
		testMarkerLog.test();
	}

	@Test
	public void test() {

		FilterManager.addMarker(MarkerFactory.getMarker("SystemOut"));
		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "-------------------------------------------------");
		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "模拟没有Marker的情况下，以下日志按照正常级别输出");
		LOGGER.debug("test debug log");
		LOGGER.info("test info log");
		LOGGER.warn("test warn log");
		LOGGER.error("test error log");

		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "-------------------------------------------------");
		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "模拟有Marker,但是没有向FilterManager注册这Marker，因此以下同样按照正常级别输出");
		String name = "NO.89757";
		LOGGER.debug(MarkerFactory.getMarker(name), "test debug log + marker, FilterManager is null");
		LOGGER.info(MarkerFactory.getMarker(name), "test info log + marker, FilterManager is null");
		LOGGER.warn(MarkerFactory.getMarker(name), "test warn log + marker, FilterManager is null");
		LOGGER.error(MarkerFactory.getMarker(name), "test error log + marker, FilterManager is null");

		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "-------------------------------------------------");
		LOGGER.info(MarkerFactory.getMarker("SystemOut"),
				"模拟有Marker，且向FilterManager注册了这个Marker，以下按照除了按照级别输出外，还会按照Marker的命中输出");
		FilterManager.addMarker(MarkerFactory.getMarker(name));
		LOGGER.debug(MarkerFactory.getMarker(name), "test debug log + marker, FilterManager is " + name);
		LOGGER.info(MarkerFactory.getMarker(name), "test info log + marker, FilterManager is " + name);
		LOGGER.warn(MarkerFactory.getMarker(name), "test warn log + marker, FilterManager is " + name);
		LOGGER.error(MarkerFactory.getMarker(name), "test error log + marker, FilterManager is " + name);

		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "-------------------------------------------------");
		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "模拟一个新Marker,这个Maraker没有注册，因此以下同样按照正常级别输出");
		name = "NO.89758";
		LOGGER.debug(MarkerFactory.getMarker(name), "test debug log + marker, FilterManager is " + name);
		LOGGER.info(MarkerFactory.getMarker(name), "test info log + marker, FilterManager is " + name);
		LOGGER.warn(MarkerFactory.getMarker(name), "test warn log + marker, FilterManager is " + name);
		LOGGER.error(MarkerFactory.getMarker(name), "test error log + marker, FilterManager is " + name);

		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "-------------------------------------------------");
		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "将上一个新Marker注册到FilterManager中，以下按照Marker的命中进行输出");
		FilterManager.addMarker(MarkerFactory.getMarker(name));
		LOGGER.debug(MarkerFactory.getMarker(name), "test debug log + marker, FilterManager is " + name);
		LOGGER.info(MarkerFactory.getMarker(name), "test info log + marker, FilterManager is " + name);
		LOGGER.warn(MarkerFactory.getMarker(name), "test warn log + marker, FilterManager is " + name);
		LOGGER.error(MarkerFactory.getMarker(name), "test error log + marker, FilterManager is " + name);

		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "-------------------------------------------------");
		LOGGER.info(MarkerFactory.getMarker("SystemOut"), "在FilterManager中清除掉上一个Marker，以下日志会按照正常级别输出");
		FilterManager.removeMarker(MarkerFactory.getMarker(name));
		LOGGER.debug(MarkerFactory.getMarker(name), "test debug log + marker, FilterManager is " + name);
		LOGGER.info(MarkerFactory.getMarker(name), "test info log + marker, FilterManager is " + name);
		LOGGER.warn(MarkerFactory.getMarker(name), "test warn log + marker, FilterManager is " + name);
		LOGGER.error(MarkerFactory.getMarker(name), "test error log + marker, FilterManager is " + name);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(ThreadContext.getCurrent().getSession().getId());
	}

}

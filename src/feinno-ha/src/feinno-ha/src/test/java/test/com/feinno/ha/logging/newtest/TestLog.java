package test.com.feinno.ha.logging.newtest;

import java.util.Queue;
import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import test.com.feinno.ha.logging.newtest.Marker.TestMarkerLog;

import com.feinno.logging.LogEvent;
import com.feinno.logging.LoggerContext;
import com.feinno.logging.appender.Appender;
import com.feinno.logging.appender.JdbcAppender;
import com.feinno.logging.common.ConcurrentFixedSizeQueue;
import com.feinno.logging.common.FireEventQueue;
import com.feinno.logging.filter.Filter;
import com.feinno.logging.filter.FilterReply;
import com.feinno.util.Action;

public class TestLog {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestLog.class);

	public static void main(String args[]) throws Exception {
		new TestLog().test();
		JdbcAppender jdbcAppender = null;
		for (Appender appender : LoggerContext.getInstance().getAppenderAttachable().getAppenders()) {
			if (appender instanceof JdbcAppender) {
				jdbcAppender = (JdbcAppender) appender;
			}
		}
//		for (int i = 0; i < 100000; i++) {
			// if (jdbcappender.createtable()) {
			// system.out.println(i + " true");
			// } else {
			// system.err.println(i + " false");
			// break;
			// }
			jdbcAppender.switchTable();
//			System.out.println(i + " switchTable");
//		}
		System.out.println("over");
		Thread.sleep(1000);
		// System.out.println("class Config1 {");
		// System.out.println("public static final String LOGGING_DEBUG_XML = new String(new byte[]"+Arrays.toString(FileUtil.read("C:\\Users\\Administrator\\git\\feinno-ha\\src\\feinno-ha\\src\\test\\java\\test\\com\\feinno\\ha\\logging\\newtest\\logging.debug.xml").getBytes()).replaceAll("\\[",
		// "{").replaceAll("\\]", "}")+");");
		// System.out.println("public static final String LOGGING_ERROR_XML = new String(new byte[]"+Arrays.toString(FileUtil.read("C:\\Users\\Administrator\\git\\feinno-ha\\src\\feinno-ha\\src\\test\\java\\test\\com\\feinno\\ha\\logging\\newtest\\logging.error.xml").getBytes()).replaceAll("\\[",
		// "{").replaceAll("\\]", "}")+");");
		// System.out.println("}");
		// System.out.println("class Config2 {");
		// System.out.println("public static final String LOGGING_CONSOLE_XML = new String(new byte[]"+Arrays.toString(FileUtil.read("C:\\Users\\Administrator\\git\\feinno-ha\\src\\feinno-ha\\src\\test\\java\\test\\com\\feinno\\ha\\logging\\newtest\\logging.console.xml").getBytes()).replaceAll("\\[",
		// "{").replaceAll("\\]", "}")+");");
		// System.out.println("public static final String LOGGING_DATABASE_XML = new String(new byte[]"+Arrays.toString(FileUtil.read("C:\\Users\\Administrator\\git\\feinno-ha\\src\\feinno-ha\\src\\test\\java\\test\\com\\feinno\\ha\\logging\\newtest\\logging.database.xml").getBytes()).replaceAll("\\[",
		// "{").replaceAll("\\]", "}")+");");
		// System.out.println("public static final String LOGGING_FILE_XML = new String(new byte[]"+Arrays.toString(FileUtil.read("C:\\Users\\Administrator\\git\\feinno-ha\\src\\feinno-ha\\src\\test\\java\\test\\com\\feinno\\ha\\logging\\newtest\\logging.file.xml").getBytes()).replaceAll("\\[",
		// "{").replaceAll("\\]", "}")+");");
		// System.out.println("}");
	}

	@Test
	public void test() throws Exception {
		// new LogManager();
		// FilterReply.DENY.value();
		// LoggerContext.getInstance().getLogger(TestLog.class).getFilter().setNextFilter(new
		// TestFilter());
		// LOGGER.info("Test formaErrorNotLn {}", LogCommon.formaErrorNotLn(new
		// RuntimeException()));
		// // 开启50个线程，每个线程写日志200次
		// LogManager.loadSettings(PropertiesUtil.xmlToProperties(Config1.LOGGING_DEBUG_XML));
		for (int j = 0; j < 50; j++) {
			LOGGER.warn("第" + j + "圈完事了");
			Thread thread = new Thread() {
				@Override
				public void run() {
					// try {
					// Thread.sleep(1000L);
					// } catch (Exception e) {
					// e.printStackTrace();
					// }
					for (int i = 0; i < 20; i++) {
						TestLog2.writeLog();
						TestLog3.writeLog();
						TestLog4.writeLog();
						TestLog5.writeLog();
						TestMarkerLog.main(null);
					}
				}
			};
			thread.setName("test" + j);
			thread.start();
			try {
				Thread.sleep(10L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// // 运行中动态修改配置文件，检查原日志队列的监控线程是否会自动死掉，是否会启动新配置的监控队列与日志级别
		// Thread.sleep(1000L);
		// System.out.println("转logging.error.xml");
		// LoggerContext.getInstance().loadSettings(PropertiesUtil.xmlToProperties(Config1.LOGGING_ERROR_XML));
		//
		// Thread.sleep(1000L);
		// System.out.println("转文本记录");
		// LoggerContext.getInstance().loadSettings(PropertiesUtil.xmlToProperties(Config2.LOGGING_FILE_XML));
		// // 假设此时日志文件突然被另一个线程删除
		// LogEvent logEventTemp = new LogEvent((com.feinno.logging.Logger)
		// LOGGER, null, null, null, null);
		// // logEventTemp.setArgumentArray(null);
		// logEventTemp.setComputer(null);
		// logEventTemp.setFormattedMessage(null);
		// logEventTemp.setLevel(LogLevel.ALL);
		// logEventTemp.setLoggerName(LOGGER.getName());
		// logEventTemp.setMessage(null);
		// // logEventTemp.setProcess(null);
		// logEventTemp.setThreadName(Thread.currentThread().getName());
		// // logEventTemp.setThrowable(null);
		// logEventTemp.setTimeStamp(System.currentTimeMillis());
		// logEventTemp.getMessage();
		// // logEventTemp.getArgumentArray();
		// List<LogEvent> logEventList = new ArrayList<LogEvent>();
		// logEventList.add(logEventTemp);
		// Queue<LogEvent> queue = new ArrayBlockingQueue<LogEvent>(1);
		// queue.add(logEventTemp);
		// CopyOnWriteArrayList<Appender> appenderList =
		// LoggerContext.getInstance().getAppenderAttachable()
		// .getAppenders();
		// for (Appender appender : appenderList) {
		// if (appender instanceof TextFileAppender) {
		// ((TextFileAppender) appender).setValid(true);
		// ((TextFileAppender) appender).setValid(false);
		// ((TextFileAppender) appender).isEnabled();
		// ((TextFileAppender) appender).firstExceptionHandle(logEventTemp);
		// ((TextFileAppender) appender).firstExceptionHandle(logEventList);
		// ((TextFileAppender) appender).secondExceptionHandle(logEventTemp);
		// ((TextFileAppender) appender).secondExceptionHandle(logEventList);
		// }
		// }
		//
		// Thread.sleep(1000L);
		// System.out.println("转数据库记录");
		// LoggerContext.getInstance().loadSettings(PropertiesUtil.xmlToProperties(Config2.LOGGING_DATABASE_XML));
		// appenderList =
		// LoggerContext.getInstance().getAppenderAttachable().getAppenders();
		// for (Appender appender : appenderList) {
		// if (appender instanceof JdbcAppender) {
		// ((JdbcAppender) appender).setValid(true);
		// ((JdbcAppender) appender).isEnabled();
		// // ((JdbcAppender) appender).firstAppend(queue);
		// ((JdbcAppender) appender).convertEvent(logEventTemp);
		// ((JdbcAppender) appender).switchTable();
		// }
		// }
		//
		// Thread.sleep(1000L);
		// System.out.println("转控制台记录");
		// LoggerContext.getInstance().loadSettings(PropertiesUtil.xmlToProperties(Config2.LOGGING_CONSOLE_XML));
		// appenderList =
		// LoggerContext.getInstance().getAppenderAttachable().getAppenders();
		// for (Appender appender : appenderList) {
		// if (appender instanceof ConsoleAppender) {
		// ((ConsoleAppender) appender).setValid(true);
		// ((ConsoleAppender) appender).isEnabled();
		// }
		// }
	}

	public static class TestFilter extends Filter {

		private static Random randoom = new Random();

		@Override
		protected FilterReply doFilter(LogEvent event) {
			int index = randoom.nextInt(4);
			if (index == 0) {
				return FilterReply.DENY;
			} else if (index == 1) {
				return FilterReply.NEUTRAL;
			} else if (index == 2) {
				return FilterReply.ACCEPT;
			} else {
				return null;
			}
		}

	}

	@Test
	public void testFreshBoxFireEventQueue() throws Exception {
		FireEventQueue<String> freshBoxFireEventQueue = FireEventQueue.newFreshBoxFireEventQueue(100L,
				new Action<String>() {

					@Override
					public void run(String a) {
						LOGGER.debug(a);
					}

				});
		for (int i = 0; i < 10; i++) {
			freshBoxFireEventQueue.add(String.valueOf(i));
			Thread.sleep(101L);
		}
		Thread.sleep(500L);
	}

	@Test
	public void testConcurrentFixedSizeQueue() {
		Queue<Integer> queue = new ConcurrentFixedSizeQueue<Integer>();
		Assert.assertTrue(queue.add(1));
		Assert.assertTrue(queue.addAll(queue));
		Assert.assertTrue(queue.contains(1));
		Assert.assertTrue(queue.containsAll(queue));
		Assert.assertEquals(Integer.valueOf(1), queue.element());
		Assert.assertTrue(queue.equals(queue));
		Assert.assertNotNull(queue.hashCode());
		Assert.assertTrue(!queue.isEmpty());
		Assert.assertNotNull(queue.iterator());
		Assert.assertTrue(queue.offer(2));
		Assert.assertEquals(Integer.valueOf(1), queue.peek());
		Assert.assertEquals(Integer.valueOf(1), queue.poll());
		Assert.assertEquals(Integer.valueOf(1), queue.remove());
		Assert.assertTrue(queue.remove(2));
		Assert.assertTrue(!queue.removeAll(queue));
		queue.clear();
		Assert.assertEquals(0, queue.size());
	}

	@Test
	public void testSpecialLoggerMethod() {
		// TRACE特殊方法的全覆盖测试
		LOGGER.trace("test {}", "trace(String message, Object param1)");
		LOGGER.trace("test {}{}", "trace(String message, Object param1", ",Object param2)");
		LOGGER.trace("test {}{}{}", new String[] { "trace", "(String message", "Object[] params)" });
		LOGGER.trace("test trace(String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.trace(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}",
				"trace(Marker marker, String message, Object param1)");
		LOGGER.trace(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}",
				"trace(Marker marker, String message, Object param1", ",Object param2)");
		LOGGER.trace(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}{}", new String[] { "trace",
				"(Marker marker, String message", "Object[] params)" });
		LOGGER.trace(MarkerFactory.getMarker("testSpecialLoggerMethod"),
				"test trace(Marker marker, String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.isTraceEnabled();
		LOGGER.isTraceEnabled(MarkerFactory.getMarker("testSpecialLoggerMethod"));

		// DEBUG特殊方法的全覆盖测试
		LOGGER.debug("test {}", "debug(String message, Object param1)");
		LOGGER.debug("test {}{}", "debug(String message, Object param1", ",Object param2)");
		LOGGER.debug("test {}{}{}", new String[] { "debug", "(String message", "Object[] params)" });
		LOGGER.debug("test debug(String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.debug(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}",
				"debug(Marker marker, String message, Object param1)");
		LOGGER.debug(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}",
				"debug(Marker marker, String message, Object param1", ",Object param2)");
		LOGGER.debug(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}{}", new String[] { "debug",
				"(Marker marker, String message", "Object[] params)" });
		LOGGER.debug(MarkerFactory.getMarker("testSpecialLoggerMethod"),
				"test debug(Marker marker, String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.isDebugEnabled();
		LOGGER.isDebugEnabled(MarkerFactory.getMarker("testSpecialLoggerMethod"));

		// INFO特殊方法的全覆盖测试
		LOGGER.info("test {}", "info(String message, Object param1)");
		LOGGER.info("test {}{}", "info(String message, Object param1", ",Object param2)");
		LOGGER.info("test {}{}{}", new String[] { "info", "(String message", "Object[] params)" });
		LOGGER.info("test info(String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.info(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}",
				"info(Marker marker, String message, Object param1)");
		LOGGER.info(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}",
				"info(Marker marker, String message, Object param1", ",Object param2)");
		LOGGER.info(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}{}", new String[] { "info",
				"(Marker marker, String message", "Object[] params)" });
		LOGGER.info(MarkerFactory.getMarker("testSpecialLoggerMethod"),
				"test info(Marker marker, String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.isInfoEnabled();
		LOGGER.isInfoEnabled(MarkerFactory.getMarker("testSpecialLoggerMethod"));
		// WARN特殊方法的全覆盖测试
		LOGGER.warn("test {}", "warn(String message, Object param1)");
		LOGGER.warn("test {}{}", "warn(String message, Object param1", ",Object param2)");
		LOGGER.warn("test {}{}{}", new String[] { "warn", "(String message", "Object[] params)" });
		LOGGER.warn("test warn(String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.warn(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}",
				"warn(Marker marker, String message, Object param1)");
		LOGGER.warn(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}",
				"warn(Marker marker, String message, Object param1", ",Object param2)");
		LOGGER.warn(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}{}", new String[] { "warn",
				"(Marker marker, String message", "Object[] params)" });
		LOGGER.warn(MarkerFactory.getMarker("testSpecialLoggerMethod"),
				"test warn(Marker marker, String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.isWarnEnabled();
		LOGGER.isWarnEnabled(MarkerFactory.getMarker("testSpecialLoggerMethod"));

		// ERROR特殊方法的全覆盖测试
		LOGGER.error("test {}", "error(String message, Object param1)");
		LOGGER.error("test {}{}", "error(String message, Object param1", ",Object param2)");
		LOGGER.error("test {}{}{}", new String[] { "error", "(String message", "Object[] params)" });
		LOGGER.error("test error(String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.error(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}",
				"error(Marker marker, String message, Object param1)");
		LOGGER.error(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}",
				"error(Marker marker, String message, Object param1", ",Object param2)");
		LOGGER.error(MarkerFactory.getMarker("testSpecialLoggerMethod"), "test {}{}{}", new String[] { "error",
				"(Marker marker, String message", "Object[] params)" });
		LOGGER.error(MarkerFactory.getMarker("testSpecialLoggerMethod"),
				"test error(Marker marker, String message, Throwable t)", new RuntimeException("Don't worry!"));
		LOGGER.isErrorEnabled();
		LOGGER.isErrorEnabled(MarkerFactory.getMarker("testSpecialLoggerMethod"));

		try {
			((com.feinno.logging.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(null);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}

		try {
			String str = null;
			LoggerFactory.getLogger(str);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}

	}
}

class Config1 {
	public static final String LOGGING_DEBUG_XML = new String(new byte[] { 60, 63, 120, 109, 108, 32, 118, 101, 114,
			115, 105, 111, 110, 61, 34, 49, 46, 48, 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70,
			45, 56, 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27,
			-123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -83, -119, -25, -70, -89, 32, 45, 45, 62,
			10, 9, 60, 108, 101, 118, 101, 108, 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
			60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -68, -109, -27,
			-83, -104, -27, -92, -124, -25, -112, -122, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99,
			97, 99, 104, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 32, 108, 97, 122,
			121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104, 67, 111, 117, 110, 116, 61, 34, 50, 48, 48,
			34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
			-105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28, -67, -107, -26, -100, -119,
			105, 115, 77, 117, 108, 116, 105, 112, 108, 101, -27, -79, -98, -26, -128, -89, -28, -72, -70, 116, 114,
			117, 101, -25, -102, -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23, -125,
			-67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28, -72, -86, 44, -27, -101, -96, -26,
			-83, -92, -27, -113, -81, -28, -69, -91, -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65, -121,
			-26, -69, -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77,
			117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99,
			111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
			101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61,
			34, 68, 69, 66, 85, 71, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67, -109, -26,
			-97, -112, -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79, -69, -25, -102, -124,
			-27, -92, -124, -25, -112, -122, -25, -69, -122, -24, -118, -126, 32, 45, 45, 62, 10, 9, 60, 108, 111, 103,
			103, 101, 114, 115, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105,
			112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111,
			109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101,
			119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101, 118, 101, 108, 61, 34,
			100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108,
			116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109,
			46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114,
			46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34,
			32, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101,
			114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101,
			121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108,
			111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103,
			51, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105,
			108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103,
			105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101,
			114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111, 103, 103,
			101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107,
			101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46,
			108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111,
			103, 52, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102,
			105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101,
			34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103,
			103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32,
			108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114,
			32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115,
			115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46,
			102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108,
			101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101,
			114, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
			61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102,
			101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101,
			115, 116, 46, 77, 97, 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76, 111, 103,
			34, 32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116,
			101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
			97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110,
			103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32,
			108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111,
			103, 103, 101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116,
			105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101,
			105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105, 34, 32, 108, 101, 118, 101,
			108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105,
			115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99,
			111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112, 115, 46, 83, 109, 115, 67, 108, 105, 101,
			110, 116, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108,
			111, 103, 103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66, -109, -27, -121, -70, -26, -106,
			-71, -27, -68, -113, -25, -102, -124, -23, -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112,
			112, 101, 110, 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27, -120, -74, -27,
			-113, -80, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62,
			10, 9, 9, 60, 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117,
			101, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106, -121, -26, -100, -84, -25, -102, -124,
			-24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101,
			120, 116, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 32, 112, 97, 116, 104, 61,
			34, 47, 116, 109, 112, 47, 116, 101, 115, 116, 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45,
			45, 32, -26, -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121, -70,
			-26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 32, 101,
			110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117,
			114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46,
			57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114, 108, 62, 32, 45, 45, 62, 10,
			9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113,
			108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 32,
			45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115,
			116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60,
			116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62,
			32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47,
			117, 115, 101, 114, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 112, 97, 115, 115, 119, 111,
			114, 100, 62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 32, 45, 45, 62,
			10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 48, 46,
			49, 48, 46, 50, 48, 56, 46, 49, 50, 50, 58, 51, 51, 48, 54, 47, 76, 111, 103, 68, 66, 60, 47, 117, 114,
			108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46,
			106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9,
			60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101,
			62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116,
			97, 98, 108, 101, 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 97, 100, 109, 105, 110, 60, 47, 117, 115,
			101, 114, 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 97, 100, 109, 105, 110, 60, 47,
			112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9,
			60, 47, 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110, 103,
			62, 10 });
	public static final String LOGGING_ERROR_XML = new String(new byte[] { 60, 63, 120, 109, 108, 32, 118, 101, 114,
			115, 105, 111, 110, 61, 34, 49, 46, 48, 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70,
			45, 56, 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27,
			-123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -83, -119, -25, -70, -89, 32, 45, 45, 62,
			10, 9, 60, 108, 101, 118, 101, 108, 62, 69, 82, 82, 79, 82, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
			60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -68, -109, -27,
			-83, -104, -27, -92, -124, -25, -112, -122, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99,
			97, 99, 104, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 108, 97,
			122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104, 67, 111, 117, 110, 116, 61, 34, 50,
			48, 48, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27,
			-65, -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28, -67, -107, -26, -100,
			-119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, -27, -79, -98, -26, -128, -89, -28, -72, -70, 116,
			114, 117, 101, -25, -102, -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
			-125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28, -72, -86, 44, -27, -101, -96,
			-26, -83, -92, -27, -113, -81, -28, -69, -91, -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65,
			-121, -26, -69, -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115,
			77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34,
			99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
			116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101,
			108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67,
			-109, -26, -97, -112, -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79, -69, -25,
			-102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24, -118, -126, 32, 45, 45, 62, 10, 9, 60,
			108, 111, 103, 103, 101, 114, 115, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117,
			108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
			116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110,
			103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101, 118,
			101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115,
			77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34,
			99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
			116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 87,
			65, 82, 78, 34, 32, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108,
			116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 52, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34,
			32, 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108,
			101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
			110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114,
			107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114,
			62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
			34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101,
			105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
			116, 46, 77, 97, 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76, 111, 103, 34,
			32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
			114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97,
			115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
			46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108,
			101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103,
			103, 101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105,
			112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101, 105,
			110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105, 34, 32, 108, 101, 118, 101, 108,
			61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115,
			77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111,
			109, 46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112, 115, 46, 83, 109, 115, 67, 108, 105, 101, 110,
			116, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108, 111,
			103, 103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66, -109, -27, -121, -70, -26, -106, -71,
			-27, -68, -113, -25, -102, -124, -23, -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112,
			101, 110, 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27, -120, -74, -27, -113,
			-80, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10,
			9, 9, 60, 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117,
			101, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106, -121, -26, -100, -84, -25, -102, -124,
			-24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101,
			120, 116, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 112, 97, 116, 104,
			61, 34, 47, 116, 109, 112, 47, 116, 101, 115, 116, 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33,
			45, 45, 32, -26, -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121,
			-70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 32,
			101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60,
			117, 114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56,
			46, 57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114, 108, 62, 32, 45, 45, 62,
			10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115,
			113, 108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62,
			32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115,
			116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60,
			116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62,
			32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47,
			117, 115, 101, 114, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 112, 97, 115, 115, 119, 111,
			114, 100, 62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 32, 45, 45, 62,
			10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 48, 46,
			49, 48, 46, 50, 48, 56, 46, 49, 50, 50, 58, 51, 51, 48, 54, 47, 76, 111, 103, 68, 66, 60, 47, 117, 114,
			108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46,
			106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9,
			60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101,
			62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116,
			97, 98, 108, 101, 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 97, 100, 109, 105, 110, 60, 47, 117, 115,
			101, 114, 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 97, 100, 109, 105, 110, 60, 47,
			112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9,
			60, 47, 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110, 103,
			62, 10 });
}

class Config2 {
	public static final String LOGGING_CONSOLE_XML = new String(new byte[] { 60, 63, 120, 109, 108, 32, 118, 101, 114,
			115, 105, 111, 110, 61, 34, 49, 46, 48, 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70,
			45, 56, 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27,
			-123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -83, -119, -25, -70, -89, 32, 45, 45, 62,
			10, 9, 60, 108, 101, 118, 101, 108, 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
			60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -68, -109, -27,
			-83, -104, -27, -92, -124, -25, -112, -122, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99,
			97, 99, 104, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 108, 97,
			122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104, 67, 111, 117, 110, 116, 61, 34, 50,
			48, 48, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27,
			-65, -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28, -67, -107, -26, -100,
			-119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, -27, -79, -98, -26, -128, -89, -28, -72, -70, 116,
			114, 117, 101, -25, -102, -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
			-125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28, -72, -86, 44, -27, -101, -96,
			-26, -83, -92, -27, -113, -81, -28, -69, -91, -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65,
			-121, -26, -69, -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115,
			77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34,
			99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
			116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101,
			108, 61, 34, 68, 69, 66, 85, 71, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67,
			-109, -26, -97, -112, -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79, -69, -25,
			-102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24, -118, -126, 32, 45, 45, 62, 10, 9, 60,
			108, 111, 103, 103, 101, 114, 115, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117,
			108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
			116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110,
			103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101, 118,
			101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105,
			115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61,
			34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105,
			108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34,
			87, 65, 82, 78, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108,
			116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 52, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34,
			32, 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108,
			101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
			110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114,
			107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114,
			62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
			34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101,
			105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
			116, 46, 77, 97, 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76, 111, 103, 34,
			32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
			114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97,
			115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
			46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108,
			101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103,
			101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
			108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110,
			110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105, 34, 32, 108, 101, 118, 101, 108, 61,
			34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77,
			117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109,
			46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112, 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116,
			34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108, 111, 103,
			103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27,
			-68, -113, -25, -102, -124, -23, -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112, 101,
			110, 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27, -120, -74, -27, -113, -80,
			-25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9,
			60, 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34,
			32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106, -121, -26, -100, -84, -25, -102, -124, -24, -66,
			-109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101, 120, 116, 32,
			101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 112, 97, 116, 104, 61, 34, 47,
			116, 109, 112, 47, 116, 101, 115, 116, 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32,
			-26, -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106,
			-71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 32, 101, 110, 97,
			98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117, 114,
			108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46, 57, 52,
			46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114, 108, 62, 32, 45, 45, 62, 10, 9, 9, 9,
			60, 33, 45, 45, 32, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46,
			106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 32, 45, 45,
			62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60,
			47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 116, 97,
			98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62, 32, 45,
			45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47, 117, 115,
			101, 114, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 112, 97, 115, 115, 119, 111, 114, 100,
			62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 32, 45, 45, 62, 10, 9, 9, 9,
			60, 117, 114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 48, 46, 49, 48, 46,
			50, 48, 56, 46, 49, 50, 50, 58, 51, 51, 48, 54, 47, 76, 111, 103, 68, 66, 60, 47, 117, 114, 108, 62, 10, 9,
			9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46, 106, 100, 98,
			99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9, 60, 100, 97,
			116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9,
			9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98,
			108, 101, 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 97, 100, 109, 105, 110, 60, 47, 117, 115, 101, 114,
			62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 97, 100, 109, 105, 110, 60, 47, 112, 97,
			115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9, 60, 47,
			97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110, 103, 62, 10 });
	public static final String LOGGING_DATABASE_XML = new String(new byte[] { 60, 63, 120, 109, 108, 32, 118, 101, 114,
			115, 105, 111, 110, 61, 34, 49, 46, 48, 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70,
			45, 56, 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27,
			-123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -83, -119, -25, -70, -89, 32, 45, 45, 62,
			10, 9, 60, 108, 101, 118, 101, 108, 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
			60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -68, -109, -27,
			-83, -104, -27, -92, -124, -25, -112, -122, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99,
			97, 99, 104, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 108, 97,
			122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104, 67, 111, 117, 110, 116, 61, 34, 50,
			48, 48, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27,
			-65, -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28, -67, -107, -26, -100,
			-119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, -27, -79, -98, -26, -128, -89, -28, -72, -70, 116,
			114, 117, 101, -25, -102, -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
			-125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28, -72, -86, 44, -27, -101, -96,
			-26, -83, -92, -27, -113, -81, -28, -69, -91, -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65,
			-121, -26, -69, -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115,
			77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34,
			99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
			116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101,
			108, 61, 34, 68, 69, 66, 85, 71, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67,
			-109, -26, -97, -112, -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79, -69, -25,
			-102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24, -118, -126, 32, 45, 45, 62, 10, 9, 60,
			108, 111, 103, 103, 101, 114, 115, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117,
			108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
			116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110,
			103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101, 118,
			101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105,
			115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61,
			34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105,
			108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34,
			87, 65, 82, 78, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108,
			116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 52, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34,
			32, 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108,
			101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
			110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114,
			107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114,
			62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
			34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101,
			105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
			116, 46, 77, 97, 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76, 111, 103, 34,
			32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
			114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97,
			115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
			46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108,
			101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103,
			101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
			108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110,
			110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105, 34, 32, 108, 101, 118, 101, 108, 61,
			34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77,
			117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109,
			46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112, 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116,
			34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108, 111, 103,
			103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27,
			-68, -113, -25, -102, -124, -23, -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112, 101,
			110, 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27, -120, -74, -27, -113, -80,
			-25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9,
			60, 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101,
			34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106, -121, -26, -100, -84, -25, -102, -124, -24,
			-66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101, 120,
			116, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 112, 97, 116, 104, 61,
			34, 47, 116, 109, 112, 47, 116, 101, 115, 116, 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45,
			45, 32, -26, -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121, -70,
			-26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 32, 101,
			110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117,
			114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46,
			57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114, 108, 62, 32, 45, 45, 62, 10,
			9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113,
			108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 32,
			45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115,
			116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60,
			116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62,
			32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47,
			117, 115, 101, 114, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 112, 97, 115, 115, 119, 111,
			114, 100, 62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 32, 45, 45, 62,
			10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 48, 46,
			49, 48, 46, 50, 48, 56, 46, 49, 50, 50, 58, 51, 51, 48, 54, 47, 76, 111, 103, 68, 66, 60, 47, 117, 114,
			108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46,
			106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9,
			60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101,
			62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116,
			97, 98, 108, 101, 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 97, 100, 109, 105, 110, 60, 47, 117, 115,
			101, 114, 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 97, 100, 109, 105, 110, 60, 47,
			112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9,
			60, 47, 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110, 103,
			62, 10 });
	public static final String LOGGING_FILE_XML = new String(new byte[] { 60, 63, 120, 109, 108, 32, 118, 101, 114,
			115, 105, 111, 110, 61, 34, 49, 46, 48, 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70,
			45, 56, 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27,
			-123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -83, -119, -25, -70, -89, 32, 45, 45, 62,
			10, 9, 60, 108, 101, 118, 101, 108, 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
			60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25, -68, -109, -27,
			-83, -104, -27, -92, -124, -25, -112, -122, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99,
			97, 99, 104, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 108, 97,
			122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104, 67, 111, 117, 110, 116, 61, 34, 50,
			48, 48, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27,
			-65, -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28, -67, -107, -26, -100,
			-119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, -27, -79, -98, -26, -128, -89, -28, -72, -70, 116,
			114, 117, 101, -25, -102, -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
			-125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28, -72, -86, 44, -27, -101, -96,
			-26, -83, -92, -27, -113, -81, -28, -69, -91, -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65,
			-121, -26, -69, -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115,
			77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34,
			99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
			116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101,
			108, 61, 34, 105, 110, 102, 111, 34, 32, 47, 62, 10, 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67,
			-109, -26, -97, -112, -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79, -69, -25,
			-102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24, -118, -126, 32, 45, 45, 62, 10, 9, 60,
			108, 111, 103, 103, 101, 114, 115, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117,
			108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
			116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110,
			103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101, 118,
			101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105,
			115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61,
			34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105,
			108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34,
			87, 65, 82, 78, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108,
			116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
			103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
			32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
			97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84, 101, 115, 116,
			76, 111, 103, 52, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60,
			102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
			101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
			103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34,
			32, 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108,
			101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
			110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114,
			107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114,
			62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
			34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101,
			105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
			116, 46, 77, 97, 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76, 111, 103, 34,
			32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
			114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97,
			115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
			46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108,
			101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103,
			101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
			108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110,
			110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105, 34, 32, 108, 101, 118, 101, 108, 61,
			34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77,
			117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109,
			46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112, 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116,
			34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108, 111, 103,
			103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27,
			-68, -113, -25, -102, -124, -23, -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112, 101,
			110, 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27, -120, -74, -27, -113, -80,
			-25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9,
			60, 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101,
			34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106, -121, -26, -100, -84, -25, -102, -124, -24,
			-66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101, 120,
			116, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 32, 112, 97, 116, 104, 61, 34,
			47, 116, 109, 112, 47, 116, 101, 115, 116, 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45,
			32, -26, -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26,
			-106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 32, 101, 110,
			97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117,
			114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46,
			57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114, 108, 62, 32, 45, 45, 62, 10,
			9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113,
			108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 32,
			45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115,
			116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60,
			116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62,
			32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47,
			117, 115, 101, 114, 62, 32, 45, 45, 62, 10, 9, 9, 9, 60, 33, 45, 45, 32, 60, 112, 97, 115, 115, 119, 111,
			114, 100, 62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 32, 45, 45, 62,
			10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 48, 46,
			49, 48, 46, 50, 48, 56, 46, 49, 50, 50, 58, 51, 51, 48, 54, 47, 76, 111, 103, 68, 66, 60, 47, 117, 114,
			108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46,
			106, 100, 98, 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9,
			60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101,
			62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116,
			97, 98, 108, 101, 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 97, 100, 109, 105, 110, 60, 47, 117, 115,
			101, 114, 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 97, 100, 109, 105, 110, 60, 47,
			112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9,
			60, 47, 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110, 103,
			62, 10 });
}

// class Config1 {
// public static final String LOGGING_DEBUG_XML = new String(new byte[]{60, 63,
// 120, 109, 108, 32, 118, 101, 114, 115, 105, 111, 110, 61, 34, 49, 46, 48, 34,
// 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70, 45, 56, 34,
// 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45,
// 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25,
// -83, -119, -25, -70, -89, 32, 45, 45, 62, 10, 9, 60, 108, 101, 118, 101, 108,
// 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -25, -68, -109, -27, -83, -104, -27, -92, -124, -25, -112, -122, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99, 97, 99, 104, 101,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 32, 108,
// 97, 122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104, 67,
// 111, 117, 110, 116, 61, 34, 50, 48, 48, 34, 32, 47, 62, 10, 10, 9, 60, 33,
// 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105,
// -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28, -67,
// -107, -26, -100, -119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, -27,
// -79, -98, -26, -128, -89, -28, -72, -70, 116, 114, 117, 101, -25, -102, -124,
// -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23, -125,
// -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28, -72,
// -86, 44, -27, -101, -96, -26, -83, -92, -27, -113, -81, -28, -69, -91, -26,
// -100, -119, -27, -92, -102, -28, -72, -86, -24, -65, -121, -26, -69, -92,
// -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114, 32,
// 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101,
// 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110,
// 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101,
// 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32,
// 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 32, 47, 62, 10, 10,
// 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67, -109, -26, -97, -112, -28,
// -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79, -69,
// -25, -102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24, -118,
// -126, 32, 45, 45, 62, 10, 9, 60, 108, 111, 103, 103, 101, 114, 115, 62, 10,
// 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105,
// 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34,
// 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101,
// 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105,
// 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
// 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109,
// 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46,
// 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114,
// 34, 32, 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 32, 32, 47, 62,
// 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
// 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
// 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116,
// 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108,
// 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84,
// 101, 115, 116, 76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34,
// 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
// 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114,
// 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
// 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114,
// 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9,
// 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
// 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116,
// 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
// 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
// 116, 46, 84, 101, 115, 116, 76, 111, 103, 52, 34, 32, 108, 101, 118, 101,
// 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108,
// 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46,
// 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102,
// 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32,
// 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 47, 62, 10, 9, 9, 9, 60,
// 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108,
// 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99,
// 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110,
// 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70,
// 105, 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101,
// 98, 117, 103, 34, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62,
// 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116,
// 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61,
// 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111,
// 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116,
// 101, 115, 116, 46, 77, 97, 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97,
// 114, 107, 101, 114, 76, 111, 103, 34, 32, 108, 101, 118, 101, 108, 61, 34,
// 68, 69, 66, 85, 71, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114,
// 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
// 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
// 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
// 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34,
// 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 32, 47,
// 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 10, 9, 9, 60,
// 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108,
// 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109,
// 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46,
// 115, 112, 105, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82,
// 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115,
// 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32,
// 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 105, 109, 112, 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116, 34, 32,
// 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9,
// 60, 47, 108, 111, 103, 103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32,
// -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, -25, -102,
// -124, -23, -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112,
// 112, 101, 110, 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26,
// -114, -89, -27, -120, -74, -27, -113, -80, -25, -102, -124, -24, -66, -109,
// -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60,
// 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61,
// 34, 116, 114, 117, 101, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32,
// -26, -106, -121, -26, -100, -84, -25, -102, -124, -24, -66, -109, -27, -121,
// -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101,
// 120, 116, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101,
// 34, 32, 112, 97, 116, 104, 61, 34, 47, 116, 109, 112, 47, 116, 101, 115, 116,
// 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26,
// -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109,
// -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60,
// 100, 97, 116, 97, 98, 97, 115, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61,
// 34, 116, 114, 117, 101, 34, 62, 10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100,
// 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56,
// 46, 57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117,
// 114, 108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111,
// 109, 46, 109, 121, 115, 113, 108, 46, 106, 100, 98, 99, 46, 68, 114, 105,
// 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9, 60,
// 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97,
// 116, 97, 98, 97, 115, 101, 62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62,
// 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101,
// 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47, 117,
// 115, 101, 114, 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100,
// 62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100,
// 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9, 60, 47,
// 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111,
// 103, 103, 105, 110, 103, 62, 10});
// public static final String LOGGING_ERROR_XML = new String(new byte[]{60, 63,
// 120, 109, 108, 32, 118, 101, 114, 115, 105, 111, 110, 61, 34, 49, 46, 48, 34,
// 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70, 45, 56, 34,
// 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45,
// 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25,
// -83, -119, -25, -70, -89, 32, 45, 45, 62, 10, 9, 60, 108, 101, 118, 101, 108,
// 62, 69, 82, 82, 79, 82, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -25, -68, -109, -27, -83, -104, -27, -92, -124, -25, -112, -122, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99, 97, 99, 104, 101,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32,
// 108, 97, 122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104,
// 67, 111, 117, 110, 116, 61, 34, 50, 48, 48, 34, 32, 47, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28,
// -67, -107, -26, -100, -119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// -27, -79, -98, -26, -128, -89, -28, -72, -70, 116, 114, 117, 101, -25, -102,
// -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
// -125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28,
// -72, -86, 44, -27, -101, -96, -26, -83, -92, -27, -113, -81, -28, -69, -91,
// -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65, -121, -26, -69,
// -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114,
// 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
// 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
// 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
// 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34,
// 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10,
// 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67, -109, -26, -97, -112,
// -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79,
// -69, -25, -102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24,
// -118, -126, 32, 45, 45, 62, 10, 9, 60, 108, 111, 103, 103, 101, 114, 115, 62,
// 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116,
// 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61,
// 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111,
// 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116,
// 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101,
// 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60, 102, 105,
// 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
// 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109,
// 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46,
// 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114,
// 34, 32, 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 32, 32, 47, 62,
// 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111,
// 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61,
// 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116,
// 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108,
// 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 84,
// 101, 115, 116, 76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69,
// 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32,
// 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101,
// 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110,
// 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101,
// 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47,
// 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108,
// 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
// 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46,
// 84, 101, 115, 116, 76, 111, 103, 52, 34, 32, 108, 101, 118, 101, 108, 61, 34,
// 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114,
// 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
// 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
// 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
// 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9,
// 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105,
// 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61,
// 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103,
// 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101,
// 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111,
// 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105,
// 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34,
// 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102,
// 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103,
// 46, 110, 101, 119, 116, 101, 115, 116, 46, 77, 97, 114, 107, 101, 114, 46,
// 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76, 111, 103, 34, 32, 108,
// 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 62, 10, 9, 9, 9, 60, 102,
// 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111,
// 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
// 46, 102, 105, 108, 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105,
// 108, 116, 101, 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 100, 101, 98,
// 117, 103, 34, 32, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114,
// 62, 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101,
// 121, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111,
// 103, 103, 105, 110, 103, 46, 115, 112, 105, 34, 32, 108, 101, 118, 101, 108,
// 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 10, 9, 9, 60, 108, 111, 103,
// 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102,
// 101, 105, 110, 110, 111, 46, 105, 109, 112, 115, 46, 83, 109, 115, 67, 108,
// 105, 101, 110, 116, 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79,
// 82, 34, 32, 47, 62, 10, 9, 60, 47, 108, 111, 103, 103, 101, 114, 115, 62, 10,
// 10, 9, 60, 33, 45, 45, 32, -24, -66, -109, -27, -121, -70, -26, -106, -71,
// -27, -68, -113, -25, -102, -124, -23, -123, -115, -25, -67, -82, 32, 45, 45,
// 62, 10, 9, 60, 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 9, 9, 60,
// 33, 45, 45, 32, -26, -114, -89, -27, -120, -74, -27, -113, -80, -25, -102,
// -124, -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45,
// 45, 62, 10, 9, 9, 60, 99, 111, 110, 115, 111, 108, 101, 32, 101, 110, 97, 98,
// 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 32, 47, 62, 10, 10, 9, 9, 60,
// 33, 45, 45, 32, -26, -106, -121, -26, -100, -84, -25, -102, -124, -24, -66,
// -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9,
// 9, 60, 116, 101, 120, 116, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102,
// 97, 108, 115, 101, 34, 32, 112, 97, 116, 104, 61, 34, 47, 116, 109, 112, 47,
// 116, 101, 115, 116, 47, 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33,
// 45, 45, 32, -26, -107, -80, -26, -115, -82, -27, -70, -109, -25, -102, -124,
// -24, -66, -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45,
// 62, 10, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 32, 101, 110, 97, 98,
// 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 62, 10, 9, 9, 9, 60, 117, 114,
// 108, 62, 106, 100, 98, 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57,
// 50, 46, 49, 54, 56, 46, 57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101,
// 115, 116, 60, 47, 117, 114, 108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118,
// 101, 114, 62, 99, 111, 109, 46, 109, 121, 115, 113, 108, 46, 106, 100, 98,
// 99, 46, 68, 114, 105, 118, 101, 114, 60, 47, 100, 114, 105, 118, 101, 114,
// 62, 10, 9, 9, 9, 60, 100, 97, 116, 97, 98, 97, 115, 101, 62, 116, 101, 115,
// 116, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9, 9, 9, 60, 116,
// 97, 98, 108, 101, 62, 72, 65, 95, 76, 111, 103, 103, 105, 110, 103, 60, 47,
// 116, 97, 98, 108, 101, 62, 10, 9, 9, 9, 60, 117, 115, 101, 114, 62, 114, 111,
// 111, 116, 60, 47, 117, 115, 101, 114, 62, 10, 9, 9, 9, 60, 112, 97, 115, 115,
// 119, 111, 114, 100, 62, 49, 50, 51, 52, 53, 54, 60, 47, 112, 97, 115, 115,
// 119, 111, 114, 100, 62, 10, 9, 9, 60, 47, 100, 97, 116, 97, 98, 97, 115, 101,
// 62, 10, 9, 60, 47, 97, 112, 112, 101, 110, 100, 101, 114, 115, 62, 10, 10,
// 60, 47, 108, 111, 103, 103, 105, 110, 103, 62, 10});
// }
// class Config2 {
// public static final String LOGGING_CONSOLE_XML = new String(new byte[]{60,
// 63, 120, 109, 108, 32, 118, 101, 114, 115, 105, 111, 110, 61, 34, 49, 46, 48,
// 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70, 45, 56,
// 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33,
// 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105,
// -25, -83, -119, -25, -70, -89, 32, 45, 45, 62, 10, 9, 60, 108, 101, 118, 101,
// 108, 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
// 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -25, -68, -109, -27, -83, -104, -27, -92, -124, -25, -112, -122, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99, 97, 99, 104, 101,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32,
// 108, 97, 122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104,
// 67, 111, 117, 110, 116, 61, 34, 50, 48, 48, 34, 32, 47, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28,
// -67, -107, -26, -100, -119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// -27, -79, -98, -26, -128, -89, -28, -72, -70, 116, 114, 117, 101, -25, -102,
// -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
// -125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28,
// -72, -86, 44, -27, -101, -96, -26, -83, -92, -27, -113, -81, -28, -69, -91,
// -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65, -121, -26, -69,
// -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114,
// 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
// 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
// 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
// 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34,
// 32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 32, 47, 62, 10,
// 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67, -109, -26, -97, -112,
// -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79,
// -69, -25, -102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24,
// -118, -126, 32, 45, 45, 62, 10, 9, 60, 108, 111, 103, 103, 101, 114, 115, 62,
// 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116,
// 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61,
// 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111,
// 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116,
// 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101,
// 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102,
// 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111,
// 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
// 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101,
// 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 32, 32, 47,
// 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108,
// 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
// 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46,
// 84, 101, 115, 116, 76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34,
// 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
// 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114,
// 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
// 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114,
// 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9,
// 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
// 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116,
// 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
// 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
// 116, 46, 84, 101, 115, 116, 76, 111, 103, 52, 34, 32, 108, 101, 118, 101,
// 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108,
// 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46,
// 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102,
// 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32,
// 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
// 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77,
// 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9,
// 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111, 103,
// 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46,
// 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111,
// 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 77, 97,
// 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76,
// 111, 103, 34, 32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34,
// 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
// 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77,
// 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 32, 47, 62, 10, 9, 9, 60,
// 47, 108, 111, 103, 103, 101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103,
// 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116,
// 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105,
// 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62,
// 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108,
// 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121,
// 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112,
// 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108,
// 111, 103, 103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66,
// -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, -25, -102, -124, -23,
// -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112, 101, 110,
// 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27,
// -120, -74, -27, -113, -80, -25, -102, -124, -24, -66, -109, -27, -121, -70,
// -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 99, 111, 110,
// 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114,
// 117, 101, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106, -121,
// -26, -100, -84, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26, -106,
// -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101, 120, 116, 32,
// 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32, 112,
// 97, 116, 104, 61, 34, 47, 116, 109, 112, 47, 116, 101, 115, 116, 47, 108,
// 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -107, -80,
// -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121,
// -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97,
// 116, 97, 98, 97, 115, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102,
// 97, 108, 115, 101, 34, 62, 10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98,
// 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46,
// 57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114,
// 108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46,
// 109, 121, 115, 113, 108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101,
// 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9, 60, 100, 97, 116,
// 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98,
// 97, 115, 101, 62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76,
// 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62, 10, 9, 9, 9,
// 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47, 117, 115, 101, 114,
// 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 49, 50, 51,
// 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60,
// 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9, 60, 47, 97, 112, 112, 101,
// 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110,
// 103, 62, 10});
// public static final String LOGGING_DATABASE_XML = new String(new byte[]{60,
// 63, 120, 109, 108, 32, 118, 101, 114, 115, 105, 111, 110, 61, 34, 49, 46, 48,
// 34, 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70, 45, 56,
// 34, 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33,
// 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105,
// -25, -83, -119, -25, -70, -89, 32, 45, 45, 62, 10, 9, 60, 108, 101, 118, 101,
// 108, 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9,
// 60, 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -25, -68, -109, -27, -83, -104, -27, -92, -124, -25, -112, -122, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99, 97, 99, 104, 101,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32,
// 108, 97, 122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104,
// 67, 111, 117, 110, 116, 61, 34, 50, 48, 48, 34, 32, 47, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28,
// -67, -107, -26, -100, -119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// -27, -79, -98, -26, -128, -89, -28, -72, -70, 116, 114, 117, 101, -25, -102,
// -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
// -125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28,
// -72, -86, 44, -27, -101, -96, -26, -83, -92, -27, -113, -81, -28, -69, -91,
// -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65, -121, -26, -69,
// -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114,
// 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
// 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
// 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
// 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34,
// 32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34, 32, 47, 62, 10,
// 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67, -109, -26, -97, -112,
// -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79,
// -69, -25, -102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24,
// -118, -126, 32, 45, 45, 62, 10, 9, 60, 108, 111, 103, 103, 101, 114, 115, 62,
// 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116,
// 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61,
// 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111,
// 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116,
// 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101,
// 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102,
// 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111,
// 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
// 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101,
// 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 32, 32, 47,
// 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108,
// 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
// 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46,
// 84, 101, 115, 116, 76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34,
// 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
// 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114,
// 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
// 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114,
// 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9,
// 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
// 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116,
// 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
// 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
// 116, 46, 84, 101, 115, 116, 76, 111, 103, 52, 34, 32, 108, 101, 118, 101,
// 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108,
// 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46,
// 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102,
// 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32,
// 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
// 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77,
// 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9,
// 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111, 103,
// 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46,
// 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111,
// 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 77, 97,
// 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76,
// 111, 103, 34, 32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34,
// 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
// 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77,
// 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 32, 47, 62, 10, 9, 9, 60,
// 47, 108, 111, 103, 103, 101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103,
// 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116,
// 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105,
// 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62,
// 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108,
// 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121,
// 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112,
// 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108,
// 111, 103, 103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66,
// -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, -25, -102, -124, -23,
// -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112, 101, 110,
// 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27,
// -120, -74, -27, -113, -80, -25, -102, -124, -24, -66, -109, -27, -121, -70,
// -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 99, 111, 110,
// 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97,
// 108, 115, 101, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106,
// -121, -26, -100, -84, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101, 120, 116,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32,
// 112, 97, 116, 104, 61, 34, 47, 116, 109, 112, 47, 116, 101, 115, 116, 47,
// 108, 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -107,
// -80, -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27,
// -121, -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100,
// 97, 116, 97, 98, 97, 115, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34,
// 116, 114, 117, 101, 34, 62, 10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98,
// 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46,
// 57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114,
// 108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46,
// 109, 121, 115, 113, 108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101,
// 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9, 60, 100, 97, 116,
// 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98,
// 97, 115, 101, 62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76,
// 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62, 10, 9, 9, 9,
// 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47, 117, 115, 101, 114,
// 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 49, 50, 51,
// 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60,
// 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9, 60, 47, 97, 112, 112, 101,
// 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110,
// 103, 62, 10});
// public static final String LOGGING_FILE_XML = new String(new byte[]{60, 63,
// 120, 109, 108, 32, 118, 101, 114, 115, 105, 111, 110, 61, 34, 49, 46, 48, 34,
// 32, 101, 110, 99, 111, 100, 105, 110, 103, 61, 34, 85, 84, 70, 45, 56, 34,
// 63, 62, 10, 60, 108, 111, 103, 103, 105, 110, 103, 62, 10, 10, 9, 60, 33, 45,
// 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65, -105, -25,
// -83, -119, -25, -70, -89, 32, 45, 45, 62, 10, 9, 60, 108, 101, 118, 101, 108,
// 62, 68, 69, 66, 85, 71, 60, 47, 108, 101, 118, 101, 108, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -25, -68, -109, -27, -83, -104, -27, -92, -124, -25, -112, -122, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 60, 99, 97, 99, 104, 101,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97, 108, 115, 101, 34, 32,
// 108, 97, 122, 121, 77, 115, 61, 34, 49, 48, 48, 34, 32, 98, 97, 116, 99, 104,
// 67, 111, 117, 110, 116, 61, 34, 50, 48, 48, 34, 32, 47, 62, 10, 10, 9, 60,
// 33, 45, 45, 32, -27, -123, -88, -27, -79, -128, -26, -105, -91, -27, -65,
// -105, -24, -65, -121, -26, -69, -92, -27, -103, -88, 44, -28, -69, -69, -28,
// -67, -107, -26, -100, -119, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// -27, -79, -98, -26, -128, -89, -28, -72, -70, 116, 114, 117, 101, -25, -102,
// -124, -27, -112, -116, -27, -112, -115, -24, -118, -126, -25, -126, -71, -23,
// -125, -67, -27, -113, -81, -28, -69, -91, -28, -72, -70, -27, -92, -102, -28,
// -72, -86, 44, -27, -101, -96, -26, -83, -92, -27, -113, -81, -28, -69, -91,
// -26, -100, -119, -27, -92, -102, -28, -72, -86, -24, -65, -121, -26, -69,
// -92, -27, -103, -88, 32, 45, 45, 62, 10, 9, 60, 102, 105, 108, 116, 101, 114,
// 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117,
// 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105,
// 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116,
// 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34,
// 32, 108, 101, 118, 101, 108, 61, 34, 105, 110, 102, 111, 34, 32, 47, 62, 10,
// 10, 9, 60, 33, 45, 45, 32, -27, -123, -73, -28, -67, -109, -26, -97, -112,
// -28, -72, -128, -28, -72, -86, -27, -116, -123, -26, -120, -106, -25, -79,
// -69, -25, -102, -124, -27, -92, -124, -25, -112, -122, -25, -69, -122, -24,
// -118, -126, 32, 45, 45, 62, 10, 9, 60, 108, 111, 103, 103, 101, 114, 115, 62,
// 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116,
// 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61,
// 34, 116, 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111,
// 46, 104, 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116,
// 101, 115, 116, 46, 84, 101, 115, 116, 76, 111, 103, 50, 34, 32, 108, 101,
// 118, 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102,
// 105, 108, 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111,
// 109, 46, 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103,
// 46, 102, 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101,
// 114, 34, 32, 108, 101, 118, 101, 108, 61, 34, 87, 65, 82, 78, 34, 32, 32, 47,
// 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108,
// 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101,
// 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115,
// 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46,
// 84, 101, 115, 116, 76, 111, 103, 51, 34, 32, 108, 101, 118, 101, 108, 61, 34,
// 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101,
// 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116, 114,
// 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108,
// 116, 101, 114, 46, 77, 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114,
// 34, 32, 47, 62, 10, 9, 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9,
// 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112,
// 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116,
// 101, 115, 116, 46, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104,
// 97, 46, 108, 111, 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115,
// 116, 46, 84, 101, 115, 116, 76, 111, 103, 52, 34, 32, 108, 101, 118, 101,
// 108, 61, 34, 100, 101, 98, 117, 103, 34, 62, 10, 9, 9, 9, 60, 102, 105, 108,
// 116, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 99, 108, 97, 115, 115, 61, 34, 99, 111, 109, 46,
// 102, 101, 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 102,
// 105, 108, 116, 101, 114, 46, 77, 68, 67, 70, 105, 108, 116, 101, 114, 34, 32,
// 47, 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
// 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77,
// 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 47, 62, 10, 9,
// 9, 60, 47, 108, 111, 103, 103, 101, 114, 62, 10, 9, 9, 60, 108, 111, 103,
// 103, 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34,
// 116, 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 116, 101, 115, 116, 46,
// 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 104, 97, 46, 108, 111,
// 103, 103, 105, 110, 103, 46, 110, 101, 119, 116, 101, 115, 116, 46, 77, 97,
// 114, 107, 101, 114, 46, 84, 101, 115, 116, 77, 97, 114, 107, 101, 114, 76,
// 111, 103, 34, 32, 108, 101, 118, 101, 108, 61, 34, 68, 69, 66, 85, 71, 34,
// 62, 10, 9, 9, 9, 60, 102, 105, 108, 116, 101, 114, 32, 105, 115, 77, 117,
// 108, 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 99, 108,
// 97, 115, 115, 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46,
// 108, 111, 103, 103, 105, 110, 103, 46, 102, 105, 108, 116, 101, 114, 46, 77,
// 97, 114, 107, 101, 114, 70, 105, 108, 116, 101, 114, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 100, 101, 98, 117, 103, 34, 32, 32, 47, 62, 10, 9, 9, 60,
// 47, 108, 111, 103, 103, 101, 114, 62, 10, 10, 9, 9, 60, 108, 111, 103, 103,
// 101, 114, 32, 105, 115, 77, 117, 108, 116, 105, 112, 108, 101, 61, 34, 116,
// 114, 117, 101, 34, 32, 107, 101, 121, 61, 34, 99, 111, 109, 46, 102, 101,
// 105, 110, 110, 111, 46, 108, 111, 103, 103, 105, 110, 103, 46, 115, 112, 105,
// 34, 32, 108, 101, 118, 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62,
// 10, 10, 9, 9, 60, 108, 111, 103, 103, 101, 114, 32, 105, 115, 77, 117, 108,
// 116, 105, 112, 108, 101, 61, 34, 116, 114, 117, 101, 34, 32, 107, 101, 121,
// 61, 34, 99, 111, 109, 46, 102, 101, 105, 110, 110, 111, 46, 105, 109, 112,
// 115, 46, 83, 109, 115, 67, 108, 105, 101, 110, 116, 34, 32, 108, 101, 118,
// 101, 108, 61, 34, 69, 82, 82, 79, 82, 34, 32, 47, 62, 10, 9, 60, 47, 108,
// 111, 103, 103, 101, 114, 115, 62, 10, 10, 9, 60, 33, 45, 45, 32, -24, -66,
// -109, -27, -121, -70, -26, -106, -71, -27, -68, -113, -25, -102, -124, -23,
// -123, -115, -25, -67, -82, 32, 45, 45, 62, 10, 9, 60, 97, 112, 112, 101, 110,
// 100, 101, 114, 115, 62, 10, 9, 9, 60, 33, 45, 45, 32, -26, -114, -89, -27,
// -120, -74, -27, -113, -80, -25, -102, -124, -24, -66, -109, -27, -121, -70,
// -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 99, 111, 110,
// 115, 111, 108, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102, 97,
// 108, 115, 101, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -106,
// -121, -26, -100, -84, -25, -102, -124, -24, -66, -109, -27, -121, -70, -26,
// -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 116, 101, 120, 116,
// 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 116, 114, 117, 101, 34, 32, 112,
// 97, 116, 104, 61, 34, 47, 116, 109, 112, 47, 116, 101, 115, 116, 47, 108,
// 111, 103, 34, 32, 47, 62, 10, 10, 9, 9, 60, 33, 45, 45, 32, -26, -107, -80,
// -26, -115, -82, -27, -70, -109, -25, -102, -124, -24, -66, -109, -27, -121,
// -70, -26, -106, -71, -27, -68, -113, 32, 45, 45, 62, 10, 9, 9, 60, 100, 97,
// 116, 97, 98, 97, 115, 101, 32, 101, 110, 97, 98, 108, 101, 100, 61, 34, 102,
// 97, 108, 115, 101, 34, 62, 10, 9, 9, 9, 60, 117, 114, 108, 62, 106, 100, 98,
// 99, 58, 109, 121, 115, 113, 108, 58, 47, 47, 49, 57, 50, 46, 49, 54, 56, 46,
// 57, 52, 46, 49, 58, 51, 51, 48, 54, 47, 116, 101, 115, 116, 60, 47, 117, 114,
// 108, 62, 10, 9, 9, 9, 60, 100, 114, 105, 118, 101, 114, 62, 99, 111, 109, 46,
// 109, 121, 115, 113, 108, 46, 106, 100, 98, 99, 46, 68, 114, 105, 118, 101,
// 114, 60, 47, 100, 114, 105, 118, 101, 114, 62, 10, 9, 9, 9, 60, 100, 97, 116,
// 97, 98, 97, 115, 101, 62, 116, 101, 115, 116, 60, 47, 100, 97, 116, 97, 98,
// 97, 115, 101, 62, 10, 9, 9, 9, 60, 116, 97, 98, 108, 101, 62, 72, 65, 95, 76,
// 111, 103, 103, 105, 110, 103, 60, 47, 116, 97, 98, 108, 101, 62, 10, 9, 9, 9,
// 60, 117, 115, 101, 114, 62, 114, 111, 111, 116, 60, 47, 117, 115, 101, 114,
// 62, 10, 9, 9, 9, 60, 112, 97, 115, 115, 119, 111, 114, 100, 62, 49, 50, 51,
// 52, 53, 54, 60, 47, 112, 97, 115, 115, 119, 111, 114, 100, 62, 10, 9, 9, 60,
// 47, 100, 97, 116, 97, 98, 97, 115, 101, 62, 10, 9, 60, 47, 97, 112, 112, 101,
// 110, 100, 101, 114, 115, 62, 10, 10, 60, 47, 108, 111, 103, 103, 105, 110,
// 103, 62, 10});
// }
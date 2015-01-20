package test.com.feinno.ha.logging.newtest;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.logging.LogEvent;
import com.feinno.logging.LogLevel;
import com.feinno.logging.appender.JdbcAppender;

public class TestJdbcAppender {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestLog.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final String url = "jdbc:mysql://127.0.0.1:3306/test";
		final String driver = "com.mysql.jdbc.Driver";
		final String user = "root";
		final String password = "123456";
		final String database = "test";
		final String table = "FAE_LOG";
		for (int i = 0; i < 100; i++) {
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					JdbcAppender jdbcAppender = new JdbcAppender(url, driver, user, password, database, table);
					jdbcAppender.doAppend(newLogEvent());
				}
			});
			thread.start();
		}
	}

	public static Queue<LogEvent> newLogEvent() {
		LogEvent logEventTemp = new LogEvent((com.feinno.logging.Logger) LOGGER, null, null, null, null);
//		logEventTemp.setArgumentArray(null);
		logEventTemp.setComputer(null);
		logEventTemp.setFormattedMessage(null);
		logEventTemp.setLevel(LogLevel.ALL);
		logEventTemp.setLoggerName(LOGGER.getName());
		logEventTemp.setMessage(null);
		// logEventTemp.setProcess(null);
		logEventTemp.setThreadName(Thread.currentThread().getName());
//		logEventTemp.setThrowable(null);
		logEventTemp.setTimeStamp(System.currentTimeMillis());
		logEventTemp.getMessage();
//		logEventTemp.getArgumentArray();
		Queue<LogEvent> queue = new ArrayBlockingQueue<LogEvent>(1);
		queue.add(logEventTemp);
		return queue;
	}

}

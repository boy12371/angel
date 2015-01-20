package test.com.feinno.ha.logging.newtest;

import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.logging.counter.LoggerCounterCategory;
import com.feinno.util.TimeSpan;

public class TestCounter {

	public static void testLoggerCounter() {
		TimeSpan span = new TimeSpan(1000);
		ObserverManager.addInspector(LoggerCounterCategory.INSTANCE, ObserverReportMode.ALL, span, new ReportCallback() {
			@Override
			public boolean handle(ObserverReport report) {
				System.out.println("============================== Observation ================================= ");
				for (ObserverReportColumn col : report.getColumns()) {
					System.out.print(col.getName() + "\t");
				}
				System.out.println();
				for (ObserverReportRow row : report.getRows()) {
					// System.out.print(row.getInstanceName());
					for (String s : row.getData()) {
						System.out.print("\t" + s);
					}
					System.out.println();
				}
				return true;
			}
		});

		// for (int i = 0; i < 50; i++) {
		// Thread thread = new Thread() {
		// public void run() {
		// LoggerCounter counter =
		// LoggerCounterCategory.instance.getLoggerCounter("logger");
		// for (int j = 0; j < 1000; j++) {
		// counter.increaseWarn();
		// counter.increaseError();
		// counter.setLastException(j+"haha");
		// try {
		// Thread.sleep(10);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
		// };
		// thread.start();
		// }

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}

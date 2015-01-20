/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.diagnostic;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.diagnostic.observation.ObserverReportSnapshot;
import com.feinno.diagnostic.observation.ObserverReportUnit;
import com.feinno.diagnostic.perfmon.CounterEntity;
import com.feinno.diagnostic.perfmon.PerformanceCounterFactory;
import com.feinno.diagnostic.perfmon.monitor.MonitorHttpServer;
import com.feinno.diagnostic.perfmon.monitor.legacy.AppBeanPerformanceCounters;
import com.feinno.util.TimeSpan;


/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CounterTest
{
	public static void main(String[] args) {
		new CounterTest().test();
	}
	@Test
	public void test()
	{
		final AppBeanPerformanceCounters appCounter = PerformanceCounterFactory.getCounters(AppBeanPerformanceCounters.class, "");
		final SampleCounter counter = PerformanceCounterFactory.getCounters(SampleCounter.class, "");
		counter.getNumber().increase();
		counter.getNumber().decrease();

		Thread tr = new Thread(new Runnable() {
			@Override
			public void run()
			{
				Random rand = new Random();
				while (true) {
					try {
						Thread.sleep(0);
						long l = 0;
						for (int i = 0; i < 1 * 1; i++) {
							l = l | System.nanoTime();
						}
						counter.getThroughput().increaseBy(1000 + rand.nextInt(1000) & l & 0x0000ffff);
						counter.getRatio().increaseRatio(l % 2 == 0);
						appCounter.setTx(counter.getRatio());
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});

		tr.start();
		
		// counter.increase(2000);
		// counter.increase(100);
		CounterEntity entity = (CounterEntity) counter.getRatio();
		ObserverReportSnapshot last = entity.getObserverSnapshot();

		Observable ob = ObserverManager.getObserverItem("sample");
		ObserverManager.addInspector(ob, ObserverReportMode.ALL, new TimeSpan(5000), 
				new ReportCallback() {
					@Override
					public boolean handle(ObserverReport report)
					
					
					{
						System.out.println(report.encodeToJson());
						return true;
					}
		});
		ObserverManager.addInspector(ObserverManager.getObserverItem("apps"), ObserverReportMode.ALL, new TimeSpan(5000), 
				new ReportCallback() {
					@Override
					public boolean handle(ObserverReport report)
					{
						System.out.println(report.encodeToJson());
						return true;
					}
		});
		try {
			new MonitorHttpServer(8089);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int i=0;
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ObserverReportSnapshot now = entity.getObserverSnapshot();
			ObserverReportUnit output = now.computeReport(last);
			System.out.println(output.toString());
			i++;
			if (i==8) {
				break;
			}
		}
	}
}

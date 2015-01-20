package test.com.feinno.serialization.protobuf;

import java.io.IOException;

import org.junit.Test;

/**
 * 多线程同时执行序列化
 * 
 * @author Lv.Mingwei
 * 
 */
public class TesterThread {

	@Test
	public void test() {

		for (int i = 0; i < 20; i++) {
			new Thread() {
				public void run() {
					try {
						Tester tester = new Tester();
						tester.testPerformance(100);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

	}

}

package test.com.feinno.runtime.bytecode;

import java.util.concurrent.TimeUnit;

public class DemoClass implements IDemo {

	public void test(int number) {
		waitMS();
		for (int i = 0; i < number; i++) {
			System.out.println(i);
			waitMS();
		}
	}

	public void waitMS() {
		try {
			TimeUnit.MILLISECONDS.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		new DemoClass().test(100);
	}
}

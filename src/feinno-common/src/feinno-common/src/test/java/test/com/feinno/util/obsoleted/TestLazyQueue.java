package test.com.feinno.util.obsoleted;

import java.util.List;

import org.junit.Test;

import com.feinno.util.Action;
import com.feinno.util.obsoleted.LazyQueue;

public class TestLazyQueue {

	@Test
	public void test() {
		LazyQueue<Integer> queue = new LazyQueue<Integer>("TestLazyQueue", 100, 3, new Action<List<Integer>>() {
			@Override
			public void run(List<Integer> a) {
				System.out.println(a);
			}
		});
		queue.enQueue(1);
		queue.enQueue(2);
		queue.enQueue(3);
		queue.enQueue(4);
		queue.enQueue(5);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		queue.flush();
		queue.getCapacity();
		queue.getName();
		queue.iterator();
		queue.setCapacity(10);
		queue.size();
		queue.toString();
		queue.setRunStatus(false);
	}

}

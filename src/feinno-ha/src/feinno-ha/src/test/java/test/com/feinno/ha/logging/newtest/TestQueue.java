package test.com.feinno.ha.logging.newtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.feinno.logging.common.FireEventQueue;
import com.feinno.util.Action;

public class TestQueue {

//	private static Logger logger = LoggerFactory.getLogger(TestQueue.class);

	public final static AtomicInteger integer1 = new AtomicInteger(0);

	public final static AtomicInteger integer2 = new AtomicInteger(0);

	public static FireEventQueue<String> createQueue(boolean enableCache) {
		if (enableCache) {
			// 一个具有缓存功能的Q
			return FireEventQueue.newCacheFireEventQueue(1000,1000, new Action<Queue<String>>() {
				@Override
				public void run(Queue<String> event) {
					for (String event2 : event) {
						integer1.incrementAndGet();
						 System.out.println(event2);
					}
				}
			});
		} else {
			// 一个普通的Q，超过Q的限制值的内容直接抛弃
			return FireEventQueue.newQuickFireEventQueue(1000, new Action<String>() {
				@Override
				public void run(String event) {
					integer1.incrementAndGet();
					// System.out.println(event);
				}
			});
		}
	}

//	public static LazyQueue<String> createLazyQueue(boolean enableCache) {
//		return new LazyQueue("Test", 10000000, 1000, new Action<List<String>>() {
//			@Override
//			public void run(List<String> event) {
//				for (String str : event) {
//					integer1.incrementAndGet();
//					// System.out.println(str);
//				}
//			}
//		});
//	}
//
//	public static LazyQueue2<String> createLazyQueue2(boolean enableCache) {
//		return new LazyQueue2("Test", 10000000, 1000, new Action<List<String>>() {
//			@Override
//			public void run(List<String> event) {
//				for (String str : event) {
//					integer1.incrementAndGet();
//					// System.out.println(str);
//				}
//			}
//		});
//	}
//
//	public static LazyQueue<String> createLazyQueue(boolean enableCache, int size) {
//		return new LazyQueue("Test", 10000000, size, new Action<List<String>>() {
//			@Override
//			public void run(List<String> event) {
//				for (String str : event) {
//					integer1.incrementAndGet();
//					// System.out.println(str);
//				}
//			}
//		});
//	}
//
//	public static LazyQueue2<String> createLazyQueue2(boolean enableCache, int size) {
//		return new LazyQueue2("Test", 10000000, size, new Action<List<String>>() {
//			@Override
//			public void run(List<String> event) {
//				for (String str : event) {
//					integer1.incrementAndGet();
//					// System.out.println(str);
//				}
//			}
//		});
//	}

	public static FireEventQueue<String> createQueue(boolean enableCache, int size,long time) {
		if (enableCache) {
			// 一个具有缓存功能的Q
			return FireEventQueue.newCacheFireEventQueue(size,time, new Action<Queue<String>>() {
				@Override
				public void run(Queue<String> event) {
					for (String event2 : event) {
						integer1.incrementAndGet();
						 System.out.println(event2);
					}
				}
			});
		} else {
			// 一个普通的Q，超过Q的限制值的内容直接抛弃
			return FireEventQueue.newQuickFireEventQueue(size, new Action<String>() {
				@Override
				public void run(String event) {
					integer1.incrementAndGet();
//					 System.out.println(event);
				}
			});
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		final LazyQueue<String> queue = createLazyQueue(true,10000);

//		 final LazyQueue2<String> queue = createLazyQueue2(true);

//		 final FeinnoQueue<String> queue = createQueue(true);
		
//		final FireEventQueue<String> queue = createQueue(true, 20,100L);
		

		final FireEventQueue<String> queue = createQueue(true, 2000,1000L);

		final List<Long> list = new ArrayList<Long>();

		final Random random = new Random(47);
		
		final StringBuffer sbBuffer = new StringBuffer();
		for (int i = 0; i < 64 * 1024; i++) {
			sbBuffer.append('a');
		}
		System.out.println("size=" + (sbBuffer.toString().getBytes().length / 1024) + "K");
//		final String string = sbBuffer.toString();
		System.out.println("ready");
		final Date startDate = new Date();
		// 创建多个线程进行生产
		for (int j = 0; j < 50; j++) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < 20000; i++) {
//						queue.enQueue(String.valueOf(integer2.incrementAndGet()));
						queue.add(String.valueOf(integer2.incrementAndGet()));
						try {
							Thread.sleep(random.nextInt(30));
//							if(i>0 && i<3000){
//								Thread.sleep(random.nextInt(4));
//							}else if(i>3000 && i<6000){
//								Thread.sleep(random.nextInt(55));
//							}else if(i>6000 && i<9000){
//								Thread.sleep(random.nextInt(5));
//							}else if(i>9000 && i<12000){
//								Thread.sleep(random.nextInt(30));
//							}else if(i>12000 && i<15000){
//								Thread.sleep(random.nextInt(6));
//							}else if(i>15000 && i<18000){
//								Thread.sleep(random.nextInt(25));
//							}else if(i>18000 && i<20000){
//								Thread.sleep(random.nextInt(4));
//							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Date endDate = new Date();
					synchronized (list) {
						list.add(endDate.getTime() - startDate.getTime());
						Collections.sort(list);
						System.out.println(list.size() + ":counter:" + integer1 + ":" + list);
					}
				}
			};
			thread.start();
		}
		
	}
}

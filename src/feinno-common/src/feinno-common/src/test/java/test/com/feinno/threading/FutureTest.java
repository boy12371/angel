/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-21
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.threading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import test.com.feinno.rpc.future.FutureClient;

import com.feinno.threading.Future;
import com.feinno.threading.FutureGroup;
import com.feinno.util.EventHandler;
import com.feinno.util.SyncInvoker;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class FutureTest {

	public static void main(String args[]) throws Exception {
		// System.out.print("Time = [" + new Date() + "] FutureTest start.");
		// FutureTest test = new FutureTest();

		// test.testManyFutureUseWait(10);
		// test.testFutureSpecialFunction();
		// test.testManyFutureUseLintener(10);
		// test.testManyFutureGroupUseWaitAll(10);
		// test.testManyFutureGroupUseLintenerWaitAll(10);
		// test.testFutureGroupSpecialFunction();
		// test.testFutureGroupAwaitAny();
		// test.testManyThreadWaitFuture(10);
		// test.testManyThreadWaitFutureGroup(3);

		// test.testManyThreadWaitAnyAndAllFutureGroup(3);
		// System.out.println("Time = [" + new Date() + "] FutureTest End.");
	}

	@Test
	public void testRpcFuture() {
		FutureClient client = new FutureClient();
		client.test1();
	}

	@Test
	public void testManyFutureUseWait() throws InterruptedException {
		testManyFutureUseWait(5);
		testManyFutureGroupUseWaitAll(3);
		testManyFutureUseWait(5);
		testFutureSpecialFunction();
	}

	@Test
	public void testManyFutureUseLintener() throws InterruptedException {
		testManyFutureUseLintener(10);
	}

	@Test
	public void testManyFutureGroupUseWaitAll() throws InterruptedException {
		testManyFutureGroupUseWaitAll(10);
	}

	@Test
	public void testManyFutureGroupUseLintenerWaitAll() throws InterruptedException {
		testManyFutureGroupUseLintenerWaitAll(10);
	}

	@Test
	public void testManyThreadWaitFuture() throws InterruptedException {
		testManyThreadWaitFuture(1);
	}

	@Test
	public void testManyThreadWaitFutureGroup() throws InterruptedException {
		testManyThreadWaitFutureGroup(1);
	}

	/**
	 * 性能测试入口
	 * 
	 * @throws Exception
	 */
	// @Test
	public void testPerformance() throws Exception {
		for (int i = 1; i < 10; i++) {
			new FutureTest().testPerformance(10000, i);
		}
	}

	/**
	 * 这个测试用于将测试Future将异步变同步
	 * 
	 * @param count
	 * @throws InterruptedException
	 */
	public void testManyFutureUseWait(int count) throws InterruptedException {
		printLine("testManyFutureUseWait");
		Future<String> future1 = null;
		for (int i = 0; i < count; i++) {
			future1 = new Future<String>();
			newThread(future1, 1000, "testManyFutureUseWait future" + i).start();
			printResult(future1.getValue().toString());
		}
	}

	/**
	 * 这个测试用于将异步测试
	 * 
	 * @param count
	 */
	public void testManyFutureUseLintener(int count) {
		printLine("testManyFutureUseLintener");
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 1000, "testManyFutureUseLintener future" + i).start();
			future1.addListener(newListener());
		}
	}

	/**
	 * 这个测试用于将多个异步操作转为同一个同步操作，当前线程会阻塞知道所有任务全部结束
	 * 
	 * @param count
	 */
	public void testManyFutureGroupUseWaitAll(int count) {
		printLine("testManyFutureGroupUseWaitAll");

		// 正常验证
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 500 * i, "testManyFutureGroupUseWaitAll future" + i).start();
			futures.add(future1);
		}
		FutureGroup<Future<String>> group = new FutureGroup<Future<String>>(futures);
		try {
			group.awaitAll();
			for (Future<String> future : futures) {
				printResult(future.getValue());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 正常未超时验证
		boolean isException = false;
		futures = new ArrayList<Future<String>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 500 * i, "testManyFutureGroupUseWaitAll future" + i).start();
			futures.add(future1);
		}
		group = new FutureGroup<Future<String>>(futures);
		try {
			group.awaitAll(500 * count * 2);
			for (Future<?> future : futures) {
				printResult((String) future.getValue());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			isException = true;
		}
		Assert.assertEquals(isException, false);

		// 超时验证
		isException = false;
		futures = new ArrayList<Future<String>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 500 * i, "testManyFutureGroupUseWaitAll future" + i).start();
			futures.add(future1);
		}
		group = new FutureGroup(futures);
		try {
			group.awaitAll(500 * count / 2);
			for (Future<?> future : futures) {
				printResult((String) future.getValue());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			isException = true;
		}
		Assert.assertTrue(isException);
	}

	/**
	 * 这个测试用于测试将等待每一个异步操作结束后，在异步线程进行输出
	 * 
	 * @param count
	 */
	public void testManyFutureGroupUseLintenerWaitAll(int count) throws InterruptedException {
		printLine("testManyFutureGroupUseLintenerWaitAll");
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 500 * count, "testManyFutureGroupUseLintenerWaitAll future" + i).start();
			futures.add(future1);
		}
		FutureGroup group = new FutureGroup(futures);
		group.addListener(new EventHandler<List<Future<String>>>() {
			@Override
			public void run(Object sender, List<Future<String>> futures) {
				for (Future<String> future : futures) {
					printResult(future.getValue());
				}
			}

		});
	}

	@Test
	public void testFutureGroupAwaitAny() {
		testFutureGroupAwaitAny(10);
	}

	public void testFutureGroupAwaitAny(int count) {
		printLine("testFutureGroupAwaitAny");

		// 功能验证
		List<Future<String>> futures = new ArrayList<Future<String>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 1000 * i, "testManyFutureGroupUseWaitAll future" + i).start();
			futures.add(future1);
		}
		FutureGroup<Future<String>> group = new FutureGroup<Future<String>>(futures);
		try {
			for (int i = 0; i < count + 3; i++) {
				Future<String> future = group.awaitAny();
				if (future == null) {
					Assert.assertEquals(i, futures.size());
					break;
				}
				if (future.isDone()) {
					printResult((String) future.getValue());
				} else {
					Assert.assertTrue(false);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 正常使用
		futures = new ArrayList<Future<String>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 1000 * i, "testManyFutureGroupUseWaitAll future" + i).start();
			futures.add(future1);
		}
		group = new FutureGroup<Future<String>>(futures);
		try {
			Future<?> future = null;
			while ((future = group.awaitAny()) != null) {
				printResult((String) future.getValue());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 超时验证
		boolean isException = false;
		futures = new ArrayList<Future<String>>();
		for (int i = 0; i < count; i++) {
			Future<String> future1 = new Future<String>();
			newThread(future1, 1000 * i, "testManyFutureGroupUseWaitAll future" + i).start();
			futures.add(future1);
		}

		group = new FutureGroup<Future<String>>(futures);
		try {
			Future<String> future = null;
			while ((future = group.awaitAny(500)) != null) {
				printResult(future.getValue());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			isException = true;
		}
		// 一定要出现错误,才算正确
		Assert.assertTrue(isException);
	}

	public void testManyThreadWaitFuture(int count) {
		printLine("testManyThreadWait");

		final Future<String> future1 = new Future<String>();
		newThread(future1, 3000, "testManyThreadWait future").start();
		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					printResult(future1.getValue() + " ThreadId = [" + Thread.currentThread().getId() + "]");
				}
			}).start();
		}
	}

	public void testManyThreadWaitFutureGroup(int count) {
		printLine("testManyThreadWait");

		Future<String> future1 = new Future<String>();
		newThread(future1, 1000, "testManyThreadWaitFutureGroup future1").start();

		Future<String> future2 = new Future<String>();
		newThread(future2, 2000, "testManyThreadWaitFutureGroup future2").start();

		Future<String> future3 = new Future<String>();
		newThread(future3, 3000, "testManyThreadWaitFutureGroup future3").start();

		final FutureGroup group = new FutureGroup(future1, future2, future3);
		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Future future = null;
						while ((future = group.awaitAny()) != null) {
							printResult(future.getValue() + " ThreadId = [" + Thread.currentThread().getId() + "]");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}
	}

	public void testManyThreadWaitAnyAndAllFutureGroup(int count) {
		printLine("testManyThreadWait");

		Future<String> future1 = new Future<String>();
		newThread(future1, 1000, "testManyThreadWaitFutureGroup future1").start();

		Future<String> future2 = new Future<String>();
		newThread(future2, 3000, "testManyThreadWaitFutureGroup future2").start();

		Future<String> future3 = new Future<String>();
		newThread(future3, 5000, "testManyThreadWaitFutureGroup future3").start();

		final FutureGroup<Future<String>> group = new FutureGroup<Future<String>>(future1, future2, future3);
		// 一堆线程正在waitall
		printLine("一堆线程正在waitall");
		for (int i = 0; i < count; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						for (Future<String> future : group.awaitAll()) {
							printResult(future.getValue() + " ThreadId = [" + Thread.currentThread().getId()
									+ "]  Wait_All");
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}).start();
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// 另一个线程却在waitany
		printLine("另一个线程却在waitany");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Future<String> future = null;
					while ((future = group.awaitAny()) != null) {
						printResult(future.getValue().toString() + " ThreadId = [" + Thread.currentThread().getId()
								+ "] Wait_Any");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * Future的特殊方法测试
	 * 
	 * @throws InterruptedException
	 */
	public void testFutureSpecialFunction() throws InterruptedException {
		Future<String> future1 = new Future<String>();
		newThread(future1, 1000, "testFutureSpecialFunction future1").start();
		// 此时最后一个任务应该已经结束
		future1.await();
		Assert.assertTrue(future1.isDone());

		// 任务结束，因为没有放入过Listener，所以这个Listener会立即输出
		future1.addListener(newListener());

		// 将已经完成的Future放入Group中，那么这个Group的relase方法会得到这个Future已完成的通知,并且Group自己Finished=true掉
		FutureGroup group = new FutureGroup(future1);
		Assert.assertTrue(group.isFinished());

		// // 创建一个需要2S执行完成的任务，我等待3S的时间，任务应该可以正常完成，没有time out的出现
		// boolean timeOut = false;
		// future1 = new Future<String>();
		// newThread(future1, 2000,
		// "testManyFutureUseWait. Time out Test").start();
		// try {
		// future1.await(3000);
		// } catch (RuntimeException e) {
		// timeOut = true;
		// }
		// Assert.assertTrue(future1.isDone());
		// Assert.assertEquals(timeOut, false);
		//
		// // 创建一个新的Future，这个Future要10s才能执行完成，但是我只等待两秒钟，如果还没有返回，则报告错误
		// future1 = new Future<String>();
		// newThread(future1, 10000,
		// "testManyFutureUseWait. Time out Test").start();
		// try {
		// future1.await(2000);
		// } catch (RuntimeException e) {
		// timeOut = true;
		// }
		// Assert.assertTrue(timeOut);
		//
		// 因为刚才等待超时了，因此这个Group其实还没有结束
		// Assert.assertEquals(future1.isDone(), false);
		// 十秒钟,我等不及了,发送完成指令,发送完指令后,这个Future应该已经停止了
		future1.complete("over");
		Assert.assertTrue(future1.isDone());

		// 如果完成了我再次提交完成消息，那么应该无影响
		if (future1.isDone()) {
			future1.complete("over");
		}

		// 小测试,取到的result不能为空
		future1 = new Future<String>();
		newThread(future1, 1000, "testManyFutureUseWait. Time out Test").start();
		Assert.assertNotNull(future1.getValue());
	}

	@Test
	public void testFutureGroupSpecialFunction() throws InterruptedException {
		printLine("testManyFutureGroupUseLintenerWaitAll");
		List<Future<?>> futures = new ArrayList<Future<?>>();
		Future<String> future1 = null;
		for (int i = 0; i < 5; i++) {
			future1 = new Future<String>();
			newThread(future1, 500 * 5, "testManyFutureGroupUseLintenerWaitAll future" + i).start();
			futures.add(future1);
		}
		FutureGroup group = new FutureGroup(futures);
		// // 设置一个较短的时间,此时应该未完成,并且报告Timeout
		// boolean isTimeOut = false;
		// try {
		// group.awaitAll(1000);
		// } catch (RuntimeException e) {
		// Assert.assertEquals(group.isFinished(), false);
		// isTimeOut = true;
		// }
		// Assert.assertTrue(isTimeOut);
		// 继续等待
		group.awaitAll();
		// 此时一定已经完成了
		Assert.assertTrue(group.isFinished());
		// 当等待所有完成时再设置Listener，那么这个Listener会立即执行，这时这个Listener的执行线程就是当前线程，否则将是最后一个触发的线程
		group.addListener(new EventHandler<List<Future<String>>>() {
			@Override
			public void run(Object sender, List<Future<String>> futures) {
				for (Future<String> future : futures) {
					printResult(future.getValue());
				}
				try {
					System.out.println("我即将被催眠,王子快来救我!");
					Thread.sleep(3000);
					System.out.println("对不起，王子不在，自己醒来吧!");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// 之前创建Future时留下了一个引用,这时对这个引用进行操作,正常应该无任何问题
		future1.complete("completion");
	}

	@Test
	public void testInterruptedException() {
		boolean isInterruptedException = false;
		final Thread currentThread = Thread.currentThread();

		// 下面用例为模拟在无限等待时出现中断的情况
		Thread thread1 = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					currentThread.interrupt();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Future<String> future1 = new Future<String>();
		thread1.start();
		try {
			future1.await();
		} catch (RuntimeException e) {
			// 不要担心，此处目的为模拟线程被中断的效果
			isInterruptedException = true;
		}
		// 只有出现中断异常,此用例才算测试通过
		Assert.assertTrue(isInterruptedException);

		// 下面用例模拟在有限时间等待时出现中断的效果
		isInterruptedException = false;
		Thread thread2 = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					currentThread.interrupt();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Future<String> future2 = new Future<String>();
		thread2.start();
		try {
			future2.await(2000);
		} catch (RuntimeException e) {
			// 不要担心，此处目的为模拟线程被中断的效果
			isInterruptedException = true;
		}
		// 只有出现中断异常,此用例才算测试通过
		Assert.assertTrue(isInterruptedException);
	}

	public static void printLine(String methodName) {
		System.out.print("Time = [" + new Date() + "] ");
		System.out.println("--------------------------------" + methodName + "------------------------------");
	}

	public static void printResult(String result) {
		System.out.println("Time = [" + new Date() + "] " + result);
	}

	/**
	 * 测试代码辅助方法
	 * 
	 * @param future
	 * @param sleepTime
	 * @param result
	 * @return
	 */
	public static <V> Thread newThread(final Future<V> future, final long sleepTime, final V result) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(sleepTime);
					future.complete(result);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		return thread;
	}

	/**
	 * 为这个Test提供的一个快捷创建Listener的方法
	 * 
	 * @return
	 */
	public static EventHandler<String> newListener() {
		return new EventHandler<String>() {
			@Override
			public void run(Object sender, String a) {
				System.out.print("Time = [" + new Date() + "] ");
				System.out.println(a);

			}
		};
	}

	/**
	 * 一个SyncInvoker的创建类
	 * 
	 * @param syncInvoker
	 * @param sleepTime
	 */
	public void testSyncInvokerHelper(final SyncInvoker syncInvoker, final long sleepTime) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(sleepTime);
					syncInvoker.getCallback().run(null);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.run();
	}

	/**
	 * 一个Future的创建类
	 * 
	 * @param future
	 * @param sleepTime
	 */
	public void testFutureHelper(final Future future, final long sleepTime) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(sleepTime);
					future.complete(null);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		thread.run();
	}

	/**
	 * 性能测试
	 * 
	 * @param count
	 * @param sleepTime
	 * @throws Exception
	 */
	public void testPerformance(int count, long sleepTime) throws Exception {
		long syncInvokerTime = 0;
		long futureTime = 0;
		for (int i = 0; i < count; i++) {
			long startTime = System.nanoTime();
			SyncInvoker syncInvoker = new SyncInvoker();
			testSyncInvokerHelper(syncInvoker, sleepTime);
			syncInvoker.waitFor(sleepTime);
			long endTime = System.nanoTime();
			syncInvokerTime += (endTime - startTime);

			startTime = System.nanoTime();
			Future<String> future1 = new Future<String>();
			testFutureHelper(future1, sleepTime);
			future1.await(sleepTime);
			endTime = System.nanoTime();
			futureTime += (endTime - startTime);
		}

		System.out.println(" Time: [" + new Date() + "]");
		System.out.println("count = [" + count + "] waitTime = [" + sleepTime + "] SyncInvoker总耗时     :"
				+ (TimeUnit.MILLISECONDS.convert(syncInvokerTime, TimeUnit.NANOSECONDS) - (count * sleepTime)) + "毫秒");

		System.out.println("count = [" + count + "] waitTime = [" + sleepTime + "] future总耗时     :"
				+ (TimeUnit.MILLISECONDS.convert(futureTime, TimeUnit.NANOSECONDS) - (count * sleepTime)) + "毫秒");

	}

}

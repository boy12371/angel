package test.com.feinno.threading;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.feinno.threading.Future;
import com.feinno.threading.FutureGroup;

public class FutureGroupTester {

//	/**
//	 * @param args
//	 */
	public static void main(String[] args) throws InterruptedException {
		new FutureGroupTester().test();
	}

	@Test
	public void test() throws InterruptedException {

		int cupNumber = Runtime.getRuntime().availableProcessors(); // 获得cpu数量
		int length = Integer.MAX_VALUE / 2; // 一半的Integer最大值的等差数列进行求和(当然可以使用公式进行快速计算，但此处是为了演示效率，因此没有使用公式)
		int average = (length + cupNumber - 1) / cupNumber; // 每一份任务的平均需要处理的数量
		long sum = 0; // 计算结果
		long startTime = System.nanoTime(); // 耗时检测
		List<Future<Long>> futures = new ArrayList<Future<Long>>(); // 每一份任务都会有一个Future
		for (int i = 0; i < cupNumber; i++) { // 根据Cpu核心数量，创建对应的计算任务
			int start = i * average;
			Future<Long> futureTemp = new Future<Long>();
			futures.add(futureTemp);
			// 开启计算任务
			TaskThread taskThread = new TaskThread(futureTemp, start, start + average > length ? length : start
					+ average);
			taskThread.start();
		}
		FutureGroup<Future<Long>> futureGroup = new FutureGroup<Future<Long>>(futures); // 将多个任务同时放入FutureGroup中
		Future<Long> future = null;
		while ((future = futureGroup.awaitAny()) != null) { // 遍历每一个完成的任务，得到任务结果后计算和值
			sum += future.getValue();
		}
		long endTime = System.nanoTime(); // 结束时间
		long distributedTime = endTime - startTime;// 分布式计算耗时
		System.out.println("计算结果:" + sum + "  分布式计算耗时:"
				+ TimeUnit.MILLISECONDS.convert(distributedTime, TimeUnit.NANOSECONDS) + "ms.");

		// 以下为传统的for循环方式计算结果
		startTime = System.nanoTime(); // 传统计算的耗时检测开始
		sum = 0;
		for (int i = 0; i < length; i++) { // 计算同样数量的等差数列的值
			sum += i;
		}
		endTime = System.nanoTime(); // 传统计算结束
		long commonTime = endTime - startTime; // 传统计算耗时
		DecimalFormat decimalFormat = new DecimalFormat("00%");
		System.out.println("计算结果:" + sum + "  普通计算耗时:"
				+ TimeUnit.MILLISECONDS.convert(commonTime, TimeUnit.NANOSECONDS) + "ms.");
		System.out.println("分布式计算的耗时是传统计算的 "
				+ (decimalFormat.format(Double.valueOf(distributedTime) / Double.valueOf(commonTime))));

	}

}

class TaskThread extends Thread {
	private Future<Long> future;
	private int start;
	private int end;

	public TaskThread(Future<Long> future, int start, int end) {
		this.future = future;
		this.start = start;
		this.end = end;
	}

	public void run() {
		long sum = 0;
		for (int i = start; i < end; i++) {
			sum += i;
		}
		future.complete(sum);
	}
}

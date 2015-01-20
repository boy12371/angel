package test.com.feinno.ha.logging.newtest;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import com.feinno.logging.common.ConcurrentFixedSizeQueue;
import com.feinno.logging.common.FireEventQueue;
import com.feinno.util.Action;

/**
 * 具有并发操作以及二级缓存的一个队列，在缓存满后或间隔指定时间后会自动触发事件
 * 
 * @author Lv.Mingwei
 * 
 * @param <E>
 */
class CacheFireEventQueue<E> extends FireEventQueue<E> {

	// 一级写缓存
	private Queue<E> writeCacheQueue1;

	// 二级写缓存，当一级写满后，由此二级缓存暂时代理一级缓存的写功能，之后一级缓存会与读队列进行切换，切换后，此二级缓存会升级为一级缓存
	private Queue<E> writeCacheQueue2;

	// 读缓存,一级缓存满后，且消费者已将上一个读缓存消费完了，此时会切换一级缓存为读缓存
	private Queue<E> readQueue;

	// 切换操作是否准备好，当消费者将当前的读缓存消费完毕后，此标识会被置为true,表示可以再次进行读写切换
	private volatile boolean isReadySwitch = true;

	// 队列虽然支持并发，但是读写切换时涉及到很多步骤，不具有原子性，不能支持并发，因此在读写切换时需要进行同步操作
	private Object synObject = new Object();

	// 扫描间隔时间，由用户在构造方法中定义，如果定义为0，那么默认不进行扫描，此间隔时间表示上一次消费时间与当前时间的间距
	private long intervalTime = 0;

	// 定时器，在间隔时间后激活{@link
	// FireEventQueue#swtichCache()}方法，对队列进行一次读写切换，在每次有切换操作后，都会重新设置下次操作时间
	private Timer scanEventTimer;

	// 满足条件后触发的事件，由用户自定义
	private Action<Queue<E>> fireEvent;

	// 缓存大小，超过此大小后会激活{@link FireEventQueue#swtichCacheMustFull()}方法进行读写切换
	private int cacheSize = Integer.MAX_VALUE;

	public CacheFireEventQueue(int cacheSize, long intervalTime, Action<Queue<E>> fireEvent) {
		if (cacheSize == 0 && intervalTime == 0) {
			throw new RuntimeException("You can not create cacheSize=0 and intervalTime=0 CacheFireEventQueue Object");
		}
		this.cacheSize = cacheSize != 0 ? cacheSize : Integer.MAX_VALUE;
		this.intervalTime = intervalTime;
		this.fireEvent = fireEvent;
		this.initialize();
	}

	/**
	 * 初始化
	 */
	private void initialize() {
		writeCacheQueue1 = new ConcurrentFixedSizeQueue<E>(cacheSize);
		writeCacheQueue2 = new ConcurrentFixedSizeQueue<E>(cacheSize);
		readQueue = new ConcurrentFixedSizeQueue<E>(cacheSize);
		initScanEventTimer();
	}

	/**
	 * 初始化扫描定时器，实现从启动开始延迟指定时间后进行缓存队列的消费
	 */
	private void initScanEventTimer() {
		// 实现定时对数据进行切换以及激活队列处理ACTION
		if (intervalTime != 0) {
			if (scanEventTimer != null) {
				scanEventTimer.cancel();
			}
			scanEventTimer = new Timer();
			scanEventTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					swtichCache();
				}
			}, intervalTime);
		}
	}

	@Override
	public void add(E e) {
		// 为了发挥并发队列的优势，入队时不进行同步，当队列已满，面临读写切换时再进行同步
		if (!writeCacheQueue1.add(e)) {
			// 一级缓存已满，此时把消息写入二级缓存中
			writeCacheQueue2.add(e);
			// 一级缓存满了后，调用缓存切换方法
			swtichCacheMustFull();
		}
	}

	// private AtomicInteger atomicInteger = new AtomicInteger();

	/**
	 * 缓存切换，将一级缓存的引用给读缓存，将原二级缓存的引用升为一级缓存
	 */
	private void swtichCache() {
		synchronized (synObject) {// 切换时会遇到多线程同时操作的威胁，因此这里同步了一下
			if (isReadySwitch) {// 如果处于可切换状态

				// System.out.println(atomicInteger.incrementAndGet() +
				// ".Timer Event 已经执行 : " + TestQueue.integer1
				// + "  外部添加个数:" + (TestQueue.integer2.get() -
				// TestQueue.integer1.get()) + ", 缓存总个数:"
				// + (writeCacheQueue1.size() + writeCacheQueue2.size()) +
				// ", 一级缓存个数:" + writeCacheQueue1.size()
				// + ", 二级缓存个数:" + writeCacheQueue2.size());

				readQueue = writeCacheQueue1;// 将一级缓存引用交给读队列，用于消费者使用
				writeCacheQueue1 = writeCacheQueue2;// 将二级缓存中的写队列给一级缓存使用
				writeCacheQueue2 = new ConcurrentFixedSizeQueue<E>(cacheSize); // 重新创建具有指定大小的二级缓存
				isReadySwitch = false;// 刚刚切换完，其他线程不可以再进行切换了，等消费完再允许切换
				initScanEventTimer(); // 重新初始化扫描定时器，从此刻开始，间隔指定时间内，进行一次定时扫描，对未处理的数据进行处理
				// 消费者，吃吧
				frieFullEvent(readQueue);
			}
		}
	}

	/**
	 * 缓存切换，将一级缓存的引用给读缓存，将原二级缓存的引用升为一级缓存，切换时涉及到多步骤操作，因此同步了一下
	 */
	private void swtichCacheMustFull() {
		synchronized (synObject) {
			if (isReadySwitch && writeCacheQueue1.size() >= cacheSize) {// 如果处于可切换状态

				// System.out.println(atomicInteger.incrementAndGet() +
				// ".Full Event 已经执行 : " + TestQueue.integer1
				// + "  外部添加个数:" + (TestQueue.integer2.get() -
				// TestQueue.integer1.get()) + ", 缓存总个数:"
				// + (writeCacheQueue1.size() + writeCacheQueue2.size()) +
				// ", 一级缓存个数:" + writeCacheQueue1.size()
				// + ", 二级缓存个数:" + writeCacheQueue2.size());

				readQueue = writeCacheQueue1;// 将一级缓存引用交给读队列，用于消费者使用
				writeCacheQueue1 = writeCacheQueue2;// 将二级缓存中的写队列给一级缓存使用
				writeCacheQueue2 = new ConcurrentFixedSizeQueue<E>(cacheSize);
				isReadySwitch = false;// 刚刚切换完，其他线程不可以再进行切换了，等消费完再允许切换
				initScanEventTimer(); // 重新初始化扫描定时器，从此刻开始，间隔指定时间内，进行一次定时扫描，对未处理的数据进行处理
				// 消费者，吃吧
				frieFullEvent(readQueue);
			}
		}
	}

	/**
	 * 当读队列满了的时候，调用此方法给消费者
	 * 
	 * @param readQueue
	 */
	private void frieFullEvent(final Queue<E> readQueue) {
		// 启动一个线程调用消费方法
		if (fireEvent != null) {
			// 考虑到线程中需要使用readQueue对象，外部需要声明为final，因此如果用同一个线程，在读和取队列切换时无法对此readQueue引用进行更新，因此只能每次消费是创建一个线程
			Thread thread = new Thread() {
				@Override
				public void run() {
					fireEvent.run(readQueue); // 回调具体业务处理方法
					readQueue.clear();// 吃完了清理盘子。
					isReadySwitch = true;// 消费完了，读队列也被清空了，此时将切换状态设置为true,可以进行再次切换了
				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}
}

/**
 * 一个具有即时响应的队列，一旦有数据入队列，则激活事件处理方法，为了防止入列速度大于处理速度，构造方法中也增加了限制队列容量的参数，参数为0时， 默认使用
 * {@link Integer#MAX_VALUE}
 * 
 * @author Lv.Mingwei
 * 
 * @param <E>
 */
class QuickFireEventQueue<E> extends FireEventQueue<E> {

	// 一级写缓存
	private LinkedBlockingQueue<E> dateQueue;

	// 此Q的容量，超过此容量，则进行忽略
	private int capacity = Integer.MAX_VALUE;

	// 当容易有内容的时候，所触发的事件
	private Action<E> fireEvent;

	public QuickFireEventQueue(int capacity, Action<E> fireEvent) {
		this.capacity = capacity != 0 ? capacity : Integer.MAX_VALUE;
		this.fireEvent = fireEvent;
		this.initialize();
		this.fireEventListener();
	}

	private void initialize() {
		dateQueue = new LinkedBlockingQueue<E>(capacity);
	}

	@Override
	public void add(E e) {
		dateQueue.offer(e);
	}

	private void fireEventListener() {
		// 启动一个线程调用消费方法
		if (fireEvent != null) {
			// 将切换标识设置为false，等待回调方法消费完读取队列中的内容后，再将标志置回来
			Thread thread = new Thread() {
				@Override
				public void run() {
					while (true) {
						try {
							fireEvent.run(dateQueue.take());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}

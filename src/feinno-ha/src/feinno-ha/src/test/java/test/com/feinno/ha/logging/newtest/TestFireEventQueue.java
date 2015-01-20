package test.com.feinno.ha.logging.newtest;

import java.util.Random;

import com.feinno.logging.common.FireEventQueue;
import com.feinno.util.Action;

public class TestFireEventQueue {

	private static boolean add;

	public static void main(String args[]) {
		//
		//
		// DynamicTableManager.processData(String.class, TestDynamicTable.class,
		// "ha_workerendpoint",
		// new DynamicTableAction<String, TestDynamicTable>() {
		//
		// @Override
		// public boolean process(ConfigTable<String, TestDynamicTable> table) {
		// if(table!=null){
		// LOGGER.info("******************************************************************************************************************************");
		// LOGGER.info("表地址: {}",table.toString());
		// LOGGER.info("数据量: {}",table.getCount());
		// LOGGER.info("表名称: {}",table.getTableName());
		// LOGGER.info("******************************************************************************************************************************");
		// }
		// else
		// LOGGER.info("failed to load dynmaic table");
		// return true;
		// }
		//
		// });
		//
		//

		final FireEventQueue<String> fireEventQueue = FireEventQueue.newFreshBoxFireEventQueue(1000,
				new Action<String>() {
					@Override
					public void run(String str) {
						System.out.println(System.currentTimeMillis() + "   " + str);
					}

				});

		final Random random = new Random(17);
		for (int j = 0; j < 3; j++) {
			final int jj = j;
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread thread = new Thread() {
				@Override
				public void run() {
					for (int i = 0; i < 20000000; i++) {
						System.out.println(System.currentTimeMillis() + "   " + "触发一次" + jj);
						fireEventQueue.add(jj + "那边来事件啦~");
						try {
							Thread.sleep(random.nextInt(1500));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (i == 100) {
							fireEventQueue.close();
						}
					}
				}
			};
			thread.setName("test" + j);
			thread.start();
		}

		// Set<TestSet> set = Collections.synchronizedSet(new
		// HashSet<TestSet>());
		// TestSet set1 = new TestSet("lv", 18);
		// TestSet set2 = new TestSet("lv", 19);
		// TestSet set3 = new TestSet("lv", 20);

		// set.add(set1);
		// set.add(set2);
		// set.add(set3);

		// if (set.contains(set1)) {
		// set.remove(set1);
		// }
		// set.add(set1);
		//
		// if (set.contains(set2)) {
		// set.remove(set2);
		// }
		// set.add(set2);
		//
		// if (set.contains(set3)) {
		// set.remove(set3);
		// }
		// set.add(set3);
		//
		// System.out.println(set);
	}

}

class TestSet {

	String name;

	Integer age;

	public TestSet(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TestSet)) {
			return false;
		}

		TestSet testSet = (TestSet) obj;
		return this.name.equals(testSet.name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String toString() {
		return "name:[" + name + "], age:[" + age + "]";
	}

}
package test.com.feinno.serialization.protobuf;

import java.util.concurrent.TimeUnit;

import test.com.feinno.serialization.protobuf.bean.Table;
import test.com.feinno.serialization.protobuf.bean.User;

import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.util.Action;

public class TestEnhanceProtoEntity {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// ProtoManager.setDebug(true);
		// System.out.println(-(1<<7));
		 test1(1000000);
		// System.out.println("-------");
		// test2(1000000);
		// ClassFile classFile =
		// ClassFile.valueOf(ProtoEntityEnhancer.toBytes(FullElementsBean.class));
		// MethodInfo method=
		// classFile.getMethods()[classFile.getMethods_count()-1];
		// System.out.println(InstructionUtil.toInstructionString(classFile,
		// method.getCodeAttribute().getCodes()));
		// TestEnhanceProtoEntity test = new TestEnhanceProtoEntity();
		// long time = test.timeCalc(new Action<Integer>() {
		//
		// @Override
		// public void run(Integer counter) {
		// for (int i = 0; i < counter; i++) {
		// ProtoManager.getEnhanceProtoEntity(Table.class);
		// ProtoManager.getEnhanceProtoEntity(User.class);
		// }
		// }
		//
		// }, 1000000);
		// System.out.println(time);

	}

	public static void test1(int count) throws Exception {
		Table table = DataCreater.newTable(true);
		table.toByteArray();

		long startTime = System.nanoTime();
		for (int i = 0; i < count; i++) {
			table = new Table();
			table.setId(1);
			table.setName("我是Table");
			table.setEmail("Table@163.com");
			User user = new User();
			user.setId(2);
			user.setName("我是User");
			user.setEmail("User@163.com");
			table.setUser(user);

			table.toByteArray();
		}
		long endTime = System.nanoTime();
		System.out.println("序列化耗时：" + TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS) + "毫秒");

		table = new Table();
		table.setId(1);
		table.setName("我是Table");
		table.setEmail("Table@163.com");
		User user = new User();
		user.setId(2);
		user.setName("我是User");
		user.setEmail("User@163.com");
		table.setUser(user);

		byte[] buffer = table.toByteArray();

		startTime = System.nanoTime();
		for (int i = 0; i < count; i++) {
			ProtoManager.parseFrom(new Table(), buffer);
		}
		endTime = System.nanoTime();
		System.out.println("反序列化耗时：" + TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS) + "毫秒");
	}

	public static void test2(int count) throws Exception {
		Table table = ProtoManager.getEnhanceProtoEntity(Table.class);
		table.toByteArray();

		long startTime = System.nanoTime();
		for (int i = 0; i < count; i++) {
			table = ProtoManager.getEnhanceProtoEntity(Table.class);
			table.setId(1);
			table.setName("我是Table");
			table.setEmail("Table@163.com");
			User user = ProtoManager.getEnhanceProtoEntity(User.class);
			user.setId(2);
			user.setName("我是User");
			user.setEmail("User@163.com");
			table.setUser(user);

			table.toByteArray();
		}
		long endTime = System.nanoTime();
		System.out.println("序列化耗时：" + TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS) + "毫秒");

		table = ProtoManager.getEnhanceProtoEntity(Table.class);
		table.setId(0);
		table.setName("我是Table");
		table.setEmail("Table@163.com");
		User user = ProtoManager.getEnhanceProtoEntity(User.class);
		user.setId(0);
		user.setName("我是User");
		user.setEmail("User@163.com");
		table.setUser(user);
		byte[] buffer = table.toByteArray();

		startTime = System.nanoTime();
		for (int i = 0; i < count; i++) {
			ProtoManager.parseFrom(ProtoManager.getEnhanceProtoEntity(Table.class), buffer);
		}
		endTime = System.nanoTime();
		System.out.println("反序列化耗时：" + TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS) + "毫秒");

		Table tableTemp = ProtoManager.parseFrom(new Table(), buffer);
		System.out.println(tableTemp.hasValue(2));
		System.out.println(tableTemp.getUser().hasValue(2));
	}

	public <T> long timeCalc(Action<T> action, T t) {
		long startTime = System.nanoTime();
		action.run(t);
		long endTime = System.nanoTime();
		return TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
	}

}

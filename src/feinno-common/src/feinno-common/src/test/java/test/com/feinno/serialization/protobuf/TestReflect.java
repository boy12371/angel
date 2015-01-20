package test.com.feinno.serialization.protobuf;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * 这是一个测试反射效率的类
 * 
 * @author Lv.Mingwei
 * 
 */
public class TestReflect {

	private static final DecimalFormat df = new DecimalFormat("0.0000");

	public static void main(String[] args) throws Exception {
//		long time[][] = new long[10000000][2];
		long time[][] = new long[10000*100][2];
		long commonSum = 0;
		long reflectSum = 0;

		UserBean user = new UserBean();
		Method method = UserBean.class.getDeclaredMethod("setName", String.class);
		for (int i = 0; i < time.length; i++) {
			time[i][0] = testCommon(user, "ZhangSan");
			time[i][1] = testReflect(method, user, "ZhangSan");
			commonSum += time[i][0];
			reflectSum += time[i][1];
		}
		System.out.println("==================" + time.length + "==================次结果统计");
		System.out
				.println("普通调用耗时 " + TimeUnit.MILLISECONDS.convert(commonSum, TimeUnit.NANOSECONDS) + "毫秒,平均每次耗时:"
						+ commonSum / time.length + "纳秒,比例:"
						+ df.format(Double.valueOf(commonSum) / Double.valueOf(commonSum)));
		System.out.println("反射调用耗时 " + TimeUnit.MILLISECONDS.convert(reflectSum, TimeUnit.NANOSECONDS) + "毫秒,平均每次耗时:"
				+ reflectSum / time.length + "纳秒,比例:"
				+ df.format(Double.valueOf(reflectSum) / Double.valueOf(commonSum)));
	}

	public static <T> long testReflect(Method method, T t, Object... params) throws Exception {
		long startTime = System.nanoTime();
		method.invoke(t, params);
		return System.nanoTime() - startTime;
	}

	public static <T> long testCommon(UserBean user, String name) throws Exception {
		long startTime = System.nanoTime();
		user.setName(name);
		return System.nanoTime() - startTime;
	}

}

class UserBean {

	private String name;

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

}

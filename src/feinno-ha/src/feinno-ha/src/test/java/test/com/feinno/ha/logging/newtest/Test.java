package test.com.feinno.ha.logging.newtest;

public class Test {

	private static Test instance = new Test();

	public static final Test getInstance() {
		return instance;
	}

	static {
		System.out.println("走静态块：");
		getInstance().init();
	}

	private Test() {
		System.out.println("构造方法：");
		init();
	}

	private void init() {
		System.out.println("Test2.test():" + Test.getInstance());
	}

	public static void main(String args[]) {
		Test.getInstance();
	}

}

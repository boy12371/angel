package test.com.feinno.serialization.protobuf.javaeval;

import java.lang.reflect.Method;

import org.junit.Test;

import com.feinno.serialization.protobuf.util.JavaEval;

/**
 * JavaEvel的测试用例
 * 
 * @author Lv.Mingwei
 * 
 */
public class JavaEvalTest {

	/**
	 * 测试类首先创建了一个接口，又创建了针对这个接口的实现类，那么此时来反射这个实现类的方法，验证可以正常反射成功
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestInnerImplement() throws Exception {
		String java0 = "package com.feinno.serialization.protobuf.extension; public interface IAutoClassTest { public void sayFeinno(String name); }";
		String java1 = "package com.feinno.serialization.protobuf.extension; public class AutoClassTest implements com.feinno.serialization.protobuf.extension.IAutoClassTest { public void sayFeinno(String name){System.out.println(String.format(name + \" : Hello Feinno!\",name));}}";
		JavaEval.compile(java0);
		Object object = JavaEval.eval(java1);
		Class.forName("com.feinno.serialization.protobuf.extension.IAutoClassTest", true,
				JavaEval.getSpecialClassLoader());
		Method m = object.getClass().getMethod("sayFeinno", new Class[] { String.class });
		Object[] o = new Object[] { "Lv.Mingwei" };
		m.invoke(object, o);
	}

	/**
	 * 字符串源码继承了外部已经存在的接口，在EVAL这个源码后上转型为外部存在的接口，并且通过正常的方法调用执行这个接口里的方法
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestOuterImplement() throws Exception {
		String java1 = "package com.test.javaeval; public class AutoClassTest implements test.com.feinno.serialization.protobuf.javaeval.IAutoClassTest{ public void sayFeinno(String name){System.out.println(String.format(name + \" : Hello Feinno!\",name));}}";
		IAutoClassTest autoClassTest = JavaEval.eval(IAutoClassTest.class, java1);
		autoClassTest.sayFeinno("Lv.Mingwei");
	}

	/**
	 * 用于类中具有内部类的测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestInnerClass() throws Exception {
		String java1 = "package com.test.javaeval; public class AutoClassTest implements test.com.feinno.serialization.protobuf.javaeval.IAutoClassTest { public void sayFeinno(String name) { System.out.println(String.format(AutoClassTest.InnerClass1.getName(name) + \" : Hello Feinno!\", name)); } public static class InnerClass1 { public static String getName(String name) { return name; } } }";
		IAutoClassTest autoClassTest = JavaEval.eval(IAutoClassTest.class, java1);
		autoClassTest.sayFeinno("Lv.Mingwei");
	}

	/**
	 * public类的测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestPublicClass() throws Exception {
		String java1 = "package com.test1; public class AutoClassTest implements test.com.feinno.serialization.protobuf.javaeval.IAutoClassTest { public void sayFeinno(String name) { System.out.println(String.format(AutoClassTest.InnerClass1.getName(name) + \" : Hello Feinno!\", name)); } public static class InnerClass1 { public static String getName(String name) { return name; } } }";
		IAutoClassTest autoClassTest = JavaEval.eval(IAutoClassTest.class, java1);
		autoClassTest.sayFeinno("Lv.Mingwei");
	}

	/**
	 * enum类的测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestEnumClass() throws Exception {
		String java1 = "package com.test2; public enum AutoClassTest { Test1, Test2; public String sayFeinno(String name) { return name + \" : Hello Feinno!\"; }}";
		JavaEval.compile(java1);
	}

	/**
	 * 抽象类的测试
	 * 
	 * @throws Exception
	 */
	@Test
	public void TestAbstractClass() throws Exception {
		String java0 = "package com.test3; public interface IAutoClassTest { public void sayFeinno(String name); }";
		String java1 = "package com.test3; public class AutoClassTest implements com.test3.IAutoClassTest { public void sayFeinno(String name) { System.out.println(String.format(AutoClassTest.InnerClass1.getName(name) + \" : Hello Feinno!\", name)); } public static class InnerClass1 { public static String getName(String name) { return name; } } }";
		JavaEval.compile(java0);
		Object object = JavaEval.eval(java1);
		Method m = object.getClass().getMethod("sayFeinno", new Class[] { String.class });
		Object[] o = new Object[] { "Lv.Mingwei" };
		m.invoke(object, o);
	}

	/**
	 * 用于测试类的互相依赖，A类需要B类，B类又需要A类，A类又需要B类...
	 */
	@Test
	public void TestANeedBNeedANeedB() throws Exception {
		String java0 = "package com.feinno.serialization.protobuf.extension; public class A implements test.com.feinno.serialization.protobuf.javaeval.IAutoClassTest {  public void sayFeinno(String name){ com.feinno.serialization.protobuf.extension.B b = new com.feinno.serialization.protobuf.extension.B(); System.out.println(String.format(name + \" : Hello Feinno!\",name));}}";
		String java1 = "package com.feinno.serialization.protobuf.extension; public class B implements test.com.feinno.serialization.protobuf.javaeval.IAutoClassTest {  public void sayFeinno(String name){ com.feinno.serialization.protobuf.extension.A a = new com.feinno.serialization.protobuf.extension.A(); System.out.println(String.format(name + \" : Hello Feinno!\",name));}}";
		// 多个类互相依赖时，将这些类同时编译
		JavaEval.compile(java0, java1);
		IAutoClassTest autoClassTest = JavaEval.eval(IAutoClassTest.class, java0);
		autoClassTest.sayFeinno("Lv.Mingwei");
	}

	/**
	 * 用于测试生成类位于的路径是否正确
	 */
	@Test
	public void TestClassPath() throws Exception {
		String java0 = "package com.testclasspath; public class AutoClassTest implements test.com.feinno.serialization.protobuf.javaeval.IAutoClassTest { public void sayFeinno(String name) { System.out.println(String.format(AutoClassTest.InnerClass1.getName(name) + \" : Hello Feinno!\", name)); } public static class InnerClass1 { public static String getName(String name) { return name; } } }";
		IAutoClassTest autoClassTest = JavaEval.eval(IAutoClassTest.class, java0);
		autoClassTest.sayFeinno("Lv.Mingwei");
	}

	// @Test
	// public void testJavaEval() throws Exception {
	// System.out.println("TestInnerImplement()");
	// TestInnerImplement();
	// System.out.println("TestOuterImplement()");
	// TestOuterImplement();
	// System.out.println("TestInnerClass()");
	// TestInnerClass();
	// System.out.println("TestPublicClass()");
	// TestPublicClass();
	// System.out.println("TestEnumClass()");
	// TestEnumClass();
	// System.out.println("TestAbstractClass()");
	// TestAbstractClass();
	// System.out.println("TestANeedBNeedANeedB()");
	// TestANeedBNeedANeedB();
	// System.out.println("TestClassPath()");
	// TestClassPath();
	// }

}

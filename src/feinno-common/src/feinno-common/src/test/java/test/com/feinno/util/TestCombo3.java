package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.feinno.util.Combo2;
import com.feinno.util.Combo3;

public class TestCombo3 {
	Combo3<String, String, String> cb3;

	@Before
	public void setUp() {
		cb3 = new Combo3<String, String, String>("test1", "test2", "test3");
	}

	@Test
	public void TestSetGet1() {
		cb3.setV1("newtest1");
		Assert.assertEquals("newtest1", cb3.getV1());
	}

	@Test
	public void TestSetGet2() {
		cb3.setV2("newtest2");
		Assert.assertEquals("newtest2", cb3.getV2());
	}

	@Test
	public void TestSetGet3() {
		cb3.setV3("newtest3");
		Assert.assertEquals("newtest3", cb3.getV3());
	}

	@Test
	public void TestHashCode() {
		System.out.println("Combo3 hashCode():" + cb3.hashCode());
		Combo3<String, String, String> cbNull = new Combo3<String, String, String>(null, null, null);
		System.out.println("Combo3 hashCode() null:" + cbNull.hashCode());
	}

	@Test
	public void TestEquals() {
		Assert.assertEquals(true, cb3.equals(cb3));
		Assert.assertEquals(false, cb3.equals(null));
		Assert.assertEquals(false, cb3.equals(new String()));
		// cbTest:null test test; new:test test test;
		Combo3<String, String, String> cbTest = new Combo3<String, String, String>(
				null, "test", "test");
		Assert.assertEquals(false, cbTest
				.equals(new Combo3<String, String, String>("test", "test",
						"test")));
		// cbTest:test null test; new:test test test;
		cbTest.setV1("test");
		cbTest.setV2(null);
		Assert.assertEquals(false, cbTest
				.equals(new Combo3<String, String, String>("test", "test",
						"test")));
		// cbTest:test test null; new:test test test;
		cbTest.setV1("test");
		cbTest.setV2("test");
		cbTest.setV3(null);
		Assert.assertEquals(false, cbTest
				.equals(new Combo3<String, String, String>("test", "test",
						"test")));
		// cbTest:test test test; new:test test test;
		cbTest.setV3("test");
		Assert.assertEquals(true, cbTest
				.equals(new Combo3<String, String, String>("test", "test",
						"test")));
		// cbTest:test test test; new:test1 test test;
		Assert.assertEquals(false, cbTest
				.equals(new Combo3<String, String, String>("test1", "test",
						"test")));
		// cbTest:test test test; new:test test1 test;
		Assert.assertEquals(false, cbTest
				.equals(new Combo3<String, String, String>("test", "test1",
						"test")));
		// cbTest:test test test; new:test test test1;
		Assert.assertEquals(false, cbTest
				.equals(new Combo3<String, String, String>("test", "test",
						"test1")));

	}
}

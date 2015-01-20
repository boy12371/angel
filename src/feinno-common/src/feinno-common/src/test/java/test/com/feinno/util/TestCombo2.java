package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import test.com.feinno.util.TestFlags.TestEI;

import com.feinno.util.Combo2;
import com.feinno.util.Flags;

public class TestCombo2 {
	Combo2<String, String> cb2;

	@Before
	public void setUp() {
		cb2 = new Combo2<String, String>("test1", "test2");
	}

	@Test
	public void TestSetGet1() {
		cb2.setV1("newtest1");
		Assert.assertEquals("newtest1", cb2.getV1());
	}

	@Test
	public void TestSetGet2() {
		cb2.setV2("newtest2");
		Assert.assertEquals("newtest2", cb2.getV2());
	}

	@Test
	public void TestHashCode() {
		System.out.println("Combo2 hashCode():" + cb2.hashCode());
		Combo2<String, String> cbNull = new Combo2<String, String>(null, null);
		System.out.println("Combo2 hashCode() null:" + cbNull.hashCode());
	}

	@Test
	public void TestEquals() {
		Assert.assertEquals(true, cb2.equals(cb2));
		Assert.assertEquals(false, cb2.equals(null));
		Assert.assertEquals(false, cb2.equals(new String()));
		// cbTest:null test; new:test test;
		Combo2<String, String> cbTest = new Combo2<String, String>(null, "test");
		Assert.assertEquals(false,
				cbTest.equals(new Combo2<String, String>("test", "test")));
		// cbTest:test null; new:test test;
		cbTest.setV1("test");
		cbTest.setV2(null);
		Assert.assertEquals(false,
				cbTest.equals(new Combo2<String, String>("test", "test")));
		// cbTest:test test; new:test test;
		cbTest.setV2("test");
		Assert.assertEquals(true,
				cbTest.equals(new Combo2<String, String>("test", "test")));
		// cbTest:test test; new:test1 test;
		Assert.assertEquals(false,
				cbTest.equals(new Combo2<String, String>("test1", "test")));
		// cbTest:test test; new:test test1;
		Assert.assertEquals(false,
				cbTest.equals(new Combo2<String, String>("test", "test1")));

	}
}

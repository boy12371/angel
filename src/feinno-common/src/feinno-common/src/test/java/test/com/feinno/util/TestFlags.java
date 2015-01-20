package test.com.feinno.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.feinno.util.EnumInteger;
import com.feinno.util.Flags;

public class TestFlags {
	private Flags TestFlags1;
	private Flags TestFlags2;

	private EnumInteger testEI1;
	private EnumInteger testEI2;

	@Before
	public void setUp() {
		TestFlags1 = new Flags(1);
		testEI1 = new TestEI(0);
		TestFlags2 = new Flags(new TestEI(0));
		testEI2 = new TestEI(1);
	}

	@Test
	public void TestOr() {
		Assert.assertEquals(1, TestFlags1.or(testEI1).intValue());
		Assert.assertEquals(1, TestFlags1.or(TestFlags2).intValue());
	}

	@Test
	public void TestAnd() {
		Assert.assertEquals(0, TestFlags1.and(testEI1).intValue());
		Assert.assertEquals(0, TestFlags1.and(TestFlags2).intValue());
	}

	@Test
	public void TestXor() {

		Assert.assertEquals(1, TestFlags1.xor(testEI1).intValue());
		// 为啥没有xor的这种方法呢？
	}

	@Test
	public void TestFlagsHas() {

		Assert.assertEquals(false, TestFlags1.has(testEI1));
		Assert.assertEquals(true, TestFlags1.has(testEI2));

	}

	@Test
	public void TestSetGetFlag() {

		TestFlags1.setFlag(testEI1, false);
		Assert.assertEquals(false, TestFlags1.getFlag(testEI1));
		TestFlags1.setFlag(testEI1, true);
		Assert.assertEquals(true, TestFlags1.getFlag(testEI2));

	}

	@Test
	public void TestExtract() {
		Assert.assertEquals(0, TestFlags1.extract(testEI1));

	}

	@Test
	public void TestGetMaskOrder() {
		TestEI testEI3 = new TestEI(7);
		Assert.assertEquals(0, Flags.getMaskOrder(testEI3));
		TestEI testEI4 = new TestEI(8);
		Assert.assertEquals(3, Flags.getMaskOrder(testEI4));

		TestEI testEI5 = new TestEI(8);
		Assert.assertEquals(0, Flags.getMaskOrder(1));
		Assert.assertEquals(3, Flags.getMaskOrder(8));

	}

	@Test
	public void TestValueOf() {
		Flags testFlags = Flags.valueOf(5);
		System.out.println("Test ValueOf :" + testFlags);

	}

	@Test
	public void TestOf() {
		Flags testFlags = Flags.of(testEI1, testEI2);
		System.out.println("Test Of :" + testFlags);

	}

	@Test
	public void TestValue() {
		Assert.assertEquals(1, TestFlags1.value());
	}

	@Test
	public void TestEquals() {
		Assert.assertEquals(true, TestFlags1.equals(TestFlags1));
		Assert.assertEquals(false, TestFlags1.equals(testEI1));
		Assert.assertEquals(false, TestFlags1.equals(TestFlags2));
		Assert.assertEquals(true, TestFlags1.equals(new Flags(1)));
	}

	class TestEI implements EnumInteger {
		private int value;

		public TestEI(int value) {
			this.value = value;
		}

		@Override
		public int intValue() {
			return value;
		}
	}
}

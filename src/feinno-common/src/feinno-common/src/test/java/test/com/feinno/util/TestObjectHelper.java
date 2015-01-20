package test.com.feinno.util;

import java.text.ParseException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.Guid;
import com.feinno.util.ObjectHelper;

public class TestObjectHelper {
	public enum Color {
		Red, Green, Blue;
	}

	@Test
	public void TestToString() {
		Object obj = null;
		Assert.assertEquals("", ObjectHelper.toString(obj));
		Assert.assertEquals("", ObjectHelper.toString(""));

	}

	@Test
	public void TestConvertTo() {
		try {
			// 正常测试
			Assert.assertEquals(null, ObjectHelper.convertTo(null, null));
			Assert.assertEquals('t', ObjectHelper.convertTo("test", Character.class));
			Assert.assertEquals('t', ObjectHelper.convertTo("test", char.class));

			Assert.assertEquals(new Byte((byte) 0), ObjectHelper.convertTo("0", Byte.class));
			Assert.assertEquals(new Byte((byte) 0), ObjectHelper.convertTo("0", byte.class));

			Assert.assertEquals(new Short((short) 0), ObjectHelper.convertTo("0", short.class));
			Assert.assertEquals(new Short((short) 0), ObjectHelper.convertTo("0", Short.class));

			Assert.assertEquals(new Integer(100), ObjectHelper.convertTo("100", Integer.class));
			Assert.assertEquals(new Integer(100), ObjectHelper.convertTo("100", int.class));

			Assert.assertEquals(new Long(100), ObjectHelper.convertTo("100", Long.class));
			Assert.assertEquals(new Long(100), ObjectHelper.convertTo("100", long.class));

			Assert.assertEquals(new Float(100), ObjectHelper.convertTo("100", Float.class));
			Assert.assertEquals(new Float(100), ObjectHelper.convertTo("100", float.class));

			Assert.assertEquals(new Double(100), ObjectHelper.convertTo("100", Double.class));
			Assert.assertEquals(new Double(100), ObjectHelper.convertTo("100", double.class));

			Assert.assertEquals(Boolean.TRUE, ObjectHelper.convertTo("true", Boolean.class));
			Assert.assertEquals(Boolean.TRUE, ObjectHelper.convertTo("1", Boolean.class));
			Assert.assertEquals(Boolean.TRUE, ObjectHelper.convertTo("true", boolean.class));
			Assert.assertEquals(Boolean.TRUE, ObjectHelper.convertTo("1", boolean.class));
			Assert.assertEquals(Boolean.FALSE, ObjectHelper.convertTo("false", Boolean.class));
			Assert.assertEquals(Boolean.FALSE, ObjectHelper.convertTo("0", Boolean.class));
			Assert.assertEquals(Boolean.FALSE, ObjectHelper.convertTo("false", boolean.class));
			Assert.assertEquals(Boolean.FALSE, ObjectHelper.convertTo("0", boolean.class));

			Assert.assertEquals(null, ObjectHelper.convertTo("yyyy-mm-dd", Date.class));
			Assert.assertEquals("Wed Dec 12 12:12:12 CST 2012",
					ObjectHelper.convertTo("2012-12-12 12:12:12", Date.class).toString());
			Assert.assertEquals("Wed Dec 12 00:00:00 CST 2012", ObjectHelper.convertTo("20121212", Date.class)
					.toString());

			Assert.assertEquals("Red", ObjectHelper.convertTo("Red", Color.class).toString());

			Assert.assertEquals("Object", ObjectHelper.convertTo("Object", Object.class).toString());

			// 异常测试
			try {
				ObjectHelper.convertTo("Test", ObjectHelper.class);
				Assert.fail();
			} catch (Exception e) {
				Assert.assertTrue(true);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testSpeConvertTo() {
		try {
			// 特殊情况测试
			Assert.assertEquals(new Short((short) 0), ObjectHelper.convertTo("-9.00929999999999E14", Short.class));
			Assert.assertEquals(new Short((short) Short.MAX_VALUE), ObjectHelper.convertTo("32767.1", short.class));

			Assert.assertEquals(new Integer(Integer.MIN_VALUE),
					ObjectHelper.convertTo("-9.00929999999999E14", Integer.class));
			Assert.assertEquals(new Integer(100), ObjectHelper.convertTo("100.1", int.class));

			Assert.assertEquals(new Long(-900929999999999L), ObjectHelper.convertTo("-9.00929999999999E14", Long.class));
			Assert.assertEquals(new Long(100), ObjectHelper.convertTo("100.1", long.class));

			Assert.assertEquals(Boolean.TRUE, ObjectHelper.convertTo("1.1", Boolean.class));
			Assert.assertEquals(Boolean.TRUE, ObjectHelper.convertTo("1.1", boolean.class));
			Assert.assertEquals(Boolean.FALSE, ObjectHelper.convertTo("0.0", Boolean.class));
			Assert.assertEquals(Boolean.FALSE, ObjectHelper.convertTo("0.0", boolean.class));

			Assert.assertEquals("Red", ObjectHelper.convertTo("Red", Color.class).toString());

			Assert.assertEquals("Object", ObjectHelper.convertTo("Object", Object.class).toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestGetClassName() {
		String str = new String();
		Assert.assertEquals("java.lang.String", ObjectHelper.getClassName(str.getClass(), false));
		Assert.assertEquals("java.langjava.lang.String", ObjectHelper.getClassName(str.getClass(), true));
	}

	@Test
	public void TestCompatibleGetHashCode() {
		short testShort = 100;
		String testStr = new String("test");
		Byte testByte = 100;
		Long testLong = (long) 100;
		Assert.assertEquals(100, ObjectHelper.compatibleGetHashCode(100));
		Assert.assertEquals(100, ObjectHelper.compatibleGetHashCode(testShort));
		Assert.assertEquals(1684300900, ObjectHelper.compatibleGetHashCode(testByte));
		Assert.assertEquals(100, ObjectHelper.compatibleGetHashCode(testLong));
		Assert.assertEquals(-354185609, ObjectHelper.compatibleGetHashCode(testStr));
		Guid guid = Guid.randomGuid();
		int i = guid.hashCode();
		Assert.assertEquals(i, ObjectHelper.compatibleGetHashCodeObject(guid));
		// Assert.assertEquals(, ObjectHelper.compatibleGetHashCode(new
		// Date()));
		System.out.println("Date compatibleGetHashCode:" + ObjectHelper.compatibleGetHashCode(new Date()));
	}

	@Test
	public void TestDumpObject() {
		String testDumpObject1 = ObjectHelper.dumpObject(Guid.randomGuid());
		System.out.println(testDumpObject1);
		Assert.assertEquals("<null>", ObjectHelper.dumpObject(null, "test"));
		String testDumpObject2 = ObjectHelper.dumpObject(Guid.randomGuid(), "data1");
		System.out.println(testDumpObject2);
	}

	@Test
	public void TestSetValue() {

	}

}

package test.com.feinno.util;

import junit.framework.Assert;
import org.junit.Test;

import test.com.feinno.serialization.protobuf.enumType.BooleanEnumType;

import com.feinno.util.EnumType;

public class TestEnumType {

	@Test
	public void TestValues() {
		Assert.assertEquals("[]", EnumTypeTest.values(EnumTypeDefaultValue.class).toString());
	}

	@Test
	public void TestValueOf() {
		try {
			System.out.println(EnumTypeDefaultValue.valueOf(EnumTypeDefaultValue.class, null));
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(true);
		}
		try {
			System.out.println(EnumTypeDefaultValue.valueOf(EnumTypeDefaultValue.class, 1));
			Assert.fail();
		} catch (RuntimeException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void EnumType() {
	}

	@Test
	public void TestException() {
		try {
			EnumTypeTest enumTypeTest = new EnumTypeTest(null);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void TestError() {
		try {
			EnumTypeTest enumTypeTest = new EnumTypeTest(new Object());
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		// 先检查key的类型
		try {
			EnumTypeIntegerTest enumTypeTest = new EnumTypeIntegerTest(Integer.valueOf(1));
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		// 检查枚举类是不是final
		try {
			EnumTypeFinalTest enumTypeTest = new EnumTypeFinalTest(Integer.valueOf(1));
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		// 检查构造函数是不是private

		try {
			EnumTypeFPTest enumTypeTest = EnumTypeFPTest.NewInstance(1);
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		// 检查是否实现valueOf方法。
		try {
			EnumTypeValueOfTest enumTypeTest = EnumTypeValueOfTest.NewInstance(1);
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		// 检查valueOf方法是不是static、public
		try {
			EnumTypeSPTest enumTypeTest = EnumTypeSPTest.NewInstance(1);
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		// 检查valueOf的返回类型是否与枚举类型一致
		try {
			EnumTypeSPReturnTest enumTypeTest = EnumTypeSPReturnTest.NewInstance(1);
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
		
		// 检查是否实现返回默认值的方法defaultValue。
		// try {
		// EnumTypeDefaultValue enumTypeTest =
		// EnumTypeDefaultValue.NewInstance(1);
		// Assert.fail();
		// } catch (Error e) {
		// Assert.assertTrue(true);
		// }
		
		// 检查defaultValue方法是不是static、public
		try {
			EnumTypeDVSP enumTypeTest = EnumTypeDVSP.NewInstance(1);
			Assert.fail();
		} catch (Error e) {
			Assert.assertTrue(true);
		}
	}

	public class EnumTypeTest extends EnumType<Object, EnumType> {

		protected EnumTypeTest(Object value) {
			super(value);
			// TODO Auto-generated constructor stub
		}
	}

	// 先检查key的类型
	public class EnumTypeIntegerTest extends EnumType<Integer, EnumType> {

		protected EnumTypeIntegerTest(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

	}

	// 检查枚举类是不是final
	final class EnumTypeFinalTest extends EnumType<Integer, EnumType> {

		protected EnumTypeFinalTest(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

	}

	// 检查构造函数是不是private
	final static class EnumTypeFPTest extends EnumType<Integer, EnumType> {

		private EnumTypeFPTest(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

		static public EnumTypeFPTest NewInstance(int value) {
			return new EnumTypeFPTest(value);
		}

	}

	// 检查是否实现valueOf方法。
	final static class EnumTypeValueOfTest extends EnumType<Integer, EnumType> {

		private EnumTypeValueOfTest(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

		static public EnumTypeValueOfTest NewInstance(int value) {
			return new EnumTypeValueOfTest(value);
		}

		EnumTypeValueOfTest valueOf(Integer key) {
			return EnumType.valueOf(EnumTypeValueOfTest.class, key);
		}
	}

	// 检查valueOf方法是不是static、public
	final static class EnumTypeSPTest extends EnumType<Integer, EnumType> {

		private EnumTypeSPTest(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

		static public EnumTypeSPTest NewInstance(int value) {
			return new EnumTypeSPTest(value);
		}

		public static EnumTypeSPTest valueOf(Integer key) {
			return EnumType.valueOf(EnumTypeSPTest.class, key);
		}
	}

	// 检查valueOf的返回类型是否与枚举类型一致
	final static class EnumTypeSPReturnTest extends EnumType<Integer, EnumType> {

		private EnumTypeSPReturnTest(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

		static public EnumTypeSPReturnTest NewInstance(int value) {
			return new EnumTypeSPReturnTest(value);
		}

		public static EnumTypeSPTest valueOf(Integer key) {
			return EnumType.valueOf(EnumTypeSPTest.class, key);
		}
	}

	// 检查是否实现返回默认值的方法defaultValue。
	final static class EnumTypeDefaultValue extends EnumType<Integer, EnumType> {

		private EnumTypeDefaultValue(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

		static public EnumTypeDefaultValue NewInstance(int value) {
			return new EnumTypeDefaultValue(value);
		}

		public static EnumTypeDefaultValue valueOf(Integer key) {
			return EnumTypeDefaultValue.valueOf(EnumTypeDefaultValue.class, key);
		}

		public static EnumTypeDefaultValue defaultValue() {
			return EnumType.valueOf(EnumTypeDefaultValue.class, true);
		}
	}

	// 检查defaultValue方法是不是static、public
	final static class EnumTypeDVSP extends EnumType<Integer, EnumType> {

		private EnumTypeDVSP(Integer value) {
			super(value);
			// TODO Auto-generated constructor stub
		}

		static public EnumTypeDVSP NewInstance(int value) {
			return new EnumTypeDVSP(value);
		}

		public static EnumTypeDVSP valueOf(Integer key) {
			return EnumTypeDefaultValue.valueOf(EnumTypeDVSP.class, key);
		}

		EnumTypeDVSP defaultValue() {
			return EnumTypeDefaultValue.valueOf(EnumTypeDVSP.class, true);
		}
	}

}

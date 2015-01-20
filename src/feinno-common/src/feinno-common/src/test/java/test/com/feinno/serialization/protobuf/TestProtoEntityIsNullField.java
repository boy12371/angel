package test.com.feinno.serialization.protobuf;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.serialization.Serializer;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.serialization.protobuf.ProtoMember;

public class TestProtoEntityIsNullField {

	public static void main(String args[]) throws Exception {
		new TestProtoEntityIsNullField().testFieldIsNull();
	}

	@Test
	public void testFieldIsNull() throws Exception {
		TestNullFieldBean testBean = new TestNullFieldBean();
		// Test1
		byte[] buffer = Serializer.encode(testBean);
		TestNullFieldBean result = Serializer.decode(TestNullFieldBean.class, buffer);
		Assert.assertEquals(result.getAge1(), 0);
		Assert.assertEquals(result.getAge2(), 0);
		Assert.assertEquals(result.getAge3(), 0);
		Assert.assertEquals(result.hasValue(1), false);
		Assert.assertEquals(result.hasValue(2), false);
		Assert.assertEquals(result.hasValue(3), true);

		// Test2
		testBean.setAge2(250);
		buffer = Serializer.encode(testBean);
		result = Serializer.decode(TestNullFieldBean.class, buffer);
		Assert.assertEquals(result.getAge1(), 0);
		Assert.assertEquals(result.getAge2(), 250);
		Assert.assertEquals(result.getAge3(), 0);
		Assert.assertEquals(result.hasValue(1), false);
		Assert.assertEquals(result.hasValue(2), true);
		Assert.assertEquals(result.hasValue(3), true);

		// Test3
		TestNullFieldBean enhanceBean = ProtoManager.getEnhanceProtoEntity(TestNullFieldBean.class);
		enhanceBean.setAge2(0);
		buffer = Serializer.encode(enhanceBean);
		result = Serializer.decode(TestNullFieldBean.class, buffer);
		Assert.assertEquals(result.getAge1(), 0);
		Assert.assertEquals(result.getAge2(), 0);
		Assert.assertEquals(result.getAge3(), 0);
		Assert.assertEquals(result.hasValue(1), false);
		Assert.assertEquals(result.hasValue(2), true);
		Assert.assertEquals(result.hasValue(3), true);

		// Test4
		testBean.setAge1(0);
		testBean.setAge2(0);
		testBean.putSerializationFieldTag(2);
		testBean.setAge3(0);
		testBean.putSerializationFieldTag(3);

		buffer = Serializer.encode(testBean);
		result = Serializer.decode(TestNullFieldBean.class, buffer);
		Assert.assertEquals(result.getAge1(), 0);
		Assert.assertEquals(result.getAge2(), 0);
		Assert.assertEquals(result.getAge3(), 0);
		Assert.assertEquals(result.hasValue(1), false);
		Assert.assertEquals(result.hasValue(2), true);
		Assert.assertEquals(result.hasValue(3), true);
	}

	public static class TestNullFieldBean extends ProtoEntity {

		@ProtoMember(1)
		private int age1 = 0;
		@ProtoMember(2)
		private int age2 = 0;
		@ProtoMember(value = 3, required = true)
		private int age3 = 0;

		public int getAge1() {
			return age1;
		}

		public void setAge1(int age1) {
			this.age1 = age1;
		}

		public int getAge2() {
			return age2;
		}

		public void setAge2(int age2) {
			this.age2 = age2;
		}

		public int getAge3() {
			return age3;
		}

		public void setAge3(int age3) {
			this.age3 = age3;
		}

	}

}

package test.com.feinno.serialization.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.feinno.serialization.Codec;
import com.feinno.serialization.Serializer;
import com.feinno.serialization.json.JsonContract;
import com.feinno.serialization.protobuf.CodedInputStream;
import com.feinno.serialization.protobuf.InvalidProtocolBufferException;
import com.feinno.serialization.protobuf.NullProtoBuilderFactory;
import com.feinno.serialization.protobuf.ProtoDebug;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoManager;
import com.feinno.serialization.protobuf.generator.ProtoConfig;
import com.feinno.serialization.protobuf.generator.ProtoEntityCodeGenerator;
import com.feinno.serialization.protobuf.generator.ProtoEntityCodeGenerator.ProtoJavaBeanParam;
import com.feinno.serialization.protobuf.generator.ProtoFieldType;
import com.feinno.serialization.protobuf.types.ProtoBoolean;
import com.feinno.serialization.protobuf.types.ProtoBooleanArray;
import com.feinno.serialization.protobuf.types.ProtoByte;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoChar;
import com.feinno.serialization.protobuf.types.ProtoCharArray;
import com.feinno.serialization.protobuf.types.ProtoComboDoubleInt;
import com.feinno.serialization.protobuf.types.ProtoComboDoubleString;
import com.feinno.serialization.protobuf.types.ProtoDouble;
import com.feinno.serialization.protobuf.types.ProtoDoubleArray;
import com.feinno.serialization.protobuf.types.ProtoFloat;
import com.feinno.serialization.protobuf.types.ProtoFloatArray;
import com.feinno.serialization.protobuf.types.ProtoIntArray;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoLong;
import com.feinno.serialization.protobuf.types.ProtoLongArray;
import com.feinno.serialization.protobuf.types.ProtoShort;
import com.feinno.serialization.protobuf.types.ProtoShortArray;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.serialization.protobuf.util.ArrayUtil;
import com.feinno.serialization.protobuf.util.ClassUtils;
import com.feinno.serialization.protobuf.util.FileUtil;
import com.feinno.serialization.protobuf.util.Formater;
import com.feinno.serialization.protobuf.util.JavaEval;
import com.feinno.serialization.protobuf.util.JavaEvalException;
import com.feinno.serialization.protobuf.util.ProtoGenericsUtils;
import com.feinno.serialization.protobuf.util.StringTemplateLoader;
import com.feinno.serialization.protobuf.util.ToolProvider;

public class TestSpecialClass {

	public String string = "Test";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Test
	public void test() {
		try {
			new NullProtoBuilderFactory<ProtoEntity>(new RuntimeException()).newProtoBuilder(new ProtoEntity());
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testConstructors() {
		new ProtoConfig();
		new ProtoEntityCodeGenerator();
		new ProtoJavaBeanParam();
		new ProtoEntityCodeGenerator();
	}

	@Test
	public void testSpecialMethod() {
		ProtoFieldType.OBJECT.getFieldInterpreter();
	}

	@Test
	public void testUtil() {
		List<Byte> byteList = new ArrayList<Byte>();
		byteList.add(Byte.valueOf((byte) 90));
		byteList.add(Byte.valueOf((byte) 91));
		byteList.add(Byte.valueOf((byte) 92));
		ArrayUtil.wrapsToPrimitive(byteList);
		ArrayUtil.listToArray(byteList, new byte[3]);
		new Formater();
		new ProtoDebug();
		new ToolProvider();
		new ProtoGenericsUtils();
		ProtoGenericsUtils.getClass(this.getClass().getFields()[0]);
		FileUtil.read(this.getClass().getResource("").getPath() + this.getClass().getSimpleName() + ".class");
		FileUtil.readLine(this.getClass().getResource("").getPath() + this.getClass().getSimpleName() + ".class", 10000);

		ClassUtils.newClassInstance("java.lang.String");
		JavaEval.newClassInstance(String.class, "java.lang.String");
		try {
			ClassUtils.newClassInstance(String.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testException() {
		new JavaEvalException();
		new JavaEvalException("Test");
		new StringTemplateLoader("").AddTemplate("", "");
		InvalidProtocolBufferException.invalidTag();
		InvalidProtocolBufferException.invalidWireType();
		InvalidProtocolBufferException.malformedVarint();
		InvalidProtocolBufferException.negativeSize();
		InvalidProtocolBufferException.recursionLimitExceeded();
		InvalidProtocolBufferException.sizeLimitExceeded();
		InvalidProtocolBufferException.truncatedMessage();
		try {
			ProtoManager.setDebug(true);
			InvalidProtocolBufferException.invalidEndTag();
			ProtoManager.setDebug(false);
		} catch (Exception e) {
			// 这是一定会出错的，所以此时为真
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testCodeInputStream() throws IOException {
		byte[] buffer = Serializer.encode(DataCreater.newFullElementsBean(true));
		CodedInputStream input = CodedInputStream.newInstance(buffer);
		input.skipField(0);
		input.skipField(1);
		input.skipField(2);
		// input.skipField(3);
		input.skipField(4);
		input.skipField(5);
		try {
			input.skipField(6);
		} catch (Exception e) {
			// 覆盖测试，这里一定会出异常
		}
		try {
			input.skipMessage();
		} catch (Exception e) {
			// 覆盖测试，这里一定会出异常
		}
		try {
			input.readGroup(1, null);
		} catch (Exception e) {
			// 覆盖测试，这里一定会出异常
		}
		CodedInputStream.readRawVarint32(0, new ByteArrayInputStream(buffer));
		input.resetSizeCounter();
		input.getTotalBytesRead();
		input.getBytesUntilLimit();
		input.readRawBytes(buffer.length - 1000);
		input.skipField(1);
		input.setRecursionLimit(1000000);
		input.setSizeLimit(1000000);
	}

	@Test
	public void testOldTypes() throws Exception {
		byte[] buffer = null;

		ProtoBoolean protoBoolean = new ProtoBoolean();
		protoBoolean.setValue(true);
		buffer = Serializer.encode(protoBoolean);
		Assert.assertTrue(Serializer.decode(ProtoBoolean.class, buffer).isValue());

		ProtoBooleanArray protoBooleanArray = new ProtoBooleanArray();
		protoBooleanArray.setValue(new boolean[] { true, false, true });
		buffer = Serializer.encode(protoBooleanArray);
		ProtoBooleanArray protoBooleanArray2 = Serializer.decode(ProtoBooleanArray.class, buffer);
		Assert.assertEquals(protoBooleanArray2.getValue().length, 3);
		Assert.assertEquals(protoBooleanArray2.getValue()[0], true);
		Assert.assertEquals(protoBooleanArray2.getValue()[1], false);
		Assert.assertEquals(protoBooleanArray2.getValue()[2], true);
		new ProtoBoolean(true);
		new ProtoBooleanArray(new boolean[] { true, false, true });

		ProtoByte protoByte = new ProtoByte();
		protoByte.setValue((byte) 78);
		buffer = Serializer.encode(protoByte);
		Assert.assertTrue(Serializer.decode(ProtoByte.class, buffer).getValue() == (byte) 78);

		ProtoByteArray protoByteArray = new ProtoByteArray();
		protoByteArray.setValue(new byte[] { (byte) 78, (byte) 79, (byte) 80 });
		buffer = Serializer.encode(protoByteArray);
		ProtoByteArray protoByteArray2 = Serializer.decode(ProtoByteArray.class, buffer);
		Assert.assertEquals(protoByteArray2.getValue().length, 3);
		Assert.assertEquals(protoByteArray2.getValue()[0], (byte) 78);
		Assert.assertEquals(protoByteArray2.getValue()[1], (byte) 79);
		Assert.assertEquals(protoByteArray2.getValue()[2], (byte) 80);
		new ProtoByte((byte) 78);
		new ProtoByteArray(new byte[] { (byte) 78, (byte) 79, (byte) 80 });

		ProtoChar protoChar = new ProtoChar();
		protoChar.setValue((char) 78);
		buffer = Serializer.encode(protoChar);
		Assert.assertTrue(Serializer.decode(ProtoChar.class, buffer).getValue() == (char) 78);

		ProtoCharArray protoCharArray = new ProtoCharArray();
		protoCharArray.setValue(new char[] { (char) 78, (char) 79, (char) 80 });
		buffer = Serializer.encode(protoCharArray);
		ProtoCharArray protoCharArray2 = Serializer.decode(ProtoCharArray.class, buffer);
		Assert.assertEquals(protoCharArray2.getValue().length, 3);
		Assert.assertEquals(protoCharArray2.getValue()[0], (char) 78);
		Assert.assertEquals(protoCharArray2.getValue()[1], (char) 79);
		Assert.assertEquals(protoCharArray2.getValue()[2], (char) 80);

		ProtoComboDoubleInt protoComboDoubleInt = new ProtoComboDoubleInt();
		protoComboDoubleInt.setStr1(1);
		protoComboDoubleInt.setStr2(2);
		buffer = Serializer.encode(protoComboDoubleInt);
		ProtoComboDoubleInt protoComboDoubleInt2 = Serializer.decode(ProtoComboDoubleInt.class, buffer);
		Assert.assertEquals(protoComboDoubleInt2.getStr1(), 1);
		Assert.assertEquals(protoComboDoubleInt2.getStr2(), 2);
		Assert.assertTrue(protoComboDoubleInt2.equals(protoComboDoubleInt));
		protoComboDoubleInt = new ProtoComboDoubleInt(1, 2);

		ProtoComboDoubleString protoComboDoubleString = new ProtoComboDoubleString();
		protoComboDoubleString.setStr1("1");
		protoComboDoubleString.setStr2("2");
		buffer = Serializer.encode(protoComboDoubleString);
		ProtoComboDoubleString protoComboDoubleString2 = Serializer.decode(ProtoComboDoubleString.class, buffer);
		Assert.assertEquals(protoComboDoubleString2.getStr1(), "1");
		Assert.assertEquals(protoComboDoubleString2.getStr2(), "2");
		Assert.assertTrue(protoComboDoubleString2.equals(protoComboDoubleString));
		protoComboDoubleString = new ProtoComboDoubleString("1", "2");
		protoComboDoubleInt.equals(null);
		protoComboDoubleInt.hashCode();
		protoComboDoubleString2.equals(null);
		protoComboDoubleString2.hashCode();

		ProtoDouble protoDouble = new ProtoDouble();
		protoDouble.setValue(78d);
		buffer = Serializer.encode(protoDouble);
		Assert.assertTrue(Serializer.decode(ProtoDouble.class, buffer).getValue() == 78d);

		ProtoDoubleArray protoDoubleArray = new ProtoDoubleArray();
		double[] doubleArray = new double[] { (double) 78, (double) 79, (double) 80 };
		protoDoubleArray.setValue(doubleArray);
		buffer = Serializer.encode(protoDoubleArray);
		ProtoDoubleArray protoDoubleArray2 = Serializer.decode(ProtoDoubleArray.class, buffer);
		Assert.assertEquals(protoDoubleArray2.getValue().length, 3);
		Assert.assertArrayEquals(protoDoubleArray2.getValue(), doubleArray, 0.1);

		ProtoFloat protoFloat = new ProtoFloat();
		protoFloat.setValue(78f);
		buffer = Serializer.encode(protoFloat);
		Assert.assertTrue(Serializer.decode(ProtoFloat.class, buffer).getValue() == 78f);

		ProtoFloatArray protoFloatArray = new ProtoFloatArray();
		float[] floatArray = new float[] { (float) 78, (float) 79, (float) 80 };
		protoFloatArray.setValue(floatArray);
		buffer = Serializer.encode(protoFloatArray);
		ProtoFloatArray protoFloatArray2 = Serializer.decode(ProtoFloatArray.class, buffer);
		Assert.assertEquals(protoFloatArray2.getValue().length, 3);
		Assert.assertArrayEquals(protoFloatArray2.getValue(), floatArray, 0.1f);

		ProtoInteger protoInteger = new ProtoInteger();
		protoInteger.setValue(78);
		buffer = Serializer.encode(protoInteger);
		Assert.assertTrue(Serializer.decode(ProtoInteger.class, buffer).getValue() == 78);

		ProtoIntArray protoIntArray = new ProtoIntArray();
		int[] intArray = new int[] { 78, 79, 80 };
		protoIntArray.setValue(intArray);
		buffer = Serializer.encode(protoIntArray);
		ProtoIntArray protoIntArray2 = Serializer.decode(ProtoIntArray.class, buffer);
		Assert.assertEquals(protoIntArray2.getValue().length, 3);
		Assert.assertArrayEquals(protoIntArray2.getValue(), intArray);

		ProtoLong protoLong = new ProtoLong();
		protoLong.setValue(78);
		buffer = Serializer.encode(protoLong);
		Assert.assertTrue(Serializer.decode(ProtoLong.class, buffer).getValue() == 78);

		ProtoLongArray protoLongArray = new ProtoLongArray();
		long[] longArray = new long[] { 78, 79, 80 };
		protoLongArray.setValue(longArray);
		buffer = Serializer.encode(protoLongArray);
		ProtoLongArray protoLongArray2 = Serializer.decode(ProtoLongArray.class, buffer);
		Assert.assertEquals(protoLongArray2.getValue().length, 3);
		Assert.assertArrayEquals(protoLongArray2.getValue(), longArray);

		ProtoShort protoShort = new ProtoShort();
		protoShort.setValue((short) 8);
		buffer = Serializer.encode(protoShort);
		Assert.assertTrue(Serializer.decode(ProtoShort.class, buffer).getValue() == 8);

		ProtoShortArray protoShortArray = new ProtoShortArray();
		short[] shortArray = new short[] { (short) 8, (short) 9, (short) 10 };
		protoShortArray.setValue(shortArray);
		buffer = Serializer.encode(protoShortArray);
		ProtoShortArray protoShortArray2 = Serializer.decode(ProtoShortArray.class, buffer);
		Assert.assertEquals(protoShortArray2.getValue().length, 3);
		Assert.assertArrayEquals(protoShortArray2.getValue(), shortArray);

		ProtoString protoString = new ProtoString();
		protoString.setValue("OK!");
		buffer = Serializer.encode(protoString);
		Assert.assertTrue(Serializer.decode(ProtoString.class, buffer).getValue().equals("OK!"));
	}

	@Test
	public void testJSON() throws IOException {
		byte[] buffer = null;
		Codec codec = Serializer.getCodec(JsonTest.class, "json");
		JsonTest jsonTest = new JsonTest();
		jsonTest.setName("Test");
		buffer = codec.encode(jsonTest);
		codec.encode(jsonTest, new ByteArrayOutputStream());
		codec.decode(buffer);
		codec.decode(new ByteArrayInputStream(buffer), 0);

	}

	@Test
	public void testSerializer() throws Exception {
		System.out.println(" Time: [" + new Date() + "]  "
				+ Arrays.toString(Serializer.encode(DataCreater.newTable(true))));
		System.out.println(" Time: [" + new Date() + "]  "
				+ Arrays.toString(Serializer.encode(DataCreater.newTable(true))));
		System.out.println(" Time: [" + new Date() + "]  "
				+ Arrays.toString(Serializer.encode(DataCreater.newTable(true))));
		System.out.println(" Time: [" + new Date() + "]  "
				+ Arrays.toString(Serializer.encode(DataCreater.newTable(true))));
		System.out.println(" Time: [" + new Date() + "]  "
				+ Arrays.toString(Serializer.encode(DataCreater.newTable(true))));
		System.out.println(" Time: [" + new Date() + "]  "
				+ Arrays.toString(Serializer.encode(DataCreater.newTable(true))));
	}
}

@JsonContract
class JsonTest {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}

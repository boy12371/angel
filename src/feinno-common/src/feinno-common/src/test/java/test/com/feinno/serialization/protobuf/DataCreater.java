package test.com.feinno.serialization.protobuf;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import test.com.feinno.serialization.protobuf.bean.DefineTypeBean;
import test.com.feinno.serialization.protobuf.bean.EnumTypeBean;
import test.com.feinno.serialization.protobuf.bean.FullElementsBean;
import test.com.feinno.serialization.protobuf.bean.ListBean;
import test.com.feinno.serialization.protobuf.bean.MapBean;
import test.com.feinno.serialization.protobuf.bean.Person;
import test.com.feinno.serialization.protobuf.bean.PersonEnum;
import test.com.feinno.serialization.protobuf.bean.ProtoComboIntegerString;
import test.com.feinno.serialization.protobuf.bean.RequiredProtoEntity;
import test.com.feinno.serialization.protobuf.bean.SimpleMapBean;
import test.com.feinno.serialization.protobuf.bean.SimpleOldMapBean;
import test.com.feinno.serialization.protobuf.bean.SinBeanTest;
import test.com.feinno.serialization.protobuf.bean.SpecialFieldBean;
import test.com.feinno.serialization.protobuf.bean.Table;
import test.com.feinno.serialization.protobuf.bean.Unsupported;
import test.com.feinno.serialization.protobuf.bean.User;
import test.com.feinno.serialization.protobuf.enumType.ByteEnumType;
import test.com.feinno.serialization.protobuf.enumType.CharacterEnumType;
import test.com.feinno.serialization.protobuf.enumType.IntegerEnumType;
import test.com.feinno.serialization.protobuf.enumType.LongEnumType;
import test.com.feinno.serialization.protobuf.enumType.ShortEnumType;
import test.com.feinno.serialization.protobuf.enumType.StringEnumType;

import com.feinno.serialization.protobuf.types.ProtoComboDoubleString;
import com.feinno.serialization.protobuf.util.ArrayUtil;
import com.feinno.util.Flags;
import com.feinno.util.Guid;

public class DataCreater {

	public static Person newPerson(boolean isFill) {
		if (!isFill) {
			return new Person();
		}
		Person person1 = new Person();
		// part 1
		person1.setInt_Test(10);
		person1.setLong_Test(100L);
		person1.setFloat_Test(1000.1f);
		person1.setDouble_Test(10000.1d);
		person1.setBoolean_Test(true);
		short s1 = 126;
		byte b1 = 8;
		person1.setShort_Test(s1);
		person1.setByte_Test(b1);
		person1.setChar_Test('a');
		// part 2
		person1.setInteger_obj(20);
		person1.setLong_obj(200L);
		person1.setFloat_obj(2000.1f);
		person1.setDouble_obj(20000.1d);
		person1.setBoolean_obj(true);
		short s2 = 256;
		byte b2 = 16;
		person1.setShort_obj(new Short(s2));
		person1.setByte_obj(new Byte(b2));
		person1.setChar_obj(new Character('b'));
		person1.setEnum_obj(PersonEnum.BEIJING);

		// part 3
		Person person2 = new Person();
		person2.setBoolean_Test(true);
		person1.setPersonTest(person2);
		person1.setStringTest("胃,你好吗？");
		person1.setTable(newTable(false));

		// part 4
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(11);
		list1.add(12);
		list1.add(13);
		person1.setInteger_List(list1);

		List<Long> list2 = new ArrayList<Long>();
		for (int i = 0; i < 1; i++) {
			list2.add(21L);
		}
		list2.add(22L);
		list2.add(23L);
		person1.setLong_List(list2);

		List<Float> list3 = new ArrayList<Float>();
		list3.add(Float.valueOf(31f));
		list3.add(32f);
		list3.add(33f);
		person1.setFloat_List(list3);

		List<Double> list4 = new ArrayList<Double>();
		list4.add(41.1d);
		list4.add(41.2d);
		list4.add(41.3d);
		person1.setDouble_List(list4);

		List<Boolean> list5 = new ArrayList<Boolean>();
		list5.add(true);
		list5.add(false);
		list5.add(true);
		person1.setBoolean_List(list5);

		List<Short> list6 = new ArrayList<Short>();
		short s61 = 61;
		short s62 = 62;
		short s63 = 63;
		list6.add(s61);
		list6.add(s62);
		list6.add(s63);
		person1.setShort_List(list6);

		List<Byte> list7 = new ArrayList<Byte>();
		byte b71 = 1;
		byte b72 = 2;
		byte b73 = 3;
		list7.add(b71);
		list7.add(b72);
		list7.add(b73);
		person1.setByte_List(list7);

		List<Character> list8 = new ArrayList<Character>();
		list8.add('a');
		list8.add('b');
		list8.add('c');
		person1.setCharacter_List(list8);

		List<PersonEnum> list9 = new ArrayList<PersonEnum>();
		list9.add(PersonEnum.BEIJING);
		list9.add(PersonEnum.SHANGHAI);
		list9.add(PersonEnum.SUZHOU);
		person1.setPersonEnum_List(list9);

		List<String> list11 = new ArrayList<String>();
		list11.add("aaaaaaaaaa");
		list11.add("bbbbbbbbbb");
		list11.add("cccccccccc");
		person1.setString_List(list11);

		List<Person> list12 = new ArrayList<Person>();
		Person personTemp = new Person();
		personTemp.setStringTest("Lv.Mingwei");
		list12.add(personTemp);
		list12.add(new Person());
		list12.add(new Person());
		person1.setPerson_List(list12);

		List<Table> list13 = new ArrayList<Table>();
		list13.add(newTable(false));
		list13.add(newTable(false));
		list13.add(newTable(false));
		person1.setTable_List(list13);

		// part 5
		// 原始类型数组
		person1.setInteger_Array(new int[] { 14, 15, 16 });

		person1.setLong_Array(new long[] { 24L, 25L, 26L });

		person1.setFloat_Array(new float[] { 34f, 35f, 36f });

		person1.setDouble_Array(new double[] { 44d, 45d, 45d });

		person1.setBoolean_Array(new boolean[] { false, true, false });

		person1.setShort_Array(new short[] { 64, 65, 66 });

		person1.setByte_Array(new byte[] { 74, 75, 76 });

		person1.setChar_Array(new char[] { 'd', 'e', 'f' });

		// 包装类型数组
		person1.setInteger_Object_Array(new Integer[] { 11, 12, 13 });

		person1.setLong_Object_Array(new Long[] { 21L, 22L, 23L });

		person1.setFloat_Object_Array(new Float[] { 31f, 32f, 33f });

		person1.setDouble_Object_Array(new Double[] { 41d, 42d, 43d });

		person1.setBoolean_Object_Array(new Boolean[] { true, false, true });

		person1.setShort_Object_Array(new Short[] { 61, 62, 63 });

		person1.setByte_Object_Array(new Byte[] { 71, 72, 73 });

		person1.setCharacter_Object_Array(new Character[] { 'a', 'b', 'c' });

		person1.setString_Object_Array(new String[] { "aaa", "bbb", "ccc" });
		Person personArrayTemp = new Person();
		personArrayTemp.setStringTest("我是作为数组测试出现的，我位于数组的第二位,请验证我的准确性");

		List<ProtoComboDoubleString> protoComboDoubleString_List = new ArrayList<ProtoComboDoubleString>();
		ProtoComboDoubleString p1 = new ProtoComboDoubleString();
		p1.setStr1("1");
		p1.setStr2("a");
		ProtoComboDoubleString p2 = new ProtoComboDoubleString();
		p2.setStr1("2");
		p2.setStr2("b");
		protoComboDoubleString_List.add(p1);
		protoComboDoubleString_List.add(p2);
		person1.setProtoComboDoubleString_List(protoComboDoubleString_List);
		return person1;
	}

	public static DefineTypeBean newDefineTypeBean(boolean isFill) {
		if (!isFill) {
			return new DefineTypeBean();
		}
		DefineTypeBean bean = new DefineTypeBean();
		// part 1
		bean.setInt_Test(10);
		bean.setInt_Test_1(101);
		bean.setInt_Test_2(102);
		bean.setInt_Test_3(103);
		bean.setInt_Test_4(104);
		bean.setInt_Test_5(105);
		bean.setLong_Test(100L);
		bean.setLong_Test_1(111L);
		bean.setLong_Test_2(112L);
		bean.setLong_Test_3(113L);
		bean.setLong_Test_4(114L);
		bean.setLong_Test_5(115L);
		bean.setFloat_Test(1000.1f);
		bean.setDouble_Test(10000.1d);
		bean.setBoolean_Test(true);
		short s1 = 126;
		byte b1 = 8;
		bean.setShort_Test(s1);
		bean.setByte_Test(b1);
		bean.setChar_Test('a');

		// part 2
		bean.setInteger_obj(20);
		bean.setInteger_obj_1(201);
		bean.setInteger_obj_2(202);
		bean.setInteger_obj_3(203);
		bean.setInteger_obj_4(204);
		bean.setInteger_obj_5(205);
		bean.setLong_obj(200L);
		bean.setLong_obj_1(211L);
		bean.setLong_obj_2(212L);
		bean.setLong_obj_3(213L);
		bean.setLong_obj_4(214L);
		bean.setLong_obj_5(215L);
		bean.setFloat_obj(2000.1f);
		bean.setDouble_obj(20000.1d);
		bean.setBoolean_obj(true);
		short s2 = 256;
		byte b2 = 16;
		bean.setShort_obj(new Short(s2));
		bean.setByte_obj(new Byte(b2));
		bean.setChar_obj(new Character('b'));

		// part3
		bean.setArray_int(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		bean.setArray_long(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
		bean.setArray_int_obj(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		bean.setArray_long_obj(new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L });
		return bean;
	}

	public static SimpleOldMapBean newSimpleOldMapBean(boolean isFill) {
		if (!isFill) {
			return new SimpleOldMapBean();
		}
		SimpleOldMapBean test = new SimpleOldMapBean();
		return test;
	}

	public static FullElementsBean newFullElementsBean(boolean isFill) {
		if (!isFill) {
			return new FullElementsBean();
		}
		FullElementsBean fullElementsBean = new FullElementsBean();
		// part 1
		fullElementsBean.setInt_Test(10);
		fullElementsBean.setLong_Test(100L);
		fullElementsBean.setFloat_Test(1000.1f);
		fullElementsBean.setDouble_Test(10000.1d);
		fullElementsBean.setBoolean_Test(true);
		short s1 = 126;
		byte b1 = 8;
		fullElementsBean.setShort_Test(s1);
		fullElementsBean.setByte_Test(b1);
		fullElementsBean.setChar_Test('a');
		// part 2
		fullElementsBean.setInteger_obj(20);
		fullElementsBean.setLong_obj(200L);
		fullElementsBean.setFloat_obj(2000.1f);
		fullElementsBean.setDouble_obj(20000.1d);
		fullElementsBean.setBoolean_obj(true);
		short s2 = 256;
		byte b2 = 16;
		fullElementsBean.setShort_obj(new Short(s2));
		fullElementsBean.setByte_obj(new Byte(b2));
		fullElementsBean.setChar_obj(new Character('b'));

		// part 3
		fullElementsBean.setStringTest("胃,\n你好吗？\n");

		FullElementsBean fullElementsBeanTemp = newFullElementsBeanTest(false);
		fullElementsBeanTemp.setStringTest("你好，我是来做内嵌测试的，看到我，说明内嵌成功！恭喜你！");

		fullElementsBean.setFullElementsBean(fullElementsBeanTemp);
		fullElementsBean.setTable(newTable(true));
		fullElementsBean.setGuid(Guid.randomGuid());
		fullElementsBean.setDate(new Date());
		fullElementsBean.setDateUTC(new Date());

		// part 4

		// 原始类型数组
		fullElementsBean.setInteger_Array(new int[] { 14, 15, 16 });

		fullElementsBean.setLong_Array(new long[] { 24L, 25L, 26L });

		fullElementsBean.setFloat_Array(new float[] { 34f, 35f, 36f });

		fullElementsBean.setDouble_Array(new double[] { 44d, 45d, 45d });

		fullElementsBean.setBoolean_Array(new boolean[] { false, true, false });

		fullElementsBean.setShort_Array(new short[] { 64, 65, 66 });

		fullElementsBean.setByte_Array(new byte[] { 74, 75, 76 });

		fullElementsBean.setChar_Array(new char[] { 'd', 'e', 'f' });

		// 包装类型数组
		fullElementsBean.setInteger_Object_Array(new Integer[] { 11, 12, 13 });

		fullElementsBean.setLong_Object_Array(new Long[] { 21L, 22L, 23L });

		fullElementsBean.setFloat_Object_Array(new Float[] { 31f, 32f, 33f });

		fullElementsBean.setDouble_Object_Array(new Double[] { 41d, 42d, 43d });

		fullElementsBean.setBoolean_Object_Array(new Boolean[] { true, false, true });

		fullElementsBean.setShort_Object_Array(new Short[] { 61, 62, 63 });

		fullElementsBean.setByte_Object_Array(new Byte[] { 71, 72, 73 });

		fullElementsBean.setCharacter_Object_Array(new Character[] { 'a', 'b', 'c' });

		fullElementsBean.setString_Object_Array(new String[] { "aaa", "bbb", "ccc" });

		fullElementsBean.setTable_Object_Array(new Table[] { newTable(true), newTable(false), newTable(true) });

		// part 5
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(11);
		list1.add(12);
		list1.add(13);
		fullElementsBean.setInteger_List(list1);

		List<Long> list2 = new ArrayList<Long>();
		for (int i = 0; i < 1; i++) {
			list2.add(21L);
		}
		list2.add(22L);
		list2.add(23L);
		fullElementsBean.setLong_List(list2);

		List<Float> list3 = new ArrayList<Float>();
		list3.add(Float.valueOf(31f));
		list3.add(32f);
		list3.add(33f);
		fullElementsBean.setFloat_List(list3);

		List<Double> list4 = new ArrayList<Double>();
		list4.add(41.1d);
		list4.add(41.2d);
		list4.add(41.3d);
		fullElementsBean.setDouble_List(list4);

		List<Boolean> list5 = new ArrayList<Boolean>();
		list5.add(true);
		list5.add(false);
		list5.add(true);
		fullElementsBean.setBoolean_List(list5);

		List<Short> list6 = new ArrayList<Short>();
		short s61 = 61;
		short s62 = 62;
		short s63 = 63;
		list6.add(s61);
		list6.add(s62);
		list6.add(s63);
		fullElementsBean.setShort_List(list6);

		List<Byte> list7 = new ArrayList<Byte>();
		byte b71 = 1;
		byte b72 = 2;
		byte b73 = 3;
		list7.add(b71);
		list7.add(b72);
		list7.add(b73);
		fullElementsBean.setByte_List(list7);

		List<Character> list8 = new ArrayList<Character>();
		list8.add('a');
		list8.add('b');
		list8.add('c');
		fullElementsBean.setCharacter_List(list8);

		List<String> list11 = new ArrayList<String>();
		list11.add("aaaaaaaaaa");
		list11.add("bbbbbbbbbb");
		list11.add("cccccccccc");
		fullElementsBean.setString_List(list11);

		List<FullElementsBean> list12 = new ArrayList<FullElementsBean>();
		list12.add(new FullElementsBean());
		list12.add(fullElementsBeanTemp);
		list12.add(new FullElementsBean());
		fullElementsBean.setFullElements_List(list12);

		List<Table> list13 = new ArrayList<Table>();
		list13.add(newTable(false));
		list13.add(newTable(false));
		list13.add(newTable(false));
		fullElementsBean.setTable_List(list13);

		List<ProtoComboDoubleString> list14 = new ArrayList<ProtoComboDoubleString>();
		list14.add(new ProtoComboDoubleString("aa", "aa"));
		list14.add(new ProtoComboDoubleString("bb", "bb"));
		list14.add(new ProtoComboDoubleString("cc", "cc"));
		fullElementsBean.setProtoComboDoubleString_List(list14);

		// PART6 MAP类型
		Map<String, String> map_S_S_Obj = new HashMap<String, String>();
		for (int i = 0; i < 1000; i++) {
			map_S_S_Obj.put(i + "key", i + "value");
		}
		fullElementsBean.setMap_S_S(map_S_S_Obj);

		Map<Integer, String> map_I_S_Obj = new HashMap<Integer, String>();
		for (int i = 0; i < 1000; i++) {
			map_I_S_Obj.put(i, i + "aaaa");
		}
		fullElementsBean.setMap_I_S(map_I_S_Obj);

		Map<String, Long> map_S_L_Obj = new HashMap<String, Long>();
		for (int i = 0; i < 1000; i++) {
			map_S_L_Obj.put(i + "key", Long.valueOf(i + "0000000"));
		}
		fullElementsBean.setMap_S_L(map_S_L_Obj);

		Random random = new Random();
		Map<Boolean, Character> map_B_C_Obj = new HashMap<Boolean, Character>();
		for (int i = 0; i < 1000; i++) {
			map_B_C_Obj.put(random.nextBoolean(), (char) (i + 97));
		}
		fullElementsBean.setMap_B_C(map_B_C_Obj);

		Map<String, FullElementsBean> map_S_Full_Obj = new HashMap<String, FullElementsBean>();
		for (int i = 0; i < 10; i++) {
			map_S_Full_Obj.put(i + "aaa", newFullElementsBeanTest(false));
		}
		fullElementsBean.setMap_S_Full(map_S_Full_Obj);

		fullElementsBean.setSqlDate(new java.sql.Date(System.currentTimeMillis()));

		return fullElementsBean;
	}
	
	public static FullElementsBean fillToFullElementsBean(FullElementsBean fullElementsBean) {
		// part 1
		fullElementsBean.setInt_Test(10);
		fullElementsBean.setLong_Test(100L);
		fullElementsBean.setFloat_Test(1000.1f);
		fullElementsBean.setDouble_Test(10000.1d);
		fullElementsBean.setBoolean_Test(true);
		short s1 = 126;
		byte b1 = 8;
		fullElementsBean.setShort_Test(s1);
		fullElementsBean.setByte_Test(b1);
		fullElementsBean.setChar_Test('a');
		// part 2
		fullElementsBean.setInteger_obj(20);
		fullElementsBean.setLong_obj(200L);
		fullElementsBean.setFloat_obj(2000.1f);
		fullElementsBean.setDouble_obj(20000.1d);
		fullElementsBean.setBoolean_obj(true);
		short s2 = 256;
		byte b2 = 16;
		fullElementsBean.setShort_obj(new Short(s2));
		fullElementsBean.setByte_obj(new Byte(b2));
		fullElementsBean.setChar_obj(new Character('b'));

		// part 3
		fullElementsBean.setStringTest("胃,\n你好吗？\n");

		FullElementsBean fullElementsBeanTemp = newFullElementsBeanTest(false);
		fullElementsBeanTemp.setStringTest("你好，我是来做内嵌测试的，看到我，说明内嵌成功！恭喜你！");

		fullElementsBean.setFullElementsBean(fullElementsBeanTemp);
		fullElementsBean.setTable(newTable(true));
		fullElementsBean.setGuid(Guid.randomGuid());
		fullElementsBean.setDate(new Date());
		fullElementsBean.setDateUTC(new Date());

		// part 4

		// 原始类型数组
		fullElementsBean.setInteger_Array(new int[] { 14, 15, 16 });

		fullElementsBean.setLong_Array(new long[] { 24L, 25L, 26L });

		fullElementsBean.setFloat_Array(new float[] { 34f, 35f, 36f });

		fullElementsBean.setDouble_Array(new double[] { 44d, 45d, 45d });

		fullElementsBean.setBoolean_Array(new boolean[] { false, true, false });

		fullElementsBean.setShort_Array(new short[] { 64, 65, 66 });

		fullElementsBean.setByte_Array(new byte[] { 74, 75, 76 });

		fullElementsBean.setChar_Array(new char[] { 'd', 'e', 'f' });

		// 包装类型数组
		fullElementsBean.setInteger_Object_Array(new Integer[] { 11, 12, 13 });

		fullElementsBean.setLong_Object_Array(new Long[] { 21L, 22L, 23L });

		fullElementsBean.setFloat_Object_Array(new Float[] { 31f, 32f, 33f });

		fullElementsBean.setDouble_Object_Array(new Double[] { 41d, 42d, 43d });

		fullElementsBean.setBoolean_Object_Array(new Boolean[] { true, false, true });

		fullElementsBean.setShort_Object_Array(new Short[] { 61, 62, 63 });

		fullElementsBean.setByte_Object_Array(new Byte[] { 71, 72, 73 });

		fullElementsBean.setCharacter_Object_Array(new Character[] { 'a', 'b', 'c' });

		fullElementsBean.setString_Object_Array(new String[] { "aaa", "bbb", "ccc" });

		fullElementsBean.setTable_Object_Array(new Table[] { newTable(true), newTable(false), newTable(true) });

		// part 5
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(11);
		list1.add(12);
		list1.add(13);
		fullElementsBean.setInteger_List(list1);

		List<Long> list2 = new ArrayList<Long>();
		for (int i = 0; i < 1; i++) {
			list2.add(21L);
		}
		list2.add(22L);
		list2.add(23L);
		fullElementsBean.setLong_List(list2);

		List<Float> list3 = new ArrayList<Float>();
		list3.add(Float.valueOf(31f));
		list3.add(32f);
		list3.add(33f);
		fullElementsBean.setFloat_List(list3);

		List<Double> list4 = new ArrayList<Double>();
		list4.add(41.1d);
		list4.add(41.2d);
		list4.add(41.3d);
		fullElementsBean.setDouble_List(list4);

		List<Boolean> list5 = new ArrayList<Boolean>();
		list5.add(true);
		list5.add(false);
		list5.add(true);
		fullElementsBean.setBoolean_List(list5);

		List<Short> list6 = new ArrayList<Short>();
		short s61 = 61;
		short s62 = 62;
		short s63 = 63;
		list6.add(s61);
		list6.add(s62);
		list6.add(s63);
		fullElementsBean.setShort_List(list6);

		List<Byte> list7 = new ArrayList<Byte>();
		byte b71 = 1;
		byte b72 = 2;
		byte b73 = 3;
		list7.add(b71);
		list7.add(b72);
		list7.add(b73);
		fullElementsBean.setByte_List(list7);

		List<Character> list8 = new ArrayList<Character>();
		list8.add('a');
		list8.add('b');
		list8.add('c');
		fullElementsBean.setCharacter_List(list8);

		List<String> list11 = new ArrayList<String>();
		list11.add("aaaaaaaaaa");
		list11.add("bbbbbbbbbb");
		list11.add("cccccccccc");
		fullElementsBean.setString_List(list11);

		List<FullElementsBean> list12 = new ArrayList<FullElementsBean>();
		list12.add(new FullElementsBean());
		list12.add(fullElementsBeanTemp);
		list12.add(new FullElementsBean());
		fullElementsBean.setFullElements_List(list12);

		List<Table> list13 = new ArrayList<Table>();
		list13.add(newTable(false));
		list13.add(newTable(false));
		list13.add(newTable(false));
		fullElementsBean.setTable_List(list13);

		List<ProtoComboDoubleString> list14 = new ArrayList<ProtoComboDoubleString>();
		list14.add(new ProtoComboDoubleString("aa", "aa"));
		list14.add(new ProtoComboDoubleString("bb", "bb"));
		list14.add(new ProtoComboDoubleString("cc", "cc"));
		fullElementsBean.setProtoComboDoubleString_List(list14);

		// PART6 MAP类型
		Map<String, String> map_S_S_Obj = new HashMap<String, String>();
		for (int i = 0; i < 1000; i++) {
			map_S_S_Obj.put(i + "key", i + "value");
		}
		fullElementsBean.setMap_S_S(map_S_S_Obj);

		Map<Integer, String> map_I_S_Obj = new HashMap<Integer, String>();
		for (int i = 0; i < 1000; i++) {
			map_I_S_Obj.put(i, i + "aaaa");
		}
		fullElementsBean.setMap_I_S(map_I_S_Obj);

		Map<String, Long> map_S_L_Obj = new HashMap<String, Long>();
		for (int i = 0; i < 1000; i++) {
			map_S_L_Obj.put(i + "key", Long.valueOf(i + "0000000"));
		}
		fullElementsBean.setMap_S_L(map_S_L_Obj);

		Random random = new Random();
		Map<Boolean, Character> map_B_C_Obj = new HashMap<Boolean, Character>();
		for (int i = 0; i < 1000; i++) {
			map_B_C_Obj.put(random.nextBoolean(), (char) (i + 97));
		}
		fullElementsBean.setMap_B_C(map_B_C_Obj);

		Map<String, FullElementsBean> map_S_Full_Obj = new HashMap<String, FullElementsBean>();
		for (int i = 0; i < 10; i++) {
			map_S_Full_Obj.put(i + "aaa", newFullElementsBeanTest(false));
		}
		fullElementsBean.setMap_S_Full(map_S_Full_Obj);

		fullElementsBean.setSqlDate(new java.sql.Date(System.currentTimeMillis()));

		return fullElementsBean;
	}

	private static FullElementsBean newFullElementsBeanTest(boolean isFill) {
		if (!isFill) {
			return new FullElementsBean();
		}
		FullElementsBean fullElementsBean = new FullElementsBean();
		// part 1
		fullElementsBean.setInt_Test(10);
		fullElementsBean.setLong_Test(100L);
		fullElementsBean.setFloat_Test(1000.1f);
		fullElementsBean.setDouble_Test(10000.1d);
		fullElementsBean.setBoolean_Test(true);
		short s1 = 126;
		byte b1 = 8;
		fullElementsBean.setShort_Test(s1);
		fullElementsBean.setByte_Test(b1);
		fullElementsBean.setChar_Test('a');
		// part 2
		fullElementsBean.setInteger_obj(20);
		fullElementsBean.setLong_obj(200L);
		fullElementsBean.setFloat_obj(2000.1f);
		fullElementsBean.setDouble_obj(20000.1d);
		fullElementsBean.setBoolean_obj(true);
		short s2 = 256;
		byte b2 = 16;
		fullElementsBean.setShort_obj(new Short(s2));
		fullElementsBean.setByte_obj(new Byte(b2));
		fullElementsBean.setChar_obj(new Character('b'));

		// part 3
		fullElementsBean.setStringTest("胃,你好吗？");

		FullElementsBean fullElementsBeanTemp = newFullElementsBean(false);
		fullElementsBeanTemp.setStringTest("你好，我是来做内嵌测试的，看到我，说明内嵌成功！恭喜你！");

		fullElementsBean.setFullElementsBean(fullElementsBeanTemp);
		fullElementsBean.setTable(newTable(false));
		fullElementsBean.setGuid(Guid.randomGuid());
		fullElementsBean.setDate(new Date());
		fullElementsBean.setDateUTC(new Date());

		// part 4
		// 原始类型数组
		fullElementsBean.setInteger_Array(new int[] { 14, 15, 16 });

		fullElementsBean.setLong_Array(new long[] { 24L, 25L, 26L });

		fullElementsBean.setFloat_Array(new float[] { 34f, 35f, 36f });

		fullElementsBean.setDouble_Array(new double[] { 44d, 45d, 45d });

		fullElementsBean.setBoolean_Array(new boolean[] { false, true, false });

		fullElementsBean.setShort_Array(new short[] { 64, 65, 66 });

		fullElementsBean.setByte_Array(new byte[] { 74, 75, 76 });

		fullElementsBean.setChar_Array(new char[] { 'd', 'e', 'f' });

		// 包装类型数组
		fullElementsBean.setInteger_Object_Array(new Integer[] { 11, 12, 13 });

		fullElementsBean.setLong_Object_Array(new Long[] { 21L, 22L, 23L });

		fullElementsBean.setFloat_Object_Array(new Float[] { 31f, 32f, 33f });

		fullElementsBean.setDouble_Object_Array(new Double[] { 41d, 42d, 43d });

		fullElementsBean.setBoolean_Object_Array(new Boolean[] { true, false, true });

		fullElementsBean.setShort_Object_Array(new Short[] { 61, 62, 63 });

		fullElementsBean.setByte_Object_Array(new Byte[] { 71, 72, 73 });

		fullElementsBean.setCharacter_Object_Array(new Character[] { 'a', 'b', 'c' });

		fullElementsBean.setString_Object_Array(new String[] { "aaa", "bbb", "ccc" });

		fullElementsBean.setTable_Object_Array(new Table[] { newTable(true), newTable(false), newTable(true) });

		// part 5
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(11);
		list1.add(12);
		list1.add(13);
		fullElementsBean.setInteger_List(list1);

		List<Long> list2 = new ArrayList<Long>();
		for (int i = 0; i < 1; i++) {
			list2.add(21L);
		}
		list2.add(22L);
		list2.add(23L);
		fullElementsBean.setLong_List(list2);

		List<Float> list3 = new ArrayList<Float>();
		list3.add(Float.valueOf(31f));
		list3.add(32f);
		list3.add(33f);
		fullElementsBean.setFloat_List(list3);

		List<Double> list4 = new ArrayList<Double>();
		list4.add(41.1d);
		list4.add(41.2d);
		list4.add(41.3d);
		fullElementsBean.setDouble_List(list4);

		List<Boolean> list5 = new ArrayList<Boolean>();
		list5.add(true);
		list5.add(false);
		list5.add(true);
		fullElementsBean.setBoolean_List(list5);

		List<Short> list6 = new ArrayList<Short>();
		short s61 = 61;
		short s62 = 62;
		short s63 = 63;
		list6.add(s61);
		list6.add(s62);
		list6.add(s63);
		fullElementsBean.setShort_List(list6);

		List<Byte> list7 = new ArrayList<Byte>();
		byte b71 = 1;
		byte b72 = 2;
		byte b73 = 3;
		list7.add(b71);
		list7.add(b72);
		list7.add(b73);
		fullElementsBean.setByte_List(list7);

		List<Character> list8 = new ArrayList<Character>();
		list8.add('a');
		list8.add('b');
		list8.add('c');
		fullElementsBean.setCharacter_List(list8);

		List<String> list11 = new ArrayList<String>();
		list11.add("aaaaaaaaaa");
		list11.add("bbbbbbbbbb");
		list11.add("cccccccccc");
		fullElementsBean.setString_List(list11);

		List<FullElementsBean> list12 = new ArrayList<FullElementsBean>();
		list12.add(new FullElementsBean());
		list12.add(fullElementsBeanTemp);
		list12.add(new FullElementsBean());
		fullElementsBean.setFullElements_List(list12);

		List<Table> list13 = new ArrayList<Table>();
		list13.add(newTable(false));
		list13.add(newTable(false));
		list13.add(newTable(false));
		fullElementsBean.setTable_List(list13);

		List<ProtoComboDoubleString> list14 = new ArrayList<ProtoComboDoubleString>();
		list14.add(new ProtoComboDoubleString("aa", "aa"));
		list14.add(new ProtoComboDoubleString("bb", "bb"));
		list14.add(new ProtoComboDoubleString("cc", "cc"));
		fullElementsBean.setProtoComboDoubleString_List(list14);

		// PART6 MAP类型
		Map<String, String> map_S_S_Obj = new HashMap<String, String>();
		for (int i = 0; i < 1000; i++) {
			map_S_S_Obj.put(i + "key", i + "value");
		}
		fullElementsBean.setMap_S_S(map_S_S_Obj);

		Map<Integer, String> map_I_S_Obj = new HashMap<Integer, String>();
		for (int i = 0; i < 1000; i++) {
			map_I_S_Obj.put(i, i + "aaaa");
		}
		fullElementsBean.setMap_I_S(map_I_S_Obj);

		Map<String, Long> map_S_L_Obj = new HashMap<String, Long>();
		for (int i = 0; i < 1000; i++) {
			map_S_L_Obj.put(i + "key", Long.valueOf(i + "0000000"));
		}
		fullElementsBean.setMap_S_L(map_S_L_Obj);

		Random random = new Random();
		Map<Boolean, Character> map_B_C_Obj = new HashMap<Boolean, Character>();
		for (int i = 0; i < 1000; i++) {
			map_B_C_Obj.put(random.nextBoolean(), (char) (i + 97));
		}
		fullElementsBean.setMap_B_C(map_B_C_Obj);
		fullElementsBean.setSqlDate(new java.sql.Date(System.currentTimeMillis()));
		return fullElementsBean;
	}

	public static Table newTable(boolean isFill) {
		if (!isFill) {
			return new Table();
		}
		Table table = new Table();
		table.setId(1);
		table.setName("我是Table");
		table.setEmail("Table@163.com");
		User user = new User();
		user.setId(2);
		user.setName("我是User");
		user.setEmail("User@163.com");
		table.setUser(user);
		return table;
	}

	public static Table fillToTable(Table table) {
		table.setId(1);
		table.setName("我是Table");
		table.setEmail("Table@163.com");
		User user = new User();
		user.setId(2);
		user.setName("我是User");
		user.setEmail("User@163.com");
		table.setUser(user);
		return table;
	}
	
	public static User fillToUser(User user) {
		user.setId(1);
		user.setName("我是Table");
		user.setEmail("Table@163.com");
		return user;
	}

	public static User newUser(boolean isFill) {
		if (!isFill) {
			return new User();
		}
		User user = new User();
		user.setId(1);
		user.setName("我是Table");
		user.setEmail("Table@163.com");
		return user;
	}

	public static SinBeanTest newSinBean(boolean isFill) {
		if (!isFill) {
			return new SinBeanTest();
		}
		SinBeanTest test = new SinBeanTest();
		List<Byte> byteList = new ArrayList<Byte>();
		for (int i = 0; i < 10; i++) {
			byteList.add((byte) i);
		}
		byte[] bytes = new byte[byteList.size()];
		ArrayUtil.listToArray(byteList, bytes);
		test.setBytes(bytes);
		test.setBytes_Obj(ArrayUtil.primitiveToWraps(bytes));
		test.setBytes_List(byteList);
		test.setB((byte) 1);

		test.setGuid(Guid.randomGuid());
		List<Guid> guid_List = new ArrayList<Guid>();
		guid_List.add(Guid.randomGuid());
		guid_List.add(Guid.randomGuid());
		guid_List.add(Guid.randomGuid());
		test.setGuid_List(guid_List);

		return test;
	}

	@SuppressWarnings({ "unchecked" })
	public static SpecialFieldBean newSpecialFieldBean(boolean isFill) {
		if (!isFill) {
			return new SpecialFieldBean();
		}
		SpecialFieldBean test = new SpecialFieldBean();
		// PART 1
		test.setGuid(Guid.randomGuid());

		test.setGuid_Array(new Guid[] { Guid.randomGuid(), Guid.randomGuid(), Guid.randomGuid() });

		List<Guid> guid_List = new ArrayList<Guid>();
		guid_List.add(Guid.randomGuid());
		guid_List.add(Guid.randomGuid());
		guid_List.add(Guid.randomGuid());
		test.setGuid_List(guid_List);

		Map<Guid, SpecialFieldBean> map = new HashMap<Guid, SpecialFieldBean>();
		map.put(Guid.randomGuid(), newSpecialFieldBean(false));
		SpecialFieldBean specialFieldBean = newSpecialFieldBean(false);
		specialFieldBean.setGuid(Guid.randomGuid());
		map.put(Guid.randomGuid(), specialFieldBean);
		map.put(Guid.randomGuid(), newSpecialFieldBean(false));
		test.setGuid_Map(map);

		// PART 3
		test.setDate(new Date());

		test.setDate_Array(new Date[] { new Date(), new Date(), new Date() });

		List<Date> date_List1 = new ArrayList<Date>();
		date_List1.add(new Date());
		date_List1.add(new Date());
		date_List1.add(new Date());
		test.setDate_List(date_List1);

		Map<Date, SpecialFieldBean> date_map = new HashMap<Date, SpecialFieldBean>();
		date_map.put(new Date(), newSpecialFieldBean(false));
		specialFieldBean.setGuid(Guid.randomGuid());
		date_map.put(new Date(), specialFieldBean);
		date_map.put(new Date(), newSpecialFieldBean(false));
		test.setDate_Map(date_map);

		// PART 4

		test.setFlags_1(new Flags(0x0C04));
		test.setFlags_Array_1(new Flags[] { new Flags(0x0C04), new Flags(0x0409), new Flags(0x0804) });

		test.setFlags_2(new Flags(0x0C04));
		test.setFlags_Array_2(new Flags[] { new Flags(0x0C04), new Flags(0x0409), new Flags(0x0804) });

		Map<Flags, SpecialFieldBean> flags_map_1 = new HashMap<Flags, SpecialFieldBean>();
		flags_map_1.put(new Flags(0x0C04), newSpecialFieldBean(false));
		specialFieldBean.setGuid(Guid.randomGuid());
		flags_map_1.put(new Flags(0x0409), specialFieldBean);
		flags_map_1.put(new Flags(0x0804), newSpecialFieldBean(false));
		test.setFlags_Map_1(flags_map_1);

		Map<Flags<?>, SpecialFieldBean> flags_map_2 = new HashMap<Flags<?>, SpecialFieldBean>();
		flags_map_2.put(new Flags(0x0C04), newSpecialFieldBean(false));
		specialFieldBean.setGuid(Guid.randomGuid());
		flags_map_2.put(new Flags(0x0409), specialFieldBean);
		flags_map_2.put(new Flags(0x0804), newSpecialFieldBean(false));
		test.setFlags_Map_2(flags_map_2);

		return test;
	}

	public static SimpleMapBean newSimpleMapBean(boolean isFill) {
		if (!isFill) {
			return new SimpleMapBean();
		}
		SimpleMapBean test = new SimpleMapBean();
		// 1
		Map<Integer, String> map_I_S_Obj = new HashMap<Integer, String>();
		for (int i = 0; i < 10; i++) {
			map_I_S_Obj.put(i, i + "aaaa");
		}
		test.setMap_I_S_Obj(map_I_S_Obj);
		// 2
		Map<String, Integer> map_S_I_Obj = new HashMap<String, Integer>();
		for (int i = 0; i < 10; i++) {
			map_S_I_Obj.put(i + "bbbb", i);
		}
		test.setMap_S_I_Obj(map_S_I_Obj);
		// 3
		Map<String, Table> map_S_T_Obj = new HashMap<String, Table>();
		for (int i = 0; i < 10; i++) {
			map_S_T_Obj.put(i + "cccc", newTable(false));
		}
		test.setMap_S_T_Obj(map_S_T_Obj);
		// 4
		Map<SinBeanTest, Table> map_SB_T_Obj = new HashMap<SinBeanTest, Table>();
		for (int i = 0; i < 10; i++) {
			SinBeanTest test2 = newSinBean(false);
			map_SB_T_Obj.put(test2, newTable(false));
		}
		test.setMap_SB_T_Obj(map_SB_T_Obj);
		// 5
		Map<String, Person> map_S_P_Obj = new HashMap<String, Person>();
		for (int i = 0; i < 10; i++) {
			map_S_P_Obj.put(i + "dddd", newPerson(false));
		}
		test.setMap_S_P_Obj(map_S_P_Obj);
		// 7
		Map<Character, Long> map_C_L_Obj = new HashMap<Character, Long>();
		for (int i = 0; i < 10; i++) {
			map_C_L_Obj.put((char) (i + 97), Long.valueOf(i + "000"));
		}
		test.setMap_C_L_Obj(map_C_L_Obj);
		// 8
		Map<String, String> map_S_S_Obj = new HashMap<String, String>();
		for (int i = 0; i < 10; i++) {
			map_S_S_Obj.put(i + "key", i + "value");
		}
		test.setMap_S_S_Obj(map_S_S_Obj);
		// 9
		Map<String, Byte> map_S_B_Obj = new HashMap<String, Byte>();
		for (int i = 0; i < 10; i++) {
			map_S_B_Obj.put(i + "key", (byte) i);
		}
		test.setMap_S_B_Obj(map_S_B_Obj);
		return test;
	}

	public static EnumTypeBean newEnumTypeBean(boolean isFill) {
		if (!isFill) {
			return new EnumTypeBean();
		}

		EnumTypeBean enumTypeBean = new EnumTypeBean();
		enumTypeBean.setIntegerEnumType(IntegerEnumType.valueOf(IntegerEnumType.class, 2));
		enumTypeBean.setLongEnumType(LongEnumType.valueOf(LongEnumType.class, 2L));
		enumTypeBean.setByteEnumType(ByteEnumType.valueOf(ByteEnumType.class, (byte) 2));
		enumTypeBean.setCharacterEnumType(CharacterEnumType.valueOf(CharacterEnumType.class, 'b'));
		enumTypeBean.setShortEnumType(ShortEnumType.valueOf(ShortEnumType.class, (short) 2));
		enumTypeBean.setStringEnumType(StringEnumType.valueOf(StringEnumType.class, "Feinno2"));

		return enumTypeBean;
	}

	public static MapBean newMapBean(boolean isFill) {
		if (!isFill) {
			return new MapBean();
		}
		MapBean mapBean = new MapBean();
		Map<Integer, String> map_I_S_Obj = new HashMap<Integer, String>();
		for (int i = 0; i < 1000; i++) {
			map_I_S_Obj.put(i, i + "aaaa");
		}
		mapBean.setMap_I_S(map_I_S_Obj);

		MapBean mapBeanTemp = new MapBean();
		Map<Integer, String> map_I_S_Obj2 = new HashMap<Integer, String>();
		for (int i = 0; i < 2000; i++) {
			map_I_S_Obj2.put(i, i + "aaaa");
		}
		mapBeanTemp.setMap_I_S(map_I_S_Obj2);
		mapBean.setMapBean(mapBeanTemp);
		return mapBean;
	}

	public static ListBean newListBean(boolean isFill) {
		if (!isFill) {
			return new ListBean();
		}
		ListBean listBean = new ListBean();
		List<ProtoComboIntegerString> list_map = new ArrayList<ProtoComboIntegerString>();
		for (int i = 0; i < 1000; i++) {
			ProtoComboIntegerString p1 = new ProtoComboIntegerString();
			p1.setKey(i);
			p1.setValue(i + "aaaaaaa");
			list_map.add(p1);
		}
		listBean.setList_map(list_map);
		return listBean;
	}

	public static RequiredProtoEntity newRequiredProtoEntity(boolean isFill) {

		if (!isFill) {
			return new RequiredProtoEntity();
		}

		RequiredProtoEntity protoEntity = new RequiredProtoEntity();
		protoEntity.setI(10086);
		protoEntity.setLongObj(100860000L);
		protoEntity.setString("Hello world!");
		protoEntity.setUtilDate(new java.util.Date());
		protoEntity.setSqlDate(new java.sql.Date(System.currentTimeMillis()));

		List<java.util.Date> utilDateList = new ArrayList<java.util.Date>();
		utilDateList.add(new java.util.Date());
		utilDateList.add(new java.util.Date());
		utilDateList.add(new java.util.Date());
		protoEntity.setUtilDateList(utilDateList);

		List<java.sql.Date> sqlDateList = new ArrayList<java.sql.Date>();
		sqlDateList.add(new java.sql.Date(System.currentTimeMillis()));
		sqlDateList.add(new java.sql.Date(System.currentTimeMillis()));
		sqlDateList.add(new java.sql.Date(System.currentTimeMillis()));
		protoEntity.setSqlDateList(sqlDateList);

		Map<String, Table> map_S_T = new HashMap<String, Table>();
		map_S_T.put("A", DataCreater.newTable(true));
		map_S_T.put("B", DataCreater.newTable(true));
		map_S_T.put("C", DataCreater.newTable(true));
		protoEntity.setMap_S_T(map_S_T);

		return protoEntity;
	}

	public static Unsupported newUnsupported(boolean isFill) {
		if (!isFill) {
			return new Unsupported();
		}
		Set<String> set = new HashSet<String>();
		set.add("Feinno");
		Unsupported unsupported = new Unsupported();
		unsupported.setSet(set);
		return unsupported;

	}
}

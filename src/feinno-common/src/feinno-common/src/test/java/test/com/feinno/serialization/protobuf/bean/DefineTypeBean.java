package test.com.feinno.serialization.protobuf.bean;

import java.util.List;
import java.util.Map;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.serialization.protobuf.ProtoType;

public class DefineTypeBean extends ProtoEntity {
	// part 1 基本数据类型
	@ProtoMember(value = 101, type = ProtoType.INT32)
	private int int_Test_1;

	@ProtoMember(value = 102, type = ProtoType.UINT32)
	private int int_Test_2;

	@ProtoMember(value = 103, type = ProtoType.SINT32)
	private int int_Test_3;

	@ProtoMember(value = 104, type = ProtoType.FIXED32)
	private int int_Test_4;

	@ProtoMember(value = 105, type = ProtoType.SFIXED32)
	private int int_Test_5;

	@ProtoMember(value = 111, type = ProtoType.INT64)
	private long long_Test_1;

	@ProtoMember(value = 112, type = ProtoType.UINT64)
	private long long_Test_2;

	@ProtoMember(value = 113, type = ProtoType.SINT64)
	private long long_Test_3;

	@ProtoMember(value = 114, type = ProtoType.FIXED64)
	private long long_Test_4;

	@ProtoMember(value = 115, type = ProtoType.SFIXED64)
	private long long_Test_5;

	@ProtoMember(121)
	private int int_Test;

	@ProtoMember(122)
	private long long_Test;

	@ProtoMember(123)
	private float float_Test;

	@ProtoMember(124)
	private double double_Test;

	@ProtoMember(125)
	private boolean boolean_Test;

	@ProtoMember(126)
	private short short_Test;

	@ProtoMember(127)
	private byte byte_Test;

	@ProtoMember(128)
	private char char_Test;

	// part 2 包装数据类型

	@ProtoMember(value = 201, type = ProtoType.INT32)
	private Integer Integer_obj_1;

	@ProtoMember(value = 202, type = ProtoType.UINT32)
	private Integer Integer_obj_2;

	@ProtoMember(value = 203, type = ProtoType.SINT32)
	private Integer Integer_obj_3;

	@ProtoMember(value = 204, type = ProtoType.FIXED32)
	private Integer Integer_obj_4;

	@ProtoMember(value = 205, type = ProtoType.SFIXED32)
	private Integer Integer_obj_5;

	@ProtoMember(value = 211, type = ProtoType.INT64)
	private Long Long_obj_1;

	@ProtoMember(value = 212, type = ProtoType.UINT64)
	private Long Long_obj_2;

	@ProtoMember(value = 213, type = ProtoType.SINT64)
	private Long Long_obj_3;

	@ProtoMember(value = 214, type = ProtoType.FIXED64)
	private Long Long_obj_4;

	@ProtoMember(value = 215, type = ProtoType.SFIXED64)
	private Long Long_obj_5;

	@ProtoMember(221)
	private Integer Integer_obj;

	@ProtoMember(222)
	private Long Long_obj;

	@ProtoMember(223)
	private Float Float_obj;

	@ProtoMember(224)
	private Double Double_obj;

	@ProtoMember(225)
	private Boolean Boolean_obj;

	@ProtoMember(226)
	private Short short_obj;

	@ProtoMember(227)
	private Byte byte_obj;

	@ProtoMember(228)
	private Character char_obj;

	// part 3 集合类型
	@ProtoMember(value = 301, type = ProtoType.FIXED64)
	private int[] array_int;

	@ProtoMember(value = 302, type = ProtoType.FIXED64)
	private long[] array_long;

	@ProtoMember(value = 303, type = ProtoType.FIXED32)
	private Integer[] array_int_obj;

	@ProtoMember(value = 304, type = ProtoType.FIXED64)
	private Long[] array_long_obj;

	private Map<Integer, Long> map_int_long;

	// part 4 复杂类型
	@ProtoMember(value = 401, type = ProtoType.FIXED32)
	private DefineTypeBean[] array;

	@ProtoMember(value = 402, type = ProtoType.FIXED32)
	private List<DefineTypeBean> list;

	@ProtoMember(value = 403, type = ProtoType.FIXED32)
	private Map<String, DefineTypeBean> map;

	public final int getInt_Test_1() {
		return int_Test_1;
	}

	public final void setInt_Test_1(int int_Test_1) {
		this.int_Test_1 = int_Test_1;
	}

	public int getInt_Test_2() {
		return int_Test_2;
	}

	public void setInt_Test_2(int int_Test_2) {
		this.int_Test_2 = int_Test_2;
	}

	public final int getInt_Test_3() {
		return int_Test_3;
	}

	public final void setInt_Test_3(int int_Test_3) {
		this.int_Test_3 = int_Test_3;
	}

	public final int getInt_Test_4() {
		return int_Test_4;
	}

	public final void setInt_Test_4(int int_Test_4) {
		this.int_Test_4 = int_Test_4;
	}

	public final int getInt_Test_5() {
		return int_Test_5;
	}

	public final void setInt_Test_5(int int_Test_5) {
		this.int_Test_5 = int_Test_5;
	}

	public final long getLong_Test_1() {
		return long_Test_1;
	}

	public final void setLong_Test_1(long long_Test_1) {
		this.long_Test_1 = long_Test_1;
	}

	public final long getLong_Test_2() {
		return long_Test_2;
	}

	public final void setLong_Test_2(long long_Test_2) {
		this.long_Test_2 = long_Test_2;
	}

	public final long getLong_Test_3() {
		return long_Test_3;
	}

	public final void setLong_Test_3(long long_Test_3) {
		this.long_Test_3 = long_Test_3;
	}

	public final long getLong_Test_4() {
		return long_Test_4;
	}

	public final void setLong_Test_4(long long_Test_4) {
		this.long_Test_4 = long_Test_4;
	}

	public final long getLong_Test_5() {
		return long_Test_5;
	}

	public final void setLong_Test_5(long long_Test_5) {
		this.long_Test_5 = long_Test_5;
	}

	public final int getInt_Test() {
		return int_Test;
	}

	public final void setInt_Test(int int_Test) {
		this.int_Test = int_Test;
	}

	public final long getLong_Test() {
		return long_Test;
	}

	public final void setLong_Test(long long_Test) {
		this.long_Test = long_Test;
	}

	public final float getFloat_Test() {
		return float_Test;
	}

	public final void setFloat_Test(float float_Test) {
		this.float_Test = float_Test;
	}

	public final double getDouble_Test() {
		return double_Test;
	}

	public final void setDouble_Test(double double_Test) {
		this.double_Test = double_Test;
	}

	public final boolean getBoolean_Test() {
		return boolean_Test;
	}

	public final void setBoolean_Test(boolean boolean_Test) {
		this.boolean_Test = boolean_Test;
	}

	public final short getShort_Test() {
		return short_Test;
	}

	public final void setShort_Test(short short_Test) {
		this.short_Test = short_Test;
	}

	public final byte getByte_Test() {
		return byte_Test;
	}

	public final void setByte_Test(byte byte_Test) {
		this.byte_Test = byte_Test;
	}

	public final char getChar_Test() {
		return char_Test;
	}

	public final void setChar_Test(char char_Test) {
		this.char_Test = char_Test;
	}

	public final Integer getInteger_obj_1() {
		return Integer_obj_1;
	}

	public final void setInteger_obj_1(Integer integer_obj_1) {
		Integer_obj_1 = integer_obj_1;
	}

	public final Integer getInteger_obj_2() {
		return Integer_obj_2;
	}

	public final void setInteger_obj_2(Integer integer_obj_2) {
		Integer_obj_2 = integer_obj_2;
	}

	public final Integer getInteger_obj_3() {
		return Integer_obj_3;
	}

	public final void setInteger_obj_3(Integer integer_obj_3) {
		Integer_obj_3 = integer_obj_3;
	}

	public final Integer getInteger_obj_4() {
		return Integer_obj_4;
	}

	public final void setInteger_obj_4(Integer integer_obj_4) {
		Integer_obj_4 = integer_obj_4;
	}

	public final Integer getInteger_obj_5() {
		return Integer_obj_5;
	}

	public final void setInteger_obj_5(Integer integer_obj_5) {
		Integer_obj_5 = integer_obj_5;
	}

	public final Long getLong_obj_1() {
		return Long_obj_1;
	}

	public final void setLong_obj_1(Long long_obj_1) {
		Long_obj_1 = long_obj_1;
	}

	public final Long getLong_obj_2() {
		return Long_obj_2;
	}

	public final void setLong_obj_2(Long long_obj_2) {
		Long_obj_2 = long_obj_2;
	}

	public final Long getLong_obj_3() {
		return Long_obj_3;
	}

	public final void setLong_obj_3(Long long_obj_3) {
		Long_obj_3 = long_obj_3;
	}

	public final Long getLong_obj_4() {
		return Long_obj_4;
	}

	public final void setLong_obj_4(Long long_obj_4) {
		Long_obj_4 = long_obj_4;
	}

	public final Long getLong_obj_5() {
		return Long_obj_5;
	}

	public final void setLong_obj_5(Long long_obj_5) {
		Long_obj_5 = long_obj_5;
	}

	public final Integer getInteger_obj() {
		return Integer_obj;
	}

	public final void setInteger_obj(Integer integer_obj) {
		Integer_obj = integer_obj;
	}

	public final Long getLong_obj() {
		return Long_obj;
	}

	public final void setLong_obj(Long long_obj) {
		Long_obj = long_obj;
	}

	public final Float getFloat_obj() {
		return Float_obj;
	}

	public final void setFloat_obj(Float float_obj) {
		Float_obj = float_obj;
	}

	public final Double getDouble_obj() {
		return Double_obj;
	}

	public final void setDouble_obj(Double double_obj) {
		Double_obj = double_obj;
	}

	public final Boolean getBoolean_obj() {
		return Boolean_obj;
	}

	public final void setBoolean_obj(Boolean boolean_obj) {
		Boolean_obj = boolean_obj;
	}

	public final Short getShort_obj() {
		return short_obj;
	}

	public final void setShort_obj(Short short_obj) {
		this.short_obj = short_obj;
	}

	public final Byte getByte_obj() {
		return byte_obj;
	}

	public final void setByte_obj(Byte byte_obj) {
		this.byte_obj = byte_obj;
	}

	public final Character getChar_obj() {
		return char_obj;
	}

	public final void setChar_obj(Character char_obj) {
		this.char_obj = char_obj;
	}

	public final DefineTypeBean[] getArray() {
		return array;
	}

	public final void setArray(DefineTypeBean[] array) {
		this.array = array;
	}

	public final List<DefineTypeBean> getList() {
		return list;
	}

	public final void setList(List<DefineTypeBean> list) {
		this.list = list;
	}

	public final Map<String, DefineTypeBean> getMap() {
		return map;
	}

	public final void setMap(Map<String, DefineTypeBean> map) {
		this.map = map;
	}

	public int[] getArray_int() {
		return array_int;
	}

	public void setArray_int(int[] array_int) {
		this.array_int = array_int;
	}

	public long[] getArray_long() {
		return array_long;
	}

	public void setArray_long(long[] array_long) {
		this.array_long = array_long;
	}

	public Integer[] getArray_int_obj() {
		return array_int_obj;
	}

	public void setArray_int_obj(Integer[] array_int_obj) {
		this.array_int_obj = array_int_obj;
	}

	public Long[] getArray_long_obj() {
		return array_long_obj;
	}

	public void setArray_long_obj(Long[] array_long_obj) {
		this.array_long_obj = array_long_obj;
	}

	public Map<Integer, Long> getMap_int_long() {
		return map_int_long;
	}

	public void setMap_int_long(Map<Integer, Long> map_int_long) {
		this.map_int_long = map_int_long;
	}

}

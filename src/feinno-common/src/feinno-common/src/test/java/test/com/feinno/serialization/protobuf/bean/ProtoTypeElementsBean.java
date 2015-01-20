package test.com.feinno.serialization.protobuf.bean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ProtoTypeElementsBean extends ProtoEntity {

	// part 1 基本数据类型
	@ProtoMember(101)
	private int int_Test;

	@ProtoMember(102)
	private long long_Test;

	@ProtoMember(103)
	private float float_Test;

	@ProtoMember(104)
	private double double_Test;

	@ProtoMember(105)
	private boolean boolean_Test;

	@ProtoMember(106)
	private short short_Test;

	@ProtoMember(107)
	private byte byte_Test;

	@ProtoMember(108)
	private char char_Test;

	// part 2 包装数据类型
	@ProtoMember(201)
	private Integer Integer_obj;

	@ProtoMember(202)
	private Long Long_obj;

	@ProtoMember(203)
	private Float Float_obj;

	@ProtoMember(204)
	private Double Double_obj;

	@ProtoMember(205)
	private Boolean Boolean_obj;

	@ProtoMember(206)
	private Short short_obj;

	@ProtoMember(207)
	private Byte byte_obj;

	@ProtoMember(208)
	private Character char_obj;

	public int getInt_Test() {
		return int_Test;
	}

	public void setInt_Test(int int_Test) {
		this.int_Test = int_Test;
	}

	public long getLong_Test() {
		return long_Test;
	}

	public void setLong_Test(long long_Test) {
		this.long_Test = long_Test;
	}

	public float getFloat_Test() {
		return float_Test;
	}

	public void setFloat_Test(float float_Test) {
		this.float_Test = float_Test;
	}

	public double getDouble_Test() {
		return double_Test;
	}

	public void setDouble_Test(double double_Test) {
		this.double_Test = double_Test;
	}

	public boolean getBoolean_Test() {
		return boolean_Test;
	}

	public void setBoolean_Test(boolean boolean_Test) {
		this.boolean_Test = boolean_Test;
	}

	public short getShort_Test() {
		return short_Test;
	}

	public void setShort_Test(short short_Test) {
		this.short_Test = short_Test;
	}

	public byte getByte_Test() {
		return byte_Test;
	}

	public void setByte_Test(byte byte_Test) {
		this.byte_Test = byte_Test;
	}

	public char getChar_Test() {
		return char_Test;
	}

	public void setChar_Test(char char_Test) {
		this.char_Test = char_Test;
	}

	public Integer getInteger_obj() {
		return Integer_obj;
	}

	public void setInteger_obj(Integer integer_obj) {
		Integer_obj = integer_obj;
	}

	public Long getLong_obj() {
		return Long_obj;
	}

	public void setLong_obj(Long long_obj) {
		Long_obj = long_obj;
	}

	public Float getFloat_obj() {
		return Float_obj;
	}

	public void setFloat_obj(Float float_obj) {
		Float_obj = float_obj;
	}

	public Double getDouble_obj() {
		return Double_obj;
	}

	public void setDouble_obj(Double double_obj) {
		Double_obj = double_obj;
	}

	public Boolean getBoolean_obj() {
		return Boolean_obj;
	}

	public void setBoolean_obj(Boolean boolean_obj) {
		Boolean_obj = boolean_obj;
	}

	public Short getShort_obj() {
		return short_obj;
	}

	public void setShort_obj(Short short_obj) {
		this.short_obj = short_obj;
	}

	public Byte getByte_obj() {
		return byte_obj;
	}

	public void setByte_obj(Byte byte_obj) {
		this.byte_obj = byte_obj;
	}

	public Character getChar_obj() {
		return char_obj;
	}

	public void setChar_obj(Character char_obj) {
		this.char_obj = char_obj;
	}

}

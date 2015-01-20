package test.com.feinno.serialization.entity;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class User extends ProtoEntity {
	@ProtoMember(1)
	private Integer intTest;
	@ProtoMember(2)
	private Long longTest;
	@ProtoMember(3)
	private String stringTest;
	@ProtoMember(4)
	private Character charTest;
	@ProtoMember(5)
	private Double doubleTest;
	@ProtoMember(6)
	private Float floatTest;
	@ProtoMember(7)
	private Boolean booleanTest;
	@ProtoMember(8)
	private int anIntTest;
	@ProtoMember(9)
	private long anLongTest;
	@ProtoMember(10)
	private char anCharTest;
	@ProtoMember(11)
	private double anDoubleTest;
	@ProtoMember(12)
	private float anFloatTest;
	@ProtoMember(13)
	private boolean anBooleanTest;
	@ProtoMember(14)
	private List<UserInfo> userInfo;

	public List<UserInfo> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List<UserInfo> userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * @return the anIntTest
	 */
	public int getAnIntTest() {
		return anIntTest;
	}

	/**
	 * @param anIntTest
	 *            the anIntTest to set
	 */
	public void setAnIntTest(int anIntTest) {
		this.anIntTest = anIntTest;
	}

	/**
	 * @return the anLongTest
	 */
	public long getAnLongTest() {
		return anLongTest;
	}

	/**
	 * @param anLongTest
	 *            the anLongTest to set
	 */
	public void setAnLongTest(long anLongTest) {
		this.anLongTest = anLongTest;
	}

	/**
	 * @return the anCharTest
	 */
	public char getAnCharTest() {
		return anCharTest;
	}

	/**
	 * @param anCharTest
	 *            the anCharTest to set
	 */
	public void setAnCharTest(char anCharTest) {
		this.anCharTest = anCharTest;
	}

	/**
	 * @return the anDoubleTest
	 */
	public double getAnDoubleTest() {
		return anDoubleTest;
	}

	/**
	 * @param anDoubleTest
	 *            the anDoubleTest to set
	 */
	public void setAnDoubleTest(double anDoubleTest) {
		this.anDoubleTest = anDoubleTest;
	}

	/**
	 * @return the anFloatTest
	 */
	public float getAnFloatTest() {
		return anFloatTest;
	}

	/**
	 * @param anFloatTest
	 *            the anFloatTest to set
	 */
	public void setAnFloatTest(float anFloatTest) {
		this.anFloatTest = anFloatTest;
	}

	/**
	 * @return the anBooleanTest
	 */
	public boolean getAnBooleanTest() {
		return anBooleanTest;
	}

	/**
	 * @param anBooleanTest
	 *            the anBooleanTest to set
	 */
	public void setAnBooleanTest(boolean anBooleanTest) {
		this.anBooleanTest = anBooleanTest;
	}

	/**
	 * @return the intTest
	 */
	public Integer getIntTest() {
		return intTest;
	}

	/**
	 * @param intTest
	 *            the intTest to set
	 */
	public void setIntTest(Integer intTest) {
		this.intTest = intTest;
	}

	/**
	 * @return the longTest
	 */
	public Long getLongTest() {
		return longTest;
	}

	/**
	 * @param longTest
	 *            the longTest to set
	 */
	public void setLongTest(Long longTest) {
		this.longTest = longTest;
	}

	/**
	 * @return the stringTest
	 */
	public String getStringTest() {
		return stringTest;
	}

	/**
	 * @param stringTest
	 *            the stringTest to set
	 */
	public void setStringTest(String stringTest) {
		this.stringTest = stringTest;
	}

	/**
	 * @return the charTest
	 */
	public Character getCharTest() {
		return charTest;
	}

	/**
	 * @param charTest
	 *            the charTest to set
	 */
	public void setCharTest(Character charTest) {
		this.charTest = charTest;
	}

	/**
	 * @return the doubleTest
	 */
	public Double getDoubleTest() {
		return doubleTest;
	}

	/**
	 * @param doubleTest
	 *            the doubleTest to set
	 */
	public void setDoubleTest(Double doubleTest) {
		this.doubleTest = doubleTest;
	}

	/**
	 * @return the floatTest
	 */
	public Float getFloatTest() {
		return floatTest;
	}

	/**
	 * @param floatTest
	 *            the floatTest to set
	 */
	public void setFloatTest(Float floatTest) {
		this.floatTest = floatTest;
	}

	/**
	 * @return the booleanTest
	 */
	public Boolean getBooleanTest() {
		return booleanTest;
	}

	/**
	 * @param booleanTest
	 *            the booleanTest to set
	 */
	public void setBooleanTest(Boolean booleanTest) {
		this.booleanTest = booleanTest;
	}

	public final boolean isInitialized() {
		return true;
	}
}

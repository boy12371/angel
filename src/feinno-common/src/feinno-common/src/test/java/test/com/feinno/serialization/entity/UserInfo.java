package test.com.feinno.serialization.entity;

import java.util.Date;
import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.Flags;

public class UserInfo extends ProtoEntity {
	@ProtoMember(value = 1, required = true)
	private String address;
	@ProtoMember(2)
	private String tel;
	@ProtoMember(3)
	private TestEnum testEnum = TestEnum.TESTONE;
	@ProtoMember(4)
	private Date date;
	@ProtoMember(5)
	private byte by;
	@ProtoMember(6)
	private Integer[] intList;
	@ProtoMember(7)
	private Byte[] byteList;
	@ProtoMember(8)
	private List<Integer> intArrayList;
	@ProtoMember(9)
	private List<String> stringArrayList;
	@ProtoMember(10)
	private Flags flags;
	@ProtoMember(11)
	private Date sqlDate;

	/**
	 * @return the flags
	 */
	public Flags getFlags() {
		return flags;
	}

	/**
	 * @param flags
	 *            the flags to set
	 */
	public void setFlags(Flags flags) {
		this.flags = flags;
	}

	/**
	 * @return the sqlDate
	 */
	public Date getSqlDate() {
		return sqlDate;
	}

	/**
	 * @param sqlDate
	 *            the sqlDate to set
	 */
	public void setSqlDate(Date sqlDate) {
		this.sqlDate = sqlDate;
	}

	/**
	 * @return the stringArrayList
	 */
	public List<String> getStringArrayList() {
		return stringArrayList;
	}

	/**
	 * @param stringArrayList
	 *            the stringArrayList to set
	 */
	public void setStringArrayList(List<String> stringArrayList) {
		this.stringArrayList = stringArrayList;
	}

	/**
	 * @return the intArrayList
	 */
	public List<Integer> getIntArrayList() {
		return intArrayList;
	}

	/**
	 * @param intArrayList
	 *            the intArrayList to set
	 */
	public void setIntArrayList(List<Integer> intArrayList) {
		this.intArrayList = intArrayList;
	}

	/**
	 * @return the byteList
	 */
	public Byte[] getByteList() {
		return byteList;
	}

	/**
	 * @param byteList
	 *            the byteList to set
	 */
	public void setByteList(Byte[] byteList) {
		this.byteList = byteList;
	}

	/**
	 * @return the intList
	 */
	public Integer[] getIntList() {
		return intList;
	}

	/**
	 * @param intList
	 *            the intList to set
	 */
	public void setIntList(Integer[] intList) {
		this.intList = intList;
	}

	/**
	 * @return the by
	 */
	public byte getBy() {
		return by;
	}

	/**
	 * @param by
	 *            the by to set
	 */
	public void setBy(byte by) {
		this.by = by;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public TestEnum getTestEnum() {
		return testEnum;
	}

	public void setTestEnum(TestEnum testEnum) {
		this.testEnum = testEnum;
	}

}

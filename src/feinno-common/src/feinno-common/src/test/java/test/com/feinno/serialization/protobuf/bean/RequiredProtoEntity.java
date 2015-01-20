package test.com.feinno.serialization.protobuf.bean;

import java.util.List;
import java.util.Map;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class RequiredProtoEntity extends ProtoEntity {
	@ProtoMember(value = 1, required = true)
	private int i;

	@ProtoMember(value = 2, required = true)
	private Long longObj;

	@ProtoMember(value = 3, required = true)
	private String string;

	@ProtoMember(value = 4, required = true, timezone = "UTC")
	private java.util.Date utilDate;

	@ProtoMember(value = 5, required = true, timezone = "UTC")
	private java.sql.Date sqlDate;

	@ProtoMember(value = 6, required = true)
	private List<java.util.Date> utilDateList;
	@ProtoMember(value = 7, required = true)
	private List<java.sql.Date> sqlDateList;

	@ProtoMember(value = 8, required = true)
	private Map<String, Table> map_S_T;

	public final int getI() {
		return i;
	}

	public final void setI(int i) {
		this.i = i;
	}

	public final Long getLongObj() {
		return longObj;
	}

	public final void setLongObj(Long longObj) {
		this.longObj = longObj;
	}

	public final String getString() {
		return string;
	}

	public final void setString(String string) {
		this.string = string;
	}

	public final java.util.Date getUtilDate() {
		return utilDate;
	}

	public final void setUtilDate(java.util.Date utilDate) {
		this.utilDate = utilDate;
	}

	public final java.sql.Date getSqlDate() {
		return sqlDate;
	}

	public final void setSqlDate(java.sql.Date sqlDate) {
		this.sqlDate = sqlDate;
	}

	public final List<java.util.Date> getUtilDateList() {
		return utilDateList;
	}

	public final void setUtilDateList(List<java.util.Date> utilDateList) {
		this.utilDateList = utilDateList;
	}

	public final List<java.sql.Date> getSqlDateList() {
		return sqlDateList;
	}

	public final void setSqlDateList(List<java.sql.Date> sqlDateList) {
		this.sqlDateList = sqlDateList;
	}

	public final Map<String, Table> getMap_S_T() {
		return map_S_T;
	}

	public final void setMap_S_T(Map<String, Table> map_S_T) {
		this.map_S_T = map_S_T;
	}
}

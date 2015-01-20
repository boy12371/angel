package com.feinno.diagnostic.observation;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class ObserverReportColumn extends ProtoEntity {
	@ProtoMember(1)
	private String name;
	@ProtoMember(2)
	private ObserverReportColumnType type = ObserverReportColumnType.LONG;

	public ObserverReportColumn(String name, ObserverReportColumnType type) {
		this.name = name;
		this.type = type;
	}

	public ObserverReportColumn() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObserverReportColumnType getType() {
		return type;
	}

	public void setType(ObserverReportColumnType type) {
		this.type = type;
	}
}

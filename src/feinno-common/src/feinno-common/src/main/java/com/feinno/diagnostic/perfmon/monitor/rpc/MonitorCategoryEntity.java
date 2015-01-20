package com.feinno.diagnostic.perfmon.monitor.rpc;

import java.util.ArrayList;
import java.util.List;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 一项可序列化列表详细信息.
 * 
 * @author jingmiao
 * 
 */
public class MonitorCategoryEntity extends ProtoEntity {
	@ProtoMember(1)
	private String name;
	@ProtoMember(2)
	private int instance;
	@ProtoMember(3)
	private List<ObserverReportColumn> columns = new ArrayList<ObserverReportColumn>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInstance() {
		return instance;
	}

	public void setInstance(int instance) {
		this.instance = instance;
	}

	public List<ObserverReportColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<ObserverReportColumn> columns) {
		this.columns = columns;
	}

}

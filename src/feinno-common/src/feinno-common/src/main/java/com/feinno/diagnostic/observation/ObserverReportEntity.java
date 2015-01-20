package com.feinno.diagnostic.observation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ObserverReportEntity extends ProtoEntity
{
	@ProtoMember(1)
	private String category;
	
	@ProtoMember(2)
	private Date time;
	
	@ProtoMember(3)
	private List<ObserverReportColumn> columns = new ArrayList<ObserverReportColumn>();
	
	@ProtoMember(4)
	private List<ObserverReportRowBean> rows = new ArrayList<ObserverReportRowBean>();

	/*
	 * private ObserverReportBean() { super(); }
	 * 
	 * public ObserverReportBean(String category, DateTime time,
	 * List<ObserverReportColumn> columns, List<ObserverReportRowBean> rows) {
	 * super(); this.category = category; this.time = time; this.columns =
	 * columns; this.rows = rows; }
	 */

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public Date getTime()
	{
		return time;
	}

	public void setTime(Date time)
	{
		this.time = time;
	}

	public List<ObserverReportColumn> getColumns()
	{
		return columns;
	}

	public void setColumns(List<ObserverReportColumn> columns)
	{
		this.columns = columns;
	}

	public List<ObserverReportRowBean> getRows()
	{
		return rows;
	}

	public void setRows(List<ObserverReportRowBean> rows)
	{
		this.rows = rows;
	}
}

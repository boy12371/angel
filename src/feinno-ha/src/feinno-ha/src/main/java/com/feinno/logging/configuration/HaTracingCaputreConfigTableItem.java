package com.feinno.logging.configuration;

import java.util.Date;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

public class HaTracingCaputreConfigTableItem extends ConfigTableItem {
	@ConfigTableField(value = "FilterId", isKeyField = true)
	private int id;

	@ConfigTableField("UserIdentity")
	private String userIdentity;

	@ConfigTableField("CreateTime")
	private Date createTime;

	public final int getId() {
		return id;
	}

	public final void setId(int id) {
		this.id = id;
	}

	public final String getIdentity() {
		return userIdentity;
	}

	public final void setMarker(String value) {
		this.userIdentity = value;
	}

	public final Date getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}

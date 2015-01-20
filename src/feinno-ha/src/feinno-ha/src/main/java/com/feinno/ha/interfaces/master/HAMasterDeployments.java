/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 15, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.master;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAMasterDeployments extends ProtoEntity
{
	@ProtoMember(1)
	private List<HAMasterDeployment> deployments;

	public List<HAMasterDeployment> getDeployments()
	{
		return deployments;
	}

	public void setDeployments(List<HAMasterDeployment> deployments)
	{
		this.deployments = deployments;
	}	
}

/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft 2011-2-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.runtime.configuration;

import java.util.UUID;

import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.FAEAppAnnotationDecoder;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.EnumInteger;

/**
 * FAE_Application配置, 通过ZooKeeper同步，全局仅有一份<br>
 * <p>
 * http://research.feinno.com/trac/fae/wiki/FAE/Route<br>
 * message FAE_Application {<br>
 * required int32 AppId = 1; <br>
 * required string AppCategory = 2; // <br>
 * required string AppName = 3; // <br>
 * required string AppWorkerId = 5; // AppWorkerPackage + ServerGroup + WorkerName <br>
 * required string Annotations = 6; // Application的Annotations用这种方式路由,
 * 格式同FAE/Annotations JSON格式 <br>
 * optional string GrayFactors = 7; // 灰度发布因子 <br>
 * required string LocalSites = 8; // 哪些Site属于IDC内配置Site <br>
 * }
 * </p>
 * @author 高磊 gaolei@feinno.com
 */
public class ApplicationEntity extends ProtoEntity
{
	private String nodeKey;		// Zookeeper中的结点ID
	
	@ProtoMember(1)
	private int appId;

	@ProtoMember(2)
	private String category;

	@ProtoMember(3)
	private String name;

	@ProtoMember(5)
	private String appWorkerId;

	@ProtoMember(6)
	private String annotations;

	@ProtoMember(7)
	private String grayFactors;

	@ProtoMember(8)
	private String site;
	
	@ProtoMember(9)		// 1.5新增
	private String workerInstance;
	
	@ProtoMember(10)	// 1.5新增
	private String serverGroup;
	
	@ProtoMember(11)    // 1.5新增
	private String appVersion;

	@ProtoMember(1001) //用于标识当前App的来源是内部Site还是外部Site
	private ApplicationSource appSource = ApplicationSource.INNER_SITE;
	
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getServerGroup()
	{
		return serverGroup;
	}

	public void setServerGroup(String serverGroup){
		this.serverGroup = serverGroup;
	}

	public String getWorkerInstance()
	{
		return workerInstance;
	}

	public void setWorkerInstance(String workerInstance)
	{
		this.workerInstance = workerInstance;
	}

	private AppBeanAnnotations appBeanAnnotations;

	public int getAppId()
	{
		return appId;
	}

	public int getBeanId()
	{
		return appId;
	}

	public void setAppId(int appId)
	{
		this.appId = appId;
	}

	public String getAppWorkerId()
	{
		return appWorkerId;
	}

	public void setAppWorkerId(String appWorkerId)
	{
		this.appWorkerId = appWorkerId;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSite()
	{
		return site;
	}

	public void setSite(String site)
	{
		this.site = site;
	}

	public String getAnnotations()
	{
		return annotations;
	}

	public void setAnnotations(String annotations)
	{
		this.annotations = annotations;
		appBeanAnnotations = FAEAppAnnotationDecoder.decode(annotations);
	}
	
	public String getCategoryMinusName()
	{
		return category + "-" + name;
	}

	public AppBeanAnnotations getAppBeanAnnotations()
	{
		return appBeanAnnotations;
	}

	public String getGrayFactors()
	{
		return grayFactors;
	}

	public void setGrayFactors(String grayFactors)
	{
		this.grayFactors = grayFactors;
	}

	public ApplicationSource getAppSource() {
		return appSource;
	}

	public void setAppSource(ApplicationSource appSource) {
		this.appSource = appSource;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
		result = prime * result + ((appBeanAnnotations == null) ? 0 : appBeanAnnotations.hashCode());
		result = prime * result + appId;
		result = prime * result + ((appWorkerId == null) ? 0 : appWorkerId.hashCode());
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((grayFactors == null) ? 0 : grayFactors.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		
		if (this == obj)
			return true;
		
		if (getClass() != obj.getClass())
			return false;
		ApplicationEntity other = (ApplicationEntity) obj;
		if (annotations == null) {
			if (other.annotations != null)
				return false;
		} else if (!annotations.equals(other.annotations))
			return false;
		if (appBeanAnnotations == null) {
			if (other.appBeanAnnotations != null)
				return false;
		} else if (!appBeanAnnotations.equals(other.appBeanAnnotations))
			return false;
		if (appId != other.appId)
			return false;
		if (appWorkerId == null) {
			if (other.appWorkerId != null)
				return false;
		} else if (!appWorkerId.equals(other.appWorkerId))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (grayFactors == null) {
			if (other.grayFactors != null)
				return false;
		} else if (!grayFactors.equals(other.grayFactors))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		return true;
	}

	public String showString()
	{
		return "Application [appId=" + appId + ", category=" + category + ", name=" + name + ", grayFactors="
				+ grayFactors + ", appWorkerId=" + appWorkerId + ", \nannotations=:\n" + annotations
				+ "\n, appBeanAnnotations=" + appBeanAnnotations + "]";
	}

	public String getNodeKey()
	{
		return nodeKey;
	}

	public void setNodeKey(String nodeKey)
	{
		this.nodeKey = nodeKey;
	}

	public static String generateNoteKey()
	{
		return UUID.randomUUID().toString();
	}
	
	public static enum ApplicationSource implements EnumInteger {
		
		INNER_SITE(0), OUTER_SITE(1);
		
		int value;

		ApplicationSource(int value) {
			this.value = value;
		}

		public int intValue() {
			return value;
		}
	}
}

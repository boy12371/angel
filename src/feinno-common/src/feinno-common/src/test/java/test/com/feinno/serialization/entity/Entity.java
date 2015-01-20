/*
 * FAE, Feinno App Engine

 *  
 * Create by duyu 2011-1-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class Entity extends ProtoEntity
{
	@ProtoMember(1)
	private Integer id;
	@ProtoMember(2)
	private String name;
	
	private String pwd;
	@ProtoMember(4)
	private List<User> userList;
	@ProtoMember(5)
	private UserInfo userInfo = new UserInfo();
	@ProtoMember(6)
	private List<UserInfo> userInfoList;
	@ProtoMember(7)
	private int[] intArray;
	@ProtoMember(8)
	private boolean[] bolArray;
	
	public boolean[] getBolArray() {
		return bolArray;
	}
	public void setBolArray(boolean[] bolArray) {
		this.bolArray = bolArray;
	}
	public int[] getIntArray() {
		return intArray;
	}
	public void setIntArray(int[] intArray) {
		this.intArray = intArray;
	}
	/**
	 * @return the userInfoList
	 */
	public List<UserInfo> getUserInfoList() {
		return userInfoList;
	}
	/**
	 * @param userInfoList the userInfoList to set
	 */
	public void setUserInfoList(List<UserInfo> userInfoList) {
		this.userInfoList = userInfoList;
	}
	/**
	 * @return the userInfo
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}
	/**
	 * @param userInfo the userInfo to set
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public final boolean isInitialized() {
		return true;
	}

}

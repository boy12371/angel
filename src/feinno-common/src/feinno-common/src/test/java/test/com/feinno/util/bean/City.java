/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2010-11-26
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package test.com.feinno.util.bean;

import java.util.Date;

/**
 * {城市类的实体bean}
 * 
 * @auther wanglihui
 */
public class City {
	private int ID;
	private String name;
	private String province;
	private String areacode;
	private String areadesc;
	private Date history;
	
	public City(){		
	}
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
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
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the areacode
	 */
	public String getAreacode() {
		return areacode;
	}
	/**
	 * @param areacode the areacode to set
	 */
	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}
	/**
	 * @return the areadesc
	 */
	public String getAreadesc() {
		return areadesc;
	}
	/**
	 * @param areadesc the areadesc to set
	 */
	public void setAreadesc(String areadesc) {
		this.areadesc = areadesc;
	}
	/**
	 * @return the history
	 */
	public Date getHistory() {
		return history;
	}
	/**
	 * @param history the history to set
	 */
	public void setHistory(Date history) {
		this.history = history;
	}
}

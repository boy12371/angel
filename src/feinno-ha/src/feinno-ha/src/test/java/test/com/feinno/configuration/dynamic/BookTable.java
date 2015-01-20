/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.configuration.dynamic;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

/**
 * 当访问Rpc的表时, 获取到的Rpc地址使用其他端口代替
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class BookTable extends ConfigTableItem {
	@ConfigTableField(value = "bookname", isKeyField = false)
	private String bookname;

	@ConfigTableField(value = "bookid", isKeyField = true)
	private int bookid;


	public String getBookname() {
		return bookname;
	}


	public void setBookname(String bookname) {
		this.bookname = bookname;
	}


	public int getBookid() {
		return bookid;
	}


	public void setBookid(int bookid) {
		this.bookid = bookid;
	}


	@Override
	public void afterLoad() throws Exception {
	}
}

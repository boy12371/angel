/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-1-24
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util.configbean;

import java.util.HashMap;
import java.util.Map;

import com.feinno.util.ConfigBean;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public class B  extends ConfigBean{
	private int b1;
	private String b2;
	private C b3 = new C();
	private Map<String, C> b4 = new HashMap();
}

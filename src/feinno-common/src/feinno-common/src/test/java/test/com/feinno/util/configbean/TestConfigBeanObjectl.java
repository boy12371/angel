/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-1-24
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.util.configbean;

import java.util.Properties;

import org.junit.Test;

import com.feinno.util.ConfigBean;
import com.feinno.util.PropertiesUtil;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public class TestConfigBeanObjectl {

	@Test
	public void test() throws Exception {
		Properties pro = null;
		pro = new Properties();
		// 通用类型测试
		pro.setProperty("key", "mysql1");
		pro.setProperty("type", "mysql");
		pro.setProperty("driver", "com.mysql.jdbc.Driver");
		pro.setProperty("url", "jdbc:mysql://localhost:3306/mysql");
		pro.setProperty("username", "root");
		pro.setProperty("password", "root");
		pro.setProperty("maxconn", "10");
		pro.setProperty("wait", "100");
		pro.setProperty("fudian", "10.01");
		pro.setProperty("shuangjing", "10.000001");
		pro.setProperty("buer", "false");
		pro.setProperty("zijie", "127");
		pro.setProperty("halfint", "32767");
		// 枚举类型测试
		pro.setProperty("day", "Mon");
		// 两级测试
		pro.setProperty("b.b1", "1");
		pro.setProperty("b.b2", "aaa");
		// 三级测试
		pro.setProperty("b.b3.c1", "2");
		pro.setProperty("b.b3.c2", "bbb");
		pro.setProperty("b.b3.c3", "Sat");
		// {}测试
		pro.setProperty("map{k1}.b1", "3");
		pro.setProperty("map{k1}.b2", "map1");
		pro.setProperty("map{k1}.b3.c1", "33");
		pro.setProperty("map{k1}.b3.c2", "ccc");
		pro.setProperty("map{k1}.b3.c3", "wed");

		pro.setProperty("map{k2}.b1", "3");
		pro.setProperty("map{k2}.b2", "map2");
		pro.setProperty("map{k2}.b3.c1", "33");
		pro.setProperty("map{k2}.b3.c2", "ccc");
		pro.setProperty("map{k2}.b3.c3", "sat");
		// 两级{}{}测试
		pro.setProperty("map{k1}.b1", "3");
		pro.setProperty("map{k1}.b2", "map1");
		pro.setProperty("map{k1}.b3.c1", "33");
		pro.setProperty("map{k1}.b3.c2", "ccc");
		pro.setProperty("map{k1}.b3.c3", "wed");
		pro.setProperty("map{k1}.b4{kc1}.c1", "333");
		pro.setProperty("map{k1}.b4{kc1}.c2", "ccc");
		pro.setProperty("map{k1}.b4{kc1}.c3", "wed");
		pro.setProperty("map{k1}.b4{kc2}.c1", "3333");
		pro.setProperty("map{k1}.b4{kc2}.c2", "cccccc");
		pro.setProperty("map{k1}.b4{kc2}.c3", "mon");

		pro.setProperty("map{k2}.b1", "3");
		pro.setProperty("map{k2}.b2", "map2");
		pro.setProperty("map{k2}.b3.c1", "33");
		pro.setProperty("map{k2}.b3.c2", "ccc");
		pro.setProperty("map{k2}.b3.c3", "sat");
		pro.setProperty("map{k2}.b4{kc1}.c1", "333");
		pro.setProperty("map{k2}.b4{kc1}.c2", "ccc");
		pro.setProperty("map{k2}.b4{kc1}.c3", "wed");
		pro.setProperty("map{k2}.b4{kc2}.c1", "3333");
		pro.setProperty("map{k2}.b4{kc2}.c2", "cccccc");
		pro.setProperty("map{k2}.b4{kc2}.c3", "sun");
		pro.setProperty("map{k2.k3.k4}.b4{kc2}.c3", "sun");
		// ===========NO Feild
		pro.setProperty("1", "1");
		pro.setProperty("1.1", "11");
		pro.setProperty("1.1.1", "111");
		pro.setProperty("1.1.1.1", "1111");
		pro.setProperty("1.1.1.1.1", "11111");

		PropertiesUtil.fillBean(new Properties(), A.class);
		A bean = ConfigBean.valueOf(pro, A.class);
		System.out.println("=========================Default Object========================");
		ConfigBeanTree configBeanTree = new ConfigBeanTree();
		configBeanTree.printTree(bean);
	}
}

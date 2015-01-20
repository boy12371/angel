/*
 * FAE, Feinno App Engine
 *  
 * Create by lichunlei 2010-12-16
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package test.com.feinno.util.propertiesutil;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接信息Bean
 * 
 * @author wanglihui
 */
public class A {

    /**
     * key对应数据库连接池的池名
     */
    private String key;
    /**
     * 数据库类型：mysql、sqlserver等
     */
    private String type;
    /**
     * 驱动类名，如：com.mysql.jdbc.Driver
     */
    private String driver;
    /**
     * 数据库url,如：jdbc:mysql://localhost:3306/mysql
     */
    private String url;
    /**
     * 数据库用户名
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 最大连接数
     */
    private int maxconn;	
    /**
     * 取得数据库连接时的最长等待时间
     */
    private long wait;
    
    /**
     * 浮点数
     */
    private float fudian;
    /**
     * 双精度数
     */
    private double shuangjing;

    /**
     * 布尔类型
     */
    private boolean buer;
    /**
     * 字节类型
     */
    private byte zijie;
    /**
     * 短整型
     */
    private short halfint;
    /**
     * 枚举类型
     */
    private WeekDay day = WeekDay.Tue;
    
    /**
     * 对象类型
     */
    private B b = new B();
    
    /**
     * Map类型
     */
    private Map<String, B> map = new HashMap();
}

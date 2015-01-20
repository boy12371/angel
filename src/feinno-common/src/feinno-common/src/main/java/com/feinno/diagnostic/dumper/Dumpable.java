/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Aug 23, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.dumper;

/**
 * 
 * <b>描述: </b>将Object信息Dump成可读的文本的工具类的统一接口
 * <p>
 * <b>功能: </b>将Object信息Dump成可读的文本的工具类的统一接口
 * <p>
 * <b>用法: </b>参见{@link ObjectDumper}
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public interface Dumpable
{
	String dumpContent();
}

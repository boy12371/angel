/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-12-16
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.rpc.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc客户端的自动注解
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RpcMethod 
{
	/** Rpc方法的名字 */
	String value() default "";
//
//	/** Rpc方法的请求类型, 在使用继承RpcServiceBase的实现方式时, 可增加安全性并提升效率 */
//	Class<?> argsType() default Void.class;
//
//	/** Rpc方法的应答类型, 在使用继承RpcServiceBase的实现方式时, 可增加安全性并提升效率 */
//	Class<?> resultsType() default Void.class;
}

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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Rpc服务的注解，用于标明透明的Rpc接口
 * 
 * <pre>
 * <code>
 * 	@RpcService("Hello")
 * 	public interface HelloService {
 * 		@RpcMethod("Add")
 * 		AddResults add(AddArgs args); 	
 * 	}
 * </code>
 * </pre>
 * 
 * @author 高磊 gaolei@feinno.com
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService 
{
	/** Rpc服务名 */
	String value() default "";
	
	/** 是否允许使用RpcServerContext.getCurrent()方法, 打开会降低性能, 建议使用直接继承RpcServiceBase的方式实现 */
	boolean threadContext() default false;
}

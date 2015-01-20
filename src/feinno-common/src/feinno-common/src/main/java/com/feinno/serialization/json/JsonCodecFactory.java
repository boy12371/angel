/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-10
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.serialization.json;

import com.feinno.serialization.Codec;
import com.feinno.serialization.CodecFactory;
import com.feinno.serialization.Serializer;

/**
 * 
 * <b>描述: </b>用于创建JSON序列化编解码器{@link JsonCodec}的工厂类，关于序列化编解码器的工厂类，请参见
 * {@link CodecFactory};
 * <p>
 * <b>功能: </b>创建JSON序列化编解码器的工厂类
 * <p>
 * <b>用法: </b> 本类作为抽象工厂的一部分，是配合{@link Serializer}使用，单独使用时无意义， 不建议直接操作此类，正确调用应从
 * {@link Serializer}开始。
 * 
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public class JsonCodecFactory extends CodecFactory {

	public JsonCodecFactory() {
		super("json");
	}

	/*
	 * @see com.feinno.serialization.CodecFactory#getCodec(java.lang.Class)
	 */
	/**
	 * {在这里补充功能说明}
	 * 
	 * @param clazz
	 * @return
	 */
	@Override
	public Codec getCodec(Class<?> clazz) {
		if (clazz.getAnnotation(JsonContract.class) == null) {
			return null;
		} else {
			return new JsonCodec(clazz);
		}
	}
}

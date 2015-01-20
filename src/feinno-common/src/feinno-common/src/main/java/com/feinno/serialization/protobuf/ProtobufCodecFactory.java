/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-1-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.serialization.protobuf;

import com.feinno.serialization.Codec;
import com.feinno.serialization.CodecFactory;
import com.feinno.serialization.Serializer;

/**
 * 
 * <b>描述: </b>用于创建Protobuf序列化编解码器{@link ProtobufCodec}的工厂类，关于序列化编解码器的工厂类，请参见
 * {@link ProtobufCodecFactory};
 * <p>
 * <b>功能: </b>创建Protobuf序列化编解码器的工厂类
 * <p>
 * <b>用法: </b>本类作为抽象工厂的一部分，是配合{@link Serializer}使用，单独使用时无意义， 不建议直接操作此类，正确调用应从
 * {@link Serializer}开始。
 * <p>
 * 
 * @author 高磊 gaolei@feinno.com
 * 
 */
public class ProtobufCodecFactory extends CodecFactory {
	public ProtobufCodecFactory() {
		super("protobuf");
	}

	/*
	 * @see com.feinno.serialization.CodecFactory#getCodec(java.lang.Class)
	 */
	@Override
	public Codec getCodec(Class<?> clazz) {
		// 如果属于PB的可序列化范围，则使用PB的Codec，否则返回空
		if (ProtoManager.checkScope(clazz)) {
			return new AnnotatedProtobufCodec(clazz);
		} else {
			return null;
		}
	}
}

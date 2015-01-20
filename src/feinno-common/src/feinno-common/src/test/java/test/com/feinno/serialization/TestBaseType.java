/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;

import org.junit.Test;

import com.feinno.serialization.Serializer;

import test.com.feinno.serialization.entity.BaseTypeBean;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestBaseType {

	@Test
	public void test() {
		BaseTypeBean type = new BaseTypeBean();
		type.setB((byte) 'b');
		type.setBo(true);
		type.setC('c');
		type.setD(1.23456789012345);
		type.setF(1.009f);
		type.setI(123);
		type.setL(4567890123l);
		type.setS((short) 10);

		try {
			byte[] by = Serializer.encode(type);
			System.out.println("type = " + by);

			BaseTypeBean obj = Serializer.decode(BaseTypeBean.class, by);
			System.out
					.println(String
							.format("byte=%s, boolean=%s, char=%s, double=%s, float=%s, int=%s, long=%s, short=%s",
									obj.getB(), obj.isBo(), obj.getC(),
									obj.getD(), obj.getF(), obj.getI(),
									obj.getL(), obj.getS()));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

}

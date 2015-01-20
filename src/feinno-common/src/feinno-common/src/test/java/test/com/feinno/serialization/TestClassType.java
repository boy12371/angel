/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import test.com.feinno.serialization.entity.ClassTypeBean;
import test.com.feinno.serialization.entity.ConfigTypeEnum;

import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestClassType {

	@Test
	public void test() {
		ClassTypeBean type = new ClassTypeBean();

		type.setStr("test Str");
		type.setB(new Byte((byte) 'b'));
		type.setBo(new Boolean(true));
		type.setC(new Character('c'));
		type.setD(new Double(1.23456789012345));
		type.setF(new Float(1.009));
		type.setI(new Integer(123));
		type.setL(new Long(4567890123l));
		type.setS(new Short("10"));
		type.setType(ConfigTypeEnum.TABLE);
		type.setDate(new Date());

		try {
			byte[] by = Serializer.encode(type);
			System.out.println("type = " + by);

			ClassTypeBean obj = Serializer.decode(ClassTypeBean.class, by);
			System.out
					.println(String
							.format("String=%s, byte=%s, boolean=%s, char=%s, double=%s, float=%s, int=%s, long=%s, short=%s",
									obj.getStr(), obj.getB().byteValue(), obj
											.getBo().booleanValue(), obj.getC()
											.charValue(), obj.getD()
											.doubleValue(), obj.getF()
											.floatValue(), obj.getI()
											.intValue(),
									obj.getL().longValue(), obj.getS()
											.shortValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

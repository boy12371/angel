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

import test.com.feinno.serialization.entity.ClassTypeArrayBean;

import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestClassTypeArray {

	@Test
	public void test() {
		ClassTypeArrayBean type = new ClassTypeArrayBean();

		String[] str = new String[5];
		Byte[] b = new Byte[5];
		Boolean[] bo = new Boolean[5];
		Character[] c = new Character[5];
		Double[] d = new Double[5];
		Float[] f = new Float[5];
		Integer[] i = new Integer[5];
		Long[] l = new Long[5];
		Short[] s = new Short[5];

		for (int j = 0; j < 5; j++) {
			str[j] = new String("test" + j);
			b[j] = new Byte((byte) (97 + j));
			bo[j] = new Boolean(j % 2 == 0 ? true : false);
			c[j] = new Character('a');
			d[j] = new Double(1.23456789012345 + j);
			f[j] = new Float(1.009f + j);
			i[j] = new Integer(100 + j);
			l[j] = new Long(45678901230l + j);
			s[j] = new Short((10 + j) + "");
		}

		type.setStrArray(str);
		type.setBArray(b);
		type.setBoArray(bo);
		type.setCArray(c);
		type.setDArray(d);
		type.setFArray(f);
		type.setIArray(i);
		type.setLArray(l);
		type.setSArray(s);

		try {
			byte[] by = Serializer.encode(type);
			System.out.println("type = " + by);

			ClassTypeArrayBean obj = Serializer.decode(
					ClassTypeArrayBean.class, by);
			for (int j = 0; j < 5; j++) {
				System.out
						.println(String
								.format("String=%s, byte=%s, boolean=%s, char=%s, double=%s, float=%s, int=%s, long=%s, short=%s",
										obj.getStrArray()[j],
										obj.getBArray()[j].byteValue(),
										obj.getBoArray()[j].booleanValue(),
										obj.getCArray()[j].charValue(),
										obj.getDArray()[j].doubleValue(),
										obj.getFArray()[j].floatValue(),
										obj.getIArray()[j].intValue(),
										obj.getLArray()[j].longValue(),
										obj.getSArray()[j].shortValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

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

import test.com.feinno.serialization.entity.BaseTypeArrayBean;

import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestBaseTypeArray {

	@Test
	public void test() {
		BaseTypeArrayBean type = new BaseTypeArrayBean();

		byte[] b = new byte[5];
		boolean[] bo = new boolean[5];
		char[] c = new char[5];
		double[] d = new double[5];
		float[] f = new float[5];
		int[] i = new int[5];
		long[] l = new long[5];
		short[] s = new short[5];

		for (int j = 0; j < 5; j++) {
			b[j] = (byte) (97 + j);
			bo[j] = j % 2 == 0 ? true : false;
			c[j] = 'a';
			d[j] = 1.23456789012345 + j;
			f[j] = 1.009f + j;
			i[j] = 100 + j;
			l[j] = 45678901230l + j;
			s[j] = (short) (10 + j);
		}

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

			BaseTypeArrayBean obj = Serializer.decode(BaseTypeArrayBean.class,
					by);
			for (int j = 0; j < 5; j++) {
				System.out
						.println(String
								.format("byte=%s, boolean=%s, char=%s, double=%s, float=%s, int=%s, long=%s, short=%s",
										obj.getBArray()[j],
										obj.getBoArray()[j],
										obj.getCArray()[j], obj.getDArray()[j],
										obj.getFArray()[j], obj.getIArray()[j],
										obj.getLArray()[j], obj.getSArray()[j]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

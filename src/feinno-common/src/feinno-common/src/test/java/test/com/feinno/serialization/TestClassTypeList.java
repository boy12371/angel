/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import test.com.feinno.serialization.entity.ClassTypeListBean;

import com.feinno.serialization.Serializer;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class TestClassTypeList {

	@Test
	public void test() {
		ClassTypeListBean type = new ClassTypeListBean();
		List<String> strList = new ArrayList<String>();
		List<Short> sList = new ArrayList<Short>();
		List<Byte> bList = new ArrayList<Byte>();
		List<Boolean> boList = new ArrayList<Boolean>();
		List<Character> cList = new ArrayList<Character>();
		List<Double> dList = new ArrayList<Double>();
		List<Float> fList = new ArrayList<Float>();
		List<Integer> iList = new ArrayList<Integer>();
		List<Long> lList = new ArrayList<Long>();
		for (int j = 0; j < 5; j++) {
			strList.add(new String("test" + j));
			bList.add(((byte) (97 + j)));
			boList.add(new Boolean(j % 2 == 0 ? true : false));
			cList.add(new Character('a'));
			dList.add(new Double(1.23456789012345 + j));
			fList.add(new Float(1.009f + j));
			iList.add(new Integer(100 + j));
			lList.add(new Long(45678901230l + j));
			sList.add(new Short((10 + j) + ""));
		}

		type.setBList(bList);
		type.setBoList(boList);
		type.setCList(cList);
		type.setDList(dList);
		type.setFList(fList);
		type.setIList(iList);
		type.setLList(lList);
		type.setSList(sList);
		type.setStrList(strList);

		try {
			byte[] by = Serializer.encode(type);
			System.out.println("type = " + by);

			ClassTypeListBean obj = Serializer.decode(ClassTypeListBean.class,
					by);
			for (int j = 0; j < 5; j++) {
				System.out
						.println(String
								.format("String=%s, byte=%s, boolean=%s, char=%s, double=%s, float=%s, int=%s, long=%s, short=%s",
										obj.getStrList().get(j), obj.getBList()
												.get(j).byteValue(), obj
												.getBoList().get(j)
												.booleanValue(), obj.getCList()
												.get(j).charValue(), obj
												.getDList().get(j)
												.doubleValue(), obj.getFList()
												.get(j).floatValue(), obj
												.getIList().get(j).intValue(),
										obj.getLList().get(j).longValue(), obj
												.getSList().get(j).shortValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

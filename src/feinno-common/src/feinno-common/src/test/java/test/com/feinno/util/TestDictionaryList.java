package test.com.feinno.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import test.com.feinno.util.bean.City;

import com.feinno.util.Action3;
import com.feinno.util.DictionaryList;
import com.feinno.util.DictionaryList.UpdateMode;
import com.feinno.util.Func;

public class TestDictionaryList {

	@Test
	public void TestCompareAll() {
		DictionaryList<String, Integer> dl = new DictionaryList<String, Integer>();
		
		
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 10; ++i) {
			list.add(i);
		}
		FuncA funcA = new FuncA();
		dl.fillWith(list, funcA);

		FuncB funcB = new FuncB();
		dl.fillWithKeys(list, funcB);

		System.out.println("Collection keys()" + dl.keys());
		System.out.println("List get()" + dl.get("test"));
		
		ActionA action = new ActionA();
		dl.compareAll(dl, action);
		
		DictionaryList<String, Integer> dlTest = new DictionaryList<String, Integer>();
		
		dl.compareAll(dlTest, action);
		
		
	}

	@Test
	public void TestListEqual() {
		List<Integer> llist = new ArrayList<Integer>();
		List<Integer> rlist = new ArrayList<Integer>();
		for (int i = 0; i < 10; ++i) {
			llist.add(i);
			rlist.add(i + 10);
		}
		Assert.assertEquals(false, DictionaryList.listEqual(llist, rlist));
		DictionaryList.listEqual(llist, rlist);
		rlist.add(0);
		Assert.assertEquals(false, DictionaryList.listEqual(llist, rlist));
		Assert.assertEquals(true, DictionaryList.listEqual(llist, llist));
	}

	class FuncA implements Func<Integer, String> {
		@Override
		public String exec(Integer obj) {
			String obj2 = new String("test");
			return obj2;
		}
	}

	class FuncB implements Func<Integer, List<String>> {
		@Override
		public List<String> exec(Integer obj) {
			List<String> obj2 = new ArrayList<String>();
			for (int i = 0; i < 10; ++i) {
				obj2.add("" + i);
			}
			return obj2;
		}
	}

	class ActionA implements Action3<String, UpdateMode, List<Integer>> {

		@Override
		public void run(String v1, UpdateMode v2, List<Integer> v3) {
			// TODO Auto-generated method stub
			// 什么逻辑？。。。
			//System.out.println("UpdateMode" + v2);
		}

	}
}

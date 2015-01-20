//package test.com.feinno.util;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//
//import junit.framework.Assert;
//import org.junit.Test;
//
//import com.feinno.util.Weigher;
//import com.feinno.util.Weighers;
//
//public class TestWeighers {
//
//	@Test
//	public void Test() {
//		Assert.assertEquals(1, Weighers.singleton().weightOf(new Object()));
//
//		Assert.assertEquals(1, Weighers.byteArray().weightOf(new byte[] { 1 }));
//
//		Assert.assertEquals(0,
//				Weighers.iterable().weightOf(new ArrayList<Object>()));
//		Assert.assertTrue(Weighers.iterable().weightOf(
//				(Iterable<Object>) (new TestIterable<Object>())) > 0);
//
//		Assert.assertEquals(0,
//				Weighers.collection().weightOf(new ArrayList<Object>()));
//
//		Assert.assertEquals(0, Weighers.list()
//				.weightOf(new ArrayList<Object>()));
//
//		Assert.assertEquals(0, Weighers.set().weightOf(new HashSet<Object>()));
//
//		Assert.assertEquals(0,
//				Weighers.map().weightOf(new HashMap<Object, Object>()));
//
//	}
//
//	@Test
//	public void TestThrow() {
//		Class clazz = Weighers.class;
//		try {
//			Constructor cs = clazz.getDeclaredConstructor();
//			cs.setAccessible(true);
//			Weighers demo = (Weighers) cs.newInstance();
//			Assert.fail();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			Assert.assertTrue(true);
//			e.printStackTrace();
//		}
//	}
//
//	class TestIterable<E> implements Iterable<Object> {
//		private String name;
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		@Override
//		public Iterator iterator() {
//			// TODO Auto-generated method stub
//			return new Itr();
//		}
//
//		private class Itr implements Iterator {
//			private int cursor = 0;
//			private Field[] fields = TestIterable.class.getDeclaredFields();
//
//			public boolean hasNext() {
//
//				return cursor != (TestIterable.class.getDeclaredFields().length);
//			}
//
//			public Object next() {
//				Object o = null;
//				try {
//					fields[cursor].setAccessible(true);
//					o = (Object) (fields[cursor].getName() + " " + fields[cursor]
//							.get(TestIterable.this));
//					cursor++;
//				} catch (Exception e) {
//					System.out.println(e);
//				}
//				return o;
//			}
//
//			@Override
//			public void remove() {
//				// TODO Auto-generated method stub
//
//			}
//
//		}
//	}
//
//}

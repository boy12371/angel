package test.com.feinno.serialization.protobuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import test.com.feinno.serialization.protobuf.bean.FullElementsBean;

import com.feinno.serialization.protobuf.ProtoManager;

public class TestNative {

	@Test
	public void test() {
		try {
			// 基本类型测试
			System.out.println("基本类型测试-----------------------------------------------------------------");
			byte[] buffer = ProtoManager.toByteArray("Lv.Mingwei");
			Assert.assertEquals("Lv.Mingwei", ProtoManager.parseFrom(new String(), buffer));

			buffer = ProtoManager.toByteArray(123456);
			Assert.assertEquals(Integer.valueOf(123456), ProtoManager.parseFrom(1, buffer));

			buffer = ProtoManager.toByteArray(10086L);
			Assert.assertEquals(Long.valueOf(10086), ProtoManager.parseFrom(1L, buffer));

			buffer = ProtoManager.toByteArray(10f);
			Assert.assertEquals(Float.valueOf(10f), ProtoManager.parseFrom(1F, buffer));

			buffer = ProtoManager.toByteArray(100d);
			Assert.assertEquals(Double.valueOf(100d), ProtoManager.parseFrom(1D, buffer));

			buffer = ProtoManager.toByteArray(true);
			Assert.assertEquals(Boolean.valueOf(true), ProtoManager.parseFrom(false, buffer));

			byte b = 8;
			buffer = ProtoManager.toByteArray(b);
			Assert.assertEquals(Byte.valueOf((byte) 8), ProtoManager.parseFrom((byte) 0, buffer));

			char c = 9;
			buffer = ProtoManager.toByteArray(c);
			Assert.assertEquals(Character.valueOf((char) 9), ProtoManager.parseFrom((char) 0, buffer));

			short s = 10;
			buffer = ProtoManager.toByteArray(s);
			Assert.assertEquals(Short.valueOf((short) 10), ProtoManager.parseFrom((short) 0, buffer));

			// 数组类型测试
			System.out.println("数组类型测试-----------------------------------------------------------------");
			buffer = ProtoManager.toByteArray(new String[] { "Lv.Mingwei", "Lv.Mingwei", "Lv.Mingwei" });
			Assert.assertTrue(Arrays.equals(new String[] { "Lv.Mingwei", "Lv.Mingwei", "Lv.Mingwei" },
					ProtoManager.parseFrom(new String[] {}, buffer)));

			buffer = ProtoManager.toByteArray(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });
			Assert.assertTrue(Arrays.equals(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
					ProtoManager.parseFrom(new int[] {}, buffer)));

			buffer = ProtoManager.toByteArray(new long[] { 10010, 10086L, 11000L, 110L, 119L });
			Assert.assertTrue(Arrays.equals(new long[] { 10010, 10086L, 11000L, 110L, 119L },
					ProtoManager.parseFrom(new long[] {}, buffer)));

			buffer = ProtoManager.toByteArray(new float[] { 10f, 11f, 12f, 13f / 14f, 15f, 16f, 17f, 18f });
			Assert.assertTrue(Arrays.equals(new float[] { 10f, 11f, 12f, 13f / 14f, 15f, 16f, 17f, 18f },
					ProtoManager.parseFrom(new float[] {}, buffer)));

			buffer = ProtoManager.toByteArray(new double[] { 100d, 101d, 102d, 103d, 104d, 105d });
			Assert.assertTrue(Arrays.equals(new double[] { 100d, 101d, 102d, 103d, 104d, 105d },
					ProtoManager.parseFrom(new double[] {}, buffer)));

			buffer = ProtoManager.toByteArray(new boolean[] { true, false, true, true, true, true, false });
			Assert.assertTrue(Arrays.equals(new boolean[] { true, false, true, true, true, true, false },
					ProtoManager.parseFrom(new boolean[] {}, buffer)));
			
			byte b1 = 1;
			byte b2 = 2;
			byte b3 = 3;
			byte b4 = 4;
			byte b5 = 5;
			buffer = ProtoManager.toByteArray(new byte[] { b1, b2, b3, b4, b5 });
			Assert.assertTrue(Arrays.equals(new byte[] { b1, b2, b3, b4, b5 },
					ProtoManager.parseFrom(new byte[] {}, buffer)));
			
			char c1 = 9;
			char c2 = 9;
			char c3 = 9;
			char c4 = 9;
			char c5 = 9;
			buffer = ProtoManager.toByteArray(new char[] { c1, c2, c3, c4, c5 });
			Assert.assertTrue(Arrays.equals(new char[] { c1, c2, c3, c4, c5 },
					ProtoManager.parseFrom(new char[] {}, buffer)));
			
			short s1 = 10;
			short s2 = 10;
			short s3 = 10;
			short s4 = 10;
			short s5 = 10;
			buffer = ProtoManager.toByteArray(new short[] { s1, s2, s3, s4, s5 });
			Assert.assertTrue(Arrays.equals(new short[] { s1, s2, s3, s4, s5 },
					ProtoManager.parseFrom(new short[] {}, buffer)));

			// List类型测试
			System.out.println("List类型测试-----------------------------------------------------------------");
			List<FullElementsBean> list = new ArrayList<FullElementsBean>();
			list.add(DataCreater.newFullElementsBean(true));
			list.add(DataCreater.newFullElementsBean(true));
			list.add(DataCreater.newFullElementsBean(true));
			list.add(DataCreater.newFullElementsBean(true));
			list.add(DataCreater.newFullElementsBean(true));
			buffer = ProtoManager.toByteArray(list, FullElementsBean.class);
			List<FullElementsBean> list2 = new ArrayList<FullElementsBean>();
			System.out.println(ProtoManager.parseFrom(buffer, list2, FullElementsBean.class).get(0).getStringTest());
			System.out.println(list2);

			// Map类型测试
			System.out.println("Map类型测试-----------------------------------------------------------------");
			Map<String, FullElementsBean> map = new HashMap<String, FullElementsBean>();
			map.put("A", DataCreater.newFullElementsBean(true));
			map.put("B", DataCreater.newFullElementsBean(true));
			map.put("C", DataCreater.newFullElementsBean(true));
			buffer = ProtoManager.toByteArray(map, String.class, FullElementsBean.class);
			System.out.println(ProtoManager.parseFrom(buffer, map, String.class, FullElementsBean.class));
		} catch (Exception e) {
			Assert.assertEquals(1, 2);
			e.printStackTrace();
		}
	}
}

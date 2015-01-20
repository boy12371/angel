//package test.com.feinno.util;
//
//import java.util.Collection;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//
//import antlr.collections.List;
//
//import com.feinno.util.CapacityLimiter;
//import com.feinno.util.EvictionListener;
//import com.feinno.util.LRUCacheManager;
//import com.feinno.util.Weigher;
//import com.feinno.util.Weighers;
//
//public class TestLRUCacheManager {
//
//	@Test
//	public void TestLRUCacheManager() {
//		LRUCacheManager.Builder<String, Integer> builderLRU = new LRUCacheManager.Builder<String, Integer>();
//		builderLRU = builderLRU.initialCapacity(5);
//		builderLRU = builderLRU.maximumWeightedCapacity(1);
//		builderLRU = builderLRU.concurrencyLevel(10);
//		builderLRU = builderLRU.weigher(Weighers.singleton());
//
//		LRUCacheManager<String, Integer> LRU = builderLRU.build();
//		LRU.setCapacity(11);
//		Assert.assertEquals(11, LRU.capacity());
//		LRU.put("test", 1);
//		Assert.assertFalse(LRU.isEmpty());
//		Assert.assertFalse(LRU.containsKey(1));
//		Assert.assertTrue(LRU.containsValue(1));
//		Assert.assertEquals(1, LRU.weightedSize());
//		Assert.assertNull(LRU.get(11));
//		LRU.clear();
//	}
//
//	@Test
//	public void TestLRUCacheManager2() {
//		LRUCacheManager.Builder<String, Integer> builderLRU = new LRUCacheManager.Builder<String, Integer>();
//		builderLRU = builderLRU.initialCapacity(5);
//		builderLRU = builderLRU.maximumWeightedCapacity(1);
//		builderLRU = builderLRU.concurrencyLevel(10);
//		builderLRU = builderLRU.weigher(TestWeigher.INSTANCE);
//		builderLRU = builderLRU.capacityLimiter(TestCapacityLimiter.INSTANCE);
//		builderLRU = builderLRU.listener(TestListener.INSTANCE);
//
//		LRUCacheManager<String, Integer> LRU = builderLRU.build();
//		LRU.setCapacity(11);
//		LRU.put("test", 1);
//		LRU.entrySet();
//		LRU.evictWith(TestCapacityLimiter.INSTANCE);
//		LRU.replace("test", 1);
//		LRU.replace("test", 1, 2);
//		LRU.remove("test");
//		LRU.remove("test", 1);
//		LRU.keySet();
//		LRU.values();
//		LRU.entrySet();
//		LRU.putIfAbsent("test", 10);
//
//	}
//
//	@Test
//	public void TestLRUCacheManagerException() {
//		try {
//			LRUCacheManager.Builder<String, Integer> builderLRU = new LRUCacheManager.Builder<String, Integer>();
//			builderLRU = builderLRU.initialCapacity(-1);
//			Assert.fail();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//			Assert.assertTrue(true);
//		}
//		try {
//			LRUCacheManager.Builder<String, Integer> builderLRU = new LRUCacheManager.Builder<String, Integer>();
//			builderLRU = builderLRU.maximumWeightedCapacity(-1);
//			Assert.fail();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//			Assert.assertTrue(true);
//		}
//		try {
//			LRUCacheManager.Builder<String, Integer> builderLRU = new LRUCacheManager.Builder<String, Integer>();
//			builderLRU = builderLRU.concurrencyLevel(-1);
//			Assert.fail();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//			Assert.assertTrue(true);
//		}
//	}
//
//	enum TestCapacityLimiter implements CapacityLimiter {
//		INSTANCE;
//		@Override
//		public boolean hasExceededCapacity(LRUCacheManager<?, ?> map) {
//			return map.weightedSize() > map.capacity();
//		}
//	}
//
//	enum TestWeigher implements Weigher<Integer> {
//		INSTANCE;
//
//		@SuppressWarnings("unused")
//		@Override
//		public int weightOf(Integer values) {
//			return values;
//		}
//
//	}
//
//	enum TestListener implements EvictionListener<String, Integer> {
//		INSTANCE;
//
//		@Override
//		public void onEviction(String key, Integer value) {
//		}
//	}
//}

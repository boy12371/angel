package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.ObjectBinder;

public class TestObjectBinder {
	@Test
	public void TestSet() {
		Object test = "test";
		ObjectBinder.set(test, "test", test);
	}

	@Test
	public void TestGet() {
		Object test = "test";
		Object testNull = null;
		Assert.assertEquals("test", ObjectBinder.get(test, "test"));
		Assert.assertEquals(null, ObjectBinder.get(testNull, null));
	}
	@Test
	public void TestRemove() {
		Object test = "test";
		ObjectBinder.remove(test);
	}
}

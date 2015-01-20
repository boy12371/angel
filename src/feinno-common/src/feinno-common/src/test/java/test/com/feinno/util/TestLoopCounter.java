package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.LoopCounter;

public class TestLoopCounter {
	@Test
	public void TestLoopCounter() {
		LoopCounter loop = new LoopCounter(10);
		loop.reset();
		Assert.assertEquals(1, loop.next());
	}
}

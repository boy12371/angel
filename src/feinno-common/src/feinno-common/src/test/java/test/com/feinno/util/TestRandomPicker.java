package test.com.feinno.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import com.feinno.util.RandomPicker;

public class TestRandomPicker {
	@Test
	public void TestRandomPicker() {
		List<String> list = new ArrayList<String>();
		RandomPicker rp1 = new RandomPicker(list);
		rp1.data();
		Assert.assertEquals(null, rp1.pickOne());
		for (int i = 0; i < 10; i++) {
			list.add("test"+i);
		}
		RandomPicker rp2 = new RandomPicker(list);
		System.out.println("RandomPicker pickOne():"+rp2.pickOne());
		System.out.println("RandomPicker date():"+rp2.data());
	}
}

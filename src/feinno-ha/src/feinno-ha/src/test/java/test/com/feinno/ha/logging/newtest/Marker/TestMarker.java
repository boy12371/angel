package test.com.feinno.ha.logging.newtest.Marker;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class TestMarker {

	@Test
	public void test() {
		IMarkerFactory markerFactory = MarkerFactory.getIMarkerFactory();
		String name = "nihao";
		Assert.assertTrue(!markerFactory.exists(name));
		Assert.assertEquals(name, MarkerFactory.getMarker(name).toString());
		Assert.assertTrue(markerFactory.exists(name));
		Assert.assertEquals(name, MarkerFactory.getMarker(name).toString());
	}

	public void testMarker() {
		for (int i = 0; i < 10000; i++) {
			createMarker(String.valueOf(i));
		}
		System.out.println("over");
	}

	public void createMarker(String name) {
		Marker marker = MarkerFactory.getMarker(name);
		System.out.println(marker);
	}

	public static void main(String args[]) {
		new TestMarker().testMarker();
	}

}

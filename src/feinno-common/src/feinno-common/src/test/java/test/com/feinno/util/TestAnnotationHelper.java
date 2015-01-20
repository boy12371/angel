package test.com.feinno.util;

import java.io.Console;
import java.io.Serializable;
import java.lang.reflect.Field;

import junit.framework.Assert;
import org.junit.Test;

import com.feinno.util.AnnotationHelper;

public class TestAnnotationHelper {
	public int test;

	@Deprecated
	@Test
	public void testAnnotation1() {

	}

	@Test
	public void TestGetAnnotation() throws Exception {
		Assert.assertEquals(null,
				AnnotationHelper.getAnnotation(String.class, String.class));
		Assert.assertEquals(null,
				AnnotationHelper.getAnnotation(TestAnnotationHelper.class,
						TestAnnotationHelper.class.getMethod(
								"TestGetAnnotation", null)));
		Assert.assertEquals(null, AnnotationHelper.getAnnotation(
				TestAnnotationHelper.class,
				TestAnnotationHelper.class.getField("test")));
		try {
			AnnotationHelper.getAnnotation(TestAnnotationHelper.class,
					TestAnnotationHelper.class.getMethod("testAnnotation1",
							null));
			Assert.fail();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			Assert.assertTrue(true);
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	@Test
	public void TestTryGetAnnotation() throws Exception {
		Assert.assertNotNull(AnnotationHelper.tryGetAnnotation(String.class));
		Assert.assertEquals(null,
				AnnotationHelper.tryGetAnnotation(Serializable.class));
		Assert.assertEquals(null,
				AnnotationHelper.tryGetAnnotation(Console.class));
		Assert.assertNotNull(AnnotationHelper.tryGetAnnotation(
				TestAnnotationHelper.class,
				TestAnnotationHelper.class.getField("test")));

	}
}

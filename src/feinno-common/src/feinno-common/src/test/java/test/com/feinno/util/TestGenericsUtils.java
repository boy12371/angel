package test.com.feinno.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import junit.framework.Assert;
import org.junit.Test;

import com.feinno.util.DictionaryList;
import com.feinno.util.GenericsUtils;
import com.mysql.jdbc.Field;

public class TestGenericsUtils {
	@Test
	public void TestGetSuperClassGenricType() {
		Assert.assertEquals("class java.lang.Object", GenericsUtils
				.getSuperClassGenricType(String.class).toString());
		Assert.assertEquals("class java.lang.Object", GenericsUtils
				.getSuperClassGenricType(ArrayList.class).toString());
		try {
			GenericsUtils.getSuperClassGenricType(ArrayList.class, 3);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(true);
		}
		try {
			GenericsUtils.getSuperClassGenricType(ArrayList.class, -3);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(true);
		}
	}

	@Test
	public void TestGetSuperClassGenricTypes() {
		Assert.assertEquals(null,
				GenericsUtils.getSuperClassGenricTypes(String.class));
		try {
			GenericsUtils.getSuperClassGenricTypes(ArrayList.class);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(true);
		}
	}
	@Test
	public void TestGetMethodGenericReturnType() {

			try {
				GenericsUtils.getMethodGenericReturnType(String.class.getMethod("length", null));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {
				GenericsUtils.getMethodGenericReturnType(DictionaryList.class.getMethod("keys", null), 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	@Test
	public void TestGetMethodGenericParameterTypes() {

			try {
				GenericsUtils.getMethodGenericParameterTypes(String.class.getMethod("length", null));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {
				GenericsUtils.getMethodGenericParameterTypes(DictionaryList.class.getMethod("keys", null), 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
}

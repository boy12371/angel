//package test.com.feinno.util.obsoleted;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//
//import com.feinno.util.obsoleted.AsyncResults;
//
//public class TestAsyncResults {
//
//	@Test
//	public void test(){
//		AsyncResults<String> result = new AsyncResults<String>(new RuntimeException(),"TestAsyncResults1");
//		result.error();
//		Assert.assertEquals(result.value(), "TestAsyncResults1");
//		result.setError(new RuntimeException());
//		result.setValue("TestAsyncResults2");
//		Assert.assertEquals(result.value(), "TestAsyncResults2");
//	}
//	
//}

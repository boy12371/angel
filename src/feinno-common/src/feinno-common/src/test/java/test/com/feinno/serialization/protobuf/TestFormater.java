package test.com.feinno.serialization.protobuf;

import org.junit.Test;

import com.feinno.serialization.protobuf.util.Formater;

/**
 * @author Administrator
 * 
 */
public class TestFormater {

	@Test
	public void testFormater() {
		Formater.formaError(new RuntimeException("Test Exception!"));
	}

}

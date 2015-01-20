package test.com.feinno.util;

import org.junit.Test;

import com.feinno.diagnostic.dumper.ObjectDumper;


public class ObjectDumperTest {
	
	@Test
	public void testDumper(){
		byte[] by = {'a','b','c'};
		String s = ObjectDumper.dumpString(by, "byte");
		System.out.println(s);
	}

}

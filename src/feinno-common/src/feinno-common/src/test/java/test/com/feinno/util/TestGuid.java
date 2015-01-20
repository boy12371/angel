package test.com.feinno.util;

import java.util.Arrays;
import java.util.UUID;
import junit.framework.Assert;
import org.junit.Test;
import com.feinno.util.Guid;

public class TestGuid {

	@Test
	public void TestRandomGuid() {
		Guid i = Guid.randomGuid();
		String s = i.toString();
		System.out.println(i.toString());
		
		byte[] byte1 = i.getData1();
		byte[] byte2 = i.getData2();
		System.out.println(Arrays.toString(byte1));
		System.out.println(Arrays.toString(byte2));
		byte[] byteTo = i.toByteArray();
		System.out.println(Arrays.toString(byteTo));
		System.out.println(i.toStr());

		Assert.assertEquals(false, i.equals(new String()));
		Assert.assertEquals(true, i.equals(i));
		Boolean bool = i.equals(Guid.randomGuid());
		System.out.println(bool);
		Guid.fromStr(null);

	}
}

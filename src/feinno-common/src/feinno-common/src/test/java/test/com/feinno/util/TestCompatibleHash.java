package test.com.feinno.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.feinno.util.CompatibleHash;

public class TestCompatibleHash {
	@Test
	public void TestCompatibleGetHashCode() {
		Assert.assertEquals(1, CompatibleHash.compatibleGetHashCode(1));
		Assert.assertEquals(1, CompatibleHash.compatibleGetHashCode((short) 1));
		System.out.println("\"test\" ompatibleGetHashCode:"
				+ CompatibleHash.compatibleGetHashCode("test"));
		System.out.println("Date ompatibleGetHashCode:"
				+ CompatibleHash.compatibleGetHashCode(new Date()));
		System.out.println("Object ompatibleGetHashCode:"
				+ CompatibleHash.compatibleGetHashCode(new Object()));
		System.out.println("byte ompatibleGetHashCode:"
				+ CompatibleHash.compatibleGetHashCode((byte)10));
		System.out.println("long ompatibleGetHashCode:"
				+ CompatibleHash.compatibleGetHashCode((long)10));

	}
}

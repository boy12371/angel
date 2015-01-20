package test.com.feinno.serialization.protobuf;

import com.feinno.serialization.protobuf.PBChecker;

public class TestPBChecker {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		PBChecker.check();
		PBChecker.check("com.feinno.imps.legacy");
	}

}

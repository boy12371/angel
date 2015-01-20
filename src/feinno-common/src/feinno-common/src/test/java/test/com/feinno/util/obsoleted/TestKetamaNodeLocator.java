package test.com.feinno.util.obsoleted;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feinno.util.obsoleted.KetamaNodeLocator;

public class TestKetamaNodeLocator {

	@Test
	public void test() {

		int VIRTUAL_NODE_COUNT = 10;
		List<String> ipList = new ArrayList<String>();
		ipList.add("192.168.0.1");
		ipList.add("192.168.0.2");
		ipList.add("192.168.0.3");
		ipList.add("192.168.0.4");
		ipList.add("192.168.0.5");
		KetamaNodeLocator<String> ketamaNodeLocator1 = new KetamaNodeLocator<String>(ipList, VIRTUAL_NODE_COUNT);

		String[] ipArray = new String[] { "192.168.0.1", "192.168.0.2", "192.168.0.3", "192.168.0.4", "192.168.0.5" };
		KetamaNodeLocator<String> ketamaNodeLocator2 = new KetamaNodeLocator<String>(ipArray, VIRTUAL_NODE_COUNT);
		System.out.println(ketamaNodeLocator1.getPrimary("AppServer"));
		System.out.println(ketamaNodeLocator2.getPrimary("SmsServer"));
	}

}

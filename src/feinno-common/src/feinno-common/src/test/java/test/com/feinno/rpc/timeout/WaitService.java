package test.com.feinno.rpc.timeout;

import java.util.Date;

import com.feinno.util.DateUtil;

public class WaitService implements IWaitService {

	@Override
	public boolean wait(int time) {
		System.out.println("    Receive  "
				+ DateUtil.formatDate(new Date(), DateUtil.DEFAULT_DATETIME_HYPHEN_FORMAT_LONG) + " wait " + time);
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
}

package test.com.feinno.ha.center.worker;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

public class FloatTest extends ConfigTableItem {

	@ConfigTableField(value = "F1", isKeyField = true)
	private int F1;

	@ConfigTableField("F2")
	private int F2;

	@ConfigTableField("F3")
	private long F3;

	public int getF1() {
		return F1;
	}

	public void setF1(int f1) {
		F1 = f1;
	}

	public int getF2() {
		return F2;
	}

	public void setF2(int f2) {
		F2 = f2;
	}

	public long getF3() {
		return F3;
	}

	public void setF3(long f3) {
		F3 = f3;
	}

}
package test.com.feinno.configuration;

import com.feinno.util.EnumInteger;

public enum SiteType implements EnumInteger{

	None(0), Peer(1), Base(2), GlobalAccess(3), GlobalBase(4), ServerAdapter(5);
	private int value;
	
	private SiteType(int value){
		this.value = value;
	}

	public int intValue() {
		return value;
	}
}
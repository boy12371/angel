package test.com.feinno.util;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.Guid;

public class GuidObject  extends ProtoEntity{

	@ProtoMember(1)
	private String str;
	
	@ProtoMember(2)
	private Guid guid;
	
	@ProtoMember(3)
	private int i;
	
	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	

	public Guid getGuid() {
		return guid;
	}

	public void setGuid(Guid guid) {
		this.guid = guid;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}
	
}

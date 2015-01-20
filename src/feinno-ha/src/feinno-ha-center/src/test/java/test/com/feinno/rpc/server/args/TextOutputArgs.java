package test.com.feinno.rpc.server.args;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class TextOutputArgs extends ProtoEntity{

	@ProtoMember(1)
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

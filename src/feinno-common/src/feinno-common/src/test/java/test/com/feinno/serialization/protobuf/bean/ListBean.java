package test.com.feinno.serialization.protobuf.bean;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ListBean extends ProtoEntity {

	@ProtoMember(11)
	private List<ProtoComboIntegerString> list_map;

	public List<ProtoComboIntegerString> getList_map() {
		return list_map;
	}

	public void setList_map(List<ProtoComboIntegerString> list_map) {
		this.list_map = list_map;
	}

}

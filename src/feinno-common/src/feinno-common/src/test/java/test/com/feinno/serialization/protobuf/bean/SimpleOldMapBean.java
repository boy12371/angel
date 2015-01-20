package test.com.feinno.serialization.protobuf.bean;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class SimpleOldMapBean extends ProtoEntity {

	@ProtoMember(1)
	private List<ProtoComboIntegerString> map_I_S_Obj;

	@ProtoMember(2)
	private List<ProtoComboStringInteger> map_S_I_Obj;

	@ProtoMember(3)
	private List<ProtoComboStringTable> map_S_T_Obj;

	@ProtoMember(4)
	private List<ProtoComboSinTestTable> map_SB_T_Obj;

	@ProtoMember(5)
	private List<ProtoComboStringPerson> map_S_P_Obj;

	public List<ProtoComboIntegerString> getMap_I_S_Obj() {
		return map_I_S_Obj;
	}

	public void setMap_I_S_Obj(List<ProtoComboIntegerString> map_I_S_Obj) {
		this.map_I_S_Obj = map_I_S_Obj;
	}

	public List<ProtoComboStringInteger> getMap_S_I_Obj() {
		return map_S_I_Obj;
	}

	public void setMap_S_I_Obj(List<ProtoComboStringInteger> map_S_I_Obj) {
		this.map_S_I_Obj = map_S_I_Obj;
	}

	public List<ProtoComboStringTable> getMap_S_T_Obj() {
		return map_S_T_Obj;
	}

	public void setMap_S_T_Obj(List<ProtoComboStringTable> map_S_T_Obj) {
		this.map_S_T_Obj = map_S_T_Obj;
	}

	public List<ProtoComboSinTestTable> getMap_SB_T_Obj() {
		return map_SB_T_Obj;
	}

	public void setMap_SB_T_Obj(List<ProtoComboSinTestTable> map_SB_T_Obj) {
		this.map_SB_T_Obj = map_SB_T_Obj;
	}

	public List<ProtoComboStringPerson> getMap_S_P_Obj() {
		return map_S_P_Obj;
	}

	public void setMap_S_P_Obj(List<ProtoComboStringPerson> map_S_P_Obj) {
		this.map_S_P_Obj = map_S_P_Obj;
	}

}

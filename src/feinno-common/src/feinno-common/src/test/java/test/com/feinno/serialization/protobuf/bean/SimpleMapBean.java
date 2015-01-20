package test.com.feinno.serialization.protobuf.bean;

import java.util.Map;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class SimpleMapBean extends ProtoEntity {

	@ProtoMember(1)
	private Map<Integer, String> map_I_S_Obj;

	@ProtoMember(2)
	private Map<String, Integer> map_S_I_Obj;

	@ProtoMember(3)
	private Map<String, Table> map_S_T_Obj;

	@ProtoMember(4)
	private Map<SinBeanTest, Table> map_SB_T_Obj;

	@ProtoMember(5)
	private Map<String, Person> map_S_P_Obj;

	@ProtoMember(7)
	private Map<Character, Long> map_C_L_Obj;

	@ProtoMember(8)
	private Map<String, String> map_S_S_Obj;

	@ProtoMember(9)
	private Map<String, Byte> map_S_B_Obj;

	public final Map<Integer, String> getMap_I_S_Obj() {
		return map_I_S_Obj;
	}

	public final void setMap_I_S_Obj(Map<Integer, String> map_I_S_Obj) {
		this.map_I_S_Obj = map_I_S_Obj;
	}

	public final Map<String, Integer> getMap_S_I_Obj() {
		return map_S_I_Obj;
	}

	public final void setMap_S_I_Obj(Map<String, Integer> map_S_I_Obj) {
		this.map_S_I_Obj = map_S_I_Obj;
	}

	public final Map<String, Table> getMap_S_T_Obj() {
		return map_S_T_Obj;
	}

	public final void setMap_S_T_Obj(Map<String, Table> map_S_T_Obj) {
		this.map_S_T_Obj = map_S_T_Obj;
	}

	public final Map<SinBeanTest, Table> getMap_SB_T_Obj() {
		return map_SB_T_Obj;
	}

	public final void setMap_SB_T_Obj(Map<SinBeanTest, Table> map_SB_T_Obj) {
		this.map_SB_T_Obj = map_SB_T_Obj;
	}

	public final Map<String, Person> getMap_S_P_Obj() {
		return map_S_P_Obj;
	}

	public final void setMap_S_P_Obj(Map<String, Person> map_S_P_Obj) {
		this.map_S_P_Obj = map_S_P_Obj;
	}

	public final Map<Character, Long> getMap_C_L_Obj() {
		return map_C_L_Obj;
	}

	public final void setMap_C_L_Obj(Map<Character, Long> map_C_L_Obj) {
		this.map_C_L_Obj = map_C_L_Obj;
	}

	public final Map<String, String> getMap_S_S_Obj() {
		return map_S_S_Obj;
	}

	public final void setMap_S_S_Obj(Map<String, String> map_S_S_Obj) {
		this.map_S_S_Obj = map_S_S_Obj;
	}

	public final Map<String, Byte> getMap_S_B_Obj() {
		return map_S_B_Obj;
	}

	public final void setMap_S_B_Obj(Map<String, Byte> map_S_B_Obj) {
		this.map_S_B_Obj = map_S_B_Obj;
	}

}

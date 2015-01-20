package test.com.feinno.serialization.protobuf.bean;

import java.util.Map;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class MapBean extends ProtoEntity {

	@ProtoMember(11)
	private Map<Integer, String> map_I_S;

	@ProtoMember(12)
	private MapBean mapBean;

	public Map<Integer, String> getMap_I_S() {
		return map_I_S;
	}

	public void setMap_I_S(Map<Integer, String> map_I_S) {
		this.map_I_S = map_I_S;
	}

	public MapBean getMapBean() {
		return mapBean;
	}

	public void setMapBean(MapBean mapBean) {
		this.mapBean = mapBean;
	}

}

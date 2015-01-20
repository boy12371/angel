package test.com.feinno.serialization.protobuf.descriptor;

import java.util.List;
import java.util.Map;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.serialization.protobuf.ProtoType;

public class OuterClass extends ProtoEntity {

	@ProtoMember(value = 1)
	private String name;

	@ProtoMember(value = 2)
	private int id;

	@ProtoMember(3)
	private String email;

	@ProtoMember(4)
	private InnerClass innerClass;

	@ProtoMember(5)
	private List<InnerClass> innerClassList;

	@ProtoMember(6)
	private EnumClass enumTest;

	@ProtoMember(7)
	private Map<EnumMapKeyClass, MapValueClass> mapTest;

	@ProtoMember(value = 8, type = ProtoType.FIXED32)
	private int fixed32IntTest;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public InnerClass getInnerClass() {
		return innerClass;
	}

	public void setInnerClass(InnerClass innerClass) {
		this.innerClass = innerClass;
	}

	public List<InnerClass> getInnerClassList() {
		return innerClassList;
	}

	public void setInnerClassList(List<InnerClass> innerClassList) {
		this.innerClassList = innerClassList;
	}

	public EnumClass getEnumTest() {
		return enumTest;
	}

	public void setEnumTest(EnumClass enumTest) {
		this.enumTest = enumTest;
	}

	public Map<EnumMapKeyClass, MapValueClass> getMapTest() {
		return mapTest;
	}

	public void setMapTest(Map<EnumMapKeyClass, MapValueClass> mapTest) {
		this.mapTest = mapTest;
	}

	public int getFixed32IntTest() {
		return fixed32IntTest;
	}

	public void setFixed32IntTest(int fixed32IntTest) {
		this.fixed32IntTest = fixed32IntTest;
	}

}

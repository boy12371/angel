package test.com.feinno.serialization.protobuf.bean;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.Flags;
import com.feinno.util.Guid;

public class SinBeanTest extends ProtoEntity {

	@ProtoMember(11)
	private byte b;

	@ProtoMember(12)
	private byte[] bytes;

	@ProtoMember(13)
	private Byte[] bytes_Obj;

	@ProtoMember(14)
	private List<Byte> bytes_List;

	@ProtoMember(21)
	private Guid guid;

	@ProtoMember(22)
	private List<Guid> guid_List;

	@ProtoMember(25)
	private List<Flags> flags_List_1;

	public final byte getB() {
		return b;
	}

	public final void setB(byte b) {
		this.b = b;
	}

	public final byte[] getBytes() {
		return bytes;
	}

	public final void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public final Byte[] getBytes_Obj() {
		return bytes_Obj;
	}

	public final void setBytes_Obj(Byte[] bytes_Obj) {
		this.bytes_Obj = bytes_Obj;
	}

	public final List<Byte> getBytes_List() {
		return bytes_List;
	}

	public final void setBytes_List(List<Byte> bytes_List) {
		this.bytes_List = bytes_List;
	}

	public final Guid getGuid() {
		return guid;
	}

	public final void setGuid(Guid guid) {
		this.guid = guid;
	}

	public final List<Guid> getGuid_List() {
		return guid_List;
	}

	public final void setGuid_List(List<Guid> guid_List) {
		this.guid_List = guid_List;
	}

	public final List<Flags> getFlags_List_1() {
		return flags_List_1;
	}

	public final void setFlags_List_1(List<Flags> flags_List_1) {
		this.flags_List_1 = flags_List_1;
	}

}

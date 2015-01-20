package test.com.feinno.serialization.protobuf.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.Flags;
import com.feinno.util.Guid;

public class SpecialFieldBean extends ProtoEntity {

	@ProtoMember(11)
	private Guid guid;

	@ProtoMember(12)
	private Guid[] guid_Array;

	@ProtoMember(13)
	private List<Guid> guid_List;

	@ProtoMember(14)
	private Map<Guid, SpecialFieldBean> guid_Map;

	@ProtoMember(31)
	private Date date;

	@ProtoMember(value = 32, timezone = "UTC")
	private Date[] date_Array;

	@ProtoMember(33)
	private List<Date> date_List;

	@ProtoMember(34)
	private Map<Date, SpecialFieldBean> date_Map;

	@ProtoMember(41)
	private Flags flags_1;

	@ProtoMember(42)
	private Flags<?> flags_2;

	@ProtoMember(43)
	private Flags[] flags_Array_1;

	@ProtoMember(44)
	private Flags<?>[] flags_Array_2;

	@ProtoMember(45)
	private List<Flags> flags_List_1;

	@ProtoMember(46)
	private List<Flags<?>> flags_List_2;

	@ProtoMember(47)
	private Map<Flags, SpecialFieldBean> flags_Map_1;

	@ProtoMember(48)
	private Map<Flags<?>, SpecialFieldBean> flags_Map_2;

	public Guid getGuid() {
		return guid;
	}

	public void setGuid(Guid guid) {
		this.guid = guid;
	}

	public Guid[] getGuid_Array() {
		return guid_Array;
	}

	public void setGuid_Array(Guid[] guid_Array) {
		this.guid_Array = guid_Array;
	}

	public List<Guid> getGuid_List() {
		return guid_List;
	}

	public void setGuid_List(List<Guid> guid_List) {
		this.guid_List = guid_List;
	}

	public Map<Guid, SpecialFieldBean> getGuid_Map() {
		return guid_Map;
	}

	public void setGuid_Map(Map<Guid, SpecialFieldBean> guid_Map) {
		this.guid_Map = guid_Map;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date[] getDate_Array() {
		return date_Array;
	}

	public void setDate_Array(Date[] date_Array) {
		this.date_Array = date_Array;
	}

	public List<Date> getDate_List() {
		return date_List;
	}

	public void setDate_List(List<Date> date_List) {
		this.date_List = date_List;
	}

	public Map<Date, SpecialFieldBean> getDate_Map() {
		return date_Map;
	}

	public void setDate_Map(Map<Date, SpecialFieldBean> date_Map) {
		this.date_Map = date_Map;
	}

	public Flags getFlags_1() {
		return flags_1;
	}

	public void setFlags_1(Flags flags_1) {
		this.flags_1 = flags_1;
	}

	public Flags<?> getFlags_2() {
		return flags_2;
	}

	public void setFlags_2(Flags<?> flags_2) {
		this.flags_2 = flags_2;
	}

	public Flags[] getFlags_Array_1() {
		return flags_Array_1;
	}

	public void setFlags_Array_1(Flags[] flags_Array_1) {
		this.flags_Array_1 = flags_Array_1;
	}

	public Flags<?>[] getFlags_Array_2() {
		return flags_Array_2;
	}

	public void setFlags_Array_2(Flags<?>[] flags_Array_2) {
		this.flags_Array_2 = flags_Array_2;
	}

	public List<Flags> getFlags_List_1() {
		return flags_List_1;
	}

	public void setFlags_List_1(List<Flags> flags_List_1) {
		this.flags_List_1 = flags_List_1;
	}

	public List<Flags<?>> getFlags_List_2() {
		return flags_List_2;
	}

	public void setFlags_List_2(List<Flags<?>> flags_List_2) {
		this.flags_List_2 = flags_List_2;
	}

	public Map<Flags, SpecialFieldBean> getFlags_Map_1() {
		return flags_Map_1;
	}

	public void setFlags_Map_1(Map<Flags, SpecialFieldBean> flags_Map_1) {
		this.flags_Map_1 = flags_Map_1;
	}

	public Map<Flags<?>, SpecialFieldBean> getFlags_Map_2() {
		return flags_Map_2;
	}

	public void setFlags_Map_2(Map<Flags<?>, SpecialFieldBean> flags_Map_2) {
		this.flags_Map_2 = flags_Map_2;
	}

}

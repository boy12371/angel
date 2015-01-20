package test.com.feinno.serialization.protobuf.bean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class BooleanTest extends ProtoEntity {

	@ProtoMember(1)
	private boolean isA;

	@ProtoMember(2)
	private boolean isb;

	@ProtoMember(3)
	private boolean Isc;

	@ProtoMember(4)
	private boolean ISD;

	@ProtoMember(5)
	private boolean isE;

	@ProtoMember(6)
	private boolean isFeinno;

	@ProtoMember(11)
	private Boolean isA_B;

	@ProtoMember(12)
	private Boolean isb_B;

	@ProtoMember(13)
	private Boolean Isc_B;

	@ProtoMember(14)
	private Boolean ISD_B;

	@ProtoMember(15)
	private Boolean isE_B;

	@ProtoMember(16)
	private Boolean isFeinno_B;

	public boolean isA() {
		return isA;
	}

	public void setA(boolean isA) {
		this.isA = isA;
	}

	public boolean isIsb() {
		return isb;
	}

	public void setIsb(boolean isb) {
		this.isb = isb;
	}

	public boolean isIsc() {
		return Isc;
	}

	public void setIsc(boolean isc) {
		Isc = isc;
	}

	public boolean isISD() {
		return ISD;
	}

	public void setISD(boolean iSD) {
		ISD = iSD;
	}

	public boolean isE() {
		return isE;
	}

	public void setE(boolean isE) {
		this.isE = isE;
	}

	public void setIsFeinno(boolean isFeinno) {
		this.isFeinno = isFeinno;
	}

	public boolean getIsFeinno() {
		return isFeinno;
	}

	public Boolean getIsA_B() {
		return isA_B;
	}

	public void setIsA_B(Boolean isA_B) {
		this.isA_B = isA_B;
	}

	public Boolean getIsb_B() {
		return isb_B;
	}

	public void setIsb_B(Boolean isb_B) {
		this.isb_B = isb_B;
	}

	public Boolean getIsc_B() {
		return Isc_B;
	}

	public void setIsc_B(Boolean isc_B) {
		Isc_B = isc_B;
	}

	public Boolean getISD_B() {
		return ISD_B;
	}

	public void setISD_B(Boolean iSD_B) {
		ISD_B = iSD_B;
	}

	public Boolean getIsE_B() {
		return isE_B;
	}

	public void setIsE_B(Boolean isE_B) {
		this.isE_B = isE_B;
	}

	public void setIsFeinno_B(Boolean isFeinno_B) {
		this.isFeinno_B = isFeinno_B;
	}

	public Boolean getIsFeinno_B() {
		return isFeinno_B;
	}
}

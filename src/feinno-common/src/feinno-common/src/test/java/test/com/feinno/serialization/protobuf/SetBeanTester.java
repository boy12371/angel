package test.com.feinno.serialization.protobuf;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import test.com.feinno.serialization.protobuf.bean.FullElementsBean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class SetBeanTester extends ProtoEntity {

	@ProtoMember(1)
	private Set<String> set_1;

	@ProtoMember(2)
	private Set<FullElementsBean> set_2;

	public Set<String> getSet_1() {
		return set_1;
	}

	public void setSet_1(Set<String> set_1) {
		this.set_1 = set_1;
	}

	public Set<FullElementsBean> getSet_2() {
		return set_2;
	}

	public void setSet_2(Set<FullElementsBean> set_2) {
		this.set_2 = set_2;
	}

	@Test
	public void test() {
		SetBeanTester setBean = new SetBeanTester();
		Set<String> s1 = new HashSet<String>();
		s1.add("A");
		s1.add("B");
		s1.add("1");
		s1.add("2");
		s1.add("1");
		setBean.setSet_1(s1);
		Set<FullElementsBean> s2 = new HashSet<FullElementsBean>();
		s2.add(DataCreater.newFullElementsBean(true));
		s2.add(DataCreater.newFullElementsBean(true));
		setBean.setSet_2(s2);
		byte[] buffer = setBean.toByteArray();
		System.out.println("Length: " + buffer.length);

		SetBeanTester resultSetBean = new SetBeanTester();
		resultSetBean.parseFrom(buffer);
		System.out.println(resultSetBean.getSet_1());
		System.out.println(resultSetBean.getSet_2());
		buffer = resultSetBean.toByteArray();
		System.out.println("Length: " + buffer.length);
		Assert.assertTrue(s1.equals(resultSetBean.getSet_1()));
	}

	public static void main(String args[]) throws Exception {
		new SetBeanTester().test();
		Thread.sleep(10000000);
	}
}

package test.com.feinno.serialization.protobuf.enhance;

import org.junit.Test;

import test.com.feinno.serialization.protobuf.DataCreater;
import test.com.feinno.serialization.protobuf.TestProtoEntityIsNullField.TestNullFieldBean;
import test.com.feinno.serialization.protobuf.bean.FullElementsBean;

import com.feinno.runtime.bytecode.type.ClassFile;
import com.feinno.serialization.protobuf.ProtoEntityEnhancer;

public class TestProtoEntityEnhancer {

	public static void main(String args[]) throws Exception {
		new TestProtoEntityEnhancer().test1();
	}

	@Test
	public void test1() throws Exception {
		ClassFile.valueOf(ProtoEntityEnhancer.toBytes(TestNullFieldBean.class));
		ProtoEntityEnhancer.getEnhanceProtoEntityClass(TestNullFieldBean.class).newInstance();
		FullElementsBean fullElementsBean = ProtoEntityEnhancer.getEnhanceProtoEntityClass(FullElementsBean.class)
				.newInstance();
		DataCreater.fillToFullElementsBean(fullElementsBean);
	}
}

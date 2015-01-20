package test.com.feinno.serialization.protobuf;

import junit.framework.Assert;

import org.junit.Test;

import test.com.feinno.serialization.protobuf.bean.FullElementsBean;

import com.feinno.serialization.protobuf.ProtoDebug;

public class TestProtoDebug {

	@Test
	public void test() {
		FullElementsBean fullElementsBean = ProtoDebug.testWriteAndMerage(DataCreater.newFullElementsBean(true));
		Assert.assertNotNull(fullElementsBean);
		System.out.println("OVER!");
	}
}

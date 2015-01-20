package test.com.feinno.serialization.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.serialization.protobuf.bean.FullElementsBean;

import com.feinno.serialization.protobuf.generator.ProtoCodeGeneratorException;

public class TestProtoEntity { 

	private static final Logger logger = LoggerFactory.getLogger(TestProtoEntity.class);

	@Test
	public void testWriteTo() {
		FullElementsBean protoEntity = DataCreater.newFullElementsBean(true);
		protoEntity.writeTo(new ByteArrayOutputStream());
	}

	@Test
	public void testToByteArray() {
		FullElementsBean protoEntity = DataCreater.newFullElementsBean(true);
		logger.info("FullElementsBean byte array:" + Arrays.toString(protoEntity.toByteArray()));
	}

	@Test
	public void testparseFromByByte() {
		FullElementsBean protoEntity = DataCreater.newFullElementsBean(true);
		byte[] buffer = protoEntity.toByteArray();
		FullElementsBean result = DataCreater.newFullElementsBean(false);
		result.parseFrom(buffer);
	}

	@Test
	public void testparseFromByStream() {
		FullElementsBean protoEntity = DataCreater.newFullElementsBean(true);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		protoEntity.writeTo(output);
		protoEntity.parseFrom(new ByteArrayInputStream(output.toByteArray()));
	}

	@Test
	public void testError() {
		boolean isTrue = false;
		FullElementsBean protoEntity = DataCreater.newFullElementsBean(true);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		protoEntity.writeTo(output);
		// failed 1
		try {
			protoEntity.parseFrom(new byte[output.toByteArray().length]);
		} catch (Exception e) {
			isTrue = true;
		}
		Assert.assertTrue(isTrue);
		isTrue = false;
		// failed 2
		try {
			byte[] buffer = new byte[output.toByteArray().length - 100];
			System.arraycopy(output.toByteArray(), 0, buffer, 0, buffer.length);
			protoEntity.parseFrom(buffer);
		} catch (Exception e) {
			isTrue = true;
		}
		Assert.assertTrue(isTrue);

		new ProtoCodeGeneratorException();
		ProtoCodeGeneratorException protoCodeGeneratorException = new ProtoCodeGeneratorException("test1");
		new ProtoCodeGeneratorException("test2", protoCodeGeneratorException);
	}

}

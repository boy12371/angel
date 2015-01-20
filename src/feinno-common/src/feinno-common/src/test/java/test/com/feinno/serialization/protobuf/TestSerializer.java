package test.com.feinno.serialization.protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.com.feinno.serialization.protobuf.bean.Table;

import com.feinno.serialization.Serializer;

public class TestSerializer {

	private static final Logger logger = LoggerFactory.getLogger(TestSerializer.class);

	@Test
	public void testDecode() {
		byte[] buffer;
		try {
			logger.info("start test Serializer. ");
			buffer = Serializer.encode(DataCreater.newTable(true));
			Serializer.decode(Table.class, buffer);
			
			Serializer.encode(DataCreater.newTable(true), new ByteArrayOutputStream());
			
			Serializer.decode(Table.class, new ByteArrayInputStream(buffer),buffer.length);
		} catch (IOException e) {
			logger.error("Serializer error! ", e);
			Assert.assertEquals(1, 2);
		}
	}
}

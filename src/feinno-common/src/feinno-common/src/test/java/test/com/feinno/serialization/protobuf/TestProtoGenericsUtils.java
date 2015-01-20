package test.com.feinno.serialization.protobuf;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.serialization.protobuf.util.ProtoGenericsUtils;

public class TestProtoGenericsUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(TestProtoGenericsUtils.class);

	@Test
	public void testGetPrimitive() {
		logger.info("Integer.class primitive {}",ProtoGenericsUtils.getPrimitive(Integer.class));
		logger.info("Long.class primitive {}",ProtoGenericsUtils.getPrimitive(Long.class));
		logger.info("Float.class primitive {}",ProtoGenericsUtils.getPrimitive(Float.class));
		logger.info("Double.class primitive {}",ProtoGenericsUtils.getPrimitive(Double.class));
		logger.info("Boolean.class primitive {}",ProtoGenericsUtils.getPrimitive(Boolean.class));
		logger.info("Byte.class primitive {}",ProtoGenericsUtils.getPrimitive(Byte.class));
		logger.info("Character.class primitive {}",ProtoGenericsUtils.getPrimitive(Character.class));
		logger.info("Short.class primitive {}",ProtoGenericsUtils.getPrimitive(Short.class));
	}

}

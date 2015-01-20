package test.com.feinno.serialization.protobuf;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.serialization.protobuf.util.ToolProvider;

public class TestToolProvider {

	private static final Logger logger = LoggerFactory.getLogger(TestToolProvider.class);

	@Test
	public void testGetSystemJavaCompiler() {
		logger.info("SystemJavaCompiler is {}", ToolProvider.getSystemJavaCompiler());
	}

	@Test
	public void testGetSystemToolClassLoader() {
		logger.info("SystemToolClassLoader is {}", ToolProvider.getSystemToolClassLoader());
	}
}

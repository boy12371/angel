package test.com.feinno.serialization.protobuf;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.serialization.protobuf.ByteString;

public class TestByteString {

	private static final Logger logger = LoggerFactory.getLogger(TestProtoEntity.class);

	@Test
	public void test() throws UnsupportedEncodingException {
		ByteString byteString = ByteString.copyFrom("Hello World. ".getBytes());
		ByteString byteStringTemp = ByteString.copyFrom("Hello World. ".getBytes());

		logger.info("ByteString body is [{}]", byteString.toStringUtf8());
		logger.info("ByteString hashCode is [{}]", byteString.hashCode());
		logger.info("ByteString equal is [{}]", byteString.equals(byteStringTemp));
		logger.info("ByteString copyFromUtf8 is [{}]", ByteString.copyFromUtf8("Hello World.").toString("UTF-8"));
		List<ByteString> list = new ArrayList<ByteString>();
		list.add(byteString);
		list.add(byteStringTemp);
		ByteString byteStringListTemp = ByteString.copyFrom(list);
		logger.info("ByteString list is [{}]", byteStringListTemp.toStringUtf8());
		
		byteString.isEmpty();
		ByteString.copyFrom(byteString.asReadOnlyByteBuffer(),10);
		ByteString.copyFrom(byteString.asReadOnlyByteBuffer());
		ByteString.copyFrom("Test","UTF-8");
//		byteString.copyTo(new byte[]{1,2,3},0);
//		byteString.copyTo(byteString.asReadOnlyByteBuffer());
		byteString.equals(byteString);
		byteString.equals("");
		byteString.equals(ByteString.copyFrom("Test","UTF-8"));
		byteString.newInput();
		byteString.newCodedInput();
		ByteString.newOutput(byteString.size());
		ByteString.newOutput();
	}

}

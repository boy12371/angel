package test.com.feinno.rpc.channel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.feinno.rpc.channel.RpcBinaryIdentity;
import com.feinno.rpc.channel.RpcBody;
import com.feinno.rpc.channel.RpcRequest;

public class TestRpcRequest {

	@Test
	public void test() {
		RpcRequest request = new RpcRequest();
		RpcBody body = new RpcBody("你好");
		request.setBody(body);

		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			request.writeToStream(outStream);

			ByteArrayInputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
			RpcBinaryIdentity idt = RpcBinaryIdentity.fromStream(inputStream);
			request = RpcRequest.fromBuffer(inputStream, idt);
			System.out.println(body.getValue().toString() + "=" + request.getBody().decode(String.class).toString());
			Assert.assertEquals(body.getValue().toString(), request.getBody().getValue().toString());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestRpcRequest().test();
	}

}

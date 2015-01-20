package test.com.feinno.rpc.channel;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;

public class TestRpcEndpoint {
	@Test
	public void testRpcTcpEnpdoint() {
		RpcTcpEndpoint ep;
		ep = (RpcTcpEndpoint)RpcEndpointFactory.parse("tcp://192.168.1.1:1234");
		Assert.assertEquals("/192.168.1.1", ep.getAddress().getAddress().toString());
		Assert.assertEquals(1234, ep.getAddress().getPort());
		
		ep = (RpcTcpEndpoint)RpcEndpointFactory.parse("tcp://192.168.1.1:1234;");
		Assert.assertEquals("/192.168.1.1", ep.getAddress().getAddress().toString());
		Assert.assertEquals(1234, ep.getAddress().getPort());

		ep = (RpcTcpEndpoint)RpcEndpointFactory.parse("tcp://192.168.1.1:1234/");
		Assert.assertEquals("/192.168.1.1", ep.getAddress().getAddress().toString());
		Assert.assertEquals(1234, ep.getAddress().getPort());

	}
	
	@Test
	public void testRpcTcpEnpdoint2() {
		RpcTcpEndpoint ep;
		ep = RpcTcpEndpoint.parse("tcp://192.168.1.1:1234");
		Assert.assertEquals("/192.168.1.1", ep.getAddress().getAddress().toString());
		Assert.assertEquals(1234, ep.getAddress().getPort());

		ep = RpcTcpEndpoint.parse("tcp://192.168.1.1:1234/");
		Assert.assertEquals("/192.168.1.1", ep.getAddress().getAddress().toString());
		Assert.assertEquals(1234, ep.getAddress().getPort());
		
		ep = RpcTcpEndpoint.parse("tcp://192.168.1.1:1234;");
		Assert.assertEquals("/192.168.1.1", ep.getAddress().getAddress().toString());
		Assert.assertEquals(1234, ep.getAddress().getPort());

	}
}

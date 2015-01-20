package test.com.feinno.rpc;

import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;

public class RpcTcpEndpointTest {
    @Test
    public void testParse(){
        RpcTcpEndpoint ep = RpcTcpEndpoint.parse("tcp://0:0:0:0:0:0:0:1%1:7001");
        Assert.assertEquals(new InetSocketAddress("0:0:0:0:0:0:0:1%1", 7001), ep.getAddress());
    }
}

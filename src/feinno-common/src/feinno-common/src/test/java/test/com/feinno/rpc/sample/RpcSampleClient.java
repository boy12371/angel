package test.com.feinno.rpc.sample;

import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcProxy;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.util.Action;

import test.com.feinno.rpc.sample.RpcSampleService.HelloArgs;
import test.com.feinno.rpc.sample.RpcSampleService.HelloResult;

public class RpcSampleClient {
	
	public static void main(String[] args)
	{
		RpcTcpEndpoint ep = RpcTcpEndpoint.parse("tcp://10.10.30.81:7008");
		RpcProxy proxy = RpcProxyFactory.getProxy(ep, "RpcSampleService");
		HelloArgs a = new HelloArgs();
		a.setStr("hello li");
		a.setBegin(0);
		a.setLen(5);
		for(int i=0;i<30;i++)
		{
			proxy.invoke("Hello", a, new Action<RpcResults>(){
	
				@Override
				public void run(RpcResults a) {
					if(a.getError()==null)
					{
						HelloResult r = a.getValue(HelloResult.class);
						System.out.println(r.getStr());
					}
					
					else
						System.out.println(a.getError().getMessage());
				}
				
			});
			if(i<20)
			{
				try {
					Thread.sleep(1000*10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					Thread.sleep(1000*60*5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

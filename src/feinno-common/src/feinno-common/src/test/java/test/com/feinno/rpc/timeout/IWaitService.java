package test.com.feinno.rpc.timeout;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

@RpcService("WaitService")
public interface IWaitService {

	@RpcMethod("wait")
	boolean wait(int time);

}

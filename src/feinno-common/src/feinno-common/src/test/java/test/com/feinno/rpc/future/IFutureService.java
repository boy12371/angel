package test.com.feinno.rpc.future;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

@RpcService("FutureService")
public interface IFutureService {

	@RpcMethod("method1")
	String method1(String args);

	@RpcMethod("exceptionMethod")
	String[] exceptionMethod(String[] args);

	@RpcMethod("timeOutMethod")
	String[] timeOutMethod(String[] args);

}

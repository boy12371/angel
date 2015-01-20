package test.com.feinno.rpc;


//public RpcFuture invoke(String method, Object args)
//{
//	return null;
//}
public class RpcFutureSample {
	// public static void main(String[] args) {
	// RpcProxy proxy =
	// RpcProxyFactory.getProxy(RpcEndpointFactory.parse("tcp://127.0.0.1:8000"),
	// "foo");
	// RpcFuture future = proxy.invoke("add", new Args(100, 100));
	//
	// //
	// // 1. 同步
	// ProtoInteger result = future.getResult();
	//
	// // 2. 异步
	// future.setListener(new Action<RpcFuture>() {
	// @Override
	// public void run(RpcFuture a) {
	// // TODO Auto-generated method stub
	// a.getError();
	// a.gerReults();
	// }
	// });
	//
	// // 3. Group
	// List<Future> futures = new ArrayList<Future>();
	// for (int i = 0; i < 10; i++) {
	// RpcProxy proxy =
	// RpcProxyFactory.getProxy(RpcEndpointFactory.parse("tcp://127.0.0.1:800" +
	// i), "foo");
	// RpcFuture future = proxy.invoke("add", new Args(100, 100));
	// futures.add(future);
	// }
	// //
	// // Sync
	// FutureGroup group = new FutureGroup(futures);
	// group.waitAll();
	//
	// //
	// // Async
	// group.setListener(WaitMode.WAIT_ALL, new Action<FutureGroup>() {
	// @Override
	// public void run(FutureGroup a) {
	// // TODO Auto-generated method stub
	// for (RpcFuture r: a.getFutures()) {
	// r.getResults();
	// }
	// }
	// });
	//
	// }
}
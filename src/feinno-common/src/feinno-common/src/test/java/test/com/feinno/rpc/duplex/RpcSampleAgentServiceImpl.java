package test.com.feinno.rpc.duplex;
/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-5-29
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
import java.util.Hashtable;
import java.util.Map;

import com.feinno.rpc.duplex.RpcDuplexClientAgent;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class RpcSampleAgentServiceImpl extends RpcServiceBase
{
	public static final RpcSampleAgentServiceImpl INSTANCE = new RpcSampleAgentServiceImpl();
	
	private Map<String, RpcDuplexClientAgent> agents;
	
	private RpcSampleAgentServiceImpl()
	{
		super("RpcSampleAgentService");
		agents = new Hashtable<String, RpcDuplexClientAgent>();
	}
	
	@RpcMethod("Register")
	public void register(RpcServerContext ctx)
	{
		ProtoString args = ctx.getArgs(ProtoString.class);
		RpcDuplexClientAgent agent = new RpcDuplexClientAgent(ctx);
		agent.setContext("Name", args.getValue());
		agents.put(args.getValue(), agent);
		ctx.getConnection().setAttachment(agent);
		ctx.end();
	}
	
	@RpcMethod("TestCallback")
	public void testCallback(RpcServerContext ctx)
	{
		RpcDuplexClientAgent agent = (RpcDuplexClientAgent)ctx.getConnection().getAttachment();
		RpcSampleAgentCallbackService service = agent.getService(RpcSampleAgentCallbackService.class);
		service.test(new ProtoString((String)agent.getContext("Name")));
		ctx.end();
	}
}

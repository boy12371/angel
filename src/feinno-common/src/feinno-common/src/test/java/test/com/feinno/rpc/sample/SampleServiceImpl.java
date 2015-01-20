/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-15
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.sample;

import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class SampleServiceImpl implements SampleService 
{
	@Override
	public ProtoInteger add(SampleService.AddArgs args)
	{
		int r = args.getA() + args.getB();
		System.out.printf("*** %d + %d = %d\n", args.getA(), args.getB(), r);
		return new ProtoInteger(r);
	}

	@Override
	public ProtoString echo(ProtoString args)
	{
		return args;
	}
}

/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.servermock;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * AddInputArgs
 * 
 * @author 李会军
 */
public class AddInputArgs extends ProtoEntity
{
	@ProtoMember(1)
	private int x;

	@ProtoMember(2)
	private int y;

	public int getX()
	{
		return x;
	}

	public AddInputArgs setX(int value)
	{
		x = value;
		return this;
	}

	public int getY()
	{
		return y;
	}

	public AddInputArgs setY(int value)
	{
		y = value;
		return this;
	}
}

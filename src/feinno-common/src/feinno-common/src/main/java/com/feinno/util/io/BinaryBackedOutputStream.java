/*
 * FAE, Feinno App Engine
 *  
 * Create by windcraft Aug 16, 2011
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * 将ByteBuffer转换为OutputStream
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class BinaryBackedOutputStream extends OutputStream
{
	private ByteBuffer buf;

	public BinaryBackedOutputStream(ByteBuffer buf)
	{
		this.buf = buf;
	}

	public synchronized void write(int b) throws IOException
	{
		buf.put((byte) b);
	}

	public synchronized void write(byte[] bytes, int off, int len) throws IOException
	{
		buf.put(bytes, off, len);
	}
}

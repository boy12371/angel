/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-6-3
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.context.NullContext;
import com.feinno.appengine.http.HttpAppBean;
import com.feinno.appengine.http.HttpAppTx;
import com.feinno.appengine.http.HttpPrefix;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@AppName(category="sample", name="Sample")
@HttpPrefix("/foo.aspx")
public class SampleHttpAppBean extends HttpAppBean<NullContext>
{
	@Override
	public void process(HttpAppTx<NullContext> tx) throws Exception
	{
		String queryString = tx.getRequest().getQueryString();
		tx.getResponse().getOutputStream().print("Hello" + queryString);
		tx.end();
	}

	@Override
	public void setup() throws Exception
	{}

	@Override
	public void load() throws Exception
	{
	}

	@Override
	public void unload() throws Exception
	{
	}

}

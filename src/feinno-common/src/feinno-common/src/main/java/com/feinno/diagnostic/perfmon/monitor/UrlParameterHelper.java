package com.feinno.diagnostic.perfmon.monitor;

import java.util.HashMap;

/**
 * 解析url参数
 * @author zhouyang
 *
 */
public class UrlParameterHelper
{
	public static HashMap<String, String> getArgs(String url)
	{
		HashMap<String, String> result = new HashMap<String, String>();
		if (url.contains("?"))
		{
			String strs[] = url.split("\\?");
			String[] args = strs[1].split("&");
			for (String arg : args)
			{
				if (arg.contains("="))
				{
					String[] params = arg.split("=");
					if (params.length == 2)
					{
						result.put(params[0], params[1]);
					}
				}
			}
		}
		return result;
	}
}

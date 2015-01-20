package com.feinno.appengine.route.gray.funcs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.feinno.appengine.route.gray.OperandType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.util.StringUtils;

class Package extends FuncBase
{
	private static final String GRAY_PACKAGE_ROOT = "AppEngine.GrayPackage.";
	private Set<String> set;
	
	public Package(List<String> args, OperandType type) throws ConfigurationException
	{
		super(args, type);
		
		String path = GRAY_PACKAGE_ROOT + args.get(0);
		
		String txt = ConfigurationManager.loadText(path, null, new ConfigUpdateAction<String>() {
			@Override
			public void run(String txt) throws Exception
			{
				set = parsePackage(txt);
			}
		});
		
		set = parsePackage(txt);
	}

	private static Set<String> parsePackage(String txt)
	{
		Set<String> set = new HashSet<String>();
		for (String s: txt.split("\\s+")) {
			if (StringUtils.isNullOrEmpty(s))
				continue;
			if (!set.contains(s))
				set.add(s);	
		}
		return set;
	}

	@Override
	public boolean apply(String fieldValue)
	{
		return set.contains(fieldValue);
	}
}

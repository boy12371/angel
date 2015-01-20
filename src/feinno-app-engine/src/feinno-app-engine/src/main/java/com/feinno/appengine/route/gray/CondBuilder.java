package com.feinno.appengine.route.gray;

import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.route.gray.ConditionParser.cond_return;
import com.feinno.appengine.route.gray.conds.Cond;
import com.feinno.appengine.route.gray.conds.FuncCond;
import com.feinno.appengine.route.gray.funcs.FuncBase;
import com.feinno.appengine.route.gray.funcs.FuncFactory;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;

public class CondBuilder
{
	private static Logger LOGGER = LoggerFactory.getLogger(CondBuilder.class);
	
	private static final String NEWLINE = System.getProperty("line.separator");

	public static Cond parse(String expression) throws ParserException
	{
		ANTLRStringStream input = new ANTLRStringStream(expression);
		ConditionLexer lexer = new ConditionLexer(input);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		ConditionParser parser = new ConditionParser(tokenStream);

		try {
			cond_return r = parser.cond();
			StringBuffer sb = new StringBuffer();
			List<String> errors = parser.getErrors();
			if (errors.size() > 0) {
				for (String msg : errors) {
					sb.append(msg).append(NEWLINE);
				}
				throw new ParserException(sb.toString());
			}
			return r.c;
		} catch (Exception e) {
			throw new ParserException("Parse failed:" + expression, e);
		}
	}
	
	static Cond buidlCond(String funcName, String fieldName, List<String> args, OperandType opType)
	{
		try {
			FuncBase func = FuncFactory.createFunc(funcName, args, opType);
			return new FuncCond(fieldName, func);
		} catch (Exception ex) {
			LOGGER.error("build cond failed: funcName {} {}", funcName, ex);
			throw new IllegalArgumentException(ex);
		}
	}
	
//	public static void main(String args[]) throws Exception{
//		ConfigurationManager.setConfigurator(new LocalConfigurator());
//		CondBuilder.parse("sid.package(\"fae_sids\")");
//	}
}

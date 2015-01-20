package com.feinno.ha.util.thresholds;

import java.util.HashMap;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.BaseTree;

import com.feinno.ha.util.thresholds.ThresholdsParser.program_return;

public class ThresholdsBuilder {

	public static HashMap<String, ThresholdExpression> parse(String expression)
			throws RecognitionException, ParserThresholdsException {
		ANTLRStringStream input = new ANTLRStringStream(expression);
		ThresholdsLexer lexer = new ThresholdsLexer(input);
		CommonTokenStream tokenStream = new CommonTokenStream(lexer);
		// System.out.println(expression);
		// tokenStream.fill();
		// List tlst = tokenStream.getTokens();
		// for (int i = 0; i < tlst.size(); i++) {
		// System.out.println(tlst.get(i));
		// }
		ThresholdsParser parser = new ThresholdsParser(tokenStream);

		program_return ret = parser.program();

		StringBuffer sb = new StringBuffer();
		List<String> errors = parser.getErrors();
		if (errors.size() > 0) {
			for (String msg : errors) {
				sb.append(msg).append(NEWLINE);
			}
			throw new ParserThresholdsException(sb.toString());
		}

		BaseTree bt = (BaseTree) ret.getTree();
		HashMap<String, ThresholdExpression> map = new HashMap<String, ThresholdExpression>();
		fillMap(map, bt);
		// System.out.println(map.size());
		// System.out.println(bt.isNil());
		// System.out.println(bt.getType());
		// List<BaseTree> lst = bt.getChildren();
		// System.out.println(bt.toStringTree());
		// HashMap<String, ThresholdExpression> map = new HashMap<String,
		// ThresholdExpression>();
		// for (int i = 0; i < lst.size(); i++) {
		// BaseTree subbt = lst.get(i);
		// System.out.println(subbt.isNil());
		// System.out.println(subbt.getType());
		// List<BaseTree> subLst = subbt.getChildren();
		// String symbols = subbt.getText();
		// ThresholdExpression exp = new ThresholdExpression();
		// exp.setSymbols(toSymbols(symbols));
		// if (subLst.size() == 2) {
		// exp.setLable(subLst.get(0).getText());
		// exp.setValue(Float.valueOf(subLst.get(1).getText()));
		// }
		// map.put(exp.getLable().trim(), exp);
		// }
		return map;
	}

	private static void fillMap(HashMap<String, ThresholdExpression> map,
			BaseTree baseTree) {
		if (baseTree.isNil()) {
			List<BaseTree> lst = baseTree.getChildren();
			for (BaseTree bt : lst) {
				fillMap(map, bt);
			}
		} else {
			List<BaseTree> subLst = baseTree.getChildren();
			if (subLst.size() == 2) {
				ThresholdExpression exp = new ThresholdExpression();
				exp.setSymbols(toSymbols(baseTree.getText()));
				exp.setLable(subLst.get(0).getText());
				exp.setValue(Float.valueOf(subLst.get(1).getText()));
				map.put(exp.getLable().replaceAll("\"", ""), exp);
			}
		}
	}

	private static LogicSymbolsType toSymbols(String text) {
		if (text.equals("<")) {
			return LogicSymbolsType.LESS_THAN;
		} else if (text.equals("<=")) {
			return LogicSymbolsType.LESS_THAN_OR_EQUALT;
		} else if (text.equals(">")) {
			return LogicSymbolsType.GREATER_THAN;
		} else if (text.equals(">=")) {
			return LogicSymbolsType.GREATER_THAN_OR_EQUALT;
		} else {
			return LogicSymbolsType.NONE;
		}
	}

	private static final String NEWLINE = System.getProperty("line.separator");

}

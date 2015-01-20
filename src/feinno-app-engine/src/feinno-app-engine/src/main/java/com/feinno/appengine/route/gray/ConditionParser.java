// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g 2011-06-14 16:57:09

package com.feinno.appengine.route.gray;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.IntStream;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.Parser;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

import com.feinno.appengine.route.gray.conds.AndCond;
import com.feinno.appengine.route.gray.conds.Cond;
import com.feinno.appengine.route.gray.conds.NotCond;
import com.feinno.appengine.route.gray.conds.OrCond;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class ConditionParser extends Parser
{
	public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ID", "STRING",
			"INT", "FLOAT", "EXPONENT", "COMMENT", "WS", "ESC_SEQ", "HEX_DIGIT", "UNICODE_ESC", "OCTAL_ESC", "'and'",
			"'or'", "'('", "')'", "'!'", "'.'", "','" };
	public static final int EOF = -1;
	public static final int T__15 = 15;
	public static final int T__16 = 16;
	public static final int T__17 = 17;
	public static final int T__18 = 18;
	public static final int T__19 = 19;
	public static final int T__20 = 20;
	public static final int T__21 = 21;
	public static final int ID = 4;
	public static final int STRING = 5;
	public static final int INT = 6;
	public static final int FLOAT = 7;
	public static final int EXPONENT = 8;
	public static final int COMMENT = 9;
	public static final int WS = 10;
	public static final int ESC_SEQ = 11;
	public static final int HEX_DIGIT = 12;
	public static final int UNICODE_ESC = 13;
	public static final int OCTAL_ESC = 14;

	// delegates
	// delegators

	public ConditionParser(TokenStream input)
	{
		this(input, new RecognizerSharedState());
	}

	public ConditionParser(TokenStream input, RecognizerSharedState state)
	{
		super(input, state);

	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor)
	{
		this.adaptor = adaptor;
	}

	public TreeAdaptor getTreeAdaptor()
	{
		return adaptor;
	}

	public String[] getTokenNames()
	{
		return ConditionParser.tokenNames;
	}

	public String getGrammarFileName()
	{
		return "/home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g";
	}

	@Override
	protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException
	{
		throw new MismatchedTokenException(ttype, input);
	}

	@Override
	public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException
	{
		throw e;
	}

	private List<String> errors = new java.util.ArrayList<String>();

	public void displayRecognitionError(String[] tokenNames, RecognitionException e)
	{
		String hdr = getErrorHeader(e);
		String msg = getErrorMessage(e, tokenNames);
		errors.add(hdr + " " + msg);
	}

	public List<String> getErrors()
	{
		return errors;
	}

	public static class cond_return extends ParserRuleReturnScope
	{
		public Cond c;
		Object tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "cond"
	// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:65:8:
	// public cond returns [Cond c] : expr EOF ;
	public final ConditionParser.cond_return cond() throws RecognitionException
	{
		ConditionParser.cond_return retval = new ConditionParser.cond_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token EOF2 = null;
		ConditionParser.expr_return expr1 = null;

		Object EOF2_tree = null;

		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:66:3:
			// ( expr EOF )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:66:5:
			// expr EOF
			{
				root_0 = (Object) adaptor.nil();

				pushFollow(FOLLOW_expr_in_cond87);
				expr1 = expr();

				state._fsp--;

				adaptor.addChild(root_0, expr1.getTree());
				EOF2 = (Token) match(input, EOF, FOLLOW_EOF_in_cond89);
				retval.c = (expr1 != null ? expr1.c : null);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "cond"

	public static class expr_return extends ParserRuleReturnScope
	{
		public Cond c;
		Object tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "expr"
	// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:69:1:
	// expr returns [Cond c] : ( func | unary | grp ) ( (a= 'and' | a= 'or' ) r=
	// expr )* ;
	public final ConditionParser.expr_return expr() throws RecognitionException
	{
		ConditionParser.expr_return retval = new ConditionParser.expr_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token a = null;
		ConditionParser.expr_return r = null;

		ConditionParser.func_return func3 = null;

		ConditionParser.unary_return unary4 = null;

		ConditionParser.grp_return grp5 = null;

		Object a_tree = null;

		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:70:3:
			// ( ( func | unary | grp ) ( (a= 'and' | a= 'or' ) r= expr )* )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:70:5:
			// ( func | unary | grp ) ( (a= 'and' | a= 'or' ) r= expr )*
			{
				root_0 = (Object) adaptor.nil();

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:70:5:
				// ( func | unary | grp )
				int alt1 = 3;
				switch (input.LA(1)) {
				case ID: {
					alt1 = 1;
				}
					break;
				case 19: {
					alt1 = 2;
				}
					break;
				case 17: {
					alt1 = 3;
				}
					break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 1, 0, input);

					throw nvae;
				}

				switch (alt1) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:70:6:
					// func
				{
					pushFollow(FOLLOW_func_in_expr110);
					func3 = func();

					state._fsp--;

					adaptor.addChild(root_0, func3.getTree());
					retval.c = (func3 != null ? func3.c : null);

				}
					break;
				case 2:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:71:7:
					// unary
				{
					pushFollow(FOLLOW_unary_in_expr121);
					unary4 = unary();

					state._fsp--;

					adaptor.addChild(root_0, unary4.getTree());
					retval.c = (unary4 != null ? unary4.c : null);

				}
					break;
				case 3:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:72:7:
					// grp
				{
					pushFollow(FOLLOW_grp_in_expr132);
					grp5 = grp();

					state._fsp--;

					adaptor.addChild(root_0, grp5.getTree());
					retval.c = (grp5 != null ? grp5.c : null);

				}
					break;

				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:73:5:
				// ( (a= 'and' | a= 'or' ) r= expr )*
				loop3: do {
					int alt3 = 2;
					int LA3_0 = input.LA(1);

					if ((LA3_0 == 15)) {
						alt3 = 1;
					} else if ((LA3_0 == 16)) {
						alt3 = 1;
					}

					switch (alt3) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:73:6:
						// (a= 'and' | a= 'or' ) r= expr
					{
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:73:6:
						// (a= 'and' | a= 'or' )
						int alt2 = 2;
						int LA2_0 = input.LA(1);

						if ((LA2_0 == 15)) {
							alt2 = 1;
						} else if ((LA2_0 == 16)) {
							alt2 = 2;
						} else {
							NoViableAltException nvae = new NoViableAltException("", 2, 0, input);

							throw nvae;
						}
						switch (alt2) {
						case 1:
							// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:73:7:
							// a= 'and'
						{
							a = (Token) match(input, 15, FOLLOW_15_in_expr146);
							a_tree = (Object) adaptor.create(a);
							root_0 = (Object) adaptor.becomeRoot(a_tree, root_0);

						}
							break;
						case 2:
							// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:73:18:
							// a= 'or'
						{
							a = (Token) match(input, 16, FOLLOW_16_in_expr153);
							a_tree = (Object) adaptor.create(a);
							root_0 = (Object) adaptor.becomeRoot(a_tree, root_0);

						}
							break;

						}

						pushFollow(FOLLOW_expr_in_expr166);
						r = expr();

						state._fsp--;

						adaptor.addChild(root_0, r.getTree());

						if ((a != null ? a.getText() : null).equals("and")) {
							retval.c = new AndCond(retval.c, (r != null ? r.c : null));
						} else {
							retval.c = new OrCond(retval.c, (r != null ? r.c : null));
						}

					}
						break;

					default:
						break loop3;
					}
				} while (true);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "expr"

	public static class grp_return extends ParserRuleReturnScope
	{
		public Cond c;
		Object tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "grp"
	// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:85:1:
	// grp returns [Cond c] : '(' expr ')' ;
	public final ConditionParser.grp_return grp() throws RecognitionException
	{
		ConditionParser.grp_return retval = new ConditionParser.grp_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal6 = null;
		Token char_literal8 = null;
		ConditionParser.expr_return expr7 = null;

		Object char_literal6_tree = null;
		Object char_literal8_tree = null;

		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:86:3:
			// ( '(' expr ')' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:86:5:
			// '(' expr ')'
			{
				root_0 = (Object) adaptor.nil();

				char_literal6 = (Token) match(input, 17, FOLLOW_17_in_grp204);
				pushFollow(FOLLOW_expr_in_grp207);
				expr7 = expr();

				state._fsp--;

				adaptor.addChild(root_0, expr7.getTree());
				char_literal8 = (Token) match(input, 18, FOLLOW_18_in_grp209);
				retval.c = (expr7 != null ? expr7.c : null);

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "grp"

	public static class unary_return extends ParserRuleReturnScope
	{
		public Cond c;
		Object tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "unary"
	// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:89:1:
	// unary returns [Cond c] : '!' expr ;
	public final ConditionParser.unary_return unary() throws RecognitionException
	{
		ConditionParser.unary_return retval = new ConditionParser.unary_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal9 = null;
		ConditionParser.expr_return expr10 = null;

		Object char_literal9_tree = null;

		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:90:3:
			// ( '!' expr )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:90:5:
			// '!' expr
			{
				root_0 = (Object) adaptor.nil();

				char_literal9 = (Token) match(input, 19, FOLLOW_19_in_unary232);
				char_literal9_tree = (Object) adaptor.create(char_literal9);
				root_0 = (Object) adaptor.becomeRoot(char_literal9_tree, root_0);

				pushFollow(FOLLOW_expr_in_unary235);
				expr10 = expr();

				state._fsp--;

				adaptor.addChild(root_0, expr10.getTree());
				retval.c = new NotCond((expr10 != null ? expr10.c : null));

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "unary"

	public static class func_return extends ParserRuleReturnScope
	{
		public Cond c;
		Object tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "func"
	// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:93:1:
	// func returns [Cond c] : (field= ID '.' )? name= ID '(' args ')' ;
	public final ConditionParser.func_return func() throws RecognitionException
	{
		ConditionParser.func_return retval = new ConditionParser.func_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token field = null;
		Token name = null;
		Token char_literal11 = null;
		Token char_literal12 = null;
		Token char_literal14 = null;
		ConditionParser.args_return args13 = null;

		Object field_tree = null;
		Object name_tree = null;
		Object char_literal11_tree = null;
		Object char_literal12_tree = null;
		Object char_literal14_tree = null;

		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:94:3:
			// ( (field= ID '.' )? name= ID '(' args ')' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:94:5:
			// (field= ID '.' )? name= ID '(' args ')'
			{
				root_0 = (Object) adaptor.nil();

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:94:5:
				// (field= ID '.' )?
				int alt4 = 2;
				int LA4_0 = input.LA(1);

				if ((LA4_0 == ID)) {
					int LA4_1 = input.LA(2);

					if ((LA4_1 == 20)) {
						alt4 = 1;
					}
				}
				switch (alt4) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:94:6:
					// field= ID '.'
				{
					field = (Token) match(input, ID, FOLLOW_ID_in_func262);
					field_tree = (Object) adaptor.create(field);
					adaptor.addChild(root_0, field_tree);

					char_literal11 = (Token) match(input, 20, FOLLOW_20_in_func264);

				}
					break;

				}

				name = (Token) match(input, ID, FOLLOW_ID_in_func271);
				name_tree = (Object) adaptor.create(name);
				root_0 = (Object) adaptor.becomeRoot(name_tree, root_0);

				char_literal12 = (Token) match(input, 17, FOLLOW_17_in_func274);
				pushFollow(FOLLOW_args_in_func277);
				args13 = args();

				state._fsp--;

				adaptor.addChild(root_0, args13.getTree());
				char_literal14 = (Token) match(input, 18, FOLLOW_18_in_func279);
				retval.c = CondBuilder.buidlCond((name != null ? name.getText() : null),
						(field != null ? field.getText() : null), (args13 != null ? args13.vals : null),
						(args13 != null ? args13.type : null));

			}

			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "func"

	public static class args_return extends ParserRuleReturnScope
	{
		public java.util.List<String> vals;
		public OperandType type;
		Object tree;

		public Object getTree()
		{
			return tree;
		}
	};

	// $ANTLR start "args"
	// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:98:1:
	// args returns [java.util.List<String> vals, OperandType type] : (a+=
	// STRING ( ',' a+= STRING )* | a+= INT ( ',' a+= INT )* | a+= FLOAT ( ','
	// a+= FLOAT )* );
	public final ConditionParser.args_return args() throws RecognitionException
	{
		ConditionParser.args_return retval = new ConditionParser.args_return();
		retval.start = input.LT(1);

		Object root_0 = null;

		Token char_literal15 = null;
		Token char_literal16 = null;
		Token char_literal17 = null;
		Token a = null;
		List list_a = null;

		Object char_literal15_tree = null;
		Object char_literal16_tree = null;
		Object char_literal17_tree = null;
		Object a_tree = null;

		java.util.List<CommonToken> tokens = new ArrayList<CommonToken>();
		retval.vals = new java.util.ArrayList<String>();

		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:108:2:
			// (a+= STRING ( ',' a+= STRING )* | a+= INT ( ',' a+= INT )* | a+=
			// FLOAT ( ',' a+= FLOAT )* )
			int alt8 = 3;
			switch (input.LA(1)) {
			case STRING: {
				alt8 = 1;
			}
				break;
			case INT: {
				alt8 = 2;
			}
				break;
			case FLOAT: {
				alt8 = 3;
			}
				break;
			default:
				NoViableAltException nvae = new NoViableAltException("", 8, 0, input);

				throw nvae;
			}

			switch (alt8) {
			case 1:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:108:4:
				// a+= STRING ( ',' a+= STRING )*
			{
				root_0 = (Object) adaptor.nil();

				a = (Token) match(input, STRING, FOLLOW_STRING_in_args326);
				a_tree = (Object) adaptor.create(a);
				adaptor.addChild(root_0, a_tree);

				if (list_a == null)
					list_a = new ArrayList();
				list_a.add(a);

				retval.type = OperandType.STRING;
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:108:44:
				// ( ',' a+= STRING )*
				loop5: do {
					int alt5 = 2;
					int LA5_0 = input.LA(1);

					if ((LA5_0 == 21)) {
						alt5 = 1;
					}

					switch (alt5) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:108:45:
						// ',' a+= STRING
					{
						char_literal15 = (Token) match(input, 21, FOLLOW_21_in_args331);
						char_literal15_tree = (Object) adaptor.create(char_literal15);
						adaptor.addChild(root_0, char_literal15_tree);

						a = (Token) match(input, STRING, FOLLOW_STRING_in_args335);
						a_tree = (Object) adaptor.create(a);
						adaptor.addChild(root_0, a_tree);

						if (list_a == null)
							list_a = new ArrayList();
						list_a.add(a);

					}
						break;

					default:
						break loop5;
					}
				} while (true);

				tokens = list_a;

			}
				break;
			case 2:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:109:4:
				// a+= INT ( ',' a+= INT )*
			{
				root_0 = (Object) adaptor.nil();

				a = (Token) match(input, INT, FOLLOW_INT_in_args346);
				a_tree = (Object) adaptor.create(a);
				adaptor.addChild(root_0, a_tree);

				if (list_a == null)
					list_a = new ArrayList();
				list_a.add(a);

				retval.type = OperandType.INT;
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:109:38:
				// ( ',' a+= INT )*
				loop6: do {
					int alt6 = 2;
					int LA6_0 = input.LA(1);

					if ((LA6_0 == 21)) {
						alt6 = 1;
					}

					switch (alt6) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:109:39:
						// ',' a+= INT
					{
						char_literal16 = (Token) match(input, 21, FOLLOW_21_in_args351);
						char_literal16_tree = (Object) adaptor.create(char_literal16);
						adaptor.addChild(root_0, char_literal16_tree);

						a = (Token) match(input, INT, FOLLOW_INT_in_args355);
						a_tree = (Object) adaptor.create(a);
						adaptor.addChild(root_0, a_tree);

						if (list_a == null)
							list_a = new ArrayList();
						list_a.add(a);

					}
						break;

					default:
						break loop6;
					}
				} while (true);

				tokens = list_a;

			}
				break;
			case 3:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:110:4:
				// a+= FLOAT ( ',' a+= FLOAT )*
			{
				root_0 = (Object) adaptor.nil();

				a = (Token) match(input, FLOAT, FOLLOW_FLOAT_in_args366);
				a_tree = (Object) adaptor.create(a);
				adaptor.addChild(root_0, a_tree);

				if (list_a == null)
					list_a = new ArrayList();
				list_a.add(a);

				retval.type = OperandType.FLOAT;
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:110:42:
				// ( ',' a+= FLOAT )*
				loop7: do {
					int alt7 = 2;
					int LA7_0 = input.LA(1);

					if ((LA7_0 == 21)) {
						alt7 = 1;
					}

					switch (alt7) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:110:43:
						// ',' a+= FLOAT
					{
						char_literal17 = (Token) match(input, 21, FOLLOW_21_in_args371);
						char_literal17_tree = (Object) adaptor.create(char_literal17);
						adaptor.addChild(root_0, char_literal17_tree);

						a = (Token) match(input, FLOAT, FOLLOW_FLOAT_in_args375);
						a_tree = (Object) adaptor.create(a);
						adaptor.addChild(root_0, a_tree);

						if (list_a == null)
							list_a = new ArrayList();
						list_a.add(a);

					}
						break;

					default:
						break loop7;
					}
				} while (true);

				tokens = list_a;

			}
				break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (Object) adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

			for (CommonToken t : tokens) {
				retval.vals.add(t.getText());
			}

		} catch (RecognitionException re) {
			reportError(re);
			recover(input, re);
			retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

		} finally {
		}
		return retval;
	}

	// $ANTLR end "args"

	// Delegated rules

	public static final BitSet FOLLOW_expr_in_cond87 = new BitSet(new long[] { 0x0000000000000000L });
	public static final BitSet FOLLOW_EOF_in_cond89 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_func_in_expr110 = new BitSet(new long[] { 0x0000000000018002L });
	public static final BitSet FOLLOW_unary_in_expr121 = new BitSet(new long[] { 0x0000000000018002L });
	public static final BitSet FOLLOW_grp_in_expr132 = new BitSet(new long[] { 0x0000000000018002L });
	public static final BitSet FOLLOW_15_in_expr146 = new BitSet(new long[] { 0x00000000000A0010L });
	public static final BitSet FOLLOW_16_in_expr153 = new BitSet(new long[] { 0x00000000000A0010L });
	public static final BitSet FOLLOW_expr_in_expr166 = new BitSet(new long[] { 0x0000000000018002L });
	public static final BitSet FOLLOW_17_in_grp204 = new BitSet(new long[] { 0x00000000000A0010L });
	public static final BitSet FOLLOW_expr_in_grp207 = new BitSet(new long[] { 0x0000000000040000L });
	public static final BitSet FOLLOW_18_in_grp209 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_19_in_unary232 = new BitSet(new long[] { 0x00000000000A0010L });
	public static final BitSet FOLLOW_expr_in_unary235 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_ID_in_func262 = new BitSet(new long[] { 0x0000000000100000L });
	public static final BitSet FOLLOW_20_in_func264 = new BitSet(new long[] { 0x0000000000000010L });
	public static final BitSet FOLLOW_ID_in_func271 = new BitSet(new long[] { 0x0000000000020000L });
	public static final BitSet FOLLOW_17_in_func274 = new BitSet(new long[] { 0x00000000000000E0L });
	public static final BitSet FOLLOW_args_in_func277 = new BitSet(new long[] { 0x0000000000040000L });
	public static final BitSet FOLLOW_18_in_func279 = new BitSet(new long[] { 0x0000000000000002L });
	public static final BitSet FOLLOW_STRING_in_args326 = new BitSet(new long[] { 0x0000000000200002L });
	public static final BitSet FOLLOW_21_in_args331 = new BitSet(new long[] { 0x0000000000000020L });
	public static final BitSet FOLLOW_STRING_in_args335 = new BitSet(new long[] { 0x0000000000200002L });
	public static final BitSet FOLLOW_INT_in_args346 = new BitSet(new long[] { 0x0000000000200002L });
	public static final BitSet FOLLOW_21_in_args351 = new BitSet(new long[] { 0x0000000000000040L });
	public static final BitSet FOLLOW_INT_in_args355 = new BitSet(new long[] { 0x0000000000200002L });
	public static final BitSet FOLLOW_FLOAT_in_args366 = new BitSet(new long[] { 0x0000000000200002L });
	public static final BitSet FOLLOW_21_in_args371 = new BitSet(new long[] { 0x0000000000000080L });
	public static final BitSet FOLLOW_FLOAT_in_args375 = new BitSet(new long[] { 0x0000000000200002L });

}
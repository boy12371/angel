// $ANTLR 3.3 Nov 30, 2010 12:45:30 /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g 2011-06-14 16:57:09

package com.feinno.appengine.route.gray;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.DFA;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;

public class ConditionLexer extends Lexer
{
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

	@Override
	public void reportError(RecognitionException e)
	{
		throw new RuntimeException(e);
	}

	// delegates
	// delegators

	public ConditionLexer()
	{
		;
	}

	public ConditionLexer(CharStream input)
	{
		this(input, new RecognizerSharedState());
	}

	public ConditionLexer(CharStream input, RecognizerSharedState state)
	{
		super(input, state);

	}

	public String getGrammarFileName()
	{
		return "/home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g";
	}

	// $ANTLR start "T__15"
	public final void mT__15() throws RecognitionException
	{
		try {
			int _type = T__15;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:18:7:
			// ( 'and' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:18:9:
			// 'and'
			{
				match("and");

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__15"

	// $ANTLR start "T__16"
	public final void mT__16() throws RecognitionException
	{
		try {
			int _type = T__16;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:19:7:
			// ( 'or' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:19:9:
			// 'or'
			{
				match("or");

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__16"

	// $ANTLR start "T__17"
	public final void mT__17() throws RecognitionException
	{
		try {
			int _type = T__17;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:20:7:
			// ( '(' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:20:9:
			// '('
			{
				match('(');

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__17"

	// $ANTLR start "T__18"
	public final void mT__18() throws RecognitionException
	{
		try {
			int _type = T__18;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:21:7:
			// ( ')' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:21:9:
			// ')'
			{
				match(')');

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__18"

	// $ANTLR start "T__19"
	public final void mT__19() throws RecognitionException
	{
		try {
			int _type = T__19;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:22:7:
			// ( '!' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:22:9:
			// '!'
			{
				match('!');

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__19"

	// $ANTLR start "T__20"
	public final void mT__20() throws RecognitionException
	{
		try {
			int _type = T__20;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:23:7:
			// ( '.' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:23:9:
			// '.'
			{
				match('.');

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__20"

	// $ANTLR start "T__21"
	public final void mT__21() throws RecognitionException
	{
		try {
			int _type = T__21;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:24:7:
			// ( ',' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:24:9:
			// ','
			{
				match(',');

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "T__21"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException
	{
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:118:5:
			// ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' |
			// '0' .. '9' | '_' )* )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:118:7:
			// ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0'
			// .. '9' | '_' )*
			{
				if ((input.LA(1) >= 'A' && input.LA(1) <= 'Z') || input.LA(1) == '_'
						|| (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
					input.consume();

				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:118:31:
				// ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
				loop1: do {
					int alt1 = 2;
					int LA1_0 = input.LA(1);

					if (((LA1_0 >= '0' && LA1_0 <= '9') || (LA1_0 >= 'A' && LA1_0 <= 'Z') || LA1_0 == '_' || (LA1_0 >= 'a' && LA1_0 <= 'z'))) {
						alt1 = 1;
					}

					switch (alt1) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:
					{
						if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'Z')
								|| input.LA(1) == '_' || (input.LA(1) >= 'a' && input.LA(1) <= 'z')) {
							input.consume();

						} else {
							MismatchedSetException mse = new MismatchedSetException(null, input);
							recover(mse);
							throw mse;
						}

					}
						break;

					default:
						break loop1;
					}
				} while (true);

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "ID"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException
	{
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:121:5:
			// ( ( '0' .. '9' )+ )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:121:7:
			// ( '0' .. '9' )+
			{
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:121:7:
				// ( '0' .. '9' )+
				int cnt2 = 0;
				loop2: do {
					int alt2 = 2;
					int LA2_0 = input.LA(1);

					if (((LA2_0 >= '0' && LA2_0 <= '9'))) {
						alt2 = 1;
					}

					switch (alt2) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:121:7:
						// '0' .. '9'
					{
						matchRange('0', '9');

					}
						break;

					default:
						if (cnt2 >= 1)
							break loop2;
						EarlyExitException eee = new EarlyExitException(2, input);
						throw eee;
					}
					cnt2++;
				} while (true);

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "INT"

	// $ANTLR start "FLOAT"
	public final void mFLOAT() throws RecognitionException
	{
		try {
			int _type = FLOAT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:5:
			// ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0'
			// .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT )
			int alt9 = 3;
			alt9 = dfa9.predict(input);
			switch (alt9) {
			case 1:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:9:
				// ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )?
			{
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:9:
				// ( '0' .. '9' )+
				int cnt3 = 0;
				loop3: do {
					int alt3 = 2;
					int LA3_0 = input.LA(1);

					if (((LA3_0 >= '0' && LA3_0 <= '9'))) {
						alt3 = 1;
					}

					switch (alt3) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:10:
						// '0' .. '9'
					{
						matchRange('0', '9');

					}
						break;

					default:
						if (cnt3 >= 1)
							break loop3;
						EarlyExitException eee = new EarlyExitException(3, input);
						throw eee;
					}
					cnt3++;
				} while (true);

				match('.');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:25:
				// ( '0' .. '9' )*
				loop4: do {
					int alt4 = 2;
					int LA4_0 = input.LA(1);

					if (((LA4_0 >= '0' && LA4_0 <= '9'))) {
						alt4 = 1;
					}

					switch (alt4) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:26:
						// '0' .. '9'
					{
						matchRange('0', '9');

					}
						break;

					default:
						break loop4;
					}
				} while (true);

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:37:
				// ( EXPONENT )?
				int alt5 = 2;
				int LA5_0 = input.LA(1);

				if ((LA5_0 == 'E' || LA5_0 == 'e')) {
					alt5 = 1;
				}
				switch (alt5) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:125:37:
					// EXPONENT
				{
					mEXPONENT();

				}
					break;

				}

			}
				break;
			case 2:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:126:9:
				// '.' ( '0' .. '9' )+ ( EXPONENT )?
			{
				match('.');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:126:13:
				// ( '0' .. '9' )+
				int cnt6 = 0;
				loop6: do {
					int alt6 = 2;
					int LA6_0 = input.LA(1);

					if (((LA6_0 >= '0' && LA6_0 <= '9'))) {
						alt6 = 1;
					}

					switch (alt6) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:126:14:
						// '0' .. '9'
					{
						matchRange('0', '9');

					}
						break;

					default:
						if (cnt6 >= 1)
							break loop6;
						EarlyExitException eee = new EarlyExitException(6, input);
						throw eee;
					}
					cnt6++;
				} while (true);

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:126:25:
				// ( EXPONENT )?
				int alt7 = 2;
				int LA7_0 = input.LA(1);

				if ((LA7_0 == 'E' || LA7_0 == 'e')) {
					alt7 = 1;
				}
				switch (alt7) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:126:25:
					// EXPONENT
				{
					mEXPONENT();

				}
					break;

				}

			}
				break;
			case 3:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:127:9:
				// ( '0' .. '9' )+ EXPONENT
			{
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:127:9:
				// ( '0' .. '9' )+
				int cnt8 = 0;
				loop8: do {
					int alt8 = 2;
					int LA8_0 = input.LA(1);

					if (((LA8_0 >= '0' && LA8_0 <= '9'))) {
						alt8 = 1;
					}

					switch (alt8) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:127:10:
						// '0' .. '9'
					{
						matchRange('0', '9');

					}
						break;

					default:
						if (cnt8 >= 1)
							break loop8;
						EarlyExitException eee = new EarlyExitException(8, input);
						throw eee;
					}
					cnt8++;
				} while (true);

				mEXPONENT();

			}
				break;

			}
			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "FLOAT"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException
	{
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:131:5:
			// ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options
			// {greedy=false; } : . )* '*/' )
			int alt13 = 2;
			int LA13_0 = input.LA(1);

			if ((LA13_0 == '/')) {
				int LA13_1 = input.LA(2);

				if ((LA13_1 == '/')) {
					alt13 = 1;
				} else if ((LA13_1 == '*')) {
					alt13 = 2;
				} else {
					NoViableAltException nvae = new NoViableAltException("", 13, 1, input);

					throw nvae;
				}
			} else {
				NoViableAltException nvae = new NoViableAltException("", 13, 0, input);

				throw nvae;
			}
			switch (alt13) {
			case 1:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:131:9:
				// '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
			{
				match("//");

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:131:14:
				// (~ ( '\\n' | '\\r' ) )*
				loop10: do {
					int alt10 = 2;
					int LA10_0 = input.LA(1);

					if (((LA10_0 >= '\u0000' && LA10_0 <= '\t') || (LA10_0 >= '\u000B' && LA10_0 <= '\f') || (LA10_0 >= '\u000E' && LA10_0 <= '\uFFFF'))) {
						alt10 = 1;
					}

					switch (alt10) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:131:14:
						// ~ ( '\\n' | '\\r' )
					{
						if ((input.LA(1) >= '\u0000' && input.LA(1) <= '\t')
								|| (input.LA(1) >= '\u000B' && input.LA(1) <= '\f')
								|| (input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF')) {
							input.consume();

						} else {
							MismatchedSetException mse = new MismatchedSetException(null, input);
							recover(mse);
							throw mse;
						}

					}
						break;

					default:
						break loop10;
					}
				} while (true);

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:131:28:
				// ( '\\r' )?
				int alt11 = 2;
				int LA11_0 = input.LA(1);

				if ((LA11_0 == '\r')) {
					alt11 = 1;
				}
				switch (alt11) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:131:28:
					// '\\r'
				{
					match('\r');

				}
					break;

				}

				match('\n');
				_channel = HIDDEN;

			}
				break;
			case 2:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:132:9:
				// '/*' ( options {greedy=false; } : . )* '*/'
			{
				match("/*");

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:132:14:
				// ( options {greedy=false; } : . )*
				loop12: do {
					int alt12 = 2;
					int LA12_0 = input.LA(1);

					if ((LA12_0 == '*')) {
						int LA12_1 = input.LA(2);

						if ((LA12_1 == '/')) {
							alt12 = 2;
						} else if (((LA12_1 >= '\u0000' && LA12_1 <= '.') || (LA12_1 >= '0' && LA12_1 <= '\uFFFF'))) {
							alt12 = 1;
						}

					} else if (((LA12_0 >= '\u0000' && LA12_0 <= ')') || (LA12_0 >= '+' && LA12_0 <= '\uFFFF'))) {
						alt12 = 1;
					}

					switch (alt12) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:132:42:
						// .
					{
						matchAny();

					}
						break;

					default:
						break loop12;
					}
				} while (true);

				match("*/");

				_channel = HIDDEN;

			}
				break;

			}
			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException
	{
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:135:5:
			// ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:135:9:
			// ( ' ' | '\\t' | '\\r' | '\\n' )
			{
				if ((input.LA(1) >= '\t' && input.LA(1) <= '\n') || input.LA(1) == '\r' || input.LA(1) == ' ') {
					input.consume();

				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}

				_channel = HIDDEN;

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "WS"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException
	{
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			CommonToken escaped = null;
			int normal;

			StringBuilder lBuf = new StringBuilder();
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:148:5:
			// ( '\"' (escaped= ESC_SEQ | normal=~ ( '\"' | '\\\\' | '\\n' |
			// '\\r' ) )* '\"' )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:149:12:
			// '\"' (escaped= ESC_SEQ | normal=~ ( '\"' | '\\\\' | '\\n' | '\\r'
			// ) )* '\"'
			{
				match('\"');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:150:12:
				// (escaped= ESC_SEQ | normal=~ ( '\"' | '\\\\' | '\\n' | '\\r'
				// ) )*
				loop14: do {
					int alt14 = 3;
					int LA14_0 = input.LA(1);

					if ((LA14_0 == '\\')) {
						alt14 = 1;
					} else if (((LA14_0 >= '\u0000' && LA14_0 <= '\t') || (LA14_0 >= '\u000B' && LA14_0 <= '\f')
							|| (LA14_0 >= '\u000E' && LA14_0 <= '!') || (LA14_0 >= '#' && LA14_0 <= '[') || (LA14_0 >= ']' && LA14_0 <= '\uFFFF'))) {
						alt14 = 2;
					}

					switch (alt14) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:150:14:
						// escaped= ESC_SEQ
					{
						int escapedStart416 = getCharIndex();
						int escapedStartLine416 = getLine();
						int escapedStartCharPos416 = getCharPositionInLine();
						mESC_SEQ();
						escaped = new CommonToken(input, Token.INVALID_TOKEN_TYPE, Token.DEFAULT_CHANNEL,
								escapedStart416, getCharIndex() - 1);
						escaped.setLine(escapedStartLine416);
						escaped.setCharPositionInLine(escapedStartCharPos416);
						lBuf.append(getText());

					}
						break;
					case 2:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:151:14:
						// normal=~ ( '\"' | '\\\\' | '\\n' | '\\r' )
					{
						normal = input.LA(1);
						if ((input.LA(1) >= '\u0000' && input.LA(1) <= '\t')
								|| (input.LA(1) >= '\u000B' && input.LA(1) <= '\f')
								|| (input.LA(1) >= '\u000E' && input.LA(1) <= '!')
								|| (input.LA(1) >= '#' && input.LA(1) <= '[')
								|| (input.LA(1) >= ']' && input.LA(1) <= '\uFFFF')) {
							input.consume();

						} else {
							MismatchedSetException mse = new MismatchedSetException(null, input);
							recover(mse);
							throw mse;
						}

						lBuf.appendCodePoint(normal);

					}
						break;

					default:
						break loop14;
					}
				} while (true);

				match('\"');
				setText(lBuf.toString());

			}

			state.type = _type;
			state.channel = _channel;
		} finally {
		}
	}

	// $ANTLR end "STRING"

	// $ANTLR start "EXPONENT"
	public final void mEXPONENT() throws RecognitionException
	{
		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:158:10:
			// ( ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:158:12:
			// ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+
			{
				if (input.LA(1) == 'E' || input.LA(1) == 'e') {
					input.consume();

				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:158:22:
				// ( '+' | '-' )?
				int alt15 = 2;
				int LA15_0 = input.LA(1);

				if ((LA15_0 == '+' || LA15_0 == '-')) {
					alt15 = 1;
				}
				switch (alt15) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:
				{
					if (input.LA(1) == '+' || input.LA(1) == '-') {
						input.consume();

					} else {
						MismatchedSetException mse = new MismatchedSetException(null, input);
						recover(mse);
						throw mse;
					}

				}
					break;

				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:158:33:
				// ( '0' .. '9' )+
				int cnt16 = 0;
				loop16: do {
					int alt16 = 2;
					int LA16_0 = input.LA(1);

					if (((LA16_0 >= '0' && LA16_0 <= '9'))) {
						alt16 = 1;
					}

					switch (alt16) {
					case 1:
						// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:158:34:
						// '0' .. '9'
					{
						matchRange('0', '9');

					}
						break;

					default:
						if (cnt16 >= 1)
							break loop16;
						EarlyExitException eee = new EarlyExitException(16, input);
						throw eee;
					}
					cnt16++;
				} while (true);

			}

		} finally {
		}
	}

	// $ANTLR end "EXPONENT"

	// $ANTLR start "HEX_DIGIT"
	public final void mHEX_DIGIT() throws RecognitionException
	{
		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:161:11:
			// ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:161:13:
			// ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
			{
				if ((input.LA(1) >= '0' && input.LA(1) <= '9') || (input.LA(1) >= 'A' && input.LA(1) <= 'F')
						|| (input.LA(1) >= 'a' && input.LA(1) <= 'f')) {
					input.consume();

				} else {
					MismatchedSetException mse = new MismatchedSetException(null, input);
					recover(mse);
					throw mse;
				}

			}

		} finally {
		}
	}

	// $ANTLR end "HEX_DIGIT"

	// $ANTLR start "ESC_SEQ"
	public final void mESC_SEQ() throws RecognitionException
	{
		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:165:5:
			// ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\'
			// ) | UNICODE_ESC | OCTAL_ESC )
			int alt18 = 3;
			int LA18_0 = input.LA(1);

			if ((LA18_0 == '\\')) {
				switch (input.LA(2)) {
				case 'u': {
					alt18 = 2;
				}
					break;
				case '\"':
				case '\'':
				case '\\':
				case 'b':
				case 'f':
				case 'n':
				case 'r':
				case 't': {
					alt18 = 1;
				}
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7': {
					alt18 = 3;
				}
					break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 18, 1, input);

					throw nvae;
				}

			} else {
				NoViableAltException nvae = new NoViableAltException("", 18, 0, input);

				throw nvae;
			}
			switch (alt18) {
			case 1:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:165:9:
				// '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' |
				// '\\\\' )
			{
				match('\\');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:165:14:
				// ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
				int alt17 = 8;
				switch (input.LA(1)) {
				case 'b': {
					alt17 = 1;
				}
					break;
				case 't': {
					alt17 = 2;
				}
					break;
				case 'n': {
					alt17 = 3;
				}
					break;
				case 'f': {
					alt17 = 4;
				}
					break;
				case 'r': {
					alt17 = 5;
				}
					break;
				case '\"': {
					alt17 = 6;
				}
					break;
				case '\'': {
					alt17 = 7;
				}
					break;
				case '\\': {
					alt17 = 8;
				}
					break;
				default:
					NoViableAltException nvae = new NoViableAltException("", 17, 0, input);

					throw nvae;
				}

				switch (alt17) {
				case 1:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:165:15:
					// 'b'
				{
					match('b');
					setText("\b");

				}
					break;
				case 2:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:166:16:
					// 't'
				{
					match('t');
					setText("\t");

				}
					break;
				case 3:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:167:16:
					// 'n'
				{
					match('n');
					setText("\n");

				}
					break;
				case 4:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:168:16:
					// 'f'
				{
					match('f');
					setText("\f");

				}
					break;
				case 5:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:169:16:
					// 'r'
				{
					match('r');
					setText("\r");

				}
					break;
				case 6:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:170:16:
					// '\\\"'
				{
					match('\"');
					setText("\"");

				}
					break;
				case 7:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:171:16:
					// '\\''
				{
					match('\'');
					setText("'");

				}
					break;
				case 8:
					// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:172:16:
					// '\\\\'
				{
					match('\\');
					setText("\\");

				}
					break;

				}

			}
				break;
			case 2:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:173:9:
				// UNICODE_ESC
			{
				mUNICODE_ESC();

			}
				break;
			case 3:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:174:9:
				// OCTAL_ESC
			{
				mOCTAL_ESC();

			}
				break;

			}
		} finally {
		}
	}

	// $ANTLR end "ESC_SEQ"

	// $ANTLR start "OCTAL_ESC"
	public final void mOCTAL_ESC() throws RecognitionException
	{
		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:5:
			// ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' (
			// '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
			int alt19 = 3;
			int LA19_0 = input.LA(1);

			if ((LA19_0 == '\\')) {
				int LA19_1 = input.LA(2);

				if (((LA19_1 >= '0' && LA19_1 <= '3'))) {
					int LA19_2 = input.LA(3);

					if (((LA19_2 >= '0' && LA19_2 <= '7'))) {
						int LA19_4 = input.LA(4);

						if (((LA19_4 >= '0' && LA19_4 <= '7'))) {
							alt19 = 1;
						} else {
							alt19 = 2;
						}
					} else {
						alt19 = 3;
					}
				} else if (((LA19_1 >= '4' && LA19_1 <= '7'))) {
					int LA19_3 = input.LA(3);

					if (((LA19_3 >= '0' && LA19_3 <= '7'))) {
						alt19 = 2;
					} else {
						alt19 = 3;
					}
				} else {
					NoViableAltException nvae = new NoViableAltException("", 19, 1, input);

					throw nvae;
				}
			} else {
				NoViableAltException nvae = new NoViableAltException("", 19, 0, input);

				throw nvae;
			}
			switch (alt19) {
			case 1:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:9:
				// '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
			{
				match('\\');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:14:
				// ( '0' .. '3' )
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:15:
				// '0' .. '3'
				{
					matchRange('0', '3');

				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:25:
				// ( '0' .. '7' )
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:26:
				// '0' .. '7'
				{
					matchRange('0', '7');

				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:36:
				// ( '0' .. '7' )
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:179:37:
				// '0' .. '7'
				{
					matchRange('0', '7');

				}

			}
				break;
			case 2:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:180:9:
				// '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
			{
				match('\\');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:180:14:
				// ( '0' .. '7' )
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:180:15:
				// '0' .. '7'
				{
					matchRange('0', '7');

				}

				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:180:25:
				// ( '0' .. '7' )
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:180:26:
				// '0' .. '7'
				{
					matchRange('0', '7');

				}

			}
				break;
			case 3:
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:181:9:
				// '\\\\' ( '0' .. '7' )
			{
				match('\\');
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:181:14:
				// ( '0' .. '7' )
				// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:181:15:
				// '0' .. '7'
				{
					matchRange('0', '7');

				}

			}
				break;

			}
		} finally {
		}
	}

	// $ANTLR end "OCTAL_ESC"

	// $ANTLR start "UNICODE_ESC"
	public final void mUNICODE_ESC() throws RecognitionException
	{
		try {
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:186:5:
			// ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:186:9:
			// '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
			{
				match('\\');
				match('u');
				mHEX_DIGIT();
				mHEX_DIGIT();
				mHEX_DIGIT();
				mHEX_DIGIT();

			}

		} finally {
		}
	}

	// $ANTLR end "UNICODE_ESC"

	public void mTokens() throws RecognitionException
	{
		// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:8:
		// ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | ID | INT |
		// FLOAT | COMMENT | WS | STRING )
		int alt20 = 13;
		alt20 = dfa20.predict(input);
		switch (alt20) {
		case 1:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:10:
			// T__15
		{
			mT__15();

		}
			break;
		case 2:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:16:
			// T__16
		{
			mT__16();

		}
			break;
		case 3:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:22:
			// T__17
		{
			mT__17();

		}
			break;
		case 4:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:28:
			// T__18
		{
			mT__18();

		}
			break;
		case 5:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:34:
			// T__19
		{
			mT__19();

		}
			break;
		case 6:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:40:
			// T__20
		{
			mT__20();

		}
			break;
		case 7:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:46:
			// T__21
		{
			mT__21();

		}
			break;
		case 8:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:52:
			// ID
		{
			mID();

		}
			break;
		case 9:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:55:
			// INT
		{
			mINT();

		}
			break;
		case 10:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:59:
			// FLOAT
		{
			mFLOAT();

		}
			break;
		case 11:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:65:
			// COMMENT
		{
			mCOMMENT();

		}
			break;
		case 12:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:73:
			// WS
		{
			mWS();

		}
			break;
		case 13:
			// /home/zhaobr/workspace/svn/235FAE/src/library/java-library/feinno-app-engine/src/main/java/com/feinno/appengine/route/grayfactors/Condition.g:1:76:
			// STRING
		{
			mSTRING();

		}
			break;

		}

	}

	protected DFA9 dfa9 = new DFA9(this);
	protected DFA20 dfa20 = new DFA20(this);
	static final String DFA9_eotS = "\5\uffff";
	static final String DFA9_eofS = "\5\uffff";
	static final String DFA9_minS = "\2\56\3\uffff";
	static final String DFA9_maxS = "\1\71\1\145\3\uffff";
	static final String DFA9_acceptS = "\2\uffff\1\2\1\1\1\3";
	static final String DFA9_specialS = "\5\uffff}>";
	static final String[] DFA9_transitionS = { "\1\2\1\uffff\12\1", "\1\3\1\uffff\12\1\13\uffff\1\4\37\uffff\1\4", "",
			"", "" };

	static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
	static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
	static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
	static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
	static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
	static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
	static final short[][] DFA9_transition;

	static {
		int numStates = DFA9_transitionS.length;
		DFA9_transition = new short[numStates][];
		for (int i = 0; i < numStates; i++) {
			DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
		}
	}

	class DFA9 extends DFA
	{

		public DFA9(BaseRecognizer recognizer)
		{
			this.recognizer = recognizer;
			this.decisionNumber = 9;
			this.eot = DFA9_eot;
			this.eof = DFA9_eof;
			this.min = DFA9_min;
			this.max = DFA9_max;
			this.accept = DFA9_accept;
			this.special = DFA9_special;
			this.transition = DFA9_transition;
		}

		public String getDescription()
		{
			return "124:1: FLOAT : ( ( '0' .. '9' )+ '.' ( '0' .. '9' )* ( EXPONENT )? | '.' ( '0' .. '9' )+ ( EXPONENT )? | ( '0' .. '9' )+ EXPONENT );";
		}
	}

	static final String DFA20_eotS = "\1\uffff\2\10\3\uffff\1\17\2\uffff\1\21\3\uffff\1\10\1\23\3\uffff"
			+ "\1\24\2\uffff";
	static final String DFA20_eofS = "\25\uffff";
	static final String DFA20_minS = "\1\11\1\156\1\162\3\uffff\1\60\2\uffff\1\56\3\uffff\1\144\1\60\3"
			+ "\uffff\1\60\2\uffff";
	static final String DFA20_maxS = "\1\172\1\156\1\162\3\uffff\1\71\2\uffff\1\145\3\uffff\1\144\1\172"
			+ "\3\uffff\1\172\2\uffff";
	static final String DFA20_acceptS = "\3\uffff\1\3\1\4\1\5\1\uffff\1\7\1\10\1\uffff\1\13\1\14\1\15\2\uffff"
			+ "\1\6\1\12\1\11\1\uffff\1\2\1\1";
	static final String DFA20_specialS = "\25\uffff}>";
	static final String[] DFA20_transitionS = {
			"\2\13\2\uffff\1\13\22\uffff\1\13\1\5\1\14\5\uffff\1\3\1\4\2"
					+ "\uffff\1\7\1\uffff\1\6\1\12\12\11\7\uffff\32\10\4\uffff\1\10" + "\1\uffff\1\1\15\10\1\2\13\10",
			"\1\15", "\1\16", "", "", "", "\12\20", "", "", "\1\20\1\uffff\12\11\13\uffff\1\20\37\uffff\1\20", "", "",
			"", "\1\22", "\12\10\7\uffff\32\10\4\uffff\1\10\1\uffff\32\10", "", "", "",
			"\12\10\7\uffff\32\10\4\uffff\1\10\1\uffff\32\10", "", "" };

	static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
	static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
	static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
	static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
	static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
	static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
	static final short[][] DFA20_transition;

	static {
		int numStates = DFA20_transitionS.length;
		DFA20_transition = new short[numStates][];
		for (int i = 0; i < numStates; i++) {
			DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
		}
	}

	class DFA20 extends DFA
	{

		public DFA20(BaseRecognizer recognizer)
		{
			this.recognizer = recognizer;
			this.decisionNumber = 20;
			this.eot = DFA20_eot;
			this.eof = DFA20_eof;
			this.min = DFA20_min;
			this.max = DFA20_max;
			this.accept = DFA20_accept;
			this.special = DFA20_special;
			this.transition = DFA20_transition;
		}

		public String getDescription()
		{
			return "1:1: Tokens : ( T__15 | T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | ID | INT | FLOAT | COMMENT | WS | STRING );";
		}
	}

}
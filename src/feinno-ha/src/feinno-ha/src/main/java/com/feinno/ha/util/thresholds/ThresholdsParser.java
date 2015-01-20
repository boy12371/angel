// $ANTLR 3.4 /home/zhouyanxjs/Temp/Thresholds/Thresholds.g 2011-11-18 16:55:06

  package com.feinno.ha.util.thresholds;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings({"all", "warnings", "unchecked"})
public class ThresholdsParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "EQUALT", "ESC_SEQ", "EXPONENT", "FLOAT", "GREATERTHAN", "GREATERTHANOREQUALT", "HEX_DIGIT", "ID", "INT", "LESSTHAN", "LESSTHANOREQUALT", "OCTAL_ESC", "STRING", "UNICODE_ESC", "WS", "'('", "')'", "';'"
    };

    public static final int EOF=-1;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int EQUALT=4;
    public static final int ESC_SEQ=5;
    public static final int EXPONENT=6;
    public static final int FLOAT=7;
    public static final int GREATERTHAN=8;
    public static final int GREATERTHANOREQUALT=9;
    public static final int HEX_DIGIT=10;
    public static final int ID=11;
    public static final int INT=12;
    public static final int LESSTHAN=13;
    public static final int LESSTHANOREQUALT=14;
    public static final int OCTAL_ESC=15;
    public static final int STRING=16;
    public static final int UNICODE_ESC=17;
    public static final int WS=18;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public ThresholdsParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public ThresholdsParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

protected TreeAdaptor adaptor = new CommonTreeAdaptor();

public void setTreeAdaptor(TreeAdaptor adaptor) {
    this.adaptor = adaptor;
}
public TreeAdaptor getTreeAdaptor() {
    return adaptor;
}
    public String[] getTokenNames() { return ThresholdsParser.tokenNames; }
    public String getGrammarFileName() { return "/home/zhouyanxjs/Temp/Thresholds/Thresholds.g"; }



      @Override
      protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        throw new MismatchedTokenException(ttype, input);
      }

      @Override
      public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
        throw e;
      }
      
      
      private List<String> errors = new java.util.ArrayList<String>();
      public void displayRecognitionError(String[] tokenNames,
                                          RecognitionException e) {
          String hdr = getErrorHeader(e);
          String msg = getErrorMessage(e, tokenNames);
          errors.add(hdr + " " + msg);
      }
      
      
      public List<String> getErrors() {
          return errors;
      } 
       
        


    public static class program_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "program"
    // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:41:1: program : ( statement )+ ;
    public final ThresholdsParser.program_return program() throws RecognitionException {
        ThresholdsParser.program_return retval = new ThresholdsParser.program_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        ThresholdsParser.statement_return statement1 =null;



        try {
            // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:41:9: ( ( statement )+ )
            // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:41:11: ( statement )+
            {
            root_0 = (Object)adaptor.nil();


            // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:41:11: ( statement )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==19) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:41:11: statement
            	    {
            	    pushFollow(FOLLOW_statement_in_program44);
            	    statement1=statement();

            	    state._fsp--;

            	    adaptor.addChild(root_0, statement1.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "program"


    public static class statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };


    // $ANTLR start "statement"
    // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:42:1: statement : '(' ! STRING ( LESSTHANOREQUALT | GREATERTHANOREQUALT | LESSTHAN | GREATERTHAN ) ^ ( FLOAT | INT ) ')' ! ';' !;
    public final ThresholdsParser.statement_return statement() throws RecognitionException {
        ThresholdsParser.statement_return retval = new ThresholdsParser.statement_return();
        retval.start = input.LT(1);


        Object root_0 = null;

        Token char_literal2=null;
        Token STRING3=null;
        Token set4=null;
        Token set5=null;
        Token char_literal6=null;
        Token char_literal7=null;

        Object char_literal2_tree=null;
        Object STRING3_tree=null;
        Object set4_tree=null;
        Object set5_tree=null;
        Object char_literal6_tree=null;
        Object char_literal7_tree=null;

        try {
            // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:42:11: ( '(' ! STRING ( LESSTHANOREQUALT | GREATERTHANOREQUALT | LESSTHAN | GREATERTHAN ) ^ ( FLOAT | INT ) ')' ! ';' !)
            // /home/zhouyanxjs/Temp/Thresholds/Thresholds.g:42:13: '(' ! STRING ( LESSTHANOREQUALT | GREATERTHANOREQUALT | LESSTHAN | GREATERTHAN ) ^ ( FLOAT | INT ) ')' ! ';' !
            {
            root_0 = (Object)adaptor.nil();


            char_literal2=(Token)match(input,19,FOLLOW_19_in_statement52); 

            STRING3=(Token)match(input,STRING,FOLLOW_STRING_in_statement54); 
            STRING3_tree = 
            (Object)adaptor.create(STRING3)
            ;
            adaptor.addChild(root_0, STRING3_tree);


            set4=(Token)input.LT(1);

            set4=(Token)input.LT(1);

            if ( (input.LA(1) >= GREATERTHAN && input.LA(1) <= GREATERTHANOREQUALT)||(input.LA(1) >= LESSTHAN && input.LA(1) <= LESSTHANOREQUALT) ) {
                input.consume();
                root_0 = (Object)adaptor.becomeRoot(
                (Object)adaptor.create(set4)
                , root_0);
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            set5=(Token)input.LT(1);

            if ( input.LA(1)==FLOAT||input.LA(1)==INT ) {
                input.consume();
                adaptor.addChild(root_0, 
                (Object)adaptor.create(set5)
                );
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            char_literal6=(Token)match(input,20,FOLLOW_20_in_statement72); 

            char_literal7=(Token)match(input,21,FOLLOW_21_in_statement74); 

            }

            retval.stop = input.LT(-1);


            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "statement"

    // Delegated rules


 

    public static final BitSet FOLLOW_statement_in_program44 = new BitSet(new long[]{0x0000000000080002L});
    public static final BitSet FOLLOW_19_in_statement52 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_STRING_in_statement54 = new BitSet(new long[]{0x0000000000006300L});
    public static final BitSet FOLLOW_set_in_statement56 = new BitSet(new long[]{0x0000000000001080L});
    public static final BitSet FOLLOW_set_in_statement67 = new BitSet(new long[]{0x0000000000100000L});
    public static final BitSet FOLLOW_20_in_statement72 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_21_in_statement74 = new BitSet(new long[]{0x0000000000000002L});

}
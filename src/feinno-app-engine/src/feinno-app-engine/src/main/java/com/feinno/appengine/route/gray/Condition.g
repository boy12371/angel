grammar Condition;

options {
  language = Java;
  output = AST;
}

@lexer::header {
  package com.feinno.appengine.route.grayfactors;
}

@parser::header {
  package com.feinno.appengine.route.grayfactors;
}


@parser::members {

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
   
}

/*
//break on first rule error?
@parser::rulecatch {
    catch (RecognitionException e) {
        throw e;
    }
    
}
*/


@lexer::members {
    @Override
    public void reportError(RecognitionException e) {
        throw new RuntimeException(e);
    }

} 



public cond returns [Cond c]  
  : expr EOF! { $c = $expr.c;}
  ;

expr returns [Cond c]
  : (func {$c = $func.c;} 
    | unary {$c = $unary.c;} 
    | grp {$c = $grp.c;} )
    ((a='and'^ | a='or'^) 
      r=expr 
      {
        if ($a.text.equals("and")) {
          $c = new AND($c, $r.c);
        } else {
          $c = new OR($c, $r.c);
         }
       }
     )*
  ; 

grp returns [Cond c]   
  : '('! expr ')'! {$c = $expr.c;}
  ; 

unary returns [Cond c]  
  : '!'^ expr {$c = new NOT($expr.c);} 
  ;

func returns [Cond c]    
  : (field=ID '.'!)? name=ID^ '('! args ')'!
      {$c = CondBuilder.buidlCond($name.text, $field.text, $args.vals, $args.type);}
  ;
  
args returns [java.util.List<String> vals, OperandType type]    
  @init {
    java.util.List<CommonToken> tokens = new ArrayList<CommonToken>();
    $vals = new java.util.ArrayList<String>();
  }
  @after {
    for(CommonToken t : tokens) {
      $vals.add(t.getText());
     }
  }
	: a+=STRING {$type = OperandType.STRING;} (',' a+=STRING)* {tokens = $a;}
	| a+=INT {$type = OperandType.INT;} (',' a+=INT)* {tokens = $a;}
	| a+=FLOAT {$type = OperandType.FLOAT;} (',' a+=FLOAT)* {tokens = $a;}
	;

//(STRING | INT | FLOAT) (',' (STRING | INT | FLOAT))*;   




ID  : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT : '0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    |   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

//STRING
//    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
//    ;
    
STRING          
@init{StringBuilder lBuf = new StringBuilder();}
    :   
           '"' 
           ( escaped=ESC_SEQ {lBuf.append(getText());} | 
             normal=~('"'|'\\'|'\n'|'\r'){lBuf.appendCodePoint(normal);} )* 
           '"'     
           {setText(lBuf.toString());}
           
    ;    
     
fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b' { setText("\b");}
              |'t' { setText("\t");}
              |'n' { setText("\n");} 
              |'f' { setText("\f");}
              |'r' { setText("\r");}
              |'\"' { setText("\"");}
              |'\'' { setText("'"); }
              |'\\' { setText("\\"); }) 
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;


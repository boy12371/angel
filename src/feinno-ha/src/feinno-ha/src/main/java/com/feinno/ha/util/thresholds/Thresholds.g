grammar Thresholds;
options{output=AST;}

@lexer::header {
  package com.feinno.ha.thresholds;
}

@parser::header {
  package com.feinno.ha.thresholds;
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

program	:	statement+;
statement	:	'('!STRING (LESSTHANOREQUALT|GREATERTHANOREQUALT|LESSTHAN|GREATERTHAN)^ (FLOAT|INT)')'!';'!;
LESSTHANOREQUALT
	:	LESSTHAN EQUALT;	
LESSTHAN	:	'<';
GREATERTHANOREQUALT
	:	GREATERTHAN EQUALT;
GREATERTHAN
	:	'>';
EQUALT	:	'=';
ID  :	('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;

INT :	'0'..'9'+
    ;

FLOAT
    :   ('0'..'9')+ '.' ('0'..'9')* EXPONENT?
    |   '.' ('0'..'9')+ EXPONENT?
    |   ('0'..'9')+ EXPONENT
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {$channel=HIDDEN;}
    ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment
EXPONENT : ('e'|'E') ('+'|'-')? ('0'..'9')+ ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
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


/*
 * Copyright (c) 2005 Einar Pehrson <einar@pehrson.nu>.
 *
 * This file is part of
 * CleanSheets - a spreadsheet application for the Java platform.
 *
 * CleanSheets is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * CleanSheets is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
header {package csheets.core.formula.compiler;}

{/**
 * A parser that generates expressions from lists of lexical tokens.
 * @author Einar Pehrson
 */}
class FormulaParser extends Parser;
options {
	buildAST = true;
	defaultErrorHandler = false;
}

/**
 * The start rule for formula expressions.
 */
/* content
	: ( expression | literal ) EOF
	; */
expression
	: EQ! comparison EOF!
	;

comparison
	: concatenation
		( ( EQ^ | NEQ^ | GT^ | LT^ | LTEQ^ | GTEQ^ ) concatenation )?
	;

concatenation
	: arithmetic_lowest
		( AMP^ arithmetic_lowest )*
	;

arithmetic_lowest
	:	arithmetic_low
		( ( PLUS^ | MINUS^ ) arithmetic_low )*
	;

arithmetic_low
	:	arithmetic_medium
		( ( MULTI^ | DIV^ ) arithmetic_medium )*
	;

arithmetic_medium
	:	arithmetic_high
		( POWER^ arithmetic_high )?
	;

arithmetic_high
	:	arithmetic_highest ( PERCENT^ )?
	;

arithmetic_highest
	:	( MINUS^ )? atom
	;

atom
	:	function_call
	|	reference
	|	literal
	|	LPAR! comparison RPAR!
	;

function_call
	:	FUNCTION^ 
		( comparison ( SEMI! comparison )* )?
		RPAR!
	;

reference
	:	CELL_REF
		( ( COLON^ ) CELL_REF )?
	|	NAME
	;

literal
	:	NUMBER
	|	STRING
	;

{import csheets.core.formula.lang.Language;

/**
 * A lexer that splits a string into a list of lexical tokens.
 * @author Einar Pehrson
 */
@SuppressWarnings("all")}
class FormulaLexer extends Lexer;

options {
	k = 4;
	caseSensitive = false;
	caseSensitiveLiterals = false;
}

/* Function calls, named ranges and cell references */
protected LETTER: ('a'..'z') ;

ALPHABETICAL
	:	( ( LETTER )+ LPAR ) => ( LETTER )+ LPAR! {
			try {
				Language.getInstance().getFunction(#getText());
				$setType(FUNCTION);
			} catch (Exception ex) {
				throw new RecognitionException(ex.toString());
			}
		}
	|	/* ( LETTER ( LETTER | NUMBER )* EXCL )? */
		( ABS )? LETTER ( LETTER )?
		( ABS )? ( DIGIT )+ {
			$setType(CELL_REF);
		}
	;

/* String literals, i.e. anything inside the delimiters */
STRING
	:	QUOT!
		(options {greedy=false;}:.)*
		QUOT!
	;
protected QUOT: '"';

/* Numeric literals */
NUMBER: ( DIGIT )+ ( COMMA ( DIGIT )+ )? ;
protected DIGIT : '0'..'9' ;

/* Comparison operators */
EQ		: "=" ;
NEQ		: "<>" ;
LTEQ	: "<=" ;
GTEQ	: ">=" ;
GT		: '>' ;
LT		: '<' ;

/* Text operators */
AMP		: '&' ;

/* Arithmetic operators */
PLUS	: '+' ;
MINUS	: '-' ;
MULTI	: '*' ;
DIV		: '/' ;
POWER	: '^' ;
PERCENT : '%' ;

/* Reference operators */
protected ABS : '$' ;
protected EXCL:  '!'  ;
COLON	: ':' ;

/* Miscellaneous operators */
COMMA	: ',' ;
SEMI	: ';' ;
LPAR	: '(' ;
RPAR	: ')' ;


/* White-space (ignored) */
WS: ( ' '
	| '\r' '\n'
	| '\n'
	| '\t'
	)
	{$setType(Token.SKIP);}
	;
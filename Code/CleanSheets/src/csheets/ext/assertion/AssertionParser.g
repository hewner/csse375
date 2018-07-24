/*
 * Copyright (c) 2005 Peter Palotas, Fredrik Johansson, Einar Pehrson,
 * Sebastian Kekkonen, Lars Magnus Lång, Malin Johansson and Sofia Nilsson
 *
 * This file is part of
 * CleanSheets Extension for Assertions
 *
 * CleanSheets Extension for Assertions is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * CleanSheets Extension for Assertions is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CleanSheets Extension for Assertions; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
 
/* ANTLR 2.7.5 grammar file for assertions 
   See www.antlr.org for details on antlr. 
   
   Compile with 'antlr AssertionParser.g' after
   installing antlr. (also make sure that 
   the antlr.jar file is in your CLASSPATH, 
   or you will get errors).
   
   Author: Peter Palotas
*/ 
header {package csheets.ext.assertion;}

options {

}


{import java.util.List;}
class AssertionParser extends Parser;

options {
	defaultErrorHandler=false;
	k=1;
}

{
}

assertion [USAssertion ass, List<Interval> orIntervals, List<Interval> exceptIntervals] throws AssertionException
	{
		ass.isInteger = false;
	}
	: (or_expr[orIntervals])? (except_clause[exceptIntervals])? (is_integer[ass])? EOF
	;
	
except_clause [List<Interval> lst] throws AssertionException
	: EXCEPT term[lst] (COMMA term[lst])*
	;
	
or_expr [List<Interval> lst] throws AssertionException
	: term[lst] (OR term[lst])*
	;
	
	
term [List<Interval> lst] throws AssertionException
	{
		double c; 
	}
	: interval[lst] 
	| c = constant 
		{
			try {
				lst.add(new Interval(c));
			} catch (IllegalArgumentException iae) {
				// This should never happen since c should be a good value.
				throw new AssertionException("Invalid constant specified in assertion");
			}
		}
	| os_interval[lst]
	;
	
interval [List<Interval> lst] throws AssertionException
	{
		double lower = 0.0, upper = 0.0;
		boolean lclosed = false, rclosed = false;
	}
	: (ltok:LBRACK {lclosed = true;}| rtok:RBRACK) lower=constant TO upper=constant (LBRACK | RBRACK {rclosed=true;} )
		{
			Token tok;
			if (lclosed)
				tok = ltok;
			else	
				tok = rtok;
			try {
				lst.add(new Interval(lower, upper, lclosed, rclosed));
			}
			catch (IllegalArgumentException iae) {
				
				throw new AssertionException(tok.getLine(), tok.getColumn(), iae.getMessage());
			}
				
		}
	;
	
constant returns [double value]
	{
		value = 0;
	}
	: r:NUMBER
		{
			value = Double.parseDouble(r.getText());
		}
	;
	
os_interval [List<Interval> lst] throws AssertionException
	{
		double c = 0.0;
		boolean eq = false, gt = true;
	}
	: (gttok:GT | lttok:LT {gt = false;} | gteqtok:GTEQ {eq = true;}| lteqtok:LTEQ{eq=true;gt=false;}) c=constant
	{
		Token tok = (gt ? (eq ? gteqtok : gttok) : (eq ? lteqtok : lttok));
		try {
			if (gt) {
				lst.add(new Interval(c, Double.POSITIVE_INFINITY, eq, false));
			} else {
				lst.add(new Interval(Double.NEGATIVE_INFINITY, c, false, eq));
			}
		} catch (IllegalArgumentException iae) {
			throw new AssertionException(tok.getLine(), tok.getColumn(), iae.toString());
		}
	}
	;
	
is_integer [USAssertion ass]
	: INTEGER
	{
		ass.isInteger = true;
	}
	;
{@SuppressWarnings("all")}
class AssertionLexer extends Lexer;

options {
	k=2;
	caseSensitive=false;
	caseSensitiveLiterals=false;
}

{

}

protected
EOL	
options {
paraphrase = "end of line";
}
	:	(	("\r\n") => "\r\n" // Evil DOS
		|	'\r'    // Macintosh
		|	'\n'    // Unix (the right way)
		)
		{ newline(); $setText("end of line"); }
	;

WS	
options {
paraphrase = "whitespace";
}
:	(' '
	|	'\t'
	|	EOL)
		{ $setType(Token.SKIP); }
	;


COMMA
options {
paraphrase = "','";
}
:	','
	;

GT
options {
paraphrase = "'>'";
}
	:	">"
	;
	
LT
options {
paraphrase = "'<'";
}
:	"<"
	;
	
GTEQ
options {
paraphrase = "'>='";
}
:	">="

	;
	
LTEQ
options {
paraphrase = "'<='";
}
:	"<="
	;

LBRACK
options {
paraphrase = "'['";
}
:	"["
	;
	

RBRACK
options {
paraphrase = "']'";
}
:	"]"
	;

protected
DIGIT:	'0'..'9'
	;
	
NUMBER
options {	
	paraphrase = "a numeric constant";
}
	: ((MINUS)? (DIGIT)* '.') => (MINUS)? (DIGIT)* '.' (DIGIT)+ (EXPONENT)?
	| (MINUS)? (DIGIT)+ (EXPONENT)? 
	;
	
protected
PLUS
options {
paraphrase = "'+'";
}
:	'+'
	;
	
protected
MINUS
options {
paraphrase = "'-'";
}
:	'-'
	;
	
protected
EXPONENT
	: 'e' ('+'|'-')? (DIGIT)+
	;


EXCEPT
	: "except"
	;
	
INTEGER
	: "integer"
	;
	
OR	
	: "or"
	;

TO	
	: "to"
	;
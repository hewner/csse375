// $ANTLR 2.7.5 (20050128): "../src/csheets/ext/assertion/AssertionParser.g" -> "AssertionParser.java"$
package csheets.ext.assertion;
import java.util.List;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
public class AssertionParser extends antlr.LLkParser       implements AssertionParserTokenTypes
 {


protected AssertionParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public AssertionParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected AssertionParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public AssertionParser(TokenStream lexer) {
  this(lexer,1);
}

public AssertionParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final void assertion(
		USAssertion ass, List<Interval> orIntervals, List<Interval> exceptIntervals
	) throws RecognitionException, TokenStreamException, AssertionException {
		
		
				ass.isInteger = false;
			
		
		{
		switch ( LA(1)) {
		case LBRACK:
		case RBRACK:
		case NUMBER:
		case GT:
		case LT:
		case GTEQ:
		case LTEQ:
		{
			or_expr(orIntervals);
			break;
		}
		case EOF:
		case EXCEPT:
		case INTEGER:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case EXCEPT:
		{
			except_clause(exceptIntervals);
			break;
		}
		case EOF:
		case INTEGER:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		{
		switch ( LA(1)) {
		case INTEGER:
		{
			is_integer(ass);
			break;
		}
		case EOF:
		{
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		match(Token.EOF_TYPE);
	}
	
	public final void or_expr(
		List<Interval> lst
	) throws RecognitionException, TokenStreamException, AssertionException {
		
		
		term(lst);
		{
		_loop10:
		do {
			if ((LA(1)==OR)) {
				match(OR);
				term(lst);
			}
			else {
				break _loop10;
			}
			
		} while (true);
		}
	}
	
	public final void except_clause(
		List<Interval> lst
	) throws RecognitionException, TokenStreamException, AssertionException {
		
		
		match(EXCEPT);
		term(lst);
		{
		_loop7:
		do {
			if ((LA(1)==COMMA)) {
				match(COMMA);
				term(lst);
			}
			else {
				break _loop7;
			}
			
		} while (true);
		}
	}
	
	public final void is_integer(
		USAssertion ass
	) throws RecognitionException, TokenStreamException {
		
		
		match(INTEGER);
		
				ass.isInteger = true;
			
	}
	
	public final void term(
		List<Interval> lst
	) throws RecognitionException, TokenStreamException, AssertionException {
		
		
				double c; 
			
		
		switch ( LA(1)) {
		case LBRACK:
		case RBRACK:
		{
			interval(lst);
			break;
		}
		case NUMBER:
		{
			c=constant();
			
						try {
							lst.add(new Interval(c));
						} catch (IllegalArgumentException iae) {
							// This should never happen since c should be a good value.
							throw new AssertionException("Invalid constant specified in assertion");
						}
					
			break;
		}
		case GT:
		case LT:
		case GTEQ:
		case LTEQ:
		{
			os_interval(lst);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
	}
	
	public final void interval(
		List<Interval> lst
	) throws RecognitionException, TokenStreamException, AssertionException {
		
		Token  ltok = null;
		Token  rtok = null;
		
				double lower = 0.0, upper = 0.0;
				boolean lclosed = false, rclosed = false;
			
		
		{
		switch ( LA(1)) {
		case LBRACK:
		{
			ltok = LT(1);
			match(LBRACK);
			lclosed = true;
			break;
		}
		case RBRACK:
		{
			rtok = LT(1);
			match(RBRACK);
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		lower=constant();
		match(TO);
		upper=constant();
		{
		switch ( LA(1)) {
		case LBRACK:
		{
			match(LBRACK);
			break;
		}
		case RBRACK:
		{
			match(RBRACK);
			rclosed=true;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		
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
	
	public final double  constant() throws RecognitionException, TokenStreamException {
		double value;
		
		Token  r = null;
		
				value = 0;
			
		
		r = LT(1);
		match(NUMBER);
		
					value = Double.parseDouble(r.getText());
				
		return value;
	}
	
	public final void os_interval(
		List<Interval> lst
	) throws RecognitionException, TokenStreamException, AssertionException {
		
		Token  gttok = null;
		Token  lttok = null;
		Token  gteqtok = null;
		Token  lteqtok = null;
		
				double c = 0.0;
				boolean eq = false, gt = true;
			
		
		{
		switch ( LA(1)) {
		case GT:
		{
			gttok = LT(1);
			match(GT);
			break;
		}
		case LT:
		{
			lttok = LT(1);
			match(LT);
			gt = false;
			break;
		}
		case GTEQ:
		{
			gteqtok = LT(1);
			match(GTEQ);
			eq = true;
			break;
		}
		case LTEQ:
		{
			lteqtok = LT(1);
			match(LTEQ);
			eq=true;gt=false;
			break;
		}
		default:
		{
			throw new NoViableAltException(LT(1), getFilename());
		}
		}
		}
		c=constant();
		
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
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"EXCEPT",
		"','",
		"OR",
		"'['",
		"']'",
		"TO",
		"a numeric constant",
		"'>'",
		"'<'",
		"'>='",
		"'<='",
		"INTEGER",
		"end of line",
		"whitespace",
		"DIGIT",
		"'+'",
		"'-'",
		"EXPONENT"
	};
	
	
	}

package org.keplerproject.dltk.ldt.core.parser;

import java.util.logging.Level;
import java.util.logging.Logger;


public class LuaLexer {
	private static final Logger LOGGER = Logger.getLogger(LuaLexer.class.getName());
	public enum Token {
		AND ("and"), BREAK("break"), DO("do"), ELSE("else"), ELSEIF("elseif"), END("end"), FALSE("false"), 
		FOR("for"), FUNCTION("function"), IF("if"), IN("in"), LOCAL("local"), NIL("nil"), NOT("not"), OR("or"), 
		REPEAT("repeat"), RETURN("return"), THEN("then"), TRUE("true"), UNTIL("until"), WHILE("while"), 
		_EOF(""), _NAME(""), _NUMBER(""), _STRING(""), 
		_DOT("."), _CONCAT(".."), _DOTS("..."),
		_COMMA(","), _COLON(":"), _SC(";"),
		_LPAREN("("), _RPAREN(")"), _LBRACK("["), _RBRACK("]"), _LCURLY("{"), _RCURLY("}"),
		_EQ("=="), _NEQ("~="), _L("<"), _LE("<="), _G(">"), _GE(">="),
		_ASSIG("="), _NOT("~"),
		_MINUS("-"), _PLUS("+"), _MULT("*"), _DIV("/")
		;
		
		private final String keyword;
		
		Token(String name) { this.keyword = name; }

		public static Token getTokenByLexeme(final String lexeme) {
			for (Token t : Token.values()) {
				if (t.keyword.equals(lexeme)) return t;
			}
			return null;
		}
		
		public static Token keyword(final String keyword) {
			Token t = getTokenByLexeme(keyword);
			return (t == null) ? Token._NAME : t;
		}
	};

	private char[] source;
	private int c = 0;
	private int line = 0;
	private LuaToken last;

	public LuaLexer(char[] source) {
		this.source = source;
	}

	public final int getLength() {
		return source.length;
	}

	public final LuaToken getToken() {
		return last;
	}
	public final boolean advance(Token token) {
		if (token.equals(last.getToken())) {
			getNextToken();
			return true;
		} else {
			return false;
		}
	}
	public final boolean match(Token token) {
		return token.equals(last.getToken());
	}
	public final LuaToken consume(Token token) {
		if (token.equals(last.getToken())) {
			return getTokenAndAdvance();
		} else {
			throw new RuntimeException("Token doesn't match: " + last.getToken() + " should be " + token);
		}
	}
	
	public final LuaToken getTokenAndAdvance() {
		final LuaToken t = last;
		getNextToken();
		return t;
	}
	
	public final LuaToken getNextToken() {
		LOGGER.log(Level.FINE, "Entering LuaLexer getNextToken");
		LOGGER.log(Level.FINE, String.format("Fetching from %d:%d", c, line));
		while (c < source.length) {
			switch (curr()) {
			case 0:
				return token(Token._EOF, "");
			case '\n':
			case '\r':
				next();
				line++;
				continue;
			case '-':
				next();
				// Minus signal
				if (curr() != '-')
					return token(Token._MINUS, "-");
				// Comment
				if (next() == '[') {
					longString();
				} else {
					// Short comment
					while (!newline())
						next();
				}
				continue;
			case '[':
				next();
				if (curr() == '=' || curr() == '[') {
					next();
					return token(Token._STRING, longString());
				}
				return token(Token._LBRACK, "[");
			case '=':
				next();
				if (curr() != '=')
					return token(Token._ASSIG, "=");
				else {
					next();
					return token(Token._EQ, "==");
				}
			case '<':
				next();
				if (curr() != '=')
					return token(Token._L, "<");
				else {
					next();
					return token(Token._LE, "<=");
				}
			case '>':
				next();
				if (curr() != '=')
					return token(Token._G, ">");
				else {
					next();
					return token(Token._GE, ">=");
				}
			case '~':
				next();
				if (curr() != '=')
					return token(Token._NOT, "~");
				else {
					next();
					return token(Token._NEQ, "~=");
				}
			case '"':
			case '\'':
				return readString(curr());
			case '.':
				if (next() == '.') {
					if (lookahead(1) == '.') {
						next();
						return token(Token._DOTS, "...");
					} else {
						return token(Token._CONCAT, "..");
					}
				} else if (!Character.isDigit(curr())) {
					return token(Token._DOT, ".");
				} else {
					return readNumber();
				}
			default:
				if (Character.isWhitespace(curr()) && !newline()) {
					next();
					continue;
				} else if (Character.isDigit(curr())) {
					return readNumber();
				} else if (Character.isLetterOrDigit(curr()) || curr() == '_') {
					return readName();
				} else {
					char c = curr();
					next();
					return token(Token.getTokenByLexeme(String.valueOf(c)), String.valueOf(c));
				}
			}
		}
		return token(Token._EOF, "<eof>");
	}

	private String longString() {
		long scope = 0;
		long count;
		StringBuffer sb = new StringBuffer();
		StringBuffer residue = new StringBuffer();
		while (next() == '=') scope++;
		next(); // [
		do {
			sb.append(residue);
			do {
				sb.append(curr());
				if (curr() == '\n' || curr() == '\r') line++;
			} while(next() != ']');
			residue = new StringBuffer();
			residue.append(']');
			count = 0;
			while (next() == '=') {
				residue.append('=');
				count++;
			}
			residue.append(curr());
		} while (count != scope || curr() != ']');
		next(); // ]
		return sb.toString();
	}

	private final char curr() {
		if (c >= source.length)
			return 0;
		return source[c];
	}

	private final char next() {
		if (++c >= source.length)
			return 0;
		return source[c];
	}
	
	private final char lookahead(int i) {
		if (c + i >= source.length) return 0;
		return source[c+i];
	}

	private final boolean newline() {
		return (curr() == '\n' || curr() == '\r');
	}

	private final LuaToken readNumber() {
		// TODO Colocar hexadecimal
		final StringBuilder sb = new StringBuilder();
		do {
			sb.append(curr());
		} while (Character.isDigit(next()));
		
		// Fraction
		if (curr() == '.') {
			do {
				sb.append(curr());
			} while (Character.isDigit(next()));			
		}
		
		// Exponent
		if (curr() == 'E' || curr() == 'e') {
			sb.append(next());
			if (curr() == '+' || curr() == '-') {
				sb.append(next());
			}
			
			do {
				sb.append(curr());
			} while (Character.isDigit(next()));	
		}
		return token(Token._NUMBER, sb.toString());
	}

	private final LuaToken readString(char del) {
		final StringBuilder sb = new StringBuilder();
		while (next() != del || curr() == '\\') {
			sb.append(curr());
		}
		next();
		return token(Token._STRING, sb.toString());
	}

	private final LuaToken readName() {
		final StringBuilder sb = new StringBuilder();
		while (Character.isLetterOrDigit(curr()) || curr() == '_') {
			sb.append(curr());
			next();
		}

		return token(Token.keyword(sb.toString()), sb.toString());
	}

	private final LuaToken token(final Token token, final String lexeme) {
		last = new LuaToken();
		last.lexeme = lexeme;
		last.token = token;
		last.line = line;
		last.position = c-lexeme.length();
		return last;
	}

	public static final boolean isBinaryExpressionToken(final LuaToken binary) {
		// TODO '^' e '%'
		switch(binary.getToken()) {
		case _PLUS:
		case _MINUS:
		case _MULT:
		case _DIV:
		case _EQ:
		case _NEQ:
		case _L:
		case _LE:
		case _G:
		case _GE:
		case AND:
		case OR:
		case _CONCAT:
			return true;
		default:
			return false;
		}
	}
	
	final class LuaToken {
		private String lexeme;
		private Token token;
		private int line;
		private int position;
		
		public String getLexeme() {
			return lexeme;
		}

		public Token getToken() {
			return token;
		}

		public int getLine() {
			return line;
		}
		public int getPosition() {
			return position;
		}
		@Override
		public String toString() {
			return String.format("[%d:%d]%s - %s", position, line, token, lexeme);
		}
	};
}

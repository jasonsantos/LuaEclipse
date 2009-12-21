package com.anwrt.ldt.internal.editor.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;

public class LuaCodeScanner extends AbstractScriptScanner {

	private static String[] fgKeywords = { "and", "break", "do", "else",
			"elseif", "end", "false", "for", "function", "if", "in", "local",
			"nil", "not", "or", "repeat", "return", "then", "true", "until",
			"while" };

	private static String fgTokenProperties[] = new String[] {
			ILuaColorConstants.LUA_STRING,
			ILuaColorConstants.LUA_SINGLE_LINE_COMMENT,
			ILuaColorConstants.LUA_MULTI_LINE_COMMENT,
			ILuaColorConstants.LUA_DEFAULT, ILuaColorConstants.LUA_KEYWORD };

	public LuaCodeScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		this.initialize();
	}

	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken keyword = this.getToken(ILuaColorConstants.LUA_KEYWORD);
		IToken comment = this
				.getToken(ILuaColorConstants.LUA_SINGLE_LINE_COMMENT);
		IToken multiline = this
				.getToken(ILuaColorConstants.LUA_MULTI_LINE_COMMENT);
		IToken other = this.getToken(ILuaColorConstants.LUA_DEFAULT);

		// Add rule for multi-line comments
		rules.add(new MultiLineRule("--[[", "]]", multiline));

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("--", comment));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new LuaWhitespaceDetector()));

		// Add word rule for keywords.
		WordRule wordRule = new WordRule(new LuaWordDetector(), other);
		for (int i = 0; i < fgKeywords.length; i++) {
			wordRule.addWord(fgKeywords[i], keyword);
		}
		rules.add(wordRule);
		this.setDefaultReturnToken(other);
		return rules;
	}

	/**
	 * Indicates if argument is a white space
	 * 
	 * @param char Tested character
	 */
	public class LuaWhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char character) {
			return Character.isWhitespace(character);
		}
	}

	public class LuaWordDetector implements IWordDetector {
		/**
		 * Indicates if argument is part of a word
		 * 
		 * @param char Tested character
		 */
		public boolean isWordPart(char character) {
			return Character.isJavaIdentifierPart(character);
		}

		/**
		 * Indicates if argument starts of a word
		 * 
		 * @param char Tested character
		 */
		public boolean isWordStart(char character) {
			return Character.isJavaIdentifierStart(character);
		}
	}

}

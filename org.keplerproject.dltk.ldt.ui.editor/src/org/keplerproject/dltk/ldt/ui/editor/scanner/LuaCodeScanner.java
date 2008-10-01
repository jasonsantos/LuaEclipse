package org.keplerproject.dltk.ldt.ui.editor.scanner;

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
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.keplerproject.dltk.ldt.ui.editor.text.ILuaColorConstants;

public class LuaCodeScanner extends AbstractScriptScanner {
	private static final String[] keywords = { "and", "break", "do", "else",
			"elseif", "end", "false", "for", "function", "if", "in", "local",
			"nil", "not", "or", "repeat", "return", "then", "true", "until",
			"while" };

	private static final String[] properties = new String[] {
		ILuaColorConstants.LUA_COMMENT,
		ILuaColorConstants.LUA_DEFAULT,
		ILuaColorConstants.LUA_KEYWORD
	};
	
	public LuaCodeScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);
		initialize();
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();
		IToken keyword = getToken(ILuaColorConstants.LUA_KEYWORD);
		IToken comment = getToken(ILuaColorConstants.LUA_COMMENT);
		IToken other = getToken(ILuaColorConstants.LUA_DEFAULT);

		rules.add(new EndOfLineRule("--", comment));
		rules.add(new WhitespaceRule(new LuaWhitespaceDetector()));
		WordRule wordRule = new WordRule(new LuaWordDetector(), other);

		for (int i = 0; i < keywords.length; i++)
			wordRule.addWord(keywords[i], keyword);

		rules.add(wordRule);
		setDefaultReturnToken(other);
		return rules;
	}

	@Override
	protected String[] getTokenProperties() {
		return properties;
	}

	public class LuaWhitespaceDetector implements IWhitespaceDetector {
		public boolean isWhitespace(char c) {
			return Character.isWhitespace(c);
		}
	}

	public class LuaWordDetector implements IWordDetector {
		public boolean isWordPart(char c) {
			return Character.isJavaIdentifierPart(c);
		}

		public boolean isWordStart(char c) {
			return Character.isJavaIdentifierPart(c);
		}
	}
}

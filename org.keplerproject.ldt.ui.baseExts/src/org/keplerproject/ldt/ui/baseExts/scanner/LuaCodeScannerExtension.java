package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;

/**
 * Basic Syntax HighLight Extension Scanner to lua code.
 * 
 * @author guilherme
 * @version 1.0
 */
public class LuaCodeScannerExtension implements IScannerRuleExtension {
	private IToken[] fTokens;

	private IPredicateRule[] fRules;

	public LuaCodeScannerExtension() {
	/*	LuaColorManager manager = new LuaColorManager();
		IToken keyword = new Token(new TextAttribute(manager
				.getColor(ILuaColorConstants.KEYWORD), null, 1));
		IToken function = new Token(new TextAttribute(manager
				.getColor(ILuaColorConstants.FUNCTION), null, 1));
		IToken string = new Token(new TextAttribute(manager
				.getColor(ILuaColorConstants.STRING)));
		IToken comment = new Token(new TextAttribute(manager
				.getColor(ILuaColorConstants.SINGLE_LINE_COMMENT)));
		IToken multi_comment = new Token(new TextAttribute(manager
				.getColor(ILuaColorConstants.MULTI_LINE_COMMENT)));
		IToken other = new Token(new TextAttribute(manager
				.getColor(ILuaColorConstants.DEFAULT)));
		fTokens = new IToken[] { keyword, function, string, comment,
				multi_comment, other };

		List rules = new ArrayList();
		rules.add(new EndOfLineRule("--", comment));
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));
		rules.add(new MultiLineRule("[[", "]]", string));
		rules.add(new NestedPatternRule("--[[","[[","]]",multi_comment));
		rules.add(new LuaWhitespaceRule(new LuaWhitespaceDetector()));
		LuaWordRule wordRule = new LuaWordRule(new LuaWordDetector(), other);
		for (int i = 0; i < ILuaSyntax.reservedwords.length; i++)
			wordRule.addWord(ILuaSyntax.reservedwords[i], keyword);

		rules.add(wordRule);
		fRules = new IPredicateRule[rules.size()];
		Iterator it = rules.iterator();
		int i = 0;
		while(it.hasNext())
			fRules[i++] = (IPredicateRule)it.next();*/

	}

	public IPredicateRule[] getRules() {
		return fRules;
	}

	public IToken[] getTokens() {
		return fTokens;
	}
}

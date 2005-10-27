package org.keplerproject.ldt.ui.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class LuaRuleBasedScanner extends RuleBasedScanner {

	public LuaRuleBasedScanner(ColorManager manager) {
		IToken singleLineComment =
			new Token(
				new TextAttribute(
					manager.getColor(ILuaColorConstants.SINGLE_LINE_COMMENT)));

		IRule[] rules = new IRule[2];
		 // Add rule for single line comments.
        rules[0] = new EndOfLineRule("--", singleLineComment);
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new LuaWhitespaceDetector());

		setRules(rules);
	}
}

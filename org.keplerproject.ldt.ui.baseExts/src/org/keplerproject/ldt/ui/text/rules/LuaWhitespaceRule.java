package org.keplerproject.ldt.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class LuaWhitespaceRule extends WhitespaceRule implements IPredicateRule{

	public LuaWhitespaceRule(IWhitespaceDetector detector) {
		super(detector);
	}

	public IToken getSuccessToken() {
		return null;
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		// TODO Auto-generated method stub
		return null;
	}

}

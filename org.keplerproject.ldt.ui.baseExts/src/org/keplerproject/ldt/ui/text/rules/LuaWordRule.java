package org.keplerproject.ldt.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.WordRule;

public class LuaWordRule  extends WordRule implements IPredicateRule{

	public IToken getSuccessToken() {
		return super.fDefaultToken;
	}
	public LuaWordRule(IWordDetector det)
	{
		super(det);
	}
	public LuaWordRule(IWordDetector det, IToken tok)
	{
		super(det,tok);
	}

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		return super.evaluate(scanner);
	}

}

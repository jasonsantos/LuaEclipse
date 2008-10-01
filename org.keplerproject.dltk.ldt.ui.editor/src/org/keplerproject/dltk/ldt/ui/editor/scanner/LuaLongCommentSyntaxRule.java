package org.keplerproject.dltk.ldt.ui.editor.scanner;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.dltk.ldt.ui.editor.ILuaPartitions;

public class LuaLongCommentSyntaxRule implements IPredicateRule {

	public IToken evaluate(ICharacterScanner scanner, boolean resume) {
		int c = scanner.read();
		if ((char) (c) == '-') {
			c = scanner.read();
			if ((char) (c) == '-') {
				c = scanner.read();
				if ((char) (c) == '[')
					return longString(scanner, ILuaPartitions.LUA_COMMENT);
				scanner.unread();
			}
			scanner.unread();
		}
		scanner.unread();
		return Token.UNDEFINED;
	}

	private IToken longString(ICharacterScanner scanner, String type) {
		long scope = 0;
		long count;
		int c = scanner.read();

		while ((char) (c) == '=') {
			c = scanner.read();
			scope++;
		}
		scanner.read(); // [

		do {
			do {
				c = scanner.read();
			} while ((char) (c) != ']');
			c = scanner.read();
			count = 0;
			while ((char) (c) == '=') {
				c = scanner.read();
				count++;
			}
		} while (count != scope || (char) (c) != ']');
		scanner.read(); // ]
		return new Token(type);
	}

	public IToken getSuccessToken() {
		return new Token(ILuaPartitions.LUA_COMMENT);
	}

	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, true);
	}

}

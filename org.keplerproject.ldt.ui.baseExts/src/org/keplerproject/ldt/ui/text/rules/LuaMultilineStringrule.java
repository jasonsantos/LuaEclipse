/*
* Copyright (C) 2003-2007 Kepler Project.
*
* Permission is hereby granted, free of charge, to any person obtaining
* a copy of this software and associated documentation files (the
* "Software"), to deal in the Software without restriction, including
* without limitation the rights to use, copy, modify, merge, publish,
* distribute, sublicense, and/or sell copies of the Software, and to
* permit persons to whom the Software is furnished to do so, subject to
* the following conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/


package org.keplerproject.ldt.ui.text.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;

/**
 * Lua Multiline String rule.
 * @author guilherme
 * @version $Id$
 *
 */
public class LuaMultilineStringrule extends MultiLineRule {

	/**
	 * @param startSequence
	 * @param endSequence
	 * @param token
	 * @param escapeCharacter
	 * @param breaksOnEOL
	 */
	public LuaMultilineStringrule(IToken token, char escapeCharacter,
			boolean breaksOnEOL) {
		
		super("[[", "]]", token, escapeCharacter, breaksOnEOL);
		
		
	}

	public LuaMultilineStringrule(IToken token, char escapeCharacter,
			boolean breaksOnEOL, boolean breaksOnEOF) {
		this(token, escapeCharacter, breaksOnEOL);
	}

	public LuaMultilineStringrule(IToken token) {
		this(token, (char) 0, false);
	}

	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
		if (resume) {
			scanner.unread();
			int c = scanner.read();
			int f = 0;
			while (c != fStartSequence[0] || f != fStartSequence[1]) {
				scanner.unread();
				scanner.unread();
				f = c;
				c = scanner.read();
			}
			scanner.unread();
			c = scanner.read();
			if (c == fStartSequence[0]) {
				if (sequenceDetected(scanner, fStartSequence, false)) {

					if (endSequenceDetected(scanner)) {
						return fToken;
					}
				}
			}

		} else {

			int c = scanner.read();
			if (c == fStartSequence[0]) {
				if (sequenceDetected(scanner, fStartSequence, false)) {

					if (endSequenceDetected(scanner)) {
						return fToken;
					}
				}
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	/*protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int c;
		char[][] delimiters = scanner.getLegalLineDelimiters();

		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			if (c == fEscapeCharacter) {
				// Skip the escaped character.
				scanner.read();
			} else if (fNestingStartSequence.length > 0
					&& c == fNestingStartSequence[0]) {
				// Check if the specified nesting start sequence has been found.
				if (sequenceDetected(scanner, fNestingStartSequence, true))
					fNestingCount++;
			} else if (fEndSequence.length > 0 && c == fEndSequence[0]) {
				// Check if the specified end sequence has been found.
				if (sequenceDetected(scanner, fEndSequence, true)) {
					fNestingCount--;
					if (fNestingCount == 0)
						return true;
				}
			} else if (fBreaksOnEOL) {
				// Check for end of line since it can be used to terminate the
				// pattern.
				for (int i = 0; i < delimiters.length; i++) {
					if (c == delimiters[i][0]
							&& sequenceDetected(scanner, delimiters[i], true))
						return true;
				}
			}
		}
		if (fBreaksOnEOF)
			return true;
		scanner.unread();
		return false;
	}*/

}

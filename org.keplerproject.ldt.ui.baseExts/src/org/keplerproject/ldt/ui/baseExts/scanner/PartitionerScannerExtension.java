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

package org.keplerproject.ldt.ui.baseExts.scanner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;
import org.keplerproject.ldt.ui.text.rules.LuaMultilineCommentrule;
import org.keplerproject.ldt.ui.text.rules.LuaWordRule;
/**
 *  Base partitioner extension point implementation
 *  
 * @author guilherme
 * @version $Id$
 * 
 */
public class PartitionerScannerExtension implements IScannerRuleExtension ,ILuaSyntax{
	private IToken[] fTokens;

	private IPredicateRule[] fRules;

	/**
	 * Create all tokens and Rules of the extension
	 *
	 */
	public PartitionerScannerExtension() {
		this.fTokens = new IToken[] { 
				new Token(ILuaPartitions.LUA_MULTI_LINE_COMMENT),
				new Token(ILuaPartitions.LUA_STRING),
				//new Token(ILuaPartitions.LUA_WORDS),
				new Token(ILuaPartitions.LUA_CODE)};
		
		List rules = new ArrayList();
		//Add rule for multi line comments.
		rules.add(new LuaMultilineCommentrule(fTokens[0]));
		
		// String Rules
		rules.add(new SingleLineRule("\"", "\"", fTokens[1], '\\'));
		rules.add(new SingleLineRule("'", "'", fTokens[1], '\\'));
		rules.add(new MultiLineRule("[[", "]]", fTokens[1], '\\'));		

		// Add word rule for keywords and constants.
		LuaWordRule wr = new LuaWordRule(new LuaWordDetector(), fTokens[2]);
		for (int i = 0; i < reservedwords.length; i++)
			wr.addWord(reservedwords[i], fTokens[2]);
		for (int i = 0; i < constants.length; i++)
			wr.addWord(constants[i], fTokens[2]);
		for (int i = 0; i < otherpredicates.length; i++)
			wr.addWord(otherpredicates[i], fTokens[2]);
		rules.add(wr);
		
	/*	LuaWhitespaceRule whiteRule = new LuaWhitespaceRule(
				new LuaWhitespaceDetector());
		rules.add(whiteRule);*/
		
		/*//single line comment part
		rules.add(new EndOfLineRule("--",fTokens[2]));*/
		
		
		this.fRules = new IPredicateRule[rules.size()];
		rules.toArray(fRules);
	}

	public IPredicateRule[] getRules() {
		return this.fRules;
	}

	public IToken[] getTokens() {
		return this.fTokens;
	}

}

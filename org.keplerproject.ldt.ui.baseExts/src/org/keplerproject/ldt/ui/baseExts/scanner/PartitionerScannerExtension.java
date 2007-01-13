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

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;
import org.keplerproject.ldt.ui.text.rules.LuaMultilineCommentrule;
/**
 *  Base partitioner extension point implementation
 *  
 * @author guilherme
 * @version $Id$
 * 
 */
public class PartitionerScannerExtension implements IScannerRuleExtension {
	private IToken[] fTokens;

	private IPredicateRule[] fRules;

	/**
	 * Create all tokens and Rules of the extension
	 *
	 */
	public PartitionerScannerExtension() {
		this.fTokens = new IToken[] { 
				new Token(ILuaPartitions.LUA_MULTI_LINE_COMMENT),
				new Token(ILuaPartitions.LUA_CODE)};
		
		List rules = new ArrayList();
		//Add rule for multi line comments.
		rules.add(new LuaMultilineCommentrule(fTokens[0]));
				
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

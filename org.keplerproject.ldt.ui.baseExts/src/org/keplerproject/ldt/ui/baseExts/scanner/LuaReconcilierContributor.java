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

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.keplerproject.ldt.ui.editors.LuaBaseScanner;
import org.keplerproject.ldt.ui.editors.LuaColorManager;
import org.keplerproject.ldt.ui.editors.NonRuleBasedDamagerRepairer;
import org.keplerproject.ldt.ui.editors.ext.ILuaReconcilierExtension;
import org.keplerproject.ldt.ui.text.rules.LuaWhitespaceRule;
import org.keplerproject.ldt.ui.text.rules.LuaWordRule;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaReconcilierContributor implements ILuaReconcilierExtension,
		ILuaSyntax {

	private LuaColorManager fColorManager;

	public void contribute(LuaColorManager colorManager,
			PresentationReconciler reconciler, ISourceViewer viewer) {
		this.fColorManager = colorManager;
		
		//Lua word reconciliers
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCodeScanner());
		reconciler.setDamager(dr, ILuaPartitions.LUA_CODE);
		reconciler.setRepairer(dr, ILuaPartitions.LUA_CODE);
		
		dr.setDocument(viewer.getDocument());
		//Lua string reconciliers
	   /* dr = new DefaultDamagerRepairer(getStringCodeScanner());*/
	    NonRuleBasedDamagerRepairer ndr1 = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager
						.getColor(ILuaColorConstants.LUA_STRING)));
		reconciler.setDamager(ndr1, ILuaPartitions.LUA_STRING);
		reconciler.setRepairer(ndr1, ILuaPartitions.LUA_STRING);
	
		ndr1.setDocument(viewer.getDocument());
		// Lua single line comment reconcilier
		dr = new DefaultDamagerRepairer(getSimpleCommentCodeScanner());
		/*NonRuleBasedDamagerRepairer ndr2 = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager
						.getColor(ILuaColorConstants.LUA_SINGLE_LINE_COMMENT)));*/
		reconciler.setDamager(dr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
		reconciler.setRepairer(dr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
		
		dr.setDocument(viewer.getDocument());
		
		// Multiline comment
		
		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager
						.getColor(ILuaColorConstants.LUA_MULTI_LINE_COMMENT)));
		ndr.setDocument(viewer.getDocument());

		reconciler.setDamager(ndr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);
		reconciler.setRepairer(ndr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);

		/*reconciler.setDamager(ndr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
		reconciler.setRepairer(ndr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);*/

		/*
		 * ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager
		 * .getColor(ILuaColorConstants.LUA_STRING)));
		 * ndr.setDocument(viewer.getDocument());
		 * dr.setDocument(viewer.getDocument()); reconciler.setDamager(ndr,
		 * ILuaPartitions.LUA_STRING); reconciler.setRepairer(ndr,
		 * ILuaPartitions.LUA_STRING);
		 */
	}

	/**
	 * Returns basic code scanner, keywords, constants, etc.. 
	 * @return
	 */
	private ITokenScanner getCodeScanner() {
		LuaBaseScanner scanner = new LuaBaseScanner();

		IToken keyword = new Token(new TextAttribute(this.fColorManager
				.getColor(ILuaColorConstants.LUA_KEYWORD), null, SWT.BOLD));
		IToken constant = new Token(new TextAttribute(this.fColorManager
				.getColor(ILuaColorConstants.LUA_CONSTANTS), null, SWT.ITALIC));
		IToken otherPredicates = new Token(new TextAttribute(this.fColorManager
				.getColor(ILuaColorConstants.LUA_OTHER_PREDICATE), null,
				SWT.BOLD | SWT.ITALIC));
		IToken other = new Token(new TextAttribute(this.fColorManager
				.getColor(ILuaColorConstants.LUA_DEFAULT), null, SWT.NONE));
		// Add generic whitespace rule.
		LuaWhitespaceRule whiteRule = new LuaWhitespaceRule(
				new LuaWhitespaceDetector());

		// Add word rule for keywords and constants.
		LuaWordRule wordRule = new LuaWordRule(new LuaWordDetector(), other);
		for (int i = 0; i < reservedwords.length; i++)
			wordRule.addWord(reservedwords[i], keyword);
		for (int i = 0; i < constants.length; i++)
			wordRule.addWord(constants[i], constant);

		LuaWordRule predRule = new LuaWordRule(new LuaWordDetector());
		for (int i = 0; i < otherpredicates.length; i++)
			predRule.addWord(otherpredicates[i], otherPredicates);

		scanner.setPredicateRules(new IPredicateRule[] {
			    predRule,
				wordRule, whiteRule });

		return scanner;
	}
	/** 
	 * returns the string code scanner..  has the rules to 
	 * highlight strings
	 * @return
	 */
	private ITokenScanner getStringCodeScanner() {
		LuaBaseScanner scanner = new LuaBaseScanner();

		IToken string = new Token(new TextAttribute(fColorManager
				.getColor(ILuaColorConstants.LUA_STRING)));
		// Add generic whitespace rule.
		LuaWhitespaceRule whiteRule = new LuaWhitespaceRule(
				new LuaWhitespaceDetector());

		// Add word rule for keywords and constants.
	
		scanner.setPredicateRules(new IPredicateRule[] {
				
				new SingleLineRule("\"", "\"", string, '\\'),
				new SingleLineRule("'", "'", string, '\\'),
				new MultiLineRule("[[", "]]", string, '\\'),
				whiteRule });

		return scanner;
	}
	/**
	 * Return the scanner to single line comments
	 * @return
	 */
	private ITokenScanner getSimpleCommentCodeScanner() {
		LuaBaseScanner scanner = new LuaBaseScanner();

		IToken scommnet = new Token(new TextAttribute(fColorManager
				.getColor(ILuaColorConstants.LUA_SINGLE_LINE_COMMENT)));
		// Add generic whitespace rule.
		LuaWhitespaceRule whiteRule = new LuaWhitespaceRule(
				new LuaWhitespaceDetector());

		// Add word rule for keywords and constants.
	
		scanner.setPredicateRules(new IPredicateRule[] {
				new EndOfLineRule("--", scommnet),
				whiteRule });

		return scanner;
	}
	
}

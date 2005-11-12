/* **********************************
 * 
 * 
 * *******************************************/
package org.keplerproject.ldt.ui.baseExts.scanner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.ldt.ui.editors.LuaBaseScanner;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;
import org.keplerproject.ldt.ui.text.rules.NestedPatternRule;
/**
 *  Base partitioner extension point implementation
 *  
 * @author guilherme
 * @version 1.0
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
				new Token(ILuaPartitions.LUA_SINGLE_LINE_COMMENT),
				new Token(ILuaPartitions.LUA_STRING),								
				new Token(ILuaPartitions.LUA_CODE)};
		
		List rules = new ArrayList();
//		 Add rule for multi line comments.
		rules.add(new NestedPatternRule("--[[", "[[", "]]", fTokens[0]));
		//Add rule for single line comments.
		rules.add(new EndOfLineRule("--", fTokens[1]));
		//Add rule for strings and character constants.
		//rules.add(new SingleLineRule("\"", "\"", fTokens[2], '\\'));
		///rules.add(new SingleLineRule("'", "'", fTokens[2], '\\'));
		rules.add(new MultiLineRule("[[", "]]", fTokens[2]));
		
		
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

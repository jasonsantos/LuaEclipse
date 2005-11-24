/* **********************************
 * 
 * 
 * *******************************************/
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

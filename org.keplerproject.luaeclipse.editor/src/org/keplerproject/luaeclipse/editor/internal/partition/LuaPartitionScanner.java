package org.keplerproject.luaeclipse.editor.internal.partition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.luaeclipse.editor.internal.text.ILuaPartitions;


/**
 * Defines rules to follow in order to highlight source code in editor
 * @author kkinfoo
 *
 */
public class LuaPartitionScanner extends RuleBasedPartitionScanner {

	public LuaPartitionScanner() {
		super();
		List<PatternRule> rules = new ArrayList<PatternRule>();

		/*
		 * Deal with single and double quote multi lines strings
		 */
		IToken string = new Token(ILuaPartitions.LUA_STRING);
		IToken singleQuoteString = new Token(
				ILuaPartitions.LUA_SINGLE_QUOTE_STRING);
		rules
				.add(new MultiLineRule(
						"\'", "\'", singleQuoteString, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$
		rules.add(new MultiLineRule("\"", "\"", string, '\\', false)); //$NON-NLS-1$ //$NON-NLS-2$

		/*
		 * Deal with comments
		 */

		// Multi-line
		IToken multiLineComment = new Token(
				ILuaPartitions.LUA_MULTI_LINE_COMMENT);
		rules.add(new MultiLineRule("--[[", "]]", multiLineComment));//$NON-NLS-1$

		// Single line
		IToken comment = new Token(ILuaPartitions.LUA_COMMENT);
		rules.add(new EndOfLineRule("--", comment)); //$NON-NLS-1$

		// Apply rules
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}

}

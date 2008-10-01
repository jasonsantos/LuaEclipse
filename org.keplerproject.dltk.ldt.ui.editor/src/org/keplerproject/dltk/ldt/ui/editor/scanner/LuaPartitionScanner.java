package org.keplerproject.dltk.ldt.ui.editor.scanner;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.dltk.ldt.ui.editor.ILuaPartitions;

public class LuaPartitionScanner extends RuleBasedPartitionScanner {
	public LuaPartitionScanner() {
		IToken string = new Token(ILuaPartitions.LUA_STRING);
		IToken comment = new Token(ILuaPartitions.LUA_COMMENT);
		
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add(new LuaLongStringSyntaxRule());
		rules.add(new LuaLongCommentSyntaxRule());
		rules.add(new EndOfLineRule("--", comment));
		//rules.add(new MultiLineRule("[[", "]]", string));
		rules.add(new MultiLineRule("\'", "\'", string, '\\'));
		rules.add(new MultiLineRule("\"", "\"", string, '\\'));
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
	
}

package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.keplerproject.ldt.ui.text.rules.NestedPatternRule;

/**
 * @author tuler
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LuaPartitionScanner extends RuleBasedPartitionScanner {
    /**
     * Constructor for LuaPartitionScanner. Creates rules to parse comment
     * partitions in an Lua document. In the constructor, is defined the entire
     * set of rules used to parse the document, in an instance of an
     * IPredicateRule. The coonstructor calls setPredicateRules method which
     * associates the rules to the scanner and makes the document ready for
     * parsing.
     */
    public LuaPartitionScanner() {
        super();

        IToken skip = new Token(ILuaPartitions.LUA_SKIP);
        IToken string = new Token(ILuaPartitions.LUA_STRING);
        IToken multiLineComment = new Token(ILuaPartitions.LUA_MULTI_LINE_COMMENT);
        IToken singleLineComment = new Token(ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
        
        setDefaultReturnToken(skip);
        
        List rules = new ArrayList();

        // Add rule for multi line comments.
		rules.add(new NestedPatternRule("--[[", "[[", "]]", multiLineComment));

        // Add rule for single line comments.
        rules.add(new EndOfLineRule("--", singleLineComment));

        // Add rule for strings and character constants.
        rules.add(new SingleLineRule("\"", "\"", string, '\\'));
        rules.add(new SingleLineRule("'", "'", string, '\\'));
        rules.add(new MultiLineRule("[[", "]]", string));

        IPredicateRule[] result = new IPredicateRule[rules.size()];
        rules.toArray(result);
        setPredicateRules(result);
    }
}

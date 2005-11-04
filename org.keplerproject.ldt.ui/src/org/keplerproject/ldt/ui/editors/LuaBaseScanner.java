package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

/**
 *  Base code and document scanner based on external Rules added to the scanner.
 *  
 * @see addRule()
 * @author guilherme
 * @version 1.0
 */
public class LuaBaseScanner extends RuleBasedPartitionScanner {
    /**
     * Constructor for LuaBaseScanner. Just a default constructor, calls super().
     * 
     */
    public LuaBaseScanner() {
        super();
    }
    
    /**
     *  Adds new Rules to the BaseScanner
     * @param rules - the vector with the rules.
     * 
     */
    public void addRules(IPredicateRule[] rules)
    {
    	IPredicateRule[] currentRules = (IPredicateRule[]) (super.fRules == null ? new IPredicateRule[] {} : super.fRules);
    	if(rules == null) //TODO LOG IT and throw Exception
    		return;
    	IPredicateRule[]newRules =  new IPredicateRule[currentRules.length + rules.length];
    	ArrayList allRules = new ArrayList();
    	allRules.addAll(Arrays.asList(currentRules));
    	allRules.addAll(Arrays.asList(rules));
    	allRules.toArray(newRules);
    	setPredicateRules(newRules);
    }
}

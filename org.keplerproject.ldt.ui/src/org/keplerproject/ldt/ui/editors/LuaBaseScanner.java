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

package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

/**
 *  Base code and document scanner based on external Rules added to the scanner.
 *  This Scanner is used at the partitioner scanner extension.
 *  
 * @see addRule()
 * @author Guilherme Martins
 * @version $Id$
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
     *  if the <code>rules</code> parameter is null. this method just return.
     *  
     * @param rules - the vector with the rules.
     * 
     */
    public void addRules(IPredicateRule[] rules)
    {
    	IPredicateRule[] currentRules = (IPredicateRule[]) (super.fRules == null ? new IPredicateRule[] {} : super.fRules);
    	if(rules == null) 
    		return;
    	IPredicateRule[]newRules =  new IPredicateRule[currentRules.length + rules.length];
    	ArrayList allRules = new ArrayList();
    	allRules.addAll(Arrays.asList(currentRules));
    	allRules.addAll(Arrays.asList(rules));
    	allRules.toArray(newRules);
    	setPredicateRules(newRules);
    }
}

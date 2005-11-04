/* ***************************************
 * LDT 
 * IScannerRuleExtension
 * 
 **********************************/
package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;

/**
 * This interface define the extension point to LuaEditor Scanners.
 * Even if the scanner is used to <i>document partition</i> or <i>syntax highlighting</i>,
 * this interface can be used to extend the rules to Scan the source document.
 * 
 * @author guilherme
 * @version 1.0
 */
public interface IScannerRuleExtension {

	/**
	 * Return all IRules objects to extend the document partitioner or the code scanner
	 * 
	 * @return IRule [] containing the rules to extend the scanner 
	 */
   IPredicateRule[] getRules();
   
   /**
    * Return the Tokens Scanned by these Rules.
    * 
    * Some uses of this method will expect diferent types of IToken.getData() returns
    * for example, to a document partitioner, will expect a String.
    * 
    * @return
    */
   IToken[] getTokens();
}

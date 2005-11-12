/* ***************************************
 * LDT 
 * IScannerRuleExtension
 * 
 **********************************/
package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.keplerproject.ldt.ui.editors.LuaBaseScanner;

/**
 * This interface define the extension point to LuaEditor Scanners.
 * Even if the scanner is used to <i>document partition</i> or <i>syntax highlighting</i>,
 * this interface can be used to extend the rules to Scan the source document.
 * 
 * <i>Note that all the methods returns a array. The position of each element in differrent arrays
 * is relevant. For example, the method getTokens will be the base array, and the Token at index
 * 0 will have the ScannerRule(with textAtribute) ,of the getRules method ,at the position 0. The same
 * thing happens with the getCompletionProcessors.</i>
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
   
   void contribute(LuaBaseScanner scanner,IDocument document);
   
   }

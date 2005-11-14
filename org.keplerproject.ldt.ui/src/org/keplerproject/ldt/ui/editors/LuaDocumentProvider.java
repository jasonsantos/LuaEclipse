/*************************************
 * 
 ***********************************/
package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;

/**
 *  Lua Editor Document Provider.
 *  This document providerm create a lua source document
 *  with a document partitioner. This partitioner is based on
 *  a <code>LuaBaseScanner</code>.
 *  The Rules of this scanner are added from the ScannerRules Extension point.
 *  
 * @author guilherme
 * @version 1.0
 *
 */
public class LuaDocumentProvider extends FileDocumentProvider {

	/**
	 * @see FileDocumentProvider.createDocument
	 */
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		// creates a base document
		if (document != null) {
			// instantiate the scanner to the partitioner
			LuaBaseScanner lscanner = new LuaBaseScanner();
			List contentTypes = new ArrayList();
			List extensionLists = LDTUIPlugin.getDefault().getScannerRulesExtension();
			
			// Load all Scanner extensions
			IScannerRuleExtension[] ruleExt =  new IScannerRuleExtension[extensionLists.size()];
			extensionLists.toArray(ruleExt);
			if(ruleExt == null)
				return document;
				
			for(int i = 0 ; i < ruleExt.length; i++)
			{
				IPredicateRule[] rules = ruleExt[i].getRules();
				//Add the rules to the Scanner.
				lscanner.addRules(rules);
				//Get the tokens to obtain the contenty type names
				IToken[] tokens = ruleExt[i].getTokens();
				if(tokens == null) throw new CoreException(STATUS_ERROR);
				for(int j = 0 ; j < tokens.length ; j++)
				{
					if(tokens[j].getData() instanceof String)
					{
						//get the content type name
						contentTypes.add(tokens[j].getData());
					}
					else
						throw new CoreException(STATUS_ERROR);
				}
			}
			// convert to String Array
			String [] contentTypesStr = new String[contentTypes.size()];
			contentTypes.toArray(contentTypesStr);
					    
			// Create a FastDocumentPartitioner with the scanner
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					lscanner , contentTypesStr);
			// Connect the document wiht the partitioner
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
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
 * @version $Id$
 *
 */
public class LuaDocumentProvider extends FileDocumentProvider {

	private String editorId;
	public LuaDocumentProvider(String editorId) {
		this.editorId = editorId;
	}
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
			List extensionLists = LDTUIPlugin.getDefault().getScannerRulesExtension(editorId);
			
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
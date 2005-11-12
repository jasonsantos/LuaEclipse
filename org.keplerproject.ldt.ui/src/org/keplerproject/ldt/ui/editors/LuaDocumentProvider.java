package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;

public class LuaDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			LuaBaseScanner lscanner = new LuaBaseScanner();
			List contentTypes = new ArrayList();
			List extensionLists = LDTUIPlugin.getDefault().getScannerRulesExtension();
			
			IScannerRuleExtension[] ruleExt =  new IScannerRuleExtension[extensionLists.size()];
			extensionLists.toArray(ruleExt);
			if(ruleExt == null)
				return document;
				
			for(int i = 0 ; i < ruleExt.length; i++)
			{
				ruleExt[i].contribute(lscanner,document);
				//TODO Exception handler
				//IPredicateRule[] rules = ruleExt[i].getRules();
				//lscanner.addRules(rules);
				IToken[] tokens = ruleExt[i].getTokens();
				if(tokens == null) throw new CoreException(STATUS_ERROR);
				for(int j = 0 ; j < tokens.length ; j++)
				{
					if(tokens[j].getData() instanceof String)
					{
						contentTypes.add(tokens[j].getData());
					}
					else
						throw new CoreException(STATUS_ERROR);
				}
			}
			// convert to String Array
			String [] contentTypesStr = new String[contentTypes.size()];
			contentTypes.toArray(contentTypesStr);
					    
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					lscanner , contentTypesStr);
			
					
			
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
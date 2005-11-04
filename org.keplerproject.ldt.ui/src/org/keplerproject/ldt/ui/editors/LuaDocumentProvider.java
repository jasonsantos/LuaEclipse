package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;

public class LuaDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			LuaBaseScanner lscanner = new LuaBaseScanner();
			List contentTypes = new ArrayList();
			IScannerRuleExtension[] ruleExt =  LDTUIPlugin.getDefault().getPartitionRuleExtension();
			if(ruleExt == null)
				return document;
				
			for(int i = 0 ; i < ruleExt.length; i++)
			{
				//TODO Exception handler
				lscanner.addRules(ruleExt[i].getRules());
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
					/*new String[] {
						ILuaPartitions.LUA_MULTI_LINE_COMMENT,
						ILuaPartitions.LUA_SINGLE_LINE_COMMENT,
						ILuaPartitions.LUA_STRING,
						ILuaPartitions.LUA_SKIP});*/
			
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
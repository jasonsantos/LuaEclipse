package org.keplerproject.ldt.ui.baseExts.completion;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentAssistExtension;

public class LuaContentAssistContributorExtension  implements ILuaContentAssistExtension{

	
	public void contribute(IEditorPart editor, ContentAssistant assistant) {
		LuaCompletionProcessor processor = new LuaCompletionProcessor();
		assistant.setContentAssistProcessor(processor,IDocument.DEFAULT_CONTENT_TYPE);

	}

}

package org.keplerproject.ldt.ui.editors;

import java.util.ResourceBundle;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.keplerproject.ldt.ui.LDTUIPlugin;

public class LuaEditor extends TextEditor {

	private ColorManager colorManager;

	public LuaEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new LuaSourceViewerConfiguration(colorManager));
		setDocumentProvider(new LuaDocumentProvider());
		setRangeIndicator(new DefaultRangeIndicator());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	protected void createActions() {
		super.createActions();
		ResourceBundle bundle = LDTUIPlugin.getDefault().getResourceBundle();
		
		setAction("LuaContentAssistProposal", new TextOperationAction(bundle,"ContentAssistProposal.",this,ISourceViewer.CONTENTASSIST_PROPOSALS));
		setAction("LuaContentAssistTipProposal", new TextOperationAction(bundle,"ContentAssistTipProposal.",this,ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));
	}

}

/**************************************
 * 
 **********************************/
package org.keplerproject.ldt.ui.editors;

import java.util.ResourceBundle;

import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.keplerproject.ldt.ui.LDTUIPlugin;
/**
 * Lua Text Editor. 
 * @author Guilherme Martins
 *
 */
public class LuaEditor extends TextEditor {

	private LuaColorManager colorManager;
	private LuaSourceViewerConfiguration sourceViewer;

	public LuaEditor() {
		super();
		// initialize the color manager
		colorManager = new LuaColorManager();
		// creates the sourceviewerConfig
		sourceViewer = new LuaSourceViewerConfiguration(colorManager);
		// IMPORTANT.. sets the EditorPart to the viewer
		sourceViewer.setEditor(this);
		// setup the source viewer
		setSourceViewerConfiguration(sourceViewer);
		// Setup the LuaDocumentProvider
		setDocumentProvider(new LuaDocumentProvider());
		setRangeIndicator(new DefaultRangeIndicator());
		
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}
	/**
	 * Unusable for now.
	 */
	protected void createActions() {
		super.createActions();
		ResourceBundle bundle = LDTUIPlugin.getDefault().getResourceBundle();
		
		setAction("LuaContentAssistProposal", new TextOperationAction(bundle,"ContentAssistProposal.",this,ISourceViewer.CONTENTASSIST_PROPOSALS));
		setAction("LuaContentAssistTipProposal", new TextOperationAction(bundle,"ContentAssistTipProposal.",this,ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));
	}
	
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		super.createPartControl(parent);
	}
	//TODO... implement Folding
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		fAnnotationAccess= createAnnotationAccess();
		fOverviewRuler= createOverviewRuler(getSharedColors());

		ISourceViewer viewer= new SourceViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);
		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

}

/*
 * Copyright (C) 2003-2007 Kepler Project. Permission is hereby granted, free of
 * charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the
 * Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.keplerproject.ldt.ui.editors;

import java.util.Map;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.keplerproject.ldt.ui.LDTUIPlugin;

/**
 * Lua Text Editor.
 * 
 * @author Guilherme Martins
 * @version $Id$
 */

public class LuaEditor extends AbstractDecoratedTextEditor {

	private final LuaColorManager			colorManager;

	private LuaSourceViewerConfiguration	sourceViewer;

	private ProjectionSupport				projectionSupport;

	private ProjectionAnnotationModel		annotationModel;

	private String							finstanceId;

	private LuaOutlinePage luaOutlinePage;

	public LuaEditor() {
		super();
		// initialize the color manager
		colorManager = createColorManager();
		setRulerContextMenuId("lua.editor.rulerMenu");
		setEditorContextMenuId("lua.editor.editorMenu");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		this.finstanceId = getConfigurationElement().getAttribute("id");

		// Setup the LuaDocumentProvider
		setDocumentProvider(createDocumentProvider(this));
		// simple editor provider to start the editor
		// setDocumentProvider(new FileDocumentProvider());
		setRangeIndicator(new DefaultRangeIndicator());
		// This register a resourceChangeListener to compile the lua script and
		// get the error message
		IResourceChangeListener listener = createResourceChangeListener(this);
		LDTUIPlugin.getWorkspace().addResourceChangeListener(
				listener,
				IResourceChangeEvent.POST_BUILD
						| IResourceChangeEvent.PRE_BUILD);

		// creates the sourceviewerConfig
		sourceViewer = createSourceConfiguration(colorManager);
		// IMPORTANT.. sets the EditorPart to the viewer
		sourceViewer.setEditor(this);
		// setup the source viewer
		setSourceViewerConfiguration(sourceViewer);
		super.init(site, input);

	}

	/**
	 * This method must create a ResourceChangeListener to be registered at the
	 * workspace. This listener will be notified when the lua file has been
	 * saved.
	 * 
	 * @param editor
	 * @return The resource change Listener
	 */
	protected IResourceChangeListener createResourceChangeListener(
			LuaEditor editor) {
		return new LuaChangeListener(editor);
	}

	/**
	 * This method creates a Document provider to the lua editor. This document
	 * provider must create partitions of the code. This default implementation
	 * uses a LDT extension point.
	 * 
	 * @param editor
	 * @return
	 */
	protected IDocumentProvider createDocumentProvider(LuaEditor editor) {
		return new LuaDocumentProvider(this.finstanceId);
	}

	/**
	 * This method creates a lua Source configuraton to the code viewer.
	 * Providing Content assist, syntax highlight and code formating to the lua
	 * code. This implementation loads the contributor os LDT extension points.
	 * 
	 * @param colorManager
	 * @return
	 */
	protected LuaSourceViewerConfiguration createSourceConfiguration(
			LuaColorManager colorManager) {
		return new LuaSourceViewerConfiguration(colorManager);
	}

	/**
	 * Creates a LuaEditor Color Manager for general propouse.
	 * 
	 * @return
	 */
	protected LuaColorManager createColorManager() {
		return new LuaColorManager();
	}
	
	

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	/**
	 * Unusable for now.!
	 */
	@Override
	protected void createActions() {
		super.createActions();

		// ResourceBundle bundle = LDTUIPlugin.getDefault().getResourceBundle();
		/*
		 * setAction("LuaContentAssistProposal", new TextOperationAction(bundle,
		 * "ContentAssistProposal.", this,
		 * ISourceViewer.CONTENTASSIST_PROPOSALS));
		 * setAction("LuaContentAssistTipProposal", new TextOperationAction(
		 * bundle, "ContentAssistTipProposal.", this,
		 * ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));
		 */
	}

	/**
	 * This method was re-implemented to create a code folding feature.
	 */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

		projectionSupport = new ProjectionSupport(viewer,
				getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		projectionSupport
				.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error");

		// TODO add this to have a better result..
		/*
		 * projectionSupport.setHoverControlCreator(new
		 * IInformationControlCreator() { public IInformationControl
		 * createInformationControl(Shell shell) { return new
		 * CustomSourceInformationControl(shell,
		 * IDocument.DEFAULT_CONTENT_TYPE); } });
		 */
		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);

		annotationModel = viewer.getProjectionAnnotationModel();
	}

	/**
	 * This method was re-implemented to create a code folding feature.
	 */
	@Override
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		fAnnotationAccess = createAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);

		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	/**
	 * Folding update Structure
	 * 
	 * @param positions
	 */
	public void updateFoldingStructure(Map newAnnotations, Map deleteAnnotations) {
		Annotation[] old = new Annotation[deleteAnnotations.size()];
		deleteAnnotations.keySet().toArray(old);

		annotationModel.modifyAnnotations(old, newAnnotations, null);
	}

	public LuaSourceViewerConfiguration getLuaSourceViewer() {
		return sourceViewer;
	}

	public String getInstanceId() {
		return finstanceId;
	}
	
	public LuaOutlinePage getContentOutline() {
		 if (luaOutlinePage == null  ) {
	            luaOutlinePage = new LuaOutlinePage(getDocumentProvider(), this);
	            
	            luaOutlinePage.setInput(getEditorInput());
	         }
		 
		 return luaOutlinePage;
	}
	

	@Override
	public Object getAdapter(Class adapter) {
		
		if (IContentOutlinePage.class.equals(adapter)) {
	        
	         return getContentOutline();
	      }

		
		return super.getAdapter(adapter);
	}
	
}

 
 